package com.stufeed.android.listener;

import android.content.DialogInterface;

/**
 * Created by RWS 5 on 11/15/2016.
 */

public interface DialogListenerLogin {
    public void onNegative(DialogInterface dialog);

    public void onPositive(DialogInterface dialog);

    public void onNutural(DialogInterface dialog);
}
