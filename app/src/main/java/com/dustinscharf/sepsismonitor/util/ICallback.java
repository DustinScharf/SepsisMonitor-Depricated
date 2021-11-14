package com.dustinscharf.sepsismonitor.util;

/**
 * A callback that can be used to return any data at any event
 *
 * @param <T> the type of the data to be returned
 */
public interface ICallback<T> {
    /**
     * A callback that can be fired at any event
     *
     * @param t the data the callback contains when it is fired
     */
    void onCallback(T t);
}
