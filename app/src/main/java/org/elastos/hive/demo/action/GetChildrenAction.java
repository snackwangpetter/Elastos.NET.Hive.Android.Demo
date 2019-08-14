package org.elastos.hive.demo.action;

import android.os.AsyncTask;

import org.elastos.hive.Children;
import org.elastos.hive.ItemInfo;
import org.elastos.hive.demo.FileItem;
import org.elastos.hive.demo.IPFSDataCenter;
import org.elastos.hive.demo.base.ActionCallback;
import org.elastos.hive.demo.base.BaseDataCenter;

import java.util.ArrayList;

public class GetChildrenAction extends AsyncTask<Void, String, ArrayList<FileItem>> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    public GetChildrenAction(BaseDataCenter dataCenter , ActionCallback actionCallback){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_GET_CHILDREN);
    }

    @Override
    protected ArrayList<FileItem> doInBackground(Void... voids) {
        ArrayList<FileItem> arrayList = new ArrayList<>();
        if (dataCenter instanceof IPFSDataCenter){
            Children children = ((IPFSDataCenter) dataCenter).doGetChildren();
            ArrayList<ItemInfo> itemInfos = children.getContent();

            for (ItemInfo itemInfo : itemInfos){
                FileItem fileItem = new FileItem(itemInfo.get(ItemInfo.name));

                arrayList.add(fileItem);
            }
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<FileItem> drive) {
        super.onPostExecute(drive);
        actionCallback.onSuccess(ActionType.ACTION_GET_CHILDREN,drive);
    }
}
