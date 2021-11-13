package com.dustinscharf.sepsismonitor.data;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

/**
 * Class design to access a database with a structure
 * where every object type its own list containing items of this type
 */
public interface IDataAccess {

    /**
     * Gives a instance of the DataAccess implementation
     *
     * @return instance of the DataAccess implementation
     */
    public static IDataAccess getInstance() {
        return DataAccess.getInstance();
    }

    /**
     * Fetches ("one time load") the whole database
     *
     * @param callback a callback that is fired when the data is received
     */
    public void fetch(ICallback<Map<String, Object>> callback);

    /**
     * Fetches ("one time load") all objects of a specific type from the database
     *
     * @param containerKey the type of the objects
     * @param callback     a callback that is fired when the data is received
     */
    public void fetchContainer(String containerKey, ICallback<Map<String, Object>> callback);

    /**
     * Fetches ("one time load") a single objects of a specific type from the database
     *
     * @param containerKey the type of the object
     * @param itemKey      the unique key of the object
     * @param callback     a callback that is fired when the data is received
     */
    public void fetchContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") the whole database
     *
     * @param callback a callback that is fired whenever data in the database changes and the data is fully received
     */
    public void subscribe(ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") all objects of a specific type from the database
     *
     * @param containerKey the type of the objects
     * @param callback     a callback that is fired whenever data in the database changes and the data is fully received
     */
    public void subscribeContainer(String containerKey, ICallback<Map<String, Object>> callback);

    /**
     * Subscribes ("load every change, whenever it will happen") a single objects of a specific type from the database
     *
     * @param containerKey the type of the object
     * @param itemKey      the unique key of the object
     * @param callback     a callback that is fired whenever data in the database changes and the data is fully received
     */
    public void subscribeContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback);

    /**
     * Adds an item to a specific item list
     * (if item list does not exist, it will be created)
     *
     * @param containerKey the type of the object to add
     * @param itemKey      the key of the object to add
     * @param addItem      the item to add
     */
    public void addItemToContainer(String containerKey, String itemKey, Map<String, Object> addItem);

    /**
     * Edit an item in a specific list
     *
     * @param containerKey the type of the object to edit
     * @param itemKey      the key of the object to edit
     * @param editItem     a map containing all the object changed like ("name", "Siegfried")
     */
    public void editItemInContainer(String containerKey, String itemKey, Map<String, Object> editItem);

    /**
     * Removed an item from an item list
     *
     * @param containerKey the type of the object to remove
     * @param itemKey      the key of the object to remove
     */
    public void removeItemFromContainer(String containerKey, String itemKey);
}
