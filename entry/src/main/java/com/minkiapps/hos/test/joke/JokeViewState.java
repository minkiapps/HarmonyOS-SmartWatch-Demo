package com.minkiapps.hos.test.joke;

import com.minkiapps.hos.test.net.model.Joke;
import com.minkiapps.hos.test.base.BaseViewState;

import java.util.List;

abstract class JokeViewState extends BaseViewState {
    public static class Loading extends JokeViewState {

    }

    public static class ErrorState extends JokeViewState {
        private final Throwable throwable;

        public ErrorState(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    public static class Loaded extends JokeViewState {
        private final List<Joke> jokes;

        public Loaded(List<Joke> jokes) {
            this.jokes = jokes;
        }

        public List<Joke> getJokes() {
            return jokes;
        }
    }
}
