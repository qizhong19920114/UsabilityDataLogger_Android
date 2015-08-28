package com.tabexample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class Tab4Activity  extends Activity
{
	
	private static final String TAG = "UDL_tab4_Activity";
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            
           // TextView  tv=new TextView(this);
            //tv.setTextSize(25);
            //tv.setGravity(Gravity.TOP);
            //tv.setText("About: ");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            setContentView(R.layout.activity_tab4);
          
        }
        
        @Override
        public void onBackPressed() {
            // your code.
        	Log.d(TAG, "onBackPressed");
        }
}
