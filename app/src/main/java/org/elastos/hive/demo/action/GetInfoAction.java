package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.File;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

public class GetInfoAction extends AsyncTask<Void,String, File.Info> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String filePath ;
    public GetInfoAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String path){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.filePath = path ;
    }


    @Override
    protected File.Info doInBackground(Void... voids) {
        File.Info fileInfo = null;


        if (dataCenter instanceof IPFSDataCenter){
            File file = ((IPFSDataCenter) dataCenter).doGetFile(filePath);
            fileInfo = ((IPFSDataCenter) dataCenter).doGetFileInfo(file);
        }
        return fileInfo;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_FILE_INFO);
    }

    @Override
    protected void onPostExecute(File.Info info) {
        super.onPostExecute(info);
        actionCallback.onSuccess(ActionType.ACTION_FILE_INFO,info);
    }
}
