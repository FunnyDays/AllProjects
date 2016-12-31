package com.example.administrator.netspeedtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.netspeedtest.utils.Utils;

public class ManagerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		context.startService(new Intent(Utils.context, ManagerService.class));
	}

}
