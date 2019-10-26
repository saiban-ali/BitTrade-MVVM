package com.fyp.bittrade.utils;

public class ExploreProgressHandler {

    public static IExploreProgressCallBack callBack;

    public static void hideExploreProgress() {
        callBack.hideProgressBar();
    }

    public static void showErrorMessage() {
        callBack.showErrorMessage();
    }

    public static void hideErrorMessage() {
        callBack.hideErrorMessage();
    }

    public static void showExploreProgress() {
        callBack.showProgressBar();
    }

}
