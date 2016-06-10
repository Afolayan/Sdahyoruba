package com.jcedar.sdahyoruba.gcm;

import android.content.Context;

public abstract class GCMCommand
{
    public abstract void execute(Context context, String type, String extraData);
}
