package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.HashMap;
import java.util.Map;

public class Hospital implements IHospital {
    private final IDataAccess dataAccess;

    public Hospital() {
        this.dataAccess = IDataAccess.getInstance();
    }

    // Hospital access
    public void fetchHospital(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetch(callback);
    }

    public void subscribeHospital(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribe(callback);
    }

    // Staff access
    public void fetchStaff(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainer("staff", callback);
    }

    public void fetchSingleStaff(String staffId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainerItem("staff", staffId, callback);
    }


    public void subscribeStaff(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainer("staff", callback);
    }

    public void subscribeSingleStaff(String staffId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainerItem("staff", staffId, callback);
    }

    // Patient access
    public void fetchPatients(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainer("patients", callback);
    }

    public void fetchSinglePatient(String patientId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainerItem("patients", patientId, callback);
    }


    public void subscribePatients(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainer("patients", callback);
    }

    public void subscribeSinglePatient(String patientId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainerItem("patients", patientId, callback);
    }

    @Override
    public boolean tryLogin(Map<String, Object> singleStaffMap, String id, String password) {
        if (singleStaffMap.get("password").toString().equals(password)) { // TODO NULL CHECK
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getStaff(ICallback<Map<IStaff, Boolean>> callback) {
        this.dataAccess.subscribeContainer("staff", new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> rawStaffMap) {
                Map<IStaff, Boolean> staff = new HashMap<>();

                for (String staffKey : rawStaffMap.keySet()) {
                    staff.put(IStaff.getNewInstance(staffKey), true);
                }

                callback.onCallback(staff);
            }
        });
    }

    @Override
    public void getStaffById(String id, ICallback<IStaff> callback) {
        callback.onCallback(IStaff.getNewInstance(id));
    }

    @Override
    public void getPatients(ICallback<Map<IPatient, Boolean>> callback) {
        this.dataAccess.subscribeContainer("patients", new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> rawPatientsMap) {
                Map<IPatient, Boolean> patients = new HashMap<>();

                for (String patientKey : rawPatientsMap.keySet()) {
                    patients.put(IPatient.getNewInstance(patientKey), true);
                }

                callback.onCallback(patients);
            }
        });
    }

    @Override
    public void getPatientsByStaff(IStaff staff, ICallback<Map<IPatient, Boolean>> callback) {
        IStaff.getNewInstance(staff.getId()).getPatients(callback);
    }

    @Override
    public void getPatientById(String id, ICallback<IPatient> callback) {
        callback.onCallback(IPatient.getNewInstance(id));
    }

    @Override
    public void putPatientIntoPhase(IPatient patient, int phase) {
        HashMap<String, Object> editItem = new HashMap<>();
        editItem.put("phase", phase);
        this.dataAccess.editItemInContainer("patients", patient.getId(), editItem);
    }

    @Override
    public void addPatient(String id, String firstName, String lastName) {
        HashMap<String, Object> addItem = new HashMap<>();
        addItem.put("firstName", firstName);
        addItem.put("lastName", lastName);
        this.dataAccess.addItemToContainer("patients", id, addItem);
    }

    @Override
    public void assignPatientToStaff(IPatient patient, IStaff staff) {
        this.dataAccess.fetchContainerItem("staff", staff.getId(), new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                Map<String, Object> staffsPatients = (Map<String, Object>) stringObjectMap.get("patients"); // TODO CAST CHECK
                staffsPatients.put(patient.getId(), true); // TODO NULL CHECK
                dataAccess.editItemInContainer("staff", staff.getId(), staffsPatients);
            }
        });
    }
}
