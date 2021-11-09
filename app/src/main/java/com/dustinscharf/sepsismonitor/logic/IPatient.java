package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

public interface IPatient extends IPerson {
    public void getId(ICallback<String> callback);

    public void getPhase(ICallback<Integer> callback);
}
