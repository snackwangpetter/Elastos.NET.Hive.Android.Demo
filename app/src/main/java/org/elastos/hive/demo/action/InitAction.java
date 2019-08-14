package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.Drive;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

public class InitAction extends AsyncTask<Void,String, Drive> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    public InitAction(BaseDataCenter dataCenter , ActionCallback actionCallback){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_INIT);
    }

    @Override
    protected Drive doInBackground(Void... voids) {
        Drive drive = null ;
        if (dataCenter instanceof IPFSDataCenter){
            ((IPFSDataCenter) dataCenter).doInit();
        }
        return drive;
    }

    @Override
    protected void onPostExecute(Drive drive) {
        super.onPostExecute(drive);
        actionCallback.onSuccess(ActionType.ACTION_INIT,drive);
    }
}
