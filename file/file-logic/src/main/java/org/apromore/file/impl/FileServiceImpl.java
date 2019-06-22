package org.apromore.file.impl;

/*-
 * #%L
 * Apromore :: file :: file-logic
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import com.google.common.io.ByteStreams;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apromore.file.File;
import org.apromore.file.FileService;
import org.apromore.file.jpa.FileDAO;
import org.apromore.file.jpa.FileRepository;
import org.apromore.file.spi.FilePlugin;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import static org.osgi.service.component.annotations.FieldOption.UPDATE;
import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Both a {@link FileService} and an {@link ItemPlugin} providing
 * file support.
 */
@Component(service = {FileService.class, ItemPlugin.class})
public final class FileServiceImpl implements FileService,
    ItemPlugin<File> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(FileServiceImpl.class);

    // OSGi services

    /** Factory service for BPMN-specific data access objects. */
    private FileRepository  fileRepository;

    /** Utility service for {@link ItemPlugin}s. */
    private ItemPluginContext itemPluginContext;

    /**
     * OSGi service bind handler.
     *
     * @param repository  factory service for file-specific data access objects
     */
    @Reference
    public void setFileRepository(final FileRepository repository) {
        this.fileRepository = repository;
    }

    /**
     * OSGi service bind handler.
     *
     * @param context  newly-bound service
     */
    @Reference
    public void setItemPluginContext(final ItemPluginContext context) {
        this.itemPluginContext = context;
    }

    /** The dynamically populated list of file plugins. */
    @Reference(bind = "onBind", cardinality = MULTIPLE, fieldOption = UPDATE,
               policy = DYNAMIC, unbind = "onUnbind", updated = "onUpdated")
    private List<FilePlugin> filePlugins;


    // ItemPlugin implementation

    @Override
    public File create(final InputStream inputStream)
        throws ItemFormatException, NotAuthorizedException {

        try {
            return createFile(inputStream);

        } catch (IOException e) {
            throw new Error("Unable to create file from stream", e);
        }
    }

    @Override
    public String getType() {
        return File.TYPE;
    }

    @Override
    public File toConcreteItem(final Item item)
        throws ItemTypeException {

        FileDAO dao = fileRepository.get(item.getId());
        if (dao == null) {
             throw new ItemTypeException(getType(), item.getType());
        }
        return new FileImpl(item, dao);
    }


    // FileService implementation

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public File createFile(final InputStream inputStream)
        throws IOException, NotAuthorizedException {

        // Materializing the inputStream into memory isn't ideal....
        //byte[] buffer = inputStream.readAllBytes();
        byte[] buffer = ByteStreams.toByteArray(inputStream);  // JDK 1.8
        inputStream.close();

        Item item = this.itemPluginContext.create(getType());

        FileDAO dao = new FileDAO();
        dao.setId(item.getId());
        dao.setContent(buffer);
        fileRepository.add(dao);

        return new FileImpl(item, dao);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public File getById(final Long id) throws NotAuthorizedException {
        Item        item = this.itemPluginContext.getById(id);
        FileDAO dao  = fileRepository.get(id);
        return new FileImpl(item, dao);
    }


    // ItemPlugins reference list listener implementation

    /**
     * @param filePlugin  the newly-bound plugin
     * @param properties  unused
     */
    public void onBind(final FilePlugin filePlugin, final Map properties) {
        LOGGER.info("Bind file plugin " + filePlugin + " with properties "
            + properties);
    }

    /**
     * @param filePlugin  the newly-unbound plugin
     * @param properties  unused
     */
    public void onUnbind(final FilePlugin filePlugin, final Map properties) {
        LOGGER.info("Unbind file plugin " + filePlugin + " with properties "
            + properties);
    }

    /**
     * @param filePlugin  the newly-updated plugin
     * @param properties  unused
     */
    public void onUpdated(final FilePlugin filePlugin, final Map properties) {
        LOGGER.info("Updated file plugin " + filePlugin + " with properties "
            + properties);
    }
}
