package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

public interface IPatient extends IPerson {
    public static IPatient getNewInstance(String id) {
        return new Patient(id);
    }

    public String getId();

    public void getPhase(ICallback<Long> callback);
}
