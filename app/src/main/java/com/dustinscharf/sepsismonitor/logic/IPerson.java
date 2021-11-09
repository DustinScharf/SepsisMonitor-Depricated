package com.dustinscharf.sepsismonitor.logic;

import com.dustinscharf.sepsismonitor.util.ICallback;

public interface IPerson {
    public void getFirstName(ICallback<String> callback);

    public void getLastName(ICallback<String> callback);
}
