package com.minkiapps.hos.test.util;

import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.concurrent.atomic.AtomicInteger;

public class UIUtils {

    public static int vpTpPixel(final Context context, final int vp) {
        return (int) (DisplayManager.getInstance()
                .getDefaultDisplay(context)
                .get()
                .getAttributes().densityPixels * vp);
    }

    public static void showKeyBoard(final TextField textField) {
        textField.setFocusable(Component.FOCUS_ENABLE);
        textField.requestFocus();
        textField.setFocusChangedListener((component, hasFocus) -> {
            if (hasFocus) {
                textField.setFocusChangedListener(null);
                textField.simulateClick();
            }
        });
    }

    public static void configureCrownWithTextInput(final Text text) {
        final int rotationSensibilityFactor = 3;
        final AtomicInteger lastCounter = new AtomicInteger();

        text.setFocusable(Component.FOCUS_ENABLE);
        text.requestFocus();
        text.setRotationEventListener((component, rotationEvent) -> {
            final float value = rotationEvent.getRotationValue();
            if(value > 0) {
                lastCounter.getAndIncrement();
            } else {
                lastCounter.getAndDecrement();
            }

            if(Math.abs(lastCounter.get()) == rotationSensibilityFactor) {
                lastCounter.set(0);
                if(value > 0) {
                    text.scrollBy(0, -vpTpPixel(text.getContext(), 10));
                } else {
                    text.scrollBy(0, vpTpPixel(text.getContext(), 10));
                }
            }

            return true;
        });
    }
}
