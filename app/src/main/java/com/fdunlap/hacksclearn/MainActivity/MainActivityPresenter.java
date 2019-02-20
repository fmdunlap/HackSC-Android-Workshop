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
    private final int MAX_BUTTONS = 4;
    // Just an arbitrarily large number since Rand was occasionally overflowing and causing... issues.
    private final int LARGE_NUM = 3999;
    private final String TAG = "MainActivityPresenter";
    // Create a random to select our correct answer
    private Random rand = new Random();
    // This is the retrofit interface that allows us to access the OpenTrivia Database.
    // We'll talk at length about Retrofit and why it's awesome.
    private OpenTDBApiInterface client;
    private MainActivityModel model;
    private MainActivityContract.View view;


    MainActivityPresenter(MainActivityContract.View view) {
        model = new MainActivityModel();
        this.view = view;

        client = OpenTDBClient.getClient().create(OpenTDBApiInterface.class);
    }

    /* This is our Retrofit wrapper. In it, we ask retrofit to request a new question (or a set of questions),
     and then define a callback -- or "What do I do when I get the response." Calls
     to the network take a lot of time, and that we don't want our UI thread to be waiting
     on the result of a network call. So, retrofit makes our lives easier and makes this asynchronous.
    (See AsynchTask in the Android docs for the underlying mechanism that allows Retrofit to do this.) */

    //TODO: Consider implementing your own caching technique to the retrofit call to get more comfortable dealing with Retrofit
    // (E.g. pull in 10 results instead of just 1, and iterate through them until you run out.)
    // Can you make it s/t there's no network delay apparent to the user? (Hint: you totally can!)
    @Override
    public void getNextQuestion() {
        client.getMultipleChoice().enqueue(new questionCallBackResponse());
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        model.setScore(savedInstanceState.getInt(MainActivityContract.SCORE_KEY));
        model.setCurrentQuestion((OpenTDBResponse.Question) savedInstanceState.getSerializable(MainActivityContract.SERIALIZED_QUESTION_KEY));
        model.setAnswerIndex(savedInstanceState.getInt(MainActivityContract.ANSWER_KEY));
        updateView();
    }

    @Override
    public Bundle bundleState(Bundle outState) {
        outState.putSerializable(MainActivityContract.SERIALIZED_QUESTION_KEY, model.getCurrentQuestion());
        outState.putInt(MainActivityContract.SCORE_KEY, model.getScore());
        outState.putInt(MainActivityContract.ANSWER_KEY, model.getAnswerIndex());
        return outState;
    }

    private void updateView() {
        view.setScore(model.getScore());

        int numAnswers = model.getCurrentQuestion().getIncorrectAnswers().size() + 1;

        ArrayList<String> buttonText = new ArrayList<>();
        buttonText.clear();
        buttonText.addAll(model.getCurrentQuestion().getIncorrectAnswers());
        buttonText.add(model.getAnswerIndex(), model.getCurrentQuestion().getCorrectAnswer());

        // Make sure that all of the relevant buttons are displayed, and set their text.
        for (int i = 0; i < numAnswers; i++) {
            view.setButtonVisibility(View.VISIBLE, i);
            view.setButtonText(buttonText.get(i), i);
        }

        // If there are any left over buttons, make them invisible
        for (int i = numAnswers; i < MAX_BUTTONS; i++) {
            view.setButtonVisibility(View.GONE, i);
        }

        // Set the question text.
        view.setQuestionText(model.getCurrentQuestion().getText());
    }

    /*
    Quick little inner class to define the behavior of the buttons. Note that since Android uses
    classes to deal with events, we can define our own constructors and store state data alongside
    event listeners.
    */
    @Override
    public void onAnswerButtonClicked(int buttonIndex) {
        if (model.getAnswerIndex() == buttonIndex) {
            view.showToast("That's correct!");
            model.setScore(model.getScore() + 1);
        } else {
            String toastMessage = String.format("Sorry, that was incorrect :( The correct answer was: %s", model.getCurrentQuestion().getCorrectAnswer());
            view.showToast(toastMessage);
        }
        getNextQuestion();
    }

    @Override
    public boolean hasSavedState(Bundle savedInstanceState) {
        return false;
    }

    private class questionCallBackResponse implements Callback<OpenTDBResponse> {
        @Override
        public void onResponse(@NonNull Call<OpenTDBResponse> call,@NonNull Response<OpenTDBResponse> response) {
            OpenTDBResponse responseBody = response.body();
            int rCode = responseBody != null ? responseBody.responseCode : -1; // quick and easy way to abort if the body is null.
            Log.d(TAG, "" + rCode);
            if (rCode == 0) {
                /* We're just getting the first question. This is some functionality that could
                   definitely be made more robust with caching/randomizing/better fetching.*/
                OpenTDBResponse.Question resultQuestion = responseBody.questions.get(0);
                    /*
                    Due to a limitation in the OpenTDB API, we cannot specify how many answers we want,
                    only whether we want T/F questions or multiple choice. So, if we get a question
                    with more answers than we can fit on the screen, we just ask for another question.
                    This is *not* ideal, and is even more reason to optimize and cache some questions.
                     */
                if (resultQuestion.incorrectAnswers.size() + 1 > MAX_BUTTONS) {
                    getNextQuestion();
                } else {
                    model.setCurrentQuestion(resultQuestion);
                    // Randomize and store the index of the correct answer.
                    int correctAnswer = rand.nextInt(LARGE_NUM) % (resultQuestion.getIncorrectAnswers().size() + 1);
                    model.setAnswerIndex(correctAnswer);
                    updateView();
                }
            } else {
                Log.d(TAG, "Response code was not 0!");
                Log.d(TAG, response.toString());
                view.showRetryDialog();
            }
        }

        @Override
        public void onFailure(@NonNull Call<OpenTDBResponse> call,@NonNull Throwable t) {
            Log.d(TAG, "Retry Dialog");
            view.showRetryDialog();
        }
    }
}
