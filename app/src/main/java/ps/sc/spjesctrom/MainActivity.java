package ps.sc.spjesctrom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ps.sc.spjesctrom.FormatHttpPostOkHttp.BasicNameValusPostOkHttp;
import ps.sc.spjesctrom.FormatHttpPostOkHttp.FromHttpPostOkHttp;


public class MainActivity extends AppCompatActivity {

    private EditText ed_url,ed_name_wifi_1,ed_pass_wifi_1,ed_name_wifi_2,ed_pass_wifi_2,
            ed_name_wifi_3,ed_pass_wifi_3,ed_name_wifi_4,ed_pass_wifi_4,ed_name_wifi_5,ed_pass_wifi_5;

    private TextView rv_button_enter;
    final private int REQUEST_MUTIPLE = 124;
    private Context context;
    private AllCommand allCommand;
    private WifiManager wifimanager;
    private WifiConfiguration wifiConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allCommand = new AllCommand();

        ed_url = findViewById(R.id.ed_url);
        rv_button_enter = findViewById(R.id.rv_button_enter);
        ed_name_wifi_1 = findViewById(R.id.ed_name_wifi_1);
        ed_pass_wifi_1 = findViewById(R.id.ed_pass_wifi_1);
        ed_name_wifi_2 = findViewById(R.id.ed_name_wifi_2);
        ed_pass_wifi_2 = findViewById(R.id.ed_pass_wifi_2);
        ed_name_wifi_3 = findViewById(R.id.ed_name_wifi_3);
        ed_pass_wifi_3 = findViewById(R.id.ed_pass_wifi_3);
        ed_name_wifi_4 = findViewById(R.id.ed_name_wifi_4);
        ed_pass_wifi_4 = findViewById(R.id.ed_pass_wifi_4);
        ed_name_wifi_5 = findViewById(R.id.ed_name_wifi_5);
        ed_pass_wifi_5 = findViewById(R.id.ed_pass_wifi_5);

        ed_name_wifi_1.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Name_WIFI_1,""));
        ed_pass_wifi_1.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Pass_WIFI_1,""));
        ed_name_wifi_2.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Name_WIFI_2,""));
        ed_pass_wifi_2.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Pass_WIFI_2,""));
        ed_name_wifi_3.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Name_WIFI_3,""));
        ed_pass_wifi_3.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Pass_WIFI_3,""));
        ed_name_wifi_4.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Name_WIFI_4,""));
        ed_pass_wifi_4.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Pass_WIFI_4,""));
        ed_name_wifi_5.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Name_WIFI_5,""));
        ed_pass_wifi_5.setText(allCommand.GetStringShare(MainActivity.this,allCommand.Pass_WIFI_5,""));

        wifimanager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);


        rv_button_enter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                allCommand.SaveStringShare(MainActivity.this,allCommand.Name_WIFI_1,ed_name_wifi_1.getText().toString().trim());
                allCommand.SaveStringShare(MainActivity.this,allCommand.Pass_WIFI_1,ed_pass_wifi_1.getText().toString().trim());

                allCommand.SaveStringShare(MainActivity.this,allCommand.Name_WIFI_2,ed_name_wifi_2.getText().toString().trim());
                allCommand.SaveStringShare(MainActivity.this,allCommand.Pass_WIFI_2,ed_pass_wifi_2.getText().toString().trim());

                allCommand.SaveStringShare(MainActivity.this,allCommand.Name_WIFI_3,ed_name_wifi_3.getText().toString().trim());
                allCommand.SaveStringShare(MainActivity.this,allCommand.Pass_WIFI_3,ed_pass_wifi_3.getText().toString().trim());

                allCommand.SaveStringShare(MainActivity.this,allCommand.Name_WIFI_4,ed_name_wifi_4.getText().toString().trim());
                allCommand.SaveStringShare(MainActivity.this,allCommand.Pass_WIFI_4,ed_pass_wifi_4.getText().toString().trim());

                allCommand.SaveStringShare(MainActivity.this,allCommand.Name_WIFI_5,ed_name_wifi_5.getText().toString().trim());
                allCommand.SaveStringShare(MainActivity.this,allCommand.Pass_WIFI_5,ed_pass_wifi_5.getText().toString().trim());


            }
        });


        onPermissionMultiple();
    }

    public void onPermissionMultiple() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add("SMS");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale

                String msg = "";
                for (int i = 0; i < permissionsNeeded.size(); i++){
                    msg += "\n" + permissionsList.get(i);
                }
                String alert1 ="Access is required.";
                String alert2 = "For complete work";
                String message = alert1 +" " + msg + " " +alert2;
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MUTIPLE);

                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MUTIPLE);
            return;
        }
        allowMultipleSuccess();
    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        //ตรวจเช็ค
        if (ActivityCompat.checkSelfPermission(MainActivity.this,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            //ขอ Permission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permission))
                return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MUTIPLE:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                /*perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);*/
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (/*perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        &&*/ perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    allowMultipleSuccess();
                } else {
                    // Permission Denied
                    //checkDataUrlPhoneNumber(1);
                    Log.e("status","Some Permission is Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener closeListener) {
        String ok = "Ok";
        String exitApp = "Exit the app";
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton(ok, okListener)
                .setNegativeButton(exitApp, closeListener)
                .create()
                .show();
    }
    private void allowMultipleSuccess(){
        Log.e("Status","อนุญาตหลายอย่างเสร็จสิ้น");

    }

}
