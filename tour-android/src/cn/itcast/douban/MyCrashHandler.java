package cn.itcast.douban;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

public class MyCrashHandler implements UncaughtExceptionHandler {
	// 1.私有化构造方法
	private MyCrashHandler() {
	};

	private static UncaughtExceptionHandler defaulthandler;
	private static MyCrashHandler myCrashHandler;
	private static Context context;

	// 2.提供一个静态的方法 获取到 当前类的一个实例
	public synchronized static MyCrashHandler getInstance(Context c) {
		defaulthandler = Thread.currentThread()
				.getDefaultUncaughtExceptionHandler();
		context = c;
		if (myCrashHandler == null) {
			myCrashHandler = new MyCrashHandler();
		}
		return myCrashHandler;
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("发生了异常");

		try {
			StringBuilder sb = new StringBuilder();

			File file = new File(Environment.getExternalStorageDirectory(),
					"error.log");
			FileOutputStream fos = new FileOutputStream(file);
			// 1.获取当前应用程序的版本号,版本信息.
			PackageManager pm = context.getPackageManager();
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(),
					0);
			String versionname = packinfo.versionName;
			sb.append("版本号" + versionname);
			sb.append("\n");

			long mimutes = System.currentTimeMillis();
			sb.append("时间" + mimutes);

			// 2.获取当前手机的操作系统的信息,手机的一些硬件信息
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);// 暴力反射 获取私有的字段
				sb.append(field.getName());
				sb.append("=");
				sb.append(field.get(null).toString());
				sb.append("\n");
			}

			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			String errorlog = writer.toString();

			System.out.println(errorlog);
			sb.append(errorlog);
			String errorinfo = sb.toString();
			byte[] result = errorinfo.getBytes();

			// String apiKey = "06469c2e1ccac8df12d3d48a56605a9d";
			// String secret = "fb078b49b67c7ad3";
			// // 2.带着申请的key去访问豆瓣的网站
			// DoubanService myService = new DoubanService("黑马7的小豆瓣", apiKey,
			// secret);
			//
			// myService.setAccessToken("dc6caafd02e736b0954600f708b5a410",
			// "ae47dd758aa2656d");
			// myService.createNote(new PlainTextConstruct("错误日记"), new
			// PlainTextConstruct(errorinfo), "private", "no");

			fos.write(result, 0, result.length);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			android.os.Process.killProcess(android.os.Process.myPid());
		}

		defaulthandler.uncaughtException(thread, ex);

		// 调用系统的异常处理的代码
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

}
