package com.minkiapps.hos.test.net;

import com.minkiapps.hos.test.net.model.Joke;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.List;

public interface ApiService {

    @GET("jokes/programming/random")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Observable<List<Joke>> fetchJokes();
}