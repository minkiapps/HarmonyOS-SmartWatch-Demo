package com.example.smartwatchexamplenewide.joke;

import com.example.smartwatchexamplenewide.P2PAbilitySlice;
import com.example.smartwatchexamplenewide.ResourceTable;
import com.example.smartwatchexamplenewide.net.model.Joke;
import com.example.smartwatchexamplenewide.util.LogUtils;
import com.example.smartwatchexamplenewide.util.UiObserver;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.RoundProgressBar;
import ohos.agp.components.Text;

import java.util.List;

public class JokeAbilitySlice extends P2PAbilitySlice {

    private static final String TAG = "JokeAPI";

    private Text jokeTextView;
    private Button getJokeButton;
    private JokeViewModel jokeViewModel;
    private RoundProgressBar progressBar;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_joke);

        jokeViewModel = new JokeViewModel();

        getJokeButton = (Button) findComponentById(ResourceTable.Id_button_get_joke);
        progressBar = (RoundProgressBar) findComponentById(ResourceTable.Id_round_progress_bar);
        jokeTextView = (Text) findComponentById(ResourceTable.Id_text_joke);
        final Button goBackButton = (Button) findComponentById(ResourceTable.Id_button_back);
        goBackButton.setClickedListener(component -> {
            terminate();
        });

        getJokeButton.setClickedListener(component -> jokeViewModel.fetchAJoke());

        jokeViewModel.getStates().addObserver(new UiObserver<JokeViewState>(this) {
            @Override
            public void onValueChanged(JokeViewState jokeViewState) {
                if (jokeViewState instanceof JokeViewState.Loading) {
                    onLoading((JokeViewState.Loading)jokeViewState);
                } else if (jokeViewState instanceof JokeViewState.ErrorState) {
                    onError((JokeViewState.ErrorState)jokeViewState);
                } else if (jokeViewState instanceof JokeViewState.Loaded) {
                    onLoaded((JokeViewState.Loaded)jokeViewState);
                }
            }
        }, false);
    }

    private void onLoading(final JokeViewState.Loading loadingState) {
        progressBar.setVisibility(Component.VISIBLE);
        jokeTextView.setVisibility(Component.HIDE);
        getJokeButton.setEnabled(false);
    }

    private void onLoaded(final JokeViewState.Loaded loadedState) {
        progressBar.setVisibility(Component.HIDE);
        jokeTextView.setVisibility(Component.VISIBLE);
        getJokeButton.setEnabled(true);

        final List<Joke> jokes = loadedState.getJokes();
        if(jokes.size() > 0) {
            final Joke joke = jokes.get(0);
            jokeTextView.setText(joke.getSetup() + " " + joke.getPunchline());
            jokeTextView.startAutoScrolling();
        } else {
            jokeTextView.setText("No Jokes available...");
        }
    }

    private void onError(final JokeViewState.ErrorState errorState) {
        progressBar.setVisibility(Component.HIDE);
        jokeTextView.setVisibility(Component.VISIBLE);
        getJokeButton.setEnabled(true);

        final Throwable throwable = errorState.getThrowable();
        jokeTextView.setText("Failed to load joke... :(");
        LogUtils.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        jokeViewModel.unbind();
    }
}
