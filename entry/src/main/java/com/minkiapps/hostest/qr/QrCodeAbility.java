package com.minkiapps.hostest.qr;

import com.minkiapps.hostest.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class QrCodeAbility extends AbilitySlice {

    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_qrcode);
    }
}
