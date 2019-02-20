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

    private TextView scoreTextView; // Score Text View ("Score: 0")
    private TextView questionTextView; //The textview *inside* of the Card View -- this is where we're displaying question text.

    // ArrayLists to hold our buttons and their text so that we can iterate through them later
    // NOTE: The correct (but much more complicated) way to do this is to use a ListView & ArrayAdapter.
    // See: https://developer.android.com/guide/topics/ui/declaring-layout.html#AdapterViews
    private ArrayList<Button> answerButtons = new ArrayList<>();

    // This is our presenter! This is where we hold all the business logic.
    private MainActivityContract.Presenter presenter;

    // *The Android Activity Lifecycle* https://s3-us-west-1.amazonaws.com/nearsoft-com-media/uploads/2018/01/android-activity-lifecycle-01.png
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this);

        // First of all, we need to assign our components to our component variables.
        scoreTextView = findViewById(R.id.score_counter_text_view);
        questionTextView = findViewById(R.id.question_text_view);
        answerButtons.add(findViewById(R.id.answer_button_1));
        answerButtons.add(findViewById(R.id.answer_button_2));
        answerButtons.add(findViewById(R.id.answer_button_3));
        answerButtons.add(findViewById(R.id.answer_button_4));

        // Iterate through the buttons and attach their onClickListeners
        for (int i = 0; i < answerButtons.size(); i++) {
            Button b = answerButtons.get(i);
            b.setOnClickListener(new answerButtonClickListener(i));
        }

        // If we've saved a question, don't get a new one and instead JUST update the new UI
        if (!presenter.hasSavedState(savedInstanceState)) {
            presenter.getNextQuestion();
        } else {
            // Restore state following lifecycle change.
            presenter.restoreState(savedInstanceState);
        }
    }

    // Preserve state on lifecycle changes.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = presenter.bundleState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void setQuestionText(String questionText) {
        questionTextView.setText(questionText);
    }

    @Override
    public void setButtonText(String buttonText, int buttonIndex) {
        answerButtons.get(buttonIndex).setText(buttonText);
    }

    @Override
    public void setButtonVisibility(int visibility, int buttonIndex) {
        answerButtons.get(buttonIndex).setVisibility(visibility);
    }

    // Helper method to build a dialog
    @Override
    public void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.something_went_wrong)
                .setTitle(R.string.network_error)
                .setPositiveButton(R.string.retry, (dialogInterface, i) -> presenter.getNextQuestion())
                .setNegativeButton(R.string.exit, ((dialogInterface, i) -> finishAndRemoveTask()))
                .setOnCancelListener((v) -> finishAndRemoveTask())
                .create().show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setScore(int score) {
        scoreTextView.setText(String.format("Score: %d", score));
    }

    @Override
    public void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    // This is the actual workhorse for retrofit. Basically, whenever we call getQuestion(), we are
    // enqueueing an instance of this class, which handles the response. If you're familiar with
    // the concept of promises in web development, this is the Android equivalent.
    //
    // If you're implementing that caching thing I mentioned on line 136, this will need to be modified, too.
    private class answerButtonClickListener implements View.OnClickListener {
        int myIndex;

        answerButtonClickListener(int myIndex) {
            this.myIndex = myIndex;
        }

        //Todo: Try adding some sort of UI event that is more pleasing than Toasts!
        @Override
        public void onClick(View v) {
            presenter.onAnswerButtonClicked(myIndex);
        }
    }


}
