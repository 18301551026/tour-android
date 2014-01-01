package cn.itcast.douban;

import java.util.HashMap;
import java.util.Map;

import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import cn.itcast.douban.domain.User;
import cn.itcast.douban.util.MyToast;
import cn.itcast.douban.util.NetUtils;

import com.alibaba.fastjson.JSON;

public class LoginActivity extends Activity {
	protected static final int SUCCESS = 10;
	protected static final int ERROR = 11;
	protected static final int NEED_CAPTCHA = 12;
	protected static final int DONT_NEED_CAPTCHA = 13;
	protected static final int GET_CAPTCHA_FINISH = 14;
	protected static final int GET_CAPTCHA_ERROR = 15;

	private EditText et_email;
	private EditText et_pwd;
	private ProgressDialog pd;
	private SharedPreferences sp;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case SUCCESS:
				MyToast.showToast(getApplicationContext(), "登陆成功", 1);

				String json = msg.getData().getString("json");
				MyToast.showToast(getApplicationContext(), json, 1);
				User currentUser = JSON.parseObject(json, User.class);
				DoubanApplication myapp = (DoubanApplication) getApplication();
				myapp.currentUser = currentUser;

				Intent intent = new Intent(LoginActivity.this,
						MainTabActivity.class);
				startActivity(intent);

				finish();
				break;
			case ERROR:
				MyToast.showToast(getApplicationContext(), "登陆失败", 1);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		pd = new ProgressDialog(this);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		et_email = (EditText) this.findViewById(R.id.EditTextEmail);
		et_pwd = (EditText) this.findViewById(R.id.EditTextPassword);

	}

	public void btnUserLogin(View view) {
		final String email = et_email.getText().toString().trim();
		final String pwd = et_pwd.getText().toString().trim();
		
		SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
		final String xmppUserName = preferences.getString(Constants.XMPP_USERNAME, "");
		MyToast.showToast(this, xmppUserName, 1);
		

		if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
			MyToast.showToast(this, "用户名或者密码不能为空", 1);
			return;
		} else {
			pd.setMessage("正在登陆");
			pd.show();

			new Thread() {
				public void run() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("xmppUserName", xmppUserName);
					params.put("userName", email);
					params.put("password", pwd);
					String json = NetUtils.callWs("userService", "doLoginWs",
							params);

					Message msg = Message.obtain();
					if (null == json || "null".equals(json.trim())) {
						msg.what = ERROR;
					} else {
						Bundle b = new Bundle();
						b.putString("json", json);
						msg.setData(b);
						msg.what = SUCCESS;
					}
					handler.sendMessage(msg);

				};
			}.start();
		}

	}

	public void btnUserCancle(View view) {
		finish();
	}
}
