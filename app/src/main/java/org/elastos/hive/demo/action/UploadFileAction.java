package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.File;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

import java.util.concurrent.ExecutionException;

public class UploadFileAction extends AsyncTask <Void,String,Void>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String internalFileAbsPath , ipfsAbsPath;
    public UploadFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback ,
                            String internalFileAbsPath , String ipfsAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.internalFileAbsPath = internalFileAbsPath ;
        this.ipfsAbsPath = ipfsAbsPath ;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Void voidField = null ;
        if (dataCenter instanceof IPFSDataCenter){
            try {
                ((IPFSDataCenter) dataCenter).doUploadFile(ipfsAbsPath,internalFileAbsPath);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return voidField ;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_UPLOAD_FILE);
    }

    @Override
    protected void onPostExecute(Void voidField) {
        super.onPostExecute(voidField);
        actionCallback.onSuccess(ActionType.ACTION_UPLOAD_FILE,voidField);
    }
}
