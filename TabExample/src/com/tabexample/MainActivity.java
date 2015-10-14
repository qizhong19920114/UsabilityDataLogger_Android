package com.tabexample;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity 
{
            /** Called when the activity is first created. */
            @Override
            public void onCreate(Bundle savedInstanceState)
            {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);

                    // create the TabHost that will contain the Tabs
                    TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


                    TabSpec tab1 = tabHost.newTabSpec("First Tab");
                    tab1.setIndicator("Record",getResources().getDrawable(R.drawable.blackandroid)); //add tab image.
                    tab1.setContent(new Intent(this,Tab1Activity.class));
                    tabHost.addTab(tab1);
                                       
                    TabSpec tab2 = tabHost.newTabSpec("Second Tab");
                    tab2.setIndicator("Analysis",getResources().getDrawable(R.drawable.blackandroid));
                    tab2.setContent(new Intent(this,Tab2Activity.class));
                    tabHost.addTab(tab2);
                    
                    
                    TabSpec tab3 = tabHost.newTabSpec("Third tab");
                    tab3.setIndicator("Setting",getResources().getDrawable(R.drawable.blackandroid));
                    tab3.setContent(new Intent(this,Tab3Activity.class));
                    tabHost.addTab(tab3);

                    TabSpec tab4 = tabHost.newTabSpec("Fourth tab");
                    tab4.setIndicator("About",getResources().getDrawable(R.drawable.blackandroid));
                    tab4.setContent(new Intent(this,Tab4Activity.class));
                    tabHost.addTab(tab4);

            }
}