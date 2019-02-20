package com.fdunlap.hacksclearn.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// These are the API prefixes for OpenTDB. The @Query will append a ?value=passed_in or &value=passed_in
// to the url in the GET annotation. There are the other annotations, (PUT, POST, DELETE, etc) that
// are standard for other APIs, as well. OpenTDB just uses GET, however.
public interface OpenTDBApiInterface {
    @GET("/api.php")
    Call<OpenTDBResponse> getQuestions(@Query(value="amount", encoded = true) int amount, @Query(value = "type", encoded = true) String type);

    @GET("/api.php?amount=1")
    Call<OpenTDBResponse> getQuestion(@Query(value = "type", encoded = true) String type);

    @GET("/api.php?amount=1&type=multiple")
    Call<OpenTDBResponse> getMultipleChoice();
}
