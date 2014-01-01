package cn.itcast.douban.util;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class NetUtils {

	/**
	 * 命名空间
	 */
	public static String NAME_SPACE = "";

	/**
	 * EndPoint
	 */
	public static String END_POINT = "";

	static {
		try {
			Properties pro = new Properties();
			pro.load(NetUtils.class
					.getResourceAsStream("/assets/config.properties"));
			NAME_SPACE = pro.getProperty("name_space");
			END_POINT = pro.getProperty("end_point");
		} catch (Exception e) {
			Log.e("douban", "error", e);
			e.printStackTrace();
		}
	}

	public static String callWs(String wsName, String methodName,
			Map<String, String> params) {
		String json = null;
		String endPoint = END_POINT + wsName;
		// 创建httpTransportSE传输对象
		HttpTransportSE ht = new HttpTransportSE(endPoint);
		ht.debug = true;
		// 使用soap1.1协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// 实例化SoapObject对象
		SoapObject request = new SoapObject(NAME_SPACE, methodName);

		// 调用参数
		Set<String> keys = params.keySet();
		for (String key : keys) {
			request.addProperty(key, params.get(key));
		}

		// 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		envelope.bodyOut = request;
		try {
			// 调用webService
			ht.call(null, envelope);
			if (envelope.getResponse() != null) {
				SoapObject result = (SoapObject) envelope.bodyIn;
				json = result.getProperty(0).toString();
			} else {

			}
		} catch (Exception e) {
			Log.e("tour", "error", e);
			e.printStackTrace();
		}
		return json;
	}

}
