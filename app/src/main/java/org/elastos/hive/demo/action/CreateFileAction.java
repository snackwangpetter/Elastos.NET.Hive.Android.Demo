package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.Directory;
import org.elastos.hive.File;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

public class CreateFileAction extends AsyncTask <Void,String,File>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String fileAbsPath ;
    public CreateFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String fileAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.fileAbsPath = fileAbsPath ;
    }


    @Override
    protected File doInBackground(Void... voids) {
        File file = null;

        if (dataCenter instanceof IPFSDataCenter){
            try{
                file = ((IPFSDataCenter) dataCenter).doCreateFile(fileAbsPath);
            }catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_CREATE_File , e);
            }

        }
        return file;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_CREATE_File);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        actionCallback.onSuccess(ActionType.ACTION_CREATE_File,file);
    }
}
