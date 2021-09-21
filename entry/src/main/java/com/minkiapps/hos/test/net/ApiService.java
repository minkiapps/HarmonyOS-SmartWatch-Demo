package com.minkiapps.hos.test.net;

import com.minkiapps.hos.test.net.model.Joke;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {

    @GET("jokes/random")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Observable<Joke> fetchJokes();
}
