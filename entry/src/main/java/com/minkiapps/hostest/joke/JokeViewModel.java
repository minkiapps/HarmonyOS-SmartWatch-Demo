package com.minkiapps.hostest.joke;

import com.minkiapps.hostest.MyApplication;
import com.minkiapps.hostest.base.BaseViewModel;
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
