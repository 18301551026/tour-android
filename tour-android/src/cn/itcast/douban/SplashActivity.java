package cn.itcast.douban;

import org.androidpn.client.ServiceManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {
	private TextView tv_version;
	private LinearLayout LinearLayout01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash);
		super.onCreate(savedInstanceState);

		// Start the service for notification
		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.notification);
		serviceManager.startService();
	}

	/**
	 * 当界面重新用户可见
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isNetworkAvaiable()) {
			// TODO:延时两秒钟 进入主界面
			new Thread() {
				@Override
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent(SplashActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}.start();

		} else {
			// 激活系统的网络设置的界面 让用户设置网络状态
			AlertDialog.Builder builder = new Builder(this);
			Drawable icon = getResources().getDrawable(
					R.drawable.indicator_input_error);
			builder.setIcon(icon);
			builder.setTitle("设置网络");
			builder.setMessage("网络连接错误,请检查网络");
			builder.setPositiveButton("设置网络", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
					intent.setClassName("com.android.settings",
							"com.android.settings.WirelessSettings");
					startActivity(intent);
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					finish();
				}
			});
			builder.create().show();
		}
	}

	/**
	 * 判断客户端的网络连接状况
	 */
	public boolean isNetworkAvaiable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		// 当前有效的(活动的)网络信息
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号错误";
		}
	}

	@Override
	protected void findView() {
		// 1.寻找控件
		tv_version = (TextView) this.findViewById(R.id.versionNumber);
		LinearLayout01 = (LinearLayout) this.findViewById(R.id.LinearLayout01);
	}

	@Override
	protected void setUpView() {
		// 2.初始化控件的文本,或者点击事件
		tv_version.setText(getVersion());

		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		LinearLayout01.startAnimation(aa);
	}
}