package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IHospital {
    public static IHospital getNewInstance() {
        return Hospital.getInstance();
    }

    public String getLoggedInStaffId();

    public void setLoggedInStaffId(String id);

    public boolean loggedInStaffIsLMMP();

    public void setLoggedInStaffIsLMMP(boolean isLMMP);

    // Hospital access
    public void fetchHospital(ICallback<Map<String, Object>> callback);

    public void subscribeHospital(ICallback<Map<String, Object>> callback);


    // Staff access
    public void fetchStaff(ICallback<Map<String, Object>> callback);

    public void fetchSingleStaff(String staffId, ICallback<Map<String, Object>> callback);

    public void subscribeStaff(ICallback<Map<String, Object>> callback);

    public void subscribeSingleStaff(String staffId, ICallback<Map<String, Object>> callback);


    // Patient access
    public void fetchPatients(ICallback<Map<String, Object>> callback);

    public void fetchSinglePatient(String patientId, ICallback<Map<String, Object>> callback);

    public void subscribePatients(ICallback<Map<String, Object>> callback);

    public void subscribeSinglePatient(String patientId, ICallback<Map<String, Object>> callback);


    public boolean tryLogin(Map<String, Object> singleStaffMap, String id, String password);


    public ArrayList<HashMap<String, Object>> getPatients(Map<String, Object> patientsMap);

    public ArrayList<HashMap<String, Object>> getPatientsByStaff(Map<String, Object> fullHospitalMap, String staffId);


    public void putPatientIntoPhase(String patientId, int phase);

    public void addPatient(String id, String firstName, String lastName);

    public void assignPatientToStaff(String patientId, String staffId);

    public String findStaffIdByAssignedPatientId(Map<String, Object> staffMap, String patientId);

    public String getPhaseById(int id);
}
