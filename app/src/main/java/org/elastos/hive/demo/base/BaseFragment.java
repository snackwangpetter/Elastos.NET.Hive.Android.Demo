package org.elastos.hive.demo.base;

import android.support.v4.app.Fragment;

public class BaseFragment <T extends BasePresenter> extends Fragment{

    BasePresenter presenter;

    void initPresenter(T t){
        presenter = t ;
    }

}
