package org.elastos.hive.demo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.elastos.hive.demo.base.BaseFragment;
import org.elastos.hive.demo.utils.ToastUtils;
import org.elastos.hive.demo.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainFragment extends BaseFragment implements MainPresenter.IView{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter adapter;
    private TextView tvPath ;
    public MainPresenter presenter ;
    private MaterialDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initPresenter();
        initData();

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initPresenter(){
        presenter = new MainPresenter(this,getActivity().getApplicationContext());
    }

    private void initData(){
        String defaltPath = Config.INTERNAL_STORAGE_DEFAULT_PATH;
        presenter.setCurrentPath(defaltPath);
    }
    private void initView(View rootView){
        tvPath = (TextView) rootView.findViewById(R.id.tv_path);
        tvPath.setMovementMethod(ScrollingMovementMethod.getInstance());
        setTitlePath(presenter.getCurrentPath());
        initRefreshLayout(rootView);
        initRecyclerView(rootView);
        addHeadView();

    }

    private BaseQuickAdapter initAdapter(ArrayList arrayList){
        adapter = new MainAdapter(arrayList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ToastUtils.showShortToastSafe("view"+view.getId()+position);
                FileItem item = (FileItem) adapter.getData().get(position);
                String fileAbsPath = item.getFileAbsPath();

                if (item.isFolder()){
                    presenter.setCurrentPath(fileAbsPath);
                    presenter.refreshData();
                }else {
                    if (presenter.getCurrentClientType() != ClientType.INTERNAL_STORAGE_TYPE){
                        showOneButtonDialog("Cannot open the remote file directly, please download the file locally first");
                    }else{

                        ((MainActivity)getActivity()).openFile(fileAbsPath);
                        ToastUtils.showShortToast("click: "+fileAbsPath);
                    }
                }
            }


        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FileItem fileItem = (FileItem) adapter.getData().get(position);
                Toast.makeText(getActivity(), "onItemChildClick" + position, Toast.LENGTH_SHORT).show();
                showPopupMenu(view , fileItem);
            }
        });


        return adapter;
    }

    private void initRecyclerView(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(initAdapter(presenter.getFileItemList()));
    }

    private void initRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshData();
            }
        });
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.layout_listitem, (ViewGroup) mRecyclerView.getParent(), false);

        ((ImageView)headView.findViewById(R.id.listitem_iv_type)).setBackgroundResource(R.mipmap.lfile_folder_style_green);
        ((TextView)headView.findViewById(R.id.listitem_tv_name)).setText("..");
        ((TextView)headView.findViewById(R.id.listitem_tv_detail)).setText("父目录");
        headView.findViewById(R.id.listitem_more_btn).setVisibility(View.INVISIBLE);

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("返回上级");
                presenter.returnParent();
            }
        });
        adapter.addHeaderView(headView);
    }

    public void setTitlePath(String path){
        tvPath.setText(path);
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View ancherView , FileItem fileItem){
        PopupMenu popupMenu = new PopupMenu(Utils.getContext(), ancherView);
        MenuInflater inflater = popupMenu.getMenuInflater();

        if (presenter.getCurrentClientType() == ClientType.INTERNAL_STORAGE_TYPE){
            inflater.inflate(R.menu.item_pop_menu_internal_storage, popupMenu.getMenu());
        }else{
            inflater.inflate(R.menu.item_pop_menu_ipfs, popupMenu.getMenu());
        }
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) field.get(popupMenu);
            mPopup.setForceShowIcon(true);
        } catch (Exception e) {
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_pop_menu_ipfs_download:
                        ((MainActivity)getActivity()).downloadFilePick(fileItem);
                        break;
                    case R.id.item_pop_menu_ipfs_delete:
                        showConfirmDeleteDialog(fileItem.getFileAbsPath());

                        break;
                    case R.id.item_pop_menu_ipfs_more:
                        ToastUtils.showShortToastSafe("more function todo ipfs");
                        break;
                    case R.id.item_pop_menu_internalstorage_more:
                        ToastUtils.showShortToastSafe("more function todo internal storage");
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private MaterialDialog getProgressDialog(){
        if (progressDialog == null){
            progressDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.wait)
                    .content(R.string.loading)
                    .progress(true, 0)
                    .build();
        }

        return progressDialog;

    }

    private void showConfirmDeleteDialog(String filePath){
        new MaterialDialog.Builder(getActivity())
                .title(R.string.prompt)
                .content(R.string.confirm_delete)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteFile(filePath);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }

    private void showOneButtonDialog(String content){
        new MaterialDialog.Builder(getActivity())
                .title(R.string.warning)
                .content(content)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private void showOneButtonDialog(int contentRes){
        new MaterialDialog.Builder(getActivity())
                .title(R.string.warning)
                .content(contentRes)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private void showProgressDialog(){
        getProgressDialog().show();
    }

    private void dismissProgressDialog(){
        if (getProgressDialog().isShowing()){
            getProgressDialog().hide();
        }

    }

    @Override
    public void refreshListView(ArrayList<FileItem> items) {
        adapter.setNewData(items);
    }


    @Override
    public void refreshTitleView(String path) {
        setTitlePath(path);
    }

    @Override
    public void refreshListViewFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog();
    }

    @Override
    public void hideProgressBar() {
        dismissProgressDialog();
    }

    @Override
    public void showSameFileDialog() {
        showOneButtonDialog(R.string.warning_same_file_name);
    }

    @Override
    public void showConnectionWrong() {
        showOneButtonDialog(R.string.connect_error);
//        ToastUtils.showShortToastSafe(R.string.connect_error);
    }
}
