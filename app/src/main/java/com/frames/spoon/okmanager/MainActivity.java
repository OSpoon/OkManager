package com.frames.spoon.okmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.spoon.okmanager.OkManager;
import com.spoon.okmanager.callback.StringCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkManager.initClient(okHttpClient);
        String address = "http://guolin.tech/api/china";
        OkManager.get()
                .url(address)
                .build()//  Log.e("TAG",response.toString());
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Log.e("TAG", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.e("TAG", response.toString());
                    }
                });
    }
}
