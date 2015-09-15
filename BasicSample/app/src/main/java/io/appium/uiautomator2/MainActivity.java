package io.appium.uiautomator2;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;

import io.appium.uiautomator2.util.Log;

public class MainActivity extends Activity {
    private static final int port = 4456;
    private static ServerInstrumentation serverInstrumentation = null;

    // adb logcat -c; adb logcat -s "appium"
    // adb uninstall com.appium.uiautomator2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(io.appium.uiautomator2.R.layout.activity_main);
        printIp();

        if (serverInstrumentation == null) {
            serverInstrumentation = ServerInstrumentation.getInstance(this, port);
            serverInstrumentation.startServer();
        }
    }

    private void printIp() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        final String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        Log.i("Device address is: http://" + ip + ":" + port);
        // also localhost:port works on device
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(io.appium.uiautomator2.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == io.appium.uiautomator2.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
