package com.dustinscharf.sepsismonitor.data;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public interface IDataAccess {
    public static IDataAccess getInstance() {
        return DataAccess.getInstance();
    }

    public void fetch(ICallback<Map<String, Object>> callback);

    public void fetchContainer(String containerKey, ICallback<Map<String, Object>> callback);

    public void fetchContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback);

    public void subscribe(ICallback<Map<String, Object>> callback);

    public void subscribeContainer(String containerKey, ICallback<Map<String, Object>> callback);

    public void subscribeContainerItem(String containerKey, String itemKey, ICallback<Map<String, Object>> callback);

    public void addItemToContainer(String containerKey, String itemKey, Map<String, Object> addItem);

    public void editItemInContainer(String containerKey, String itemKey, Map<String, Object> editItem);

    public void removeItemFromContainer(String containerKey, String itemKey);
}
