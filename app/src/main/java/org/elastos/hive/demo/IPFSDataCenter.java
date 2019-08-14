package org.elastos.hive.demo;

import android.content.Context;

import org.elastos.hive.Children;
import org.elastos.hive.Client;
import org.elastos.hive.Directory;
import org.elastos.hive.Drive;
import org.elastos.hive.HiveException;
import org.elastos.hive.IPFSEntry;
import org.elastos.hive.demo.action.CreateDirectoryAction;
import org.elastos.hive.demo.action.GetChildrenAction;
import org.elastos.hive.demo.action.InitAction;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.vendors.ipfs.IPFSParameter;

import org.elastos.hive.demo.base.BaseDataCenter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class IPFSDataCenter extends BaseDataCenter {
    private String currentPath = "/";
    private static String catchPath ;
    private static final String[] rpcAddrs = {
            "52.83.119.110",
            "52.83.159.189",
            "3.16.202.140",
            "18.217.147.205",
            "18.219.53.133"
    };

    private static Client client ;
    private static Drive drive;

    private ActionCallback actionCallback ;

    private Context context ;
    public IPFSDataCenter(Context context , ActionCallback actionCallback){
        this.context = context ;
        this.actionCallback = actionCallback ;
        init();
    }

//    private Directory directory ;

    public void init(){
        new InitAction(this,actionCallback).execute(null,null,null);
    }

    public void doInit(){
        catchPath = context.getExternalCacheDir().getAbsolutePath();
        getClient();
        getDefaultDrive();
    }

    public static Client getClient(){
        if (client == null){
            String uid = null;
            IPFSParameter parameter = new IPFSParameter(new IPFSEntry(uid, rpcAddrs), catchPath);
            try {
                client = Client.createInstance(parameter);
                client.login(null);
            } catch (HiveException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    private Drive getDefaultDrive(){
        if (drive == null){
            try {
                Client client = getClient();
                drive = getClient().getDefaultDrive().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return drive;
    }

    public Children doGetChildren(){
        Children children = null;
        try {
            Directory directory = getDefaultDrive().getDirectory(currentPath).get();
            children = directory.getChildren().get();
        } catch (ExecutionException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return children ;
    }

    @Override
    protected ArrayList getChildrenDetails(String path) {
        ArrayList<FileItem> arrayList = new ArrayList<>();
        new GetChildrenAction(this , actionCallback).execute(null,null,null);
        return arrayList;
    }


    public void docreateDirectory(String path){
        try {
            drive.createDirectory("/test"+System.currentTimeMillis()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createDirectory(){
        new CreateDirectoryAction(this,actionCallback).execute(null,null,null);
    }




}
