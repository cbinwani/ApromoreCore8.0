package org.apromore.item.spi;

import java.io.InputStream;
import java.io.IOException;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

/**
 * Services required to provide a concrete subtype of {@link Item}.
 *
 * @param <T>  the interface of the cpncrete subtype
 */
public interface ItemPlugin<T extends Item> {

    /**
     * @param inputStream  no guarantee is made as to whether the stream will be
     *     closed after this method returns
     * @return a concrete subclass of {@link Item}
     * @throws IOException  if the <var>inputStream</var> cannot be read
     * @throws ItemFormatException  if the <var>inputStream</var> cannot be
     *     parsed
     * @throws NotAuthorizedException  if the caller isn't authorized to create
     *     the {@link Item}
     */
    T create(InputStream inputStream) throws IOException, ItemFormatException,
       NotAuthorizedException;

    String getType();

    /**
     * Used to convert instances of {@link Item} to their particular concrete
     * subtype (for example, {@link org.apromore.bpmn_item.BPMNItem}).
     *
     * @param item  an existing item of the subtype managed by this plugin
     * @return the corresponding instance of a concrete subclass of {@link Item}
     * @throws ItemTypeException  if the <var>item</var> is not an instance of
     *     the subtype managed by this plugin
     */
    T toConcreteItem(Item item) throws ItemTypeException;
}
