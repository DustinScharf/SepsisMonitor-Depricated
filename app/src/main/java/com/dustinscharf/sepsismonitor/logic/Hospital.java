package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.R;
import com.dustinscharf.sepsismonitor.data.DataAccess;
import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Hospital implements IHospital {
    private static Hospital hospitalSingleton;

    public static Hospital getInstance() {
        if (hospitalSingleton == null) {
            hospitalSingleton = new Hospital();
        }
        return hospitalSingleton;
    }

    private final IDataAccess dataAccess;

    private String loggedInStaffId;
    private boolean loggedInStaffIsLMMP;

    private Hospital() {
        this.dataAccess = IDataAccess.getInstance();
    }

    @Override
    public String getLoggedInStaffId() {
        return this.loggedInStaffId;
    }

    @Override
    public void setLoggedInStaffId(String id) {
        this.loggedInStaffId = id;
    }

    @Override
    public boolean loggedInStaffIsLMMP() {
        return this.loggedInStaffIsLMMP;
    }

    @Override
    public void setLoggedInStaffIsLMMP(boolean isLMMP) {
        this.loggedInStaffIsLMMP = isLMMP;
    }

    @Override
    public void fetchHospital(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetch(callback);
    }

    @Override
    public void subscribeHospital(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribe(callback);
    }

    @Override
    public void fetchStaff(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainer("staff", callback);
    }

    @Override
    public void fetchSingleStaff(String staffId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainerItem("staff", staffId, callback);
    }

    @Override
    public void subscribeStaff(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainer("staff", callback);
    }

    public void subscribeSingleStaff(String staffId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainerItem("staff", staffId, callback);
    }

    @Override
    public void fetchPatients(ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainer("patients", callback);
    }

    @Override
    public void fetchSinglePatient(String patientId, ICallback<Map<String, Object>> callback) {
        this.dataAccess.fetchContainerItem("patients", patientId, callback);
    }

    @Override
    public void subscribePatients(ICallback<Map<String, Object>> callback) {
        this.dataAccess.subscribeContainer("patients", callback);
    }

    @Override
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
    public ArrayList<HashMap<String, Object>> getPatientsByStaff(Map<String, Object> fullHospitalMap, String staffId) {
        ArrayList<HashMap<String, Object>> patients = new ArrayList<>();

        Map<String, Object> allPatients = (Map<String, Object>) fullHospitalMap.get("patients");

        Map<String, Object> allStaffs = (Map<String, Object>) fullHospitalMap.get("staff");
        Map<String, Object> singleStaff = (Map<String, Object>) allStaffs.get(staffId);
        Map<String, Object> singleStaffsPatients = (Map<String, Object>) singleStaff.get("patients");

        for (String patientKey : singleStaffsPatients.keySet()) {
            HashMap<String, Object> patient = new HashMap<>();
            patient.put(patientKey, allPatients.get(patientKey));
            patients.add(patient);
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
        addItem.put("phase", 0);
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

    @Override
    public String findStaffIdByAssignedPatientId(Map<String, Object> staffMap, String patientId) {
        for (Map.Entry<String, Object> staffInfo : staffMap.entrySet()) {

            Map<String, Object> staff = (Map<String, Object>) staffInfo.getValue();

            if (staff.get("patients") != null && ((Map<String, Object>) staff.get("patients")).containsKey(patientId)) {
                return staffInfo.getKey();
            }
        }
        return "NOT ASSIGNED";
    }

    @Override
    public String getPhaseById(int id) {
        switch (id) {
            case 0:
                return "Registration";
            case 1:
                return "ER Triage";
            case 2:
                return "ER Sepsis Triage";
            case 3:
                return "IV Antibiotics";
            case 4:
                return "Admission";
            case 5:
                return "Release";
            default:
                return "Archive...";
        }
    }
}
