package org.apromore.item.spi;

import java.io.InputStream;
import java.io.IOException;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

/**
 * Services required to provide a concrete subtype of {@link Item}.
 */
public interface ItemPlugin<T extends Item> {  // TODO: Generic parameter

    /**
     * @param inputStream  no guarantee is made as to whether the stream will be closed after this method returns
     * @return a concrete subclass of {@link Item}
     * @throws IOException  if the <var>inputStream</var> cannot be read
     * @throws ItemFormatException  if the <var>inputStream</var> cannot be parsed
     * @throws NotAuthorizedException  if the caller isn't authorized to create the {@link Item}
     */
    public T create(InputStream inputStream) throws IOException, ItemFormatException, NotAuthorizedException;  // TODO: return type should be expressed generically as "some extension of Item"

    public String getType();

    /**
     * Used to convert instances of {@link Item} to their particular concrete subtype (e.g. BPMNItem)
     *
     * @param item  an existing item of the subtype managed by this plugin
     * @return the corresponding instance of a concrete subclass of {@link Item}
     * @throws ItemTypeException  if the <var>item</var> is not an instance of the subtype managed by this plugin
     */
    public T toConcreteItem(Item item) throws ItemTypeException;  // TODO: return type should be expressed generically as "some extension of Item"
}
