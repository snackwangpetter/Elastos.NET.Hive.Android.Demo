package org.elastos.hive.demo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<FileItem,BaseViewHolder> {
    public MainAdapter(List data) {
        super(R.layout.layout_listitem, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FileItem item) {
        helper.addOnClickListener(R.id.listitem_more_btn);
        if (item.isFolder()){
            helper.setImageResource(R.id.listitem_iv_type,R.mipmap.lfile_folder_style_green);
            helper.setVisible(R.id.listitem_more_btn, false);
        } else {
            helper.setImageResource(R.id.listitem_iv_type,R.mipmap.lfile_file_style_green);
            helper.setVisible(R.id.listitem_more_btn, true);
        }
        helper.setText(R.id.listitem_tv_name,item.getFileName());
        helper.setText(R.id.listitem_tv_detail,item.getFileDetail());
    }
}
