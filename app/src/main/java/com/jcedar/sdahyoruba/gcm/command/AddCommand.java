package com.jcedar.sdahyoruba.gcm.command;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.jcedar.sdahyoruba.gcm.GCMCommand;
import com.jcedar.sdahyoruba.helper.AccountUtils;
import com.jcedar.sdahyoruba.provider.DataContract;
import com.jcedar.sdahyoruba.sync.SyncHelper;

/**
 * Created by Afolayan on 28/1/2016.
 */
public class AddCommand extends GCMCommand {
    private static final String TAG = AddCommand.class.getSimpleName();

    @Override
    public void execute(Context context, String type, String extraData) {
        Log.d(TAG, "Trying to sync deleted account");
        Bundle settings = new Bundle();
        settings.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settings.putBoolean(SyncHelper.GCM_TRIGGERED, true);
        settings.putInt(SyncHelper.ACTION, Integer.parseInt(type));

        ContentResolver.requestSync(
                AccountUtils.getChosenAccount(context),
                DataContract.CONTENT_AUTHORITY, settings);
    }
}
