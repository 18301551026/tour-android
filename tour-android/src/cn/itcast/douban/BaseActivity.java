package cn.itcast.douban;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

/**
 * 当前豆瓣客户端所有activity的基类
 * 
 * @author zehua
 * 
 */
public abstract class BaseActivity extends Activity {
	private SharedPreferences sp;
	// protected DoubanService myService;
	protected RelativeLayout loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 前提 必须先设置contentview
		// 初始化view
		sp = getSharedPreferences("config", MODE_PRIVATE);
		findView();
		loading = (RelativeLayout) this.findViewById(R.id.loading);		
		setUpView();
	}

	protected void showLoading() {
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		loading.startAnimation(aa);
		loading.setVisibility(View.VISIBLE);
	}

	protected void hideLoading() {
		AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
		aa.setDuration(2000);
		loading.startAnimation(aa);
		loading.setVisibility(View.INVISIBLE);
	}

	protected abstract void findView();

	protected abstract void setUpView();

	@Override
	protected void onStart() {
		super.onStart();
		// 1.豆瓣官网申请一个 apkkey 还有secret
		// String apiKey = "06469c2e1ccac8df12d3d48a56605a9d";
		// String secret = "fb078b49b67c7ad3";
		//
		// //2.带着申请的key去访问豆瓣的网站
		// myService = new DoubanService("黑马7的小豆瓣", apiKey,
		// secret);
		//
		// ArrayList<String> tokens = getUserAuthInfo();
		// myService.setAccessToken(tokens.get(0), tokens.get(1));

	}

	/*	*//**
	 * 判断用户是否已经完成了认证
	 * 
	 * @return
	 */
	/*
	 * protected boolean isUserAuth() {
	 * 
	 * String accesstoken = sp.getString("accesstoken", null); String
	 * tokensecret = sp.getString("tokensecret", null); if (accesstoken == null
	 * || tokensecret == null) { return false; }else{ return true; }
	 * 
	 * }
	 *//**
	 * 判断用户是否已经完成了认证
	 * 
	 * @return
	 */
	/*
	 * protected ArrayList<String> getUserAuthInfo() { ArrayList<String> tokens
	 * = new ArrayList<String>(); String accesstoken =
	 * sp.getString("accesstoken", null); String tokensecret =
	 * sp.getString("tokensecret", null); tokens.add(accesstoken);
	 * tokens.add(tokensecret); return tokens; }
	 */

}
