package com.sonant.dsin.classes;


import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class ForceUpdateChecker {
    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
        public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String KEY_UPDATE_URL = "force_update_store_url";
    public static final String KEY_MUST_UPDATE = "must_update";
    boolean mustUpdateStatus = false;

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public ForceUpdateChecker(@NonNull Context context, OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        Log.i(TAG, "check: 1 " + remoteConfig.getBoolean(KEY_UPDATE_REQUIRED));
        Log.i(TAG, "check: 2 " + remoteConfig.getString(KEY_UPDATE_URL));
        Log.i(TAG, "check: 3 " + remoteConfig.getString(KEY_CURRENT_VERSION));
        Log.i(TAG, "check: 4 " + remoteConfig.getBoolean(KEY_MUST_UPDATE));
        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            Log.i(TAG, "check: 2");
            String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

            if (!TextUtils.equals(currentVersion, appVersion)
                    && onUpdateNeededListener != null) {
                Log.i(TAG, "check: 3 " + currentVersion + " appversion " + appVersion);
                onUpdateNeededListener.onUpdateNeeded(updateUrl);
            } else if (TextUtils.equals(currentVersion, appVersion)
                    && onUpdateNeededListener != null) {
                onUpdateNeededListener.onUpdateNeeded(null);
            }
        }
    }

    public boolean mustUpdate() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        if (remoteConfig.getBoolean(KEY_MUST_UPDATE)) {
            mustUpdateStatus = true;
        } else {
            mustUpdateStatus = false;
        }
        return mustUpdateStatus;
    }

    private String getAppVersion(Context context) {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public ForceUpdateChecker check() {
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
    }
}