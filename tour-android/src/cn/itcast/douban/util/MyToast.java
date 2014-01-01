package cn.itcast.douban.util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

	public static void showToast(Context context, String text, int duration) {
		Toast t = Toast.makeText(context, text, duration);
		t.show();
	}
}
