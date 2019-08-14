package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.Directory;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

public class CreateDirectoryAction extends AsyncTask <Void,String,Directory>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    public CreateDirectoryAction(BaseDataCenter dataCenter , ActionCallback actionCallback){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
    }


    @Override
    protected Directory doInBackground(Void... voids) {
        Directory directory = null;

        if (dataCenter instanceof IPFSDataCenter){
            ((IPFSDataCenter) dataCenter).docreateDirectory(null);
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_CREATE_DIR);
    }

    @Override
    protected void onPostExecute(Directory directory) {
        super.onPostExecute(directory);
        actionCallback.onSuccess(ActionType.ACTION_CREATE_DIR,directory);
    }
}
