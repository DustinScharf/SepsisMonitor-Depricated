package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.HashMap;
import java.util.Map;

public class Staff implements IStaff {
    private static final String containerKey = "staff";

    private final IDataAccess dataAccess;

    private final String id;

    public Staff(String id) {
        this.dataAccess = IDataAccess.getInstance();

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
    public void getPassword(ICallback<String> callback) {
        this.dataAccess.subscribeContainerItem(containerKey, this.id, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                callback.onCallback(stringObjectMap.get("password").toString()); // TODO NULL CHECK
            }
        });
    }

    @Override
    public void isLMMP(ICallback<Boolean> callback) {
        this.dataAccess.subscribeContainerItem(containerKey, this.id, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                callback.onCallback((Boolean) stringObjectMap.get("isLMMP")); // TODO CAST CHECK
            }
        });
    }

    @Override
    public void getPatients(ICallback<Map<IPatient, Boolean>> callback) {
        this.dataAccess.subscribeContainerItem(containerKey, this.id, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                Map<IPatient, Boolean> patients = new HashMap<>();

                Map<String, Object> rawPatientsMap = (Map<String, Object>) stringObjectMap.get("patients"); // TODO CAST CHECK
                for (String patientKey : rawPatientsMap.keySet()) { // TODO NULL CHECK
                    patients.put(IPatient.getNewInstance(patientKey), true);
                }

                callback.onCallback(patients);
            }
        });
    }
}
