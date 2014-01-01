package cn.itcast.douban;

import android.app.Application;
import cn.itcast.douban.domain.User;

public class DoubanApplication extends Application {

	public User currentUser;

	@Override
	public void onCreate() {

		Thread.currentThread().setUncaughtExceptionHandler(
				MyCrashHandler.getInstance(getApplicationContext()));

		super.onCreate();
	}
}
