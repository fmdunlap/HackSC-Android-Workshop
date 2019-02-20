package com.fdunlap.hacksclearn.MainActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fdunlap.hacksclearn.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private final String TAG = "MainActivity"; // Logging helper.

    // This is our presenter! This is where we hold all the business logic.
    private MainActivityContract.Presenter presenter;

    // *The Android Activity Lifecycle* https://s3-us-west-1.amazonaws.com/nearsoft-com-media/uploads/2018/01/android-activity-lifecycle-01.png
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this);

    }
}
