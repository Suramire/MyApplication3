package com.suramire.myapplication.mvp;

import android.os.Handler;

/**
 * Created by Suramire on 2017/7/20.
 */

public class BasePresenter {
    private IBaseModel baseModel;
    private BaseView baseView;
    private final Handler handler;

    public BasePresenter(BaseView baseView) {
        this.baseView = baseView;
        baseModel = new BaseModel();
        handler = new Handler();
    }

    public void show(final String what){
        baseView.showLoading();
        baseModel.getData(what, new OnGetDataListener() {
            @Override
            public void getSuccessful(final Object data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseView.showSuccessful(data);
                        baseView.cancelLoading();
                    }
                });

            }

            @Override
            public void getFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseView.showFail();
                        baseView.cancelLoading();
                    }
                });

            }
        });
    }
}
