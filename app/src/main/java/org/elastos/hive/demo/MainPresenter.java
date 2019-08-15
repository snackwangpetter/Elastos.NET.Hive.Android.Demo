package org.elastos.hive.demo;

import android.content.Context;

import org.elastos.hive.demo.action.ActionType;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;
import org.elastos.hive.demo.base.BasePresenter;
import org.elastos.hive.demo.utils.FileUtils;
import org.elastos.hive.demo.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainPresenter extends BasePresenter {
    private Context context ;
    private String internalStorageCurrentPath = Config.INTERNAL_STORAGE_DEFAULT_PATH ;
    private String ipfsCurrentPath = Config.IPFS_DEFAULT_PATH ;
    private String oneDriveCurrentPath = Config.ONEDRIVE_DEFAULT_PATH ;

    private BaseDataCenter internalStorageDataCenter ,oneDriveDataCenter , ipfsDataCenter;

    private IView iView ;
    private ClientType currentClientType = ClientType.INTERNAL_STORAGE_TYPE;
    private ClientType lastClentType = ClientType.INTERNAL_STORAGE_TYPE;

    private BaseDataCenter getDataCenter(){
        switch (currentClientType){
            case IPFS_TYPE:
                if (ipfsDataCenter == null){
                    ipfsDataCenter = new IPFSDataCenter(context, new ActionCallback() {
                        @Override
                        public void onPreAction(ActionType type) {
                            iView.showProgressBar();
                        }

                        @Override
                        public void onFail(ActionType type, Exception e) {
                            iView.hideProgressBar();
                            ToastUtils.showShortToastSafe(e.getMessage());
                        }

                        @Override
                        public void onSuccess(ActionType type, Object body) {
                            iView.hideProgressBar();

                            switch (type){
                                case ACTION_GET_CHILDREN:
                                    ArrayList<FileItem> arrayList = (ArrayList<FileItem>) body;

                                    iView.refreshListView(arrayList);
                                    iView.refreshTitleView(getCurrentPath());
                                    iView.refreshListViewFinish();
                                    break;
                                case ACTION_CREATE_DIR:
                                    refreshData();
                                    break;
                                case ACTION_CREATE_File:
                                    refreshData();
                                    break;
                                case ACTION_UPLOAD_FILE:
                                    refreshData();
                                    break;
                            }


                        }
                    });

                }
                return ipfsDataCenter;
            case ONEDRIVE_TYPE:
                if (oneDriveDataCenter == null){
                    oneDriveDataCenter = new OneDriveDataCenter();
                }
                return oneDriveDataCenter;
            case INTERNAL_STORAGE_TYPE:
                if (internalStorageDataCenter == null){
                    internalStorageDataCenter = new InternalStorageDataCenter();
                }
                return internalStorageDataCenter;

            default:
                return null;
        }
    }

    public MainPresenter(IView iView , Context context){
        this.iView = iView ;
        this.context = context ;


    }

    public void setCurrentPath(String path){
        switch (currentClientType){
            case IPFS_TYPE:
                ipfsCurrentPath = path ;
                break;
            case ONEDRIVE_TYPE:
                oneDriveCurrentPath = path ;
                break;
            case INTERNAL_STORAGE_TYPE:
                internalStorageCurrentPath = path;
                break;
        }
    }

    public String getCurrentPath(){
        switch (currentClientType){
            case IPFS_TYPE:
                return ipfsCurrentPath;
            case ONEDRIVE_TYPE:
                return oneDriveCurrentPath;
            case INTERNAL_STORAGE_TYPE:
                return internalStorageCurrentPath;

            default:
                return null;
        }
    }

    public void refreshData(){
        ArrayList<FileItem> items = null;
        String currentPath = getCurrentPath();
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                items = ((InternalStorageDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
            case ONEDRIVE_TYPE:
                break;

            case IPFS_TYPE:
                items = ((IPFSDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
        }
        
        if (null==items){
            items = new ArrayList<>();
        }
        iView.refreshListView(items);
        iView.refreshTitleView(currentPath);
        iView.refreshListViewFinish();
    }

    public ArrayList<FileItem> getFileItemList(){
        ArrayList<FileItem> fileItems = new ArrayList<>();
        String currentPath = getCurrentPath();
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                fileItems = ((InternalStorageDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
            case IPFS_TYPE:
                fileItems = ((IPFSDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
        }
        return fileItems;
    }

    public void createDirectory(String fileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                File file = new File(fileAbsPath);
                if (!file.exists()){
                    file.mkdir();
                }
                refreshData();
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).createDirectory(fileAbsPath);
                break;
        }
    }

    public void createFile(String fileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                File file = new File(fileAbsPath);
                if (!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                refreshData();
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).createFile(fileAbsPath);
                break;
        }
    }

    public void uploadFile(String ipfsAbsPath , String internalFileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).uploadFile(ipfsAbsPath , internalFileAbsPath);
                break;
        }
    }

    public void returnParent(){
        String parentPath = FileUtils.getParent(getCurrentPath());
        setCurrentPath(parentPath);
        refreshData();
    }

    public void changeClientType(ClientType clientType){
        this.currentClientType = clientType ;

        if (lastClentType != currentClientType){
            refreshData();
            lastClentType = currentClientType;
        }
    }

    public ClientType getCurrentClientType(){
        return currentClientType ;
    }

    public interface IView{
        void refreshListView(ArrayList<FileItem> items);
        void refreshTitleView(String path);

        void refreshListViewFinish();

        void showProgressBar();

        void hideProgressBar();
    }
}
