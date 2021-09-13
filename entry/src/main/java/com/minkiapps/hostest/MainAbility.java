package com.minkiapps.hostest;

import com.huawei.watch.kit.hiwear.HiWear;
import com.huawei.watch.kit.hiwear.p2p.P2pClient;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainAbility extends Ability {

    private final static String peerPkgName = "com.minkiapps.wearengine.test";
    private final static String peerFinger = "CFCC7E8B7AF0C5B2B488190B17B897BB483541B26A7F15065602D716E586FEDA";

    private final static int REQUEST_PERMISSION_CODE = 10001;

    private P2pClient p2pClient;

    public P2pClient getP2pClient() {
        return p2pClient;
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        setSwipeToDismiss(true);

        p2pClient = HiWear.getP2pClient(this);
        p2pClient.setPeerPkgName(peerPkgName);
        p2pClient.setPeerFingerPrint(peerFinger);

        final String[] permission = {
                "ohos.permission.MICROPHONE",
                "ohos.permission.WRITE_USER_STORAGE",
                "ohos.permission.READ_HEALTH_DATA"
        };
        final List<String> permissionList = new ArrayList<>();
        for (String s : permission) {
            if (verifySelfPermission(s) != 0 && canRequestPermission(s)) {
                permissionList.add(s);
            }
        }
        if (permissionList.size() > 0) {
            requestPermissionsFromUser(permissionList.toArray(new String[0]), REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions,
                                                   int[] grantResults) {
        // Match requestCode of requestPermissionsFromUser.
        if (requestCode == REQUEST_PERMISSION_CODE) {
            final boolean allGranted = Arrays.stream(grantResults).allMatch(i -> i == 0);
            if(!allGranted) {
                new ToastDialog(getContext())
                        .setAlignment(LayoutAlignment.CENTER)
                        .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                        .setText("Not all permission are granted!")
                        .show();
            }
        }
    }
}
