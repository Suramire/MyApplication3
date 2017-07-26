package com.suramire.myapplication.mvp;

import android.os.Handler;

/**
 * Created by Suramire on 2017/7/21.
 */

public class PresenterNotification {
    private final Handler handler;
    BaseView baseView;
    IMoudleNotification moudleNotification;

    public PresenterNotification(BaseView baseView) {
        this.baseView = baseView;
        moudleNotification = new MoudelNotification();
        handler = new Handler();
    }

    public void getNotification(){
        moudleNotification.getData(new OnGetDataListener() {
            @Override
            public void getSuccessful(final Object data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseView.showSuccessful(data);
                    }
                });
            }

            @Override
            public void getFail() {

            }
        });
    }
}
