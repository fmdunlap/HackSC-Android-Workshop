package com.fdunlap.hacksclearn.retrofit;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

// This is a very literal JSON -> POJO translation, and it serves as the backbone of how Retrofit
// stores our data from the API.
// NOTE: The @SerializedName is the name that comes *directly* from the JSON. The actual variable
// can be named whatever you like.
public class OpenTDBResponse  {
    @SerializedName("response_code")
    public Integer responseCode;

    @SerializedName("results")
    public List<Question> questions = null;

    public class Question implements Serializable{
        @SerializedName("category")
        String category;

        @SerializedName("type")
        String type;

        @SerializedName("difficulty")
        String difficulty;

        @SerializedName("question")
        String text;

        @SerializedName("correct_answer")
        String correctAnswer;

        @SerializedName("incorrect_answers")
        public List<String> incorrectAnswers;

        public String getCategory() {
            return Html.fromHtml(category, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        public String getType() {
            return Html.fromHtml(type, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        public String getDifficulty() {
            return Html.fromHtml(difficulty, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        public String getText() {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        public String getCorrectAnswer() {
            return Html.fromHtml(correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        //That stream is a quick and dirty little way to escape the html
        public List<String> getIncorrectAnswers() {
            return incorrectAnswers
                    .stream()
                    .map(answer -> Html.fromHtml(answer, Html.FROM_HTML_MODE_COMPACT).toString())
                    .collect(Collectors.toList());
        }
    }
}
