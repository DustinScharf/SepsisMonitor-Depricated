package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.data.DataAccess;
import com.dustinscharf.sepsismonitor.data.IDataAccess;
import com.dustinscharf.sepsismonitor.util.ICallback;

import java.util.Map;

public interface IStaff extends IPerson {
    public static IStaff getNewInstance(String id) {
        return new Staff(id);
    }

    public String getId();

    public void getPassword(ICallback<String> callback);

    public void isLMMP(ICallback<Boolean> callback);

    public void getPatients(ICallback<Map<IPatient, Boolean>> callback);
}
