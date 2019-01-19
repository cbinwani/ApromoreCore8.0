package org.apromore.item_ui;

import java.io.IOException;
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

@Component(service = {UIPlugin.class})
public final class UploadItemUIPlugin extends AbstractUIPlugin {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(UploadItemUIPlugin.class);

    @Reference
    private ItemService itemService;

    @Reference
    private UIService uiService;

    /*
    public UploadItemUIPlugin(ItemService itemService, UIService uiService) {
        this.itemService = itemService;
        this.uiService = uiService;
    }
    */

    @Override
    public String getGroupLabel() {
        return "Item";
    }

    /** @return the text appearing on the plugin's menuitem */
    @Override
    public String getLabel() {
        return "Upload File";
    }

    /** @return uploads are enabled as long as the user is logged in */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return true; //context.getUser() != null;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(final UIPluginContext context) {
        uiService.authenticate("All uploads must be have an owner.",
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
                                itemService.create(media.getStreamData());

                            } catch (IOException | ItemFormatException
                                | NotAuthorizedException e) {
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
