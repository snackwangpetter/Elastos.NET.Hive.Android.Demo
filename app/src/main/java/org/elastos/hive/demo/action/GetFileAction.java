package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.Directory;
import org.elastos.hive.File;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

public class GetFileAction extends AsyncTask<Void,String, File> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String path ;
    public GetFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String path){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.path = path ;
    }


    @Override
    protected File doInBackground(Void... voids) {
        File file = null;

        if (dataCenter instanceof IPFSDataCenter){
            ((IPFSDataCenter) dataCenter).doGetFile(path);
        }
        return file;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_GET_FILE);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        actionCallback.onSuccess(ActionType.ACTION_GET_FILE,file);
    }
}
