package org.apromore.service.bpmndiagramimporter.impl;

/*-
 * #%L
 * Apromore :: BPMN diagram parser service
 * %%
 * Copyright (C) 2012 - 2019 The Apromore Initiative
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import org.processmining.models.graphbased.directed.ContainableDirectedGraphElement;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.bpmn.*;
import org.processmining.models.graphbased.directed.bpmn.elements.*;

import com.processconfiguration.*;
import org.omg.spec.bpmn._20100524.model.*;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.osgi.service.component.annotations.Component;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.plugins.bpmn.BpmnAssociation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adriano on 29/10/2015.
 */
@Component
public class BPMNDiagramImporterImpl implements BPMNDiagramImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BPMNDiagramImporterImpl.class);

    private BPMNDiagram diagram = new BPMNDiagramImpr("");

    private Map<String, Swimlane> idToPool = new HashMap<>();
    private Map<String, BPMNNode> idToNode = new HashMap<>();
    private Map<Event, String> boundToFix = new HashMap<>();

    private Set<TDataAssociation> dataAssociations = new HashSet<>();
    private Set<TAssociation> associations = new HashSet<>();
    private Set<TSequenceFlow> flows = new HashSet<>();
    private Set<TMessageFlow> messageFlows = new HashSet<>();
    private List<JAXBElement<? extends TRootElement>> processes = new LinkedList<>();


    public BPMNDiagramImporterImpl() {}

    public BPMNDiagram importBPMNDiagram(String xmlProcess) throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        TDefinitions definitions;

        diagram = new BPMNDiagramImpr("");
        idToPool = new HashMap<>();
        idToNode = new HashMap<>();
        boundToFix = new HashMap<>();
        dataAssociations = new HashSet<>();
        associations = new HashSet<>();
        flows = new HashSet();
        messageFlows = new HashSet<>();
        processes = new LinkedList<>();

        try {
            // Creating the JAXBContext from the xsd file
            LOGGER.info("importBPMNDiagram: Creating JAXBcontext...");
/*
            jaxbContext = JAXBContext.newInstance( TDefinitions.class, Configurable.class,
                                                ConfigurationAnnotationAssociation.class,
                                                ConfigurationAnnotationShape.class, Variants.class );
*/
/*
            Class[] classes = { TDefinitions.class, Configurable.class,
                                                ConfigurationAnnotationAssociation.class,
                                                ConfigurationAnnotationShape.class, Variants.class };
*/
            //Map<String, String> properties = new HashMap<>();
            //properties.put(JAXBContext.JAXB_CONTEXT_FACTORY, "org.eclipse.persistence.jaxb.JAXBContextFactory");

            JAXBContext jaxbContext;
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(TDefinitions.class.getClassLoader());
                jaxbContext = JAXBContext.newInstance(TDefinitions.class, Configurable.class,
                                                ConfigurationAnnotationAssociation.class,
                                                ConfigurationAnnotationShape.class, Variants.class);

            } finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
            LOGGER.info("Created JAXBcontext!");

            //LOGGER.info("importBPMNDiagram: Creating Unmarshaller...");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            //LOGGER.info("Created Unmarshaller");

/*
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);

            File schemaFile = new File(TDefinitions.class.getClassLoader().getResource("xsd/BPMN20.xsd").getFile());
            Schema schema = sf.newSchema(schemaFile);
            unmarshaller.setSchema(schema);
*/

            //LOGGER.info("importBPMNDiagram: Unmarshalling...");
            Object o = unmarshaller.unmarshal(new StringReader(xmlProcess));
            //LOGGER.info("importBPMNDiagram: Unmarshalling... DONE!");

            if( o instanceof JAXBElement ) o = ((JAXBElement) o).getValue();
            if( !(o instanceof TDefinitions) ) throw new Exception("TDefinition element NOT found!");

            //LOGGER.info("Definitions element got!");
            definitions = (TDefinitions) o;

            for( JAXBElement<? extends TRootElement> rootElement : definitions.getRootElement() ) {

                if( rootElement.getValue() instanceof TProcess ) {
                    //LOGGER.info("TProcess found: " + rootElement.getValue().getId());
                    processes.add(rootElement);
                } else if( rootElement.getValue() instanceof TCallableElement ) {
                    //LOGGER.info("TCallableElement found: " + rootElement.getValue().getId() );
                    //TODO nothing
                } else if( rootElement.getValue() instanceof TCollaboration ) {
                    //LOGGER.info("TCollaboration found: " + rootElement.getValue().getId());
                    handleCollaboration((TCollaboration) rootElement.getValue());
                }
            }

            for( JAXBElement<? extends TRootElement> process : processes )
                if (process.getValue() instanceof TProcess) {
                    LOGGER.info("Analyzing Process: " + process.getValue().getId());
                    unfoldProcess(process.getValue(), null);
                    LOGGER.info("Added Process: " + process.getValue().getId());
                }

            buildEdges();
            fixBounds();
            checkCollapsedSubprocesses();
            checkCollapsedPools();

            //LOGGER.info("Parsing DONE!");
            return diagram;

        } catch( UnmarshalException ue ) {
            LOGGER.error("importBPMNDiagram: unable to unmarshall the xml file.", ue);
            throw ue;
        } catch( JAXBException je ) {
            LOGGER.error("importBPMNDiagram: unable to create the JAXBContext.", je);
            throw je;
        } catch(Exception e) {
            LOGGER.error("importBPMNDiagram: error creating the importBPMNDiagram.", e);
            throw e;
        }
    }

    @SuppressWarnings("nullness")
    private void handleCollaboration(TCollaboration collaboration) {

        for( TParticipant participant : collaboration.getParticipant() ) {
            Swimlane s;
            String procRef;

            if( participant.getProcessRef() != null ) {
                procRef = participant.getProcessRef().getLocalPart();
                s = diagram.addSwimlane(participant.getName(), null, SwimlaneType.POOL);
                idToPool.put(procRef, s);
                idToNode.put(participant.getId(), s);
                LOGGER.info("Added Pool: " + participant.getId() + " = " + s.getId());
            } else LOGGER.info("Unaddable Pool: " + participant.getId());
        }

        for( TMessageFlow messageFlow : collaboration.getMessageFlow() )
            messageFlows.add(messageFlow);
    }


    /**** adding method for BPMNEdge objects ****/
    private void buildEdges() {
        String src;
        String tgt;
        String direction;
        Association a;

        for( TSequenceFlow flow : flows ) {
            src = flow.getSourceRef().getId();
            tgt = flow.getTargetRef().getId();
            if( idToNode.containsKey(src) && idToNode.containsKey(tgt) ) {
                diagram.addFlow(idToNode.get(src), idToNode.get(tgt), flow.getName());
                //if( (idToNode.get(src) instanceof Event) || (idToNode.get(tgt) instanceof Event) )
                        //LOGGER.info("Adding Event Flow: " + src + " > " + tgt);
            } else LOGGER.info("[" + idToNode.containsKey(src) + ":" + idToNode.containsKey(tgt)  + "] Unfixable Flow: " + src + " > " + tgt);
        }

        for( TAssociation association : associations ) {
            src = association.getSourceRef().getLocalPart();
            tgt = association.getTargetRef().getLocalPart();
            if( idToNode.containsKey(src) && idToNode.containsKey(tgt) ) {
                    direction = association.getAssociationDirection().value();
                    a = diagram.addAssociation(idToNode.get(src), idToNode.get(tgt), BpmnAssociation.AssociationDirection.valueOf(direction.toUpperCase()));
                    //LOGGER.info("Added Association(" + a.getDirection() + "): " + src + " > " + tgt);
            } else LOGGER.info("[" + idToNode.containsKey(src) + ":" + idToNode.containsKey(tgt)  + "] Unfixable Association: " + src + " > " + tgt);
        }

        for( TDataAssociation dataAssociation: dataAssociations ) {
            tgt = dataAssociation.getTargetRef().getId();

            for( JAXBElement je : dataAssociation.getSourceRef() )
                if( je.getValue() instanceof TBaseElement ) {
                    src = ((TBaseElement) je.getValue()).getId();
                    if( idToNode.containsKey(src) && idToNode.containsKey(tgt) ) {
                        diagram.addDataAssociation(idToNode.get(src), idToNode.get(tgt), "");
                        //LOGGER.info("Added DataAssociation: " + src + " > " + tgt);
                    } else LOGGER.info("[" + idToNode.containsKey(src) + ":" + idToNode.containsKey(tgt)  + "] Unfixable DataAssociation: " + src + " > " + tgt);
                }
        }

        for( TMessageFlow messageFlow : messageFlows ) {
            src = messageFlow.getSourceRef().getLocalPart();
            tgt = messageFlow.getTargetRef().getLocalPart();
            if( idToNode.containsKey(src) && idToNode.containsKey(tgt) ) {
                diagram.addMessageFlow(idToNode.get(src), idToNode.get(tgt), "");
                //LOGGER.info("Added MessageFlow: " + src + " > " + tgt);
            } else LOGGER.info("[" + idToNode.containsKey(src) + ":" + idToNode.containsKey(tgt)  + "] Unfixable MessageFlow: " + src + " > " + tgt);
        }

    }

    private void fixBounds() {
        for( Event e : boundToFix.keySet() )
            if( idToNode.get(boundToFix.get(e)) instanceof Activity ) {
                e.setExceptionFor((Activity) idToNode.get(boundToFix.get(e)));
                ((Activity) idToNode.get(boundToFix.get(e))).incNumOfBoundaryEvents();
                LOGGER.info("FIXING - boundaryEvent: " + e.getLabel() + " > " + idToNode.get(boundToFix.get(e)).getLabel());
            } else LOGGER.info("Unfixable boundaryEvent: " + e.getLabel() + " > " + boundToFix.get(e));

        for( Event e : diagram.getEvents() )
            if( (e.getEventUse() == Event.EventUse.CATCH) && (e.getEventType() == Event.EventType.INTERMEDIATE) && (diagram.getInEdges(e).size() == 0) ) {
                if( e.getParentSubProcess() != null ) {
//                    e.setExceptionFor(e.getParentSubProcess());
//                    e.getParentSubProcess().incNumOfBoundaryEvents();
                    LOGGER.info("FIX - found ghost boundary event: " + e.getId() + " for " + e.getParentSubProcess().getId());
//                    SubProcess parentProcess = e.getParentSubProcess().getParentSubProcess();
                    Swimlane pool = e.getParentSubProcess().getParentPool();
//                    e.getParentSubProcess().getChildren().remove(e);
//                    e.setParentSubprocess(parentProcess);
                    e.getBoundingNode().setParentSubprocess(e.getParentSubProcess());
                    if(e.getParentSwimlane() != null) e.getParentSwimlane().getChildren().remove(e);
                    e.setParentSwimlane(pool);
                    for( BPMNEdge<? extends BPMNNode, ? extends BPMNNode> oe : diagram.getOutEdges(e) )
                        fixExceptionFlowParents(oe.getTarget(), pool, e.getParentSubProcess());
                } else LOGGER.error("ERROR - impossible to fix ghost boundary event: " + e.getId());
            }
    }

    private void fixExceptionFlowParents(BPMNNode n, Swimlane pool, SubProcess parentProcess) {
        if( (n.getParentPool() == pool) && (n.getParentSubProcess() == parentProcess) ) return;

        if( n.getParentSubProcess() != null ) n.getParentSubProcess().getChildren().remove(n);
        if( n.getParentPool() != null ) n.getParentPool().getChildren().remove(n);

        n.setParentSubprocess(parentProcess);
        n.setParentSwimlane(pool);

        for( BPMNEdge<? extends BPMNNode, ? extends BPMNNode> oe : diagram.getOutEdges(n) )
            fixExceptionFlowParents(oe.getTarget(), pool, parentProcess);
    }

    private void checkCollapsedSubprocesses() {
        for( SubProcess sp : diagram.getSubProcesses() )
            if( sp.getChildren().size() == 0 ) sp.setBCollapsed(true);
    }

    @SuppressWarnings("nullness")
    private void checkCollapsedPools() {
        boolean uselessPool;

        for( Swimlane p : diagram.getPools() ) {
            uselessPool = true;
            for( ContainableDirectedGraphElement n : p.getChildren() )
                    if( !(n instanceof SubProcess) ) uselessPool = false;

            if(uselessPool) {
                if( p.getChildren().size() != 0 )
                    for( ContainableDirectedGraphElement n : p.getChildren() )
                        if( n instanceof SubProcess ) ((SubProcess) n).setParentSwimlane(null);

                diagram.removeSwimlane(p);
                LOGGER.info("removed useless pool: " + p.getId());
            }
        }
    }

    /**** adding methods for BPMNNode objects ****/
    private void addDataObject(TDataObject dataObject, SubProcess parentProcess, Swimlane pool) {
        DataObject d = diagram.addDataObject(dataObject.getName());
        //TODO try adding parent process and swimlane
        d.setParentSwimlane(pool);
        d.setParentSubprocess(parentProcess);
        idToNode.put(dataObject.getId(), d);
        //LOGGER.info( (parentProcess==null ? "null" : parentProcess.getId()) + " - " + (pool==null ? "null" : pool.getId()) + " - added dataObject: " + dataObject.getId() + " = " + d.getId());
    }

    private void addTextAnnotation(TTextAnnotation textAnnotation, SubProcess parentProcess, Swimlane pool) {
        String text = "";
        if( textAnnotation.getText().getContent().get(0) instanceof String ) text = ((String)textAnnotation.getText().getContent().get(0)).replaceAll("\n", " ");
        TextAnnotation ta = diagram.addTextAnnotation(text);
        //TODO try adding parent process and swimlane
        ta.setParentSwimlane(pool);
        ta.setParentSubprocess(parentProcess);
        idToNode.put(textAnnotation.getId(), ta);
        //LOGGER.info( (parentProcess==null ? "null" : parentProcess.getId()) + " - " + (pool==null ? "null" : pool.getId()) + " - added textAnnotation:" + textAnnotation.getId() + " = " + ta.getId());
    }

    @SuppressWarnings("nullness")
    private void addEvent(TEvent event, SubProcess parentProcess, Swimlane pool) {
        Event e;
        String label = event.getName();

        Event.EventUse use = Event.EventUse.CATCH;
        Event.EventType type = Event.EventType.INTERMEDIATE;
        Event.EventTrigger trigger = Event.EventTrigger.NONE;
        Activity activity = null;
        String activityID = null;
        boolean toFix = false;
        boolean isInterrupting = true;

        if( event instanceof TCatchEvent ) {
            use = Event.EventUse.CATCH;
            if( event instanceof TStartEvent ) type = Event.EventType.START;
            else if( event instanceof TIntermediateCatchEvent ) type = Event.EventType.INTERMEDIATE;
            else if( event instanceof TBoundaryEvent ) {
                type = Event.EventType.INTERMEDIATE;
                toFix = true;
                activityID = ((TBoundaryEvent) event).getAttachedToRef().getLocalPart();
                //LOGGER.info("Boundary event found: " + activityID + " < " + event.getId() );
            }

            if( ((TCatchEvent)event).getEventDefinition().size() > 1 ) trigger = Event.EventTrigger.MULTIPLE;
            else if( (((TCatchEvent)event).getEventDefinition().size() != 0) && (((TCatchEvent)event).getEventDefinition().get(0) != null) ) {
                TEventDefinition definition = ((TCatchEvent)event).getEventDefinition().get(0).getValue();
                if( definition instanceof TTimerEventDefinition ) trigger = Event.EventTrigger.TIMER;
                else if( definition instanceof TCancelEventDefinition ) trigger = Event.EventTrigger.CANCEL;
                else if( definition instanceof TTerminateEventDefinition ) trigger = Event.EventTrigger.TERMINATE;
                else if( definition instanceof TConditionalEventDefinition ) trigger = Event.EventTrigger.CONDITIONAL;
                else if( definition instanceof TCompensateEventDefinition ) trigger = Event.EventTrigger.COMPENSATION;
                else if( definition instanceof TErrorEventDefinition ) trigger = Event.EventTrigger.ERROR;
                else if( definition instanceof TLinkEventDefinition ) trigger = Event.EventTrigger.LINK;
                else if( definition instanceof TMessageEventDefinition ) trigger = Event.EventTrigger.MESSAGE;
                else if( definition instanceof TSignalEventDefinition ) trigger = Event.EventTrigger.SIGNAL;
                else if( definition instanceof TEscalationEventDefinition ) trigger = Event.EventTrigger.NONE; //missing escalation trigger in BPMNDiagram!
            }

            for( TDataAssociation dataAssociation : ((TCatchEvent) event).getDataOutputAssociation() )
                dataAssociations.add(dataAssociation);

        } else if( event instanceof TThrowEvent ) {
            use = Event.EventUse.THROW;
            if( event instanceof TEndEvent ) type = Event.EventType.END;
            else if( event instanceof TIntermediateThrowEvent ) type = Event.EventType.INTERMEDIATE;
            else if( event instanceof TImplicitThrowEvent ) type = Event.EventType.INTERMEDIATE;

            if( ((TThrowEvent)event).getEventDefinition().size() > 1 ) trigger = Event.EventTrigger.MULTIPLE;
            else if( (((TThrowEvent)event).getEventDefinition().size() != 0) && (((TThrowEvent)event).getEventDefinition().get(0)) != null ) {
                TEventDefinition definition = ((TThrowEvent)event).getEventDefinition().get(0).getValue();
                if( definition instanceof TTimerEventDefinition ) trigger = Event.EventTrigger.TIMER;
                else if( definition instanceof TCancelEventDefinition ) trigger = Event.EventTrigger.CANCEL;
                else if( definition instanceof TTerminateEventDefinition ) trigger = Event.EventTrigger.TERMINATE;
                else if( definition instanceof TConditionalEventDefinition ) trigger = Event.EventTrigger.CONDITIONAL;
                else if( definition instanceof TCompensateEventDefinition ) trigger = Event.EventTrigger.COMPENSATION;
                else if( definition instanceof TErrorEventDefinition ) trigger = Event.EventTrigger.ERROR;
                else if( definition instanceof TLinkEventDefinition ) trigger = Event.EventTrigger.LINK;
                else if( definition instanceof TMessageEventDefinition ) trigger = Event.EventTrigger.MESSAGE;
                else if( definition instanceof TSignalEventDefinition ) trigger = Event.EventTrigger.SIGNAL;
                else if( definition instanceof TEscalationEventDefinition ) trigger = Event.EventTrigger.NONE; //missing escalation trigger in BPMNDiagram!
            }

            for( TDataAssociation dataAssociation : ((TThrowEvent) event).getDataInputAssociation() )
                dataAssociations.add(dataAssociation);
        }

        e = diagram.addEvent(label, type, trigger, use, parentProcess, isInterrupting, activity);
        e.setParentSwimlane(pool);
        if( toFix ) boundToFix.put(e, activityID);
        idToNode.put(event.getId(), e);
        LOGGER.info("Added Event(" + e.getEventType() + "," + e.getEventTrigger() + "): " + e.getLabel() + " " + event.getId() + " = " + e.getId());
    }

    private void addTask(TTask task, SubProcess parentProcess, Swimlane pool) {
        Activity a;
        String label = task.getName();

        boolean isBCompensation = task.isIsForCompensation();
        boolean isBMultiinstance = false;
        boolean isBLooped = false;
        boolean isBAdhoc = false;
        boolean isBCollapsed = false;

        JAXBElement loopCharacteristics = task.getLoopCharacteristics();

        if( loopCharacteristics != null ) {
            if( loopCharacteristics.getValue() instanceof TMultiInstanceLoopCharacteristics ) isBMultiinstance = true;
            else isBLooped = true;
        }

        for( TDataAssociation dataAssociation : task.getDataOutputAssociation() )
            dataAssociations.add(dataAssociation);

        for( TDataAssociation dataAssociation : task.getDataInputAssociation() )
            dataAssociations.add(dataAssociation);

        a = diagram.addActivity(label, isBLooped, isBAdhoc, isBCompensation, isBMultiinstance, isBCollapsed, parentProcess);
        a.setParentSwimlane(pool);
        idToNode.put(task.getId(), a);
        //LOGGER.info("(" + isBCompensation + ") Added Activity: " + task.getId() + " = " + a.getId());
    }

    private void addGateway(TGateway gateway, SubProcess parentProcess, Swimlane pool) {
        Gateway g;
        String label = gateway.getName();
        Gateway.GatewayType type = Gateway.GatewayType.DATABASED;

        if(gateway instanceof TComplexGateway) type = Gateway.GatewayType.COMPLEX;
        else if(gateway instanceof TParallelGateway) type = Gateway.GatewayType.PARALLEL;
        else if(gateway instanceof TEventBasedGateway) type = Gateway.GatewayType.EVENTBASED;
        else if(gateway instanceof TExclusiveGateway)  type = Gateway.GatewayType.DATABASED;
        else if(gateway instanceof TInclusiveGateway)  type = Gateway.GatewayType.INCLUSIVE;

        g = diagram.addGateway(label, type, parentProcess);
        g.setParentSwimlane(pool);
        idToNode.put(gateway.getId(), g);
        //LOGGER.info("Added gateway: " + gateway.getId() + " = " + g.getId());
    }

    private void addCallActivity(TCallActivity callActivity, SubProcess parentProcess, Swimlane pool) {
        CallActivity ca;
        String label = callActivity.getName();

        boolean isBCompensation = callActivity.isIsForCompensation();
        boolean isBMultiinstance = false;
        boolean isBLooped = false;
        boolean isBAdhoc = false;
        boolean isBCollapsed = false;

        JAXBElement loopCharacteristics = callActivity.getLoopCharacteristics();

        if( loopCharacteristics != null ) {
            if( loopCharacteristics.getValue() instanceof TMultiInstanceLoopCharacteristics ) isBMultiinstance = true;
            else isBLooped = true;
        }

        for( TDataAssociation dataAssociation : callActivity.getDataOutputAssociation() )
            dataAssociations.add(dataAssociation);

        for( TDataAssociation dataAssociation : callActivity.getDataInputAssociation() )
            dataAssociations.add(dataAssociation);

        ca = diagram.addCallActivity(label, isBLooped, isBAdhoc, isBCompensation, isBMultiinstance, isBCollapsed, parentProcess);
        ca.setParentSwimlane(pool);
        idToNode.put(callActivity.getId(), ca);
        //LOGGER.info("(" + isBCompensation + ") Added CallActivity: " + callActivity.getId() + " = " + ca.getId());
    }

    private void addSubProcess(TSubProcess subProcess, SubProcess parentProcess, Swimlane pool) {
        SubProcess sp;
        String label = subProcess.getName();

        boolean isBCompensation = subProcess.isIsForCompensation();
        boolean isTriggeredByEvent = subProcess.isTriggeredByEvent();
        boolean isBMultiinstance = false;
        boolean isBLooped = false;
        boolean isBAdhoc = false;
        boolean isBCollapsed = false;
        JAXBElement loopCharacteristics = subProcess.getLoopCharacteristics();

        if( subProcess instanceof TAdHocSubProcess ) isBAdhoc = true;
        if( loopCharacteristics != null ) {
            if( loopCharacteristics.getValue() instanceof TMultiInstanceLoopCharacteristics ) isBMultiinstance = true;
            else isBLooped = true;
        }

        for( TDataAssociation dataAssociation : subProcess.getDataOutputAssociation() )
            dataAssociations.add(dataAssociation);

        for( TDataAssociation dataAssociation : subProcess.getDataInputAssociation() )
            dataAssociations.add(dataAssociation);

        sp = diagram.addSubProcess(label, isBLooped, isBAdhoc, isBCompensation, isBMultiinstance, isBCollapsed, isTriggeredByEvent, parentProcess);
        sp.setParentSwimlane(pool);
        idToNode.put(subProcess.getId(), sp);
        LOGGER.info("Analyzing subProcess: " + subProcess.getId());
        unfoldProcess(subProcess, sp);
        LOGGER.info("Added SubProcess: " + subProcess.getId() + " = " + sp.getId());
    }

    @SuppressWarnings("nullness")
    private void unfoldProcess(TBaseElement root, @Nullable SubProcess parentProcess) {

        if( root instanceof TProcess ) {
            Swimlane pool = idToPool.get(root.getId());
            if( (pool == null) && (processes.size() > 1) ) {
                pool = diagram.addSwimlane("unknown", null, SwimlaneType.POOL);
                LOGGER.info("Added Pool unknown: " + pool.getId());
            }

            /**** Artifact contains TextAnnotation and Association objects ****/
            for( JAXBElement artifact : ((TProcess) root).getArtifact() ) {

                if( artifact.getValue() instanceof TAssociation )
                    this.associations.add((TAssociation) artifact.getValue());

                if( artifact.getValue() instanceof TTextAnnotation )
                    this.addTextAnnotation((TTextAnnotation) artifact.getValue(), null,  pool);
            }

            /**** FlowElement contains Flow, Event, DataObject, Activity, Gateway, CallActivity and SubProcess objects ****/
            for( JAXBElement flowElement : ((TProcess) root).getFlowElement() ) {

                if( flowElement.getValue() instanceof TSubProcess )
                    this.addSubProcess((TSubProcess) flowElement.getValue(), null, pool);

                else if( flowElement.getValue() instanceof TEvent )
                    this.addEvent((TEvent) flowElement.getValue(), null, pool);

                else if( flowElement.getValue() instanceof TTask )
                    this.addTask((TTask) flowElement.getValue(), null, pool);

                else if( flowElement.getValue() instanceof TGateway )
                    this.addGateway((TGateway) flowElement.getValue(), null, pool);

                else if( flowElement.getValue() instanceof TCallActivity )
                    this.addCallActivity((TCallActivity) flowElement.getValue(), null, pool);

                else if( flowElement.getValue() instanceof TDataObject )
                    this.addDataObject((TDataObject) flowElement.getValue(), null, pool);

                else if( flowElement.getValue() instanceof TSequenceFlow )
                    this.flows.add((TSequenceFlow) flowElement.getValue());
            }

            for( TLaneSet laneSet : ((TProcess) root).getLaneSet() ) addLanes(laneSet, pool);

        } else if( root instanceof TSubProcess ) {
            /**** Artifact contains TextAnnotation and Association objects ****/
            for( JAXBElement artifact : ((TSubProcess) root).getArtifact() ) {

                if( artifact.getValue() instanceof TAssociation )
                    this.associations.add((TAssociation) artifact.getValue());

                if( artifact.getValue() instanceof TTextAnnotation )
                    this.addTextAnnotation((TTextAnnotation) artifact.getValue(), parentProcess, null);
            }

            /**** FlowElement contains Flow, Event, DataObject, Activity, Gateway, CallActivity and SubProcess objects ****/
            for( JAXBElement flowElement : ((TSubProcess) root).getFlowElement() ) {

                if( flowElement.getValue() instanceof TSubProcess )
                    this.addSubProcess((TSubProcess) flowElement.getValue(), parentProcess, null);

                else if( flowElement.getValue() instanceof TEvent )
                    this.addEvent((TEvent) flowElement.getValue(), parentProcess, null);

                else if( flowElement.getValue() instanceof TTask )
                    this.addTask((TTask) flowElement.getValue(), parentProcess, null);

                else if( flowElement.getValue() instanceof TGateway )
                    this.addGateway((TGateway) flowElement.getValue(), parentProcess, null);

                else if( flowElement.getValue() instanceof TCallActivity )
                    this.addCallActivity((TCallActivity) flowElement.getValue(), parentProcess, null);

                else if( flowElement.getValue() instanceof TDataObject )
                    this.addDataObject((TDataObject) flowElement.getValue(), parentProcess, null);

                else if( flowElement.getValue() instanceof TSequenceFlow )
                    this.flows.add((TSequenceFlow) flowElement.getValue());
            }

            for( TLaneSet laneSet : ((TSubProcess) root).getLaneSet() ) addLanes(laneSet, null);
        }
    }

    private void addLanes(TLaneSet laneSet, Swimlane parent) {
        for( TLane lane : laneSet.getLane() ) {
            Swimlane s = diagram.addSwimlane(lane.getName(), parent, SwimlaneType.LANE);

            for( JAXBElement node : lane.getFlowNodeRef() ) {
                if( node.getValue() instanceof TBaseElement ) {
                    String id = ((TBaseElement) node.getValue()).getId();
                    if( idToNode.containsKey(id) ) {
                        idToNode.get(id).setParentSwimlane(s);
                        //LOGGER.info("Lane set.");
                    } else {
                        LOGGER.info("Unsettable lane for: " + id);
                    }
                } else {
                    LOGGER.info("ERROR setting a lane for a node that is instanceof: " + node.getValue().getClass().getName());
                }
                //LOGGER.info("Trying to set Lane > " + lane.getId() + " for:  " + id);
            }

            TLaneSet childSet = lane.getChildLaneSet();
            if( childSet != null ) addLanes(childSet, s);
        }
    }

    private class BPMNDiagramImpr extends BPMNDiagramImpl {

        private BPMNDiagramImpr(String label) { super(label); }

        @Override
        public void removeEdge(DirectedGraphEdge edge) {
            if( edge instanceof Flow ) {
                this.flows.remove(edge);
            } else if( edge instanceof MessageFlow ) {
                this.messageFlows.remove(edge);
            } else if( edge instanceof Association ) {
                this.associations.remove(edge);
            } else if( edge instanceof DataAssociation ) {
                this.dataAssociations.remove(edge);
            }

            try {
                this.graphElementRemoved(edge);
            } catch( NullPointerException npe ) {
                LOGGER.info("NullPointerException removing the edge (" + edge.getClass().getSimpleName() + "): " + edge.getLabel());
            }
        }

        @Override
        public void removeNode(DirectedGraphNode node) {
            super.removeNode(node);
            try {
                this.graphElementRemoved(node);
            } catch( NullPointerException npe ) {
                LOGGER.info("NullPointerException removing the node (" + node.getClass().getSimpleName() + "): " + node.getLabel());
            }
        }
    }

}
