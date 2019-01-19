package org.apromore.xes_item.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import javax.transaction.Transactional;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.xes_item.XESItem;
import org.apromore.xes_item.XESItemService;
import org.apromore.xes_item.jpa.XESItemDAO;
import org.apromore.xes_item.jpa.XESItemRepository;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = {ItemPlugin.class, XESItemService.class})
public class XESItemPluginImpl implements ItemPlugin<XESItem>, XESItemService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(XESItemPluginImpl.class);

    @Reference
    private ItemPluginContext itemPluginContext;

    @Reference
    private XESItemRepository xesItemRepository;

    /*
    public XESItemPluginImpl(ItemPluginContext itemPluginContext) {
        if (itemPluginContext == null) {
            throw new IllegalArgumentException("Item plugin context missing");
        }

        this.itemPluginContext = itemPluginContext;
    }

    public void setXesItemRepository(XesItemRepository newRepository) {
        this.xesItemRepository = newRepository;
    }
    */

    // ItemPlugin implementation

    public XESItem create(InputStream inputStream) throws ItemFormatException,
        NotAuthorizedException {

        return createXESItem(new StreamSource(inputStream));
    }

    public String getType() {
        return "XES";  // TODO: make this a constant
    }

    public XESItem toConcreteItem(Item item) throws ItemTypeException {
        XESItemDAO dao = xesItemRepository.get(item.getId());
        if (dao == null) {
             throw new ItemTypeException(getType(), item.getType());
        }
        return new XESItemImpl(item, dao);
    }

    // XESItemService implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public XESItem createXESItem(Source source) throws ItemFormatException,
        NotAuthorizedException {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TransformerFactory.newInstance()
                              .newTransformer()
                              .transform(source, new StreamResult(baos));

            // Validate
            try {
                XesXmlParser parser = new XesXmlParser();
                List<XLog> validated =
                    parser.parse(new ByteArrayInputStream(baos.toByteArray()));
                if (validated.size() != 1) {
                    throw new Exception("File contained " + validated.size()
                        + " XES event logs");
                }
                XLog log = validated.get(0);
                if (log == null) {
                    throw new Exception("File contained 1 XES event log, but "
                        + "it was null (which makes no sense, so rejecting)");
                }
                LOGGER.debug("Successfully parsed XES log");

            } catch (Exception e) {
                throw new ItemFormatException(getType(), e);
            }

            // Construct and persist

            Item item = this.itemPluginContext.create(getType());

            XESItemDAO dao = new XESItemDAO();
            dao.setId(item.getId());
            dao.setXmlSerialization(baos.toByteArray());
            xesItemRepository.add(dao);

            return new XESItemImpl(item, dao);

        } catch (TransformerConfigurationException e) {
            throw new Error("Server configuration error", e);

        } catch (TransformerException e) {
            throw new ItemFormatException(getType(), e);
        }
    }

    public XESItem getById(Long id) throws NotAuthorizedException {
        Item item = this.itemPluginContext.getById(id);
        XESItemDAO dao = xesItemRepository.get(id);
        return new XESItemImpl(item, dao);
    }
}
