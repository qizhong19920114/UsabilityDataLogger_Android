package com.tabexample;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;


public class StartBrowserService extends Service {


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
    	StartBrowserService getService() {
            return StartBrowserService.this;
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
       
        //Launch browser...
    	Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
        startActivity(launchIntent);
    	return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}