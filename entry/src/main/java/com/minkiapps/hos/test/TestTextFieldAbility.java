package com.minkiapps.hos.test;

import com.minkiapps.hostest.BuildConfig;
import com.minkiapps.hostest.ResourceTable;
import com.minkiapps.hos.test.util.LogUtils;
import com.minkiapps.hos.test.util.UIUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.TextField;

public class TestTextFieldAbility extends AbilitySlice {

    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_test_textfield);

        final TextField textField = (TextField)findComponentById(ResourceTable.Id_tf_ability_test_textfield);
        if(BuildConfig.DEBUG) {
            textField.setText("Hi my name is Max, I am the new guy here. I am a very good iOS developer. In my free time I like to do sports and yoga. Feel free to ask me anything, I am looking forward getting knowing you all. My seat is in the right left corner. Hey, maybe we could arrange an after work beer this Friday? What do you think?");
        }
        textField.setEditorActionListener(actionId -> {
            LogUtils.d("TEST TEXT INPUT", "Editor Action ID: " + actionId);
            return true;
        });

        UIUtils.configureCrownWithTextInput(textField);
        UIUtils.showKeyBoard(textField);
    }
}
