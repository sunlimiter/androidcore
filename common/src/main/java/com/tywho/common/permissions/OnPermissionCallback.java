package com.tywho.common.permissions;

/**
 * Created by limit on 2017/3/29.
 */

public interface OnPermissionCallback {
    public static final int PERMISSION_ALERT_WINDOW = 0xad1;
    public static final int PERMISSION_WRITE_SETTING = 0xad2;

    public void onSuccess(String... permissions);

    public void onFail(String... permissions);
}
