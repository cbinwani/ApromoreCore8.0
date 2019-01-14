package org.apromore.xes_item.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apromore.xes_item.jpa.XesItemDao;
import org.apromore.xes_item.jpa.XesItemRepository;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XESItemPluginImpl implements ItemPlugin<XESItem>, XESItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XESItemPluginImpl.class);

    private ItemPluginContext itemPluginContext;
    private XesItemRepository xesItemRepository;

    public XESItemPluginImpl(ItemPluginContext itemPluginContext) {
        if (itemPluginContext == null) {
            throw new IllegalArgumentException("Item plugin context missing");
        }

        this.itemPluginContext = itemPluginContext;
    }

    public void setXesItemRepository(XesItemRepository newRepository) {
        this.xesItemRepository = newRepository;
    }

    // ItemPlugin implementation

    public XESItem create(InputStream inputStream) throws ItemFormatException, NotAuthorizedException {
        return createXESItem(new StreamSource(inputStream));
    }

    public String getType() {
        return "XES";  // TODO: make this a constant
    }

    public XESItem toConcreteItem(Item item) throws ItemTypeException {
        XesItemDao dao = xesItemRepository.get(item.getId());
        if (dao == null) {
             throw new ItemTypeException(getType(), item.getType());
        }
        XESItemImpl xesItemImpl = new XESItemImpl(dao);
        xesItemImpl.item = item;
        return xesItemImpl;
    }

    // XESItemService implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public XESItem createXESItem(Source source) throws ItemFormatException, NotAuthorizedException {  // TODO: make transactional
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(baos));

            // Validate
            try {
                XesXmlParser parser = new XesXmlParser();
                List<XLog> validated = parser.parse(new ByteArrayInputStream(baos.toByteArray()));
                if (validated.size() != 1) {
                    throw new Exception("File contained " + validated.size() + " XES event logs");
                }
                XLog log = validated.get(0);
                if (log == null) {
                    throw new Exception("File contained 1 XES event log, but it was null (which makes no sense, so rejecting)");
                }
                LOGGER.debug("Successfully parsed XES log"); 

            } catch (Exception e) {
                throw new ItemFormatException(getType(), e);
            }

            // Construct and persist

            Item item = this.itemPluginContext.create(getType());

            XesItemDao dao = new XesItemDao();
            dao.setId(item.getId());
            dao.setXmlSerialization(baos.toByteArray());
            xesItemRepository.add(dao);

            XESItemImpl xesItem = new XESItemImpl(dao);
            xesItem.item = item;

            return xesItem;

        } catch (TransformerConfigurationException e) {
            throw new Error("Server configuration error", e);

        } catch (TransformerException e) {
            throw new ItemFormatException(getType(), e);
        }
    }

    public XESItem getById(Long id) throws NotAuthorizedException {
        XesItemDao dao = xesItemRepository.get(id);
        XESItemImpl xesItemImpl = new XESItemImpl(dao);
        xesItemImpl.item = this.itemPluginContext.getById(id);
        return xesItemImpl;
    }
}
