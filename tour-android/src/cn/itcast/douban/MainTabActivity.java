package cn.itcast.douban;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainTabActivity extends TabActivity {
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity);
		mTabHost = getTabHost();

		TabSpec tabNotConfirm = mTabHost.newTabSpec("未提交");
		tabNotConfirm.setIndicator(getTabView(R.drawable.tab_main_nav_me, "未提交"));
		Intent notConfirmIntent = new Intent(this, NotConfirmTourActivity.class);
		tabNotConfirm.setContent(notConfirmIntent);

		TabSpec tabIsConfirmBook = mTabHost.newTabSpec("已提交");
		tabIsConfirmBook.setIndicator(getTabView(R.drawable.tab_main_nav_home, "已提交"));
		Intent isConfirmIntent = new Intent(this, IsConfirmTourActivity.class);
		tabIsConfirmBook.setContent(isConfirmIntent);

		mTabHost.addTab(tabNotConfirm);
		mTabHost.addTab(tabIsConfirmBook);
		
	}

	private View getTabView(int icon, String text) {
		View view = View.inflate(this, R.layout.tab_main_nav, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
		tvTitle.setText(text);
		ivIcon.setImageResource(icon);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("haha"); // 随便指定一个menu的标题
		// 必须要指定 否则会空指针
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		System.out.println("keycode" + event.getKeyCode());
		System.out.println("keycode" + event.getKeyCode());

		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("重置账号信息");
			builder.setPositiveButton("确定", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences sp = getSharedPreferences("config",
							MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("accesstoken", null);
					editor.putString("tokensecret", null);
					editor.commit();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

				}
			});
			builder.create().show();

			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("重置账号信息");
		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sp = getSharedPreferences("config",
						MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("accesstoken", null);

				editor.putString("tokensecret", null);

				editor.commit();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
		return false;
	}
}
