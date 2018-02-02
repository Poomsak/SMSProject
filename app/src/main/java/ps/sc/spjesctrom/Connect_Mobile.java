package ps.sc.spjesctrom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ps.sc.spjesctrom.FormatHttpPostOkHttp.BasicNameValusPostOkHttp;
import ps.sc.spjesctrom.FormatHttpPostOkHttp.FromHttpPostOkHttp;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Lenovo on 29-01-2018.
 */

public class Connect_Mobile {
    private Context mContext;
    private WifiManager wifimanager;
    private TimerTask timerTask;
    private Timer timer;
    private WifiConfiguration wifiConfig;
    private boolean OnImternect;

    private int netId;

    public Connect_Mobile(Context mContext) {
        this.mContext = mContext;
    }

    public int checkWifi_or_Mobile(){
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);

        //For 3G check
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        //For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();

        //Log.e("MainActivity ",is3g + " net " + isWifi);

        if (!is3g && !isWifi)
        {
            //Close
            Log.e("MainActivity", "0");
            return 0;
        }
        else
        {
            //Open
            Log.e("MainActivity", "1");
            return 1;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void onOpenWifi(){
        wifimanager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        final AllCommand allCommand = new AllCommand();
        timer = new Timer();
        if (wifimanager.isWifiEnabled()) { // Wi-Fi adapter is ON



        }
        else {// Wi-Fi adapter is OFF

            Log.e("MainActivity", "Close WIFI");
            EnableWiFi();

            ConnectNect(allCommand.GetStringShare(mContext,allCommand.Name_WIFI_1,""),
                    allCommand.GetStringShare(mContext,allCommand.Pass_WIFI_1,""),
                    false,true,false,false,false);

        }
    }

    private void ConnectNect(String Name, String Pass, final boolean wifi_1_m, final boolean wifi_2_m,
                             final boolean wifi_3_m, final boolean wifi_4_m, final boolean wifi_5_m){

        final AllCommand allCommand = new AllCommand();
        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", Name);
        wifiConfig.preSharedKey = String.format("\"%s\"", Pass);

        netId = wifimanager.addNetwork(wifiConfig);
        wifimanager.disconnect();
        wifimanager.enableNetwork(netId, true);
        wifimanager.reconnect();

        new Thread(new Runnable() {
            @SuppressLint("StaticFieldLeak")
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) { }

                if (wifimanager.isWifiEnabled()) { // Wi-Fi adapter is ON

                    WifiInfo wifiInfo = wifimanager.getConnectionInfo();

                    if( wifiInfo.getNetworkId() == -1 ){
                        Log.e("MainActivity", "no Connect wifi_1");

                        if (wifi_2_m){
                            ConnectNect(allCommand.GetStringShare(mContext,allCommand.Name_WIFI_2,""),
                                    allCommand.GetStringShare(mContext,allCommand.Pass_WIFI_2,""),
                                    false,false,true,false,false);
                            Log.e("MainActivity", "no Connect wifi_2");
                        }else if (wifi_3_m){

                            ConnectNect(allCommand.GetStringShare(mContext,allCommand.Name_WIFI_1,""),
                                    allCommand.GetStringShare(mContext,allCommand.Pass_WIFI_1,""),
                                    false,false,false,true,false);
                            Log.e("MainActivity", "no Connect wifi_3");

                        }else if (wifi_4_m){

                            ConnectNect(allCommand.GetStringShare(mContext,allCommand.Name_WIFI_1,""),
                                    allCommand.GetStringShare(mContext,allCommand.Pass_WIFI_1,""),
                                    false,false,false,false,true);
                            Log.e("MainActivity", "no Connect wifi_4");

                        }else if (wifi_5_m){

                            ConnectNect(allCommand.GetStringShare(mContext,allCommand.Name_WIFI_1,""),
                                    allCommand.GetStringShare(mContext,allCommand.Pass_WIFI_1,""),
                                    false,false,false,false,false);
                            Log.e("MainActivity", "no Connect wifi_5");
                        }else {

                            Log.e("MainActivity", "no Connect wifi");
                        }
                    }else {
                        Log.e("MainActivity", "Connect successful wifi");

                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... strings) {
                                ArrayList<FromHttpPostOkHttp> params = new ArrayList<FromHttpPostOkHttp>();
                                params.add(new BasicNameValusPostOkHttp().BasicNameValusPostOkHttp("FName", "Poomsak"));
                                params.add(new BasicNameValusPostOkHttp().BasicNameValusPostOkHttp("LName",
                                        allCommand.GetStringShare(mContext,allCommand.SMS_MESSAGE,"")));
                                return allCommand.POST_OK_HTTP_SendData("http://192.168.1.45/TestSMS/testSMS.php",params);
                            }
                        }.execute();
                    }
                }
                else {
                    Log.e("MainActivity", "Wi-Fi adapter is OFF");
                }

            }
        }).start();

    }

    private void ConnectNect(String Name,String Pass){

        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", Name);
        wifiConfig.preSharedKey = String.format("\"%s\"", Pass);

        int netId = wifimanager.addNetwork(wifiConfig);
        wifimanager.disconnect();
        wifimanager.enableNetwork(netId, true);
        wifimanager.reconnect();


        CountDownTimer cdt = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Tick
                checkWifiOnAndConnected();
            }

            public void onFinish() {
                // Finish
            }
        }.start();

        /*timerTask = new TimerTask() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void run() {
                checkWifiOnAndConnected();
            }
        };
        timer.schedule(timerTask,0,1000);*/

    }
    private void stopTimeTask(){
        if (timerTask !=null){
            timerTask.cancel();
            timerTask = null;
        }
    }
    private boolean checkWifiOnAndConnected() {
        if (wifimanager.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifimanager.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                Log.e("MainActivity", "no Connect");
                return false; // Not connected to an access point
            }else {
                stopTimeTask();
                Log.e("MainActivity", "Connect successful");
                return true; // Connected to an access point
            }
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }
    public void EnableWiFi(){

        wifimanager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);

    }

    public void DisableWiFi(){

        wifimanager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifimanager.setWifiEnabled(false);

    }

}
