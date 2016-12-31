package com.example.administrator.netspeedtest.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class Utils {
	public static Context context;
	public static void init(Context context) {
		Utils.context = context.getApplicationContext();
	}

	/**
	 * 判断进程是否运行
	 * @return
	 */
	public static boolean isProessRunning(Context context, String proessName) {

		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
			if (info.processName.equals(proessName)) {
				isRunning = true;
			}
		}

		return isRunning;
	}
}
