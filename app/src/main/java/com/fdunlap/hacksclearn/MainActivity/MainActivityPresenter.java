package com.fdunlap.hacksclearn.MainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.fdunlap.hacksclearn.retrofit.OpenTDBApiInterface;
import com.fdunlap.hacksclearn.retrofit.OpenTDBClient;
import com.fdunlap.hacksclearn.retrofit.OpenTDBResponse;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private final String TAG = "MainActivityPresenter";
    private final int MAX_BUTTONS = 4;
    private final int LARGE_NUM = 3999;
    private Random rand = new Random();
    private MainActivityModel model;
    private MainActivityContract.View view;


    MainActivityPresenter(MainActivityContract.View view) {
        model = new MainActivityModel();
        this.view = view;
    }

    private class questionCallBackResponse implements Callback<OpenTDBResponse> {
        @Override
        public void onResponse(@NonNull Call<OpenTDBResponse> call,@NonNull Response<OpenTDBResponse> response) {

        }

        @Override
        public void onFailure(@NonNull Call<OpenTDBResponse> call,@NonNull Throwable t) {
            Log.d(TAG, "QuestionCallBack Failed");
        }
    }
}
