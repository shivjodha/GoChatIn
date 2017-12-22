package com.gochatin.gochatin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gochatin.gochatin.ChatActivity;

import halper.WebsocketClass;

/**
 * Created by Dell on 8/28/2017.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent pushIntent = new Intent(context, WebsocketClass.class);
            context.startService(pushIntent);
        }
    }
}
