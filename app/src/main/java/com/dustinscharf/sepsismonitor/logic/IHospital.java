package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public interface IHospital {
    public void tryLogin(String id, String password, ICallback<IStaff> callback);

    public void getStaff(ICallback<Map<IStaff, Boolean>> callback);

    public void getPatients(ICallback<Map<IPatient, Boolean>> callback);

    public void getPatientsByStaff(IStaff staff, ICallback<Map<IPatient, Boolean>> callback);

    public void getPatientById(String id, ICallback<IPatient> callback);

    public void movePatientToNextPhase(IPatient patient);

    public void putPatientIntoPhase(IPatient patient, int phase);

    public void addPatient(IPatient patient);

    public void assignPatientToStaff(IPatient patient, IStaff staff);
}
