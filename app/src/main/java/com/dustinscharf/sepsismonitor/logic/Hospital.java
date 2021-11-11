package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public ArrayList<HashMap<String, Object>> getPatients(Map<String, Object> patientsMap) {
        ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

        for (String patientKey : patientsMap.keySet()) {
            HashMap<String, Object> patient = new HashMap<>();
            patient.put(patientKey, patientsMap.get(patientKey));
            patients.add(patient);
        }

        return patients;
    }

    @Override
    public Map<String, Object> getPatientsByStaff(Map<String, Object> fullHospitalMap, String staffId) {
        Map<String, Object> patients = new HashMap<>();

        Map<String, Object> allStaffs = (Map<String, Object>) fullHospitalMap.get("staff");
        Map<String, Object> singleStaff = (Map<String, Object>) allStaffs.get(staffId);
        Map<String, Object> singleStaffsPatients = (Map<String, Object>) singleStaff.get("patients");

        Map<String, Object> allPatients = (Map<String, Object>) fullHospitalMap.get("patients");
        for (String patientId : singleStaffsPatients.keySet()) {
            patients.put(patientId, allPatients.get(patientId));
        }
        return patients;
    }

    @Override
    public void putPatientIntoPhase(String patientId, int phase) {
        HashMap<String, Object> editItem = new HashMap<>();
        editItem.put("phase", phase);
        this.dataAccess.editItemInContainer("patients", patientId, editItem);
    }

    @Override
    public void addPatient(String id, String firstName, String lastName) {
        HashMap<String, Object> addItem = new HashMap<>();
        addItem.put("firstName", firstName);
        addItem.put("lastName", lastName);
        this.dataAccess.addItemToContainer("patients", id, addItem);
    }

    @Override
    public void assignPatientToStaff(String patientId, String staffId) {
        this.fetchSingleStaff(staffId, new ICallback<Map<String, Object>>() {
            @Override
            public void onCallback(Map<String, Object> stringObjectMap) {
                ((Map<String, Object>) stringObjectMap.get("patients")).put(patientId, true);
                dataAccess.editItemInContainer("staff", staffId, stringObjectMap);
            }
        });
    }
}
