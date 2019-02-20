package com.fdunlap.hacksclearn.MainActivity;

import com.fdunlap.hacksclearn.retrofit.OpenTDBResponse;

// The model class manages data just like a POJO. If you're familiar with Web Development, it's just
// like 'state' in many frameworks.
class MainActivityModel {
    final String TAG = "MainActivityModel"; // Logging helper.

    private int score;
    private OpenTDBResponse.Question currentQuestion;
    private int answerIndex;

    int getScore(){
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    OpenTDBResponse.Question getCurrentQuestion() {
        return currentQuestion;
    }

    void setCurrentQuestion(OpenTDBResponse.Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    int getAnswerIndex() {
        return answerIndex;
    }

    void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }
}
