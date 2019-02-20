package com.fdunlap.hacksclearn;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fdunlap.hacksclearn.retrofit.OpenTDBApiInterface;
import com.fdunlap.hacksclearn.retrofit.OpenTDBClient;
import com.fdunlap.hacksclearn.retrofit.OpenTDBResponse;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int MAX_BUTTONS = 4;
    private final int LARGE_NUM = 3999;

    // Score Text View ("Score: 0")
    private TextView scoreTextView;
    //The textview *inside* of the Card View -- this is where we're displaying question text.
    private TextView questionTextView;

    // An array to hold our buttons so that we can iterate through them later
    private ArrayList<Button> answerButtons = new ArrayList<>();

    // This is our POJO that models the question. The buttonText array list stores the text that
    // will go on our buttons *in the order they're displayed.*
    private OpenTDBResponse.Question currentQuestion;
    private ArrayList<String> buttonText = new ArrayList<>();

    // This is the index of the button that has the correct answer.
    private int correctAnswer;

    // Create a random to select our correct answer
    Random rand = new Random();

    private int score = 0;

    // This is the retrofit interface that allows us to access the OpenTrivia Database.
    // We'll talk at length about Retrofit and why it's awesome.
    OpenTDBApiInterface client;

    // *The Android Activity Lifecycle* https://s3-us-west-1.amazonaws.com/nearsoft-com-media/uploads/2018/01/android-activity-lifecycle-01.png
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First of all, we need to assign our components to our component variables.
        scoreTextView = findViewById(R.id.score_counter_text_view);
        questionTextView = findViewById(R.id.question_text_view);
        answerButtons.add((Button) findViewById(R.id.answer_button_1));
        answerButtons.add((Button) findViewById(R.id.answer_button_2));
        answerButtons.add((Button) findViewById(R.id.answer_button_3));
        answerButtons.add((Button) findViewById(R.id.answer_button_4));
        
        // Get the OpenTDB client
        client = OpenTDBClient.getClient().create(OpenTDBApiInterface.class);

        // Iterate through the buttons and attach their onClickListeners
        for(int i = 0; i < answerButtons.size(); i++){
            Button b = answerButtons.get(i);
            b.setOnClickListener(new buttonClickListener(i));
        }
        
        // If we've saved a question, don't get a new one and instead JUST update the new UI
        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.serialized_question_key))) {
            getQuestion();
        } else {
            // Restore state following lifecycle change.
            currentQuestion = (OpenTDBResponse.Question) savedInstanceState.getSerializable(getString(R.string.serialized_question_key));
            score = savedInstanceState.getInt(getString(R.string.score_key));
            updateUi();
        }
    }

    private void updateUi() {
        // Parse and set the score text
        String scoreText = String.format(getString(R.string.empty_score) + "%d", score);
        scoreTextView.setText(scoreText);

        // Todo: Do we really need to randomize after every transition? See if you can preserve the ordering of the answer buttons.
        randomizeAnswers();

        int numAnswers = currentQuestion.getIncorrectAnswers().size() + 1;

        // Make sure that all of the relevant buttons are displayed, and set their text.
        for(int i = 0; i < numAnswers; i++){
            answerButtons.get(i).setVisibility(View.VISIBLE);
            answerButtons.get(i).setText(buttonText.get(i));
        }

        // If there are any left over buttons, make them invisible
        for(int i = numAnswers; i < MAX_BUTTONS; i++){
            answerButtons.get(i).setVisibility(View.GONE);
        }

        // Set the question text.
        questionTextView.setText(currentQuestion.getText());
    }

    private void randomizeAnswers() {
        // Randomly select a button index to be the 'correct answer'
        correctAnswer = rand.nextInt(LARGE_NUM) % (currentQuestion.getIncorrectAnswers().size() + 1);
        // Clear out existing buttonText array.
        buttonText.clear();
        // Add all the incorrect answers,
        buttonText.addAll(currentQuestion.getIncorrectAnswers());
        // Insert the correct answer into the correct index of the array.
        Log.d("TAG", correctAnswer + "");
        buttonText.add(correctAnswer, currentQuestion.getCorrectAnswer());
    }
    
    // This is our Retrofit wrapper. in it, we ask retrofit to request a new question (or set of questions),
    // and then define a callback -- or "What do I do when I get the response." Remember that calls 
    // to the network take a lot of time, and that we don't want our UI thread to be waiting
    // on the result of a network call. So, retrofit makes our lives easier and makes this asynchronous.
    // (See AsynchTask in the Android docs for the underlying mechanism that allows Retrofit to do this.)

    //TODO: Consider implementing your own caching technique to the retrofit call to get more comfortable dealing with Retrofit
    // (E.g. pull in 10 results instead of just 1, and iterate through them until you run out.)
    // Can you make it s/t there's no network delay apparent to the user? (Hint: you totally can!)
    public void getQuestion() {
        client.getMultipleChoice().enqueue(new questionCallBackResponse());
    }

    // Preserve state on lifecycle changes.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(getString(R.string.serialized_question_key), currentQuestion);
        outState.putInt(getString(R.string.score_key) , score);
        super.onSaveInstanceState(outState);
    }

    // Helper method to build a dialog
    private void createRetryDialog(Call<OpenTDBResponse> call){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.something_went_wrong)
                .setTitle(R.string.network_error)
                .setPositiveButton(R.string.retry, (dialogInterface, i) -> getQuestion())
                .setNegativeButton(R.string.exit, ((dialogInterface, i) -> finishAndRemoveTask()))
                .setOnCancelListener((v) -> finishAndRemoveTask())
                .create().show();
    }

    private class questionCallBackResponse implements Callback<OpenTDBResponse>{
        @Override
        public void onResponse(Call<OpenTDBResponse> call, Response<OpenTDBResponse> response) {
            OpenTDBResponse res = response.body();
            int rCode = res.responseCode;
            Log.d("TAG", res.responseCode + "");
            if(rCode == 0){
                    /* We're just getting the first question. This is some functionality that could
                       definitely be made more robust with caching/randomizing/better fetching. */
                OpenTDBResponse.Question resultQuestion = res.questions.get(0);
                    /*
                    Due to a limitation in the OpenTDB API, we cannot specify how many answers we want,
                    only whether we want T/F questions or multiple choice. So, if we get a question
                    with more answers than we can fit on the screen, we just ask for another question.
                    This is *not* ideal, and is even more reason to optimize and cache some questions.
                     */
                if(resultQuestion.incorrectAnswers.size() + 1 > MAX_BUTTONS){
                    getQuestion();
                } else {
                    currentQuestion = resultQuestion;
                    updateUi();
                }
            } else {
                Log.d("TAG", "Retry Dialog");
                createRetryDialog(call);
            }
        }

        @Override
        public void onFailure(Call<OpenTDBResponse> call, Throwable t) {
            Log.d("TAG", "Retry Dialog");
            createRetryDialog(call);
        }
    }

    /*
    Quick little inner class to define the behavior of the buttons. Note that since Android uses
    classes to deal with events, we can define our own constructors and store state data alongside
    event listeners.
    */
    private class buttonClickListener implements View.OnClickListener {
        int myIndex;

        buttonClickListener(int myIndex){
            this.myIndex = myIndex;
        }

        //Todo: Try adding some sort of UI event that is more pleasing than Toasts!
        @Override
        public void onClick(View view) {
            if(correctAnswer == myIndex){
                Toast.makeText(MainActivity.this, "That's correct!", Toast.LENGTH_SHORT).show();
                score++;
            } else {
                String toastMessage = String.format("Sorry, that was incorrect :( The correct answer was: %s",buttonText.get(correctAnswer));
                Toast.makeText(MainActivity.this,  toastMessage, Toast.LENGTH_SHORT).show();
            }
            getQuestion();
        }
    }
}
