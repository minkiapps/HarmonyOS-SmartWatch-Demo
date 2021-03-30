package com.example.smartwatchexamplenewide;

import com.huawei.watch.kit.hiwear.HiWear;
import com.huawei.watch.kit.hiwear.p2p.P2pClient;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class MainAbility extends Ability {

    private final static String peerPkgName = "com.akent.androidtest";
    private final static String peerFinger = "CFCC7E8B7AF0C5B2B488190B17B897BB483541B26A7F15065602D716E586FEDA";

    private P2pClient p2pClient;

    public P2pClient getP2pClient() {
        return p2pClient;
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        p2pClient = HiWear.getP2pClient(this);
        p2pClient.setPeerPkgName(peerPkgName);
        p2pClient.setPeerFingerPrint(peerFinger);

        final String[] permission = {
                "ohos.permission.MICROPHONE",
                "ohos.permission.WRITE_USER_STORAGE"
        };
        final List<String> permissionList = new ArrayList<>();
        for (String s : permission) {
            if (verifySelfPermission(s) != 0 && canRequestPermission(s)) {
                permissionList.add(s);
            }
        }
        if (permissionList.size() > 0) {
            requestPermissionsFromUser(permissionList.toArray(new String[0]), 0);
        }
    }
}
