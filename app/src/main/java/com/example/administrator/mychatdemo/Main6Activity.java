package com.example.administrator.mychatdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main6Activity extends AppCompatActivity {
  private Button button4;
    private   Button button5;
  private WebView webView;
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        button4= (Button) findViewById(R.id.button4);
        button5=(Button)findViewById(R.id.button5);
//        webView=(WebView)findViewById(R.id.webView);
        button5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                singleThreadExecutor.shutdown();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new FileServer()).start();

                singleThreadExecutor.submit(new FileClient());



//                webView.getSettings().setDomStorageEnabled(true);
//              webView.loadUrl("http://220.195.3.37:8080/sgrz/index.html");
//                webView.setWebViewClient(new WebViewClient());

                //使用简单的loadData()方法总会导致乱码，有可能是Android API的Bug
                //webView.loadData(data, "text/html", "GBK");

//                AlertDialog.Builder builder = new AlertDialog.Builder(Main6Activity.this, android.R.style.Theme_Light);
//                builder.setIcon(android.R.drawable.stat_sys_warning);
//                builder.setTitle("您确定要退出？");
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
//                    }
//                });
//                builder.setPositiveButton("  确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
//                });
//                builder.create().show();
            }
        });

    }

}
