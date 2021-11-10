package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public class Patient implements IPatient {
    private static final String containerKey = "patients";

    private final IDataAccess dataAccess;

    private final String id;

    public Patient(String id) {
        this.dataAccess = IDataAccess.getNewInstance("hospital");

        this.id = id;
    }

    @Override
    public void getFirstName(ICallback<String> callback) {
        this.dataAccess.subscribeContainerItem(containerKey, this.id, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                callback.onCallback(stringObjectMap.get("firstName").toString()); // TODO NULL CHECK
            }
        });
    }

    @Override
    public void getLastName(ICallback<String> callback) {
        this.dataAccess.subscribeContainerItem(containerKey, this.id, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                callback.onCallback(stringObjectMap.get("lastName").toString()); // TODO NULL CHECK
            }
        });
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void getPhase(ICallback<Long> callback) {
        this.dataAccess.subscribeContainerItem(containerKey, this.id, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                callback.onCallback((Long) stringObjectMap.get("phase")); // TODO NULL CHECK
            }
        });
    }
}
