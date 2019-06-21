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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.transaction.Transactional;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apromore.file.File;
import org.apromore.file.FileService;
import org.apromore.file.jpa.FileDAO;
import org.apromore.file.jpa.FileRepository;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Both a {@link FileService} and an {@link ItemPlugin} providing
 * file support.
 */
@Component(service = {FileService.class, ItemPlugin.class})
public final class FilePluginImpl implements FileService,
    ItemPlugin<File> {

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


    // ItemPlugin implementation

    @Override
    public File create(final InputStream inputStream)
        throws ItemFormatException, NotAuthorizedException {

        return createFile(inputStream);
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
    public File createFile(final InputStream in)
        throws NotAuthorizedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TransformerFactory.newInstance()
                              .newTransformer()
                              .transform(new StreamSource(in),
                                         new StreamResult(baos));

            Item item = this.itemPluginContext.create(getType());

            FileDAO dao = new FileDAO();
            dao.setId(item.getId());
            dao.setContent(baos.toByteArray());
            fileRepository.add(dao);

            return new FileImpl(item, dao);

        } catch (TransformerConfigurationException e) {
            throw new Error("Server configuration error", e);

        } catch (TransformerException e) {
            throw new Error("Server error", e);
        }
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public File getById(final Long id) throws NotAuthorizedException {
        Item        item = this.itemPluginContext.getById(id);
        FileDAO dao  = fileRepository.get(id);
        return new FileImpl(item, dao);
    }
}
