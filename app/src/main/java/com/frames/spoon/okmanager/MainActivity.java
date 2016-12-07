package com.frames.spoon.okmanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spoon.okmanager.OkManager;
import com.spoon.okmanager.callback.StringCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private String getUrl = "http://guolin.tech/api/weather";
    private String postUrl = "http://club.dzwww.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LgZ7e&mobile=2&handlekey=loginform&inajax=1";
    private TextView tv_result;
    private Button btn_get;
    private Button btn_post_string;
    private Button btn_post_kv;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post_string = (Button) findViewById(R.id.btn_post_string);
        btn_post_kv = (Button) findViewById(R.id.btn_post_kv);

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        btn_post_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postString();
            }
        });

        btn_post_kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postKV();
            }
        });
    }

    private void postKV() {
        OkManager.post()
                .addParams("username", "zx1234567890")
                .addParams("password", "zx1234567890")
                .url(postUrl)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        tv_result.setText(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tv_result.setText(response);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showProgressDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeProgressDialog();
                    }
                });
    }

    private void postString() {
        OkManager.postString()
                .content("username=zx1234567890&password=zx1234567890")
                .url(getUrl)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        tv_result.setText(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tv_result.setText(response);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showProgressDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeProgressDialog();
                    }
                });
    }

    private void getData() {
        OkManager.get()
                .url(getUrl)
                .addParams("cityid", "CN101010100")
                .addParams("key", "bc0418b57b2d4918819d3974ac1285d9")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        tv_result.setText(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tv_result.setText(response);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showProgressDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        closeProgressDialog();
                    }
                });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
