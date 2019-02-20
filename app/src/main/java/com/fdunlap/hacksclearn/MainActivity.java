package com.fdunlap.hacksclearn;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fdunlap.hacksclearn.retrofit.OpenTDBApiInterface;
import com.fdunlap.hacksclearn.retrofit.OpenTDBClient;
import com.fdunlap.hacksclearn.retrofit.OpenTDBResponse;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity"; // Logging helper.
    private final int MAX_BUTTONS = 4;
    // Just an arbitrarily large number since Rand (??) was occasionally overflowing and causing... issues.
    private final int LARGE_NUM = 3999;
    Random rand = new Random();

    // This is the retrofit interface that allows us to access the OpenTrivia Database.
    // We'll talk at length about Retrofit and why it's awesome.
    OpenTDBApiInterface client;
    // This is our POJO that models the question we get as a response from OpenTDB
    private OpenTDBResponse.Question currentQuestion;


    // *The Android Activity Lifecycle* https://s3-us-west-1.amazonaws.com/nearsoft-com-media/uploads/2018/01/android-activity-lifecycle-01.png
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the OpenTDB client
        client = OpenTDBClient.getClient().create(OpenTDBApiInterface.class);


        getQuestion();
        updateUi();
    }

    private void updateUi() {

    }

    public void getQuestion() {
    }

    // Preserve state on lifecycle changes.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // Helper method to build a dialog
    private void createRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.something_went_wrong)
                .setTitle(R.string.network_error)
                .setPositiveButton(R.string.retry, (dialogInterface, i) -> getQuestion())
                .setNegativeButton(R.string.exit, ((dialogInterface, i) -> finishAndRemoveTask()))
                .setOnCancelListener((v) -> finishAndRemoveTask())
                .create().show();
    }

    private class questionCallBackResponse implements Callback<OpenTDBResponse> {
        @Override
        public void onResponse(@NonNull Call<OpenTDBResponse> call,@NonNull Response<OpenTDBResponse> response) {
            OpenTDBResponse body = response.body();
        }

        @Override
        public void onFailure(@NonNull Call<OpenTDBResponse> call,@NonNull Throwable t) {
            Log.d(TAG, "Question Callback failed. Creating retry dialog.");
            createRetryDialog();
        }
    }

    /*
    Quick little inner class to define the behavior of the buttons. Note that since Android uses
    classes to deal with events, we can define our own constructors and store state data alongside
    event listeners.
    */
    private class buttonClickListener implements View.OnClickListener {

        buttonClickListener(){

        }

        //Todo: Try adding some sort of UI event that is more pleasing than Toasts!
        @Override
        public void onClick(View view) {

        }
    }
}
