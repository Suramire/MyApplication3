package com.suramire.myapplication.mvp;

/**
 * Created by Suramire on 2017/7/20.
 */

public interface BaseView {
    void showLoading();

    void cancelLoading();

    void showSuccessful(Object data);

    void showFail();
}
