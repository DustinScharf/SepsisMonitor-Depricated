package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public interface IHospital {
    public static IHospital getNewInstance() {
        return new Hospital();
    }

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

    public void getStaff(ICallback<Map<IStaff, Boolean>> callback);

    public void getStaffById(String id, ICallback<IStaff> callback);

    public void getPatients(ICallback<Map<IPatient, Boolean>> callback);

    public void getPatientsByStaff(IStaff staff, ICallback<Map<IPatient, Boolean>> callback);

    public void getPatientById(String id, ICallback<IPatient> callback);

    public void putPatientIntoPhase(IPatient patient, int phase);

    public void addPatient(String id, String firstName, String lastName);

    public void assignPatientToStaff(IPatient patient, IStaff staff);
}
