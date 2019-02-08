package org.apromore.folder_ui;

import java.io.IOException;
import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.PathAlreadyExistsException;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.ItemService;
import org.apromore.item.NotAuthorizedException;
import org.apromore.ui.UIService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
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
 * {@link UIPlugin} for the Item/Upload File to Folder command.
 */
@Component(service = {UIPlugin.class})
public final class UploadItemUIPlugin extends AbstractUIPlugin {

    /**
     * When creating a new folder, this is the highest number that
     * will be appended to the default name to avoid clashes.
     *
     * For instance, "New folder 100".
     */
    private static final int ITEM_NAME_RETRY_LIMIT = 100;

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(UploadItemUIPlugin.class);

    /**
     * Used to store uploaded items.
     */
    @Reference
    private FolderService folderService;

    /**
     * Used to store uploaded items.
     */
    @Reference
    private ItemService itemService;

    /**
     * Used to prompt the user to login if they try to upload while
     * unauthenticated.
     */
    @Reference
    private UIService uiService;

    /** Sole constructor. */
    public UploadItemUIPlugin() {
        super("item.group", "uploadItem.label");
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
        uiService.authenticate(getLocalizedString("uploadItem.mustHaveOwner"),
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

                            int counter = 1;
                            String newItemName = "New item";
                            Folder parentFolder = (Folder) context
                                .getSessionAttribute(
                                    SelectItemUIPlugin.USER_FOLDER_ATTRIBUTE);

                            // Beware: this loop is awful code and you need to
                            // be particularly alert while modifying it
                            do {
                                try {
                                    Item item = itemService.create(
                                        media.getStreamData());
                                    folderService.createPath(
                                        parentFolder,
                                        newItemName,
                                        item);

                                    context.getParentComponent().invalidate();
                                    return;

                                } catch (PathAlreadyExistsException e) {
                                    // Generate a new folder name and retry
                                    counter++;
                                    newItemName = "New item " + counter;
                                    continue;

                                } catch (IOException
                                    | ItemFormatException
                                    | NotAuthorizedException e) {

                                    e.printStackTrace();
                                    Messagebox.show("Upload failed for "
                                        + media.getName() + "\n"
                                        + e.getMessage(),
                                        "Attention",
                                        Messagebox.OK,
                                        Messagebox.ERROR);
                                    return;
                                }
                            } while (counter <= ITEM_NAME_RETRY_LIMIT);

                            // Achievement!  Created more than
                            // ITEM_NAME_RETRY_LIMIT items without renaming any.
                            // Proud of yourself?
                            Messagebox.show("Too many new items.\n"
                                + "Rename or delete some items.",
                                "Attention", Messagebox.OK, Messagebox.ERROR);
                        }
                    }
                });
            }
        }, null);  // do nothing if login is cancelled
    }
}
