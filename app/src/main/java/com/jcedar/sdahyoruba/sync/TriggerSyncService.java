package com.jcedar.sdahyoruba.sync;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jcedar.sdahyoruba.helper.AccountUtils;
import com.jcedar.sdahyoruba.provider.DataContract;

/**
 * Created by Afolayan on 28/1/2016.
 */
public class TriggerSyncService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String accountName = AccountUtils.getChosenAccountName(context);
        if (TextUtils.isEmpty(accountName)) {
            return;
        }

        ContentResolver.requestSync(
                AccountUtils.getChosenAccount(context),
                DataContract.CONTENT_AUTHORITY, new Bundle());
    }
}
