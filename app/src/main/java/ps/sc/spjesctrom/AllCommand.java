package ps.sc.spjesctrom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ps.sc.spjesctrom.FormatHttpPostOkHttp.FromHttpPostOkHttp;


public class AllCommand {

	public static String SHARE_NAME = "SAVE_STRING";
	public static String Name_WIFI_1 = "Name_WIFI_1";
	public static String Pass_WIFI_1 = "Pass_WIFI_1";
	public static String Name_WIFI_2 = "Name_WIFI_2";
	public static String Pass_WIFI_2 = "Pass_WIFI_2";
	public static String Name_WIFI_3 = "Name_WIFI_3";
	public static String Pass_WIFI_3 = "Pass_WIFI_3";
	public static String Name_WIFI_4 = "Name_WIFI_4";
	public static String Pass_WIFI_4 = "Pass_WIFI_4";
	public static String Name_WIFI_5 = "Name_WIFI_5";
	public static String Pass_WIFI_5 = "Pass_WIFI_5";

	public static String SMS_MESSAGE = "SMS_MESSAGE";

	public boolean isConnectingToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
	public String GET_OK_HTTP_SendData(String url) {
		ShowLogCat("GET_OK_HTTP_SendData",url);
		try{
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		}catch (Exception e){
			ShowLogCat("Err","GET_OK_HTTP_SendData " + e.getMessage());
			return "";
		}
	}
	public String POST_OK_HTTP_SendData(String url, ArrayList<FromHttpPostOkHttp> params) {
		ShowLogCat("POST_OK_HTTP_SendData",url);
		try{
			OkHttpClient client = new OkHttpClient();
			MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
			for (int i = 0;i<params.size();i++){
				Log.e("Check Data",params.get(i).getKEY_POST().toString()+ " : " +params.get(i).getVALUS_POST().toString());
				multipartBuilder.addFormDataPart(params.get(i).getKEY_POST().toString(),params.get(i).getVALUS_POST().toString());
			}
			RequestBody requestBody = multipartBuilder.build();
			Request request = new Request.Builder()
					.url(url)
					.post(requestBody)
					.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()){
				return response.body().string().toString();
			}
		}catch (Exception e){
			Log.e("*** Err ***","Err POST_OK_HTTP_SendData " + e.getMessage());
			return "";
		}
		return "";
	}


	@TargetApi(Build.VERSION_CODES.KITKAT)
	public String setEncodeBase64(final String input) {
		try {
			byte[] message = input.getBytes(StandardCharsets.UTF_8);
			return  Base64.encodeToString(message, Base64.DEFAULT);
		}catch (Exception e){
			ShowLogCat("Err","setEncodeBase64 " + e.getMessage());
		}
		return "";
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public String getEncodeBase64(final String encodeBase64) {
		try {
			byte[] decoded = Base64.decode(encodeBase64, Base64.DEFAULT);
			return new String(decoded, StandardCharsets.UTF_8);
		}catch (Exception e){
			ShowLogCat("Err","getEncodeBase64 " + e.getMessage());

		}
		return "";
	}
	public String GetStringShare(Context _context, String strKey, String strDe) {
		SharedPreferences shLang;
		shLang = _context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
		if (shLang != null) {
			String strShare = shLang.getString(strKey, strDe);
			return strShare;
		}
		return "";
	}
	public void SaveStringShare(Context _context, String strKey, String strDe){
		SharedPreferences shLang;
		SharedPreferences.Editor edShLang;
		shLang = _context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
		edShLang = shLang.edit();
		edShLang.remove(strKey);
		edShLang.commit();
		edShLang.putString(strKey, strDe);
		edShLang.commit();
	}

	public void ShowLogCat(String tag, String msg){
		if (BuildConfig.DEBUG){
			Log.e("***AllCommand***",tag + " : " + msg);
		}
	}




}
