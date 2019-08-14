package org.elastos.hive.demo.base;

import org.elastos.hive.demo.action.ActionType;

public interface ActionCallback<T> {
    void onPreAction(ActionType type);
    void onFail(ActionType type , Exception e);
    void onSuccess(ActionType type , T body);
}
