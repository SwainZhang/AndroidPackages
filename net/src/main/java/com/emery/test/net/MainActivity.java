package com.emery.test.net;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<CityInfo> mCityInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.citys);
        registe();
    }

     public void show(View view){
         mCityInfos = XmlParserUtil.domParseXml(this, "city_xml.xml");
         mListView.setAdapter(new MyAdapter());

         ConnectivityManager cm = (ConnectivityManager) getSystemService(Context
                 .CONNECTIVITY_SERVICE);
         NetworkInfo[] allNetworkInfo = cm.getAllNetworkInfo();
         for (NetworkInfo networkInfo : allNetworkInfo) {
             System.out.println(networkInfo);
         }
     }
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return  mCityInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView=new TextView(MainActivity.this);
            CityInfo cityInfo = mCityInfos.get(i);
            System.out.println("--"+cityInfo.toString());
            // textView.setText(cityInfo.getName());
             textView.setText(getResources().getString(R.string.city,cityInfo.getId(),cityInfo.getName(),cityInfo.getCode()));
            return textView;
        }
    }

    public void registe(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new ConnectionChangeReceiver(),filter);
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
          NetworkUtil.connectionChanged(context);
        }
    }
}
