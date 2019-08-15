package org.elastos.hive.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.elastos.hive.demo.base.BaseFragment;
import org.elastos.hive.demo.utils.ToastUtils;

import java.util.ArrayList;

public class MainFragment extends BaseFragment implements MainPresenter.IView{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseQuickAdapter adapter;
    private TextView tvPath ;
    public MainPresenter presenter ;
    public RelativeLayout progressLayout;

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
//        tvPath.setSelected(true);
        setTitlePath(presenter.getCurrentPath());
        initRefreshLayout(rootView);
        initRecyclerView(rootView);
        addHeadView();

        progressLayout = rootView.findViewById(R.id.progress_layout);
    }

    private BaseQuickAdapter initAdapter(ArrayList arrayList){
        adapter = new MainAdapter(arrayList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileItem item = (FileItem) adapter.getData().get(position);
                String fileAbsPath = item.getFileAbsPath();

                if (item.isFolder()){
                    presenter.setCurrentPath(fileAbsPath);
                    presenter.refreshData();
                }else {
                    ToastUtils.showShortToast("click: "+fileAbsPath);
                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                FileItem item = (FileItem) adapter.getData().get(position);
                if (item.isFolder()){
                    ToastUtils.showShortToastSafe(item.getFileName());
                }else{
                    //TODO show confirm dialog
                    //TODO show internal storage save path select dialog
                    //TODO download
                }

                return false;
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

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("返回上级");
                presenter.returnParent();
            }
        });
        adapter.addHeaderView(headView);
    }

    private void showProgressLayout(){
        progressLayout.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressLayout(){
        progressLayout.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void setTitlePath(String path){
        tvPath.setText(path);
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
        showProgressLayout();
    }

    @Override
    public void hideProgressBar() {
        hideProgressLayout();
    }


}
