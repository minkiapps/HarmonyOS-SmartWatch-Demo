package com.example.smartwatchexamplenewide.joke;

import com.example.smartwatchexamplenewide.MyApplication;
import com.example.smartwatchexamplenewide.base.BaseViewModel;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class JokeViewModel extends BaseViewModel<JokeViewState> {

    public void fetchAJoke() {
        final Observable<JokeViewState> obs = MyApplication.getApiService()
                .fetchJokes()
                .map(JokeViewState.Loaded::new)
                .cast(JokeViewState.class)
                .onErrorReturn(JokeViewState.ErrorState::new)
                .subscribeOn(Schedulers.io())
                .startWith(new JokeViewState.Loading());

        super.subscribe(obs);
    }
}
