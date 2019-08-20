package org.elastos.hive.demo;

import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;

import org.elastos.hive.demo.activity.SimpleCarrier;
import org.elastos.hive.demo.utils.ToastUtils;
import org.elastos.hive.demo.utils.Utils;

public class HiveApplication extends MultiDexApplication {
    private Handler mHandler;
    private static SimpleCarrier sSimpleCarrier;

    @Override
    public void onCreate() {
        Utils.init(this);
        super.onCreate();

        mHandler = new CarrierHandler();
        sSimpleCarrier = SimpleCarrier.getInstance(this, mHandler);
    }

    public class CarrierHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SimpleCarrier.ONREADY: {
                    ToastUtils.showShortToastSafe(R.string.carrier_ready);
                    break;
                }
            }
        }
    }

    public SimpleCarrier getCarrier(){
        return sSimpleCarrier ;
    }
}
