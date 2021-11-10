package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public interface IHospital {
    public static IHospital getNewInstance() {
        return new Hospital();
    }

    public void tryLogin(String id, String password, ICallback<IStaff> callback);

    public void getStaff(ICallback<Map<IStaff, Boolean>> callback);

    public void getStaffById(String id, ICallback<IStaff> callback);

    public void getPatients(ICallback<Map<IPatient, Boolean>> callback);

    public void getPatientsByStaff(IStaff staff, ICallback<Map<IPatient, Boolean>> callback);

    public void getPatientById(String id, ICallback<IPatient> callback);

    public void putPatientIntoPhase(IPatient patient, int phase);

    public void addPatient(String id, String firstName, String lastName);

    public void assignPatientToStaff(IPatient patient, IStaff staff);
}
