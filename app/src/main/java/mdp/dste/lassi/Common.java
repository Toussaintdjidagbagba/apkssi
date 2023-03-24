package mdp.dste.lassi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;

public class Common {
    private boolean isWifiEnable = false;
    private boolean isMobileNetworkAvailable = false;

    public boolean isConnectedToInternet(Context context) {
        return  isNetWorkAvailableNow(context);
    }

    public boolean isWifiEnable() {
        return isWifiEnable;
    }

    public void setIsWifiEnable(boolean newisWifiEnable) {
        isWifiEnable = newisWifiEnable;
    }

    public boolean isMobileNetworkAvailable() {
        return isMobileNetworkAvailable;
    }

    public void setIsMobileNetworkAvailable(boolean newisMobileNetworkAvailable) {
        isMobileNetworkAvailable = newisMobileNetworkAvailable;
    }

    public boolean checkisConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean isOnline() {
        /*Just to check Time delay*/
        long t = Calendar.getInstance().getTimeInMillis();

        Runtime runtime = Runtime.getRuntime();
        try {
            /*Pinging to Google server*/
            //Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 185.98.131.154");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long t2 = Calendar.getInstance().getTimeInMillis();
            Log.i("NetWork check Time", (t2 - t) + "");
        }
        return false;
    }

    public boolean isNetWorkAvailableNow(Context context) {
        boolean isNetworkAvailable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        setIsWifiEnable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
        setIsMobileNetworkAvailable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());

        if (isWifiEnable() || isMobileNetworkAvailable()) {
            /*Sometime wifi is connected but service provider never connected to internet
            so cross check one more time*/
            if (isOnline())
                isNetworkAvailable = true;
        }

        return isNetworkAvailable;
    }
}
