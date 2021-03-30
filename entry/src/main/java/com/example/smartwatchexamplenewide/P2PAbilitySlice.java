package com.example.smartwatchexamplenewide;

import com.huawei.watch.kit.hiwear.p2p.P2pClient;
import ohos.aafwk.ability.AbilitySlice;

public abstract class P2PAbilitySlice extends AbilitySlice {

    protected P2pClient getP2PClient() {
        return ((MainAbility)getAbility()).getP2pClient();
    }
}
