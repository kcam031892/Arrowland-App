package com.arrowland.arrowland;

import android.support.annotation.NonNull;

/**
 * Created by Mhack Bautista on 8/18/2018.
 */

public interface MembershipStatusCallbacks {

    void onSuccess(@NonNull String status);

    void onError(@NonNull Throwable throwable);
}
