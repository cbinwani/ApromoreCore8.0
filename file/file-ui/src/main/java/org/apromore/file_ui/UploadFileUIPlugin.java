package org.apromore.file_ui;

/*-
 * #%L
 * Apromore :: file :: file-ui
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

import java.io.IOException;
import org.apromore.file.FileService;
import org.apromore.item.NotAuthorizedException;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.apromore.user.UserService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;

/**
 * {@link UIPlugin} for the Item/Upload File command.
 */
@Component(service = {UIPlugin.class})
public final class UploadFileUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(UploadFileUIPlugin.class);

    /**
     * Used to store uploaded files.
     */
    @Reference
    private FileService fileService;

    /**
     * Used to prompt the user to login if they try to upload while
     * unauthenticated.
     */
    @Reference
    private UserService userService;

    /** Sole constructor. */
    public UploadFileUIPlugin() {
        super("item.group", "uploadFile.label", "uploadFile.iconSclass");
    }

    /** {@inheritDoc}
     *
     * This implementation is always enabled.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return true; //context.getUser() != null;
    }

    /** {@inheritDoc}
     *
     * This implementation prompts the user to upload a file.
     *
     * Upload is only permitted when the user is authenticated.
     * If the user session isn't yet authenticated, they will be prompted
     * to do so.
     * Upload will be aborted if the authentication fails.
     */
    @Override
    @SuppressWarnings("checkstyle:JavadocMethod")  // buggy @inheritDoc warning
    public void execute(final UIPluginContext context) {
        userService.authenticate(getLocalizedString("uploadFile.mustHaveOwner"),
            new Runnable() {

            /**
             * The maximal allowed number of files that an user can upload at
             * once.
             * If non-positive, 1 is assumed.
             *
             * @see {@link org.zkoss.zul.Fileupload#get(int)}
             */
            static final int  MAX_ALLOWED_FILES = 100;

            public void run() {  // perform the upload if login is successful
                Fileupload.get(MAX_ALLOWED_FILES,
                    new EventListener<UploadEvent>() {

                    public void onEvent(final UploadEvent uploadEvent) {
                        for (Media media: uploadEvent.getMedias()) {
                            LOGGER.debug("Upload file " + media.getName());
                            try {
                                fileService.createFile(media.getStreamData());

                            } catch (IOException | NotAuthorizedException e) {
                                e.printStackTrace();
                                Messagebox.show("Upload failed for "
                                    + media.getName() + "\n" + e.getMessage(),
                                    "Attention",
                                    Messagebox.OK,
                                    Messagebox.ERROR);
                            }
                        }
                    }
                });
            }
        }, null);  // do nothing if login is cancelled
    }
}
