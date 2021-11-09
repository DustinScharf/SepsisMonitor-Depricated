package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public interface IStaff extends IPerson {
    public void getId(ICallback<String> callback);

    public void getPassword(ICallback<String> callback);

    public void isLMMP(ICallback<Boolean> callback);

    public void getPatients(ICallback<Map<IPatient, Boolean>> callback);
}
