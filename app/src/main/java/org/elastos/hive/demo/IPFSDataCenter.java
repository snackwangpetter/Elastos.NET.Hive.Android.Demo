package org.elastos.hive.demo;

import android.content.Context;
import android.util.Log;

import org.elastos.hive.Children;
import org.elastos.hive.Client;
import org.elastos.hive.Directory;
import org.elastos.hive.Drive;
import org.elastos.hive.File;
import org.elastos.hive.HiveException;
import org.elastos.hive.IPFSEntry;
import org.elastos.hive.demo.action.CreateDirectoryAction;
import org.elastos.hive.demo.action.GetChildrenAndInfoAction;
import org.elastos.hive.demo.action.GetInfoAction;
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
        new InitAction(this,actionCallback).execute();
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

    public Children doGetChildren(String path){
        Children children = null;
        try {
            Directory directory = getDefaultDrive().getDirectory(path).get();
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
//        new GetChildrenAction(this , actionCallback).execute(null,null,null);
        new GetChildrenAndInfoAction(this,actionCallback,path).execute();
        return arrayList;
    }

    public void getChildrenAndInfo(){


    }

    public void dogetChildrenAndInfo(ArrayList<FileItem> fileItems){

    }


    public void docreateDirectory(String path){
        try {
            drive.createDirectory(path).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createDirectory(String fileAbsPath){
        new CreateDirectoryAction(this,actionCallback , fileAbsPath).execute();
    }

    public void getFileInfo(String path){
        new GetInfoAction(this,actionCallback,path).execute();
    }

    public File.Info doGetFileInfo(File file){
        File.Info fileInfo = null ;
        try {
            fileInfo = file.getInfo().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return fileInfo ;
    }

    public void getFile(String path){

    }

    public File doGetFile(String path){
        File file = null;
        try {
            file = drive.getFile(path).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return file ;
    }





}
