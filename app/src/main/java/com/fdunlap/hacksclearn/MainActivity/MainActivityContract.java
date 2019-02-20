package com.fdunlap.hacksclearn.MainActivity;

import android.os.Bundle;

public interface MainActivityContract {
    String ANSWER_KEY = "answer_key";
    String SCORE_KEY = "score_key";
    String SERIALIZED_QUESTION_KEY = "serialized_question";

    interface View {
        void setQuestionText(String questionText);

        void setButtonText(String buttonText, int buttonIndex);

        void setButtonVisibility(int visibility, int buttonIndex);

        void showRetryDialog();

        void setScore(int score);

        void showToast(String toastMessage);
    }

    interface Presenter {
        void getNextQuestion();

        void restoreState(Bundle savedInstanceState);

        Bundle bundleState(Bundle outState);

        void onAnswerButtonClicked(int buttonIndex);

        boolean hasSavedState(Bundle savedInstanceState);
    }

}
