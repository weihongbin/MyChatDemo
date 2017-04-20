package com.example.administrator.mychatdemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main5Activity extends AppCompatActivity {

    EditText msg_et = null;
    Button send_bt = null;
    TextView info_tv = null;
    Button btn;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public android.os.Handler Handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        msg_et = (EditText) findViewById(R.id.msg_et);
        send_bt = (Button) findViewById(R.id.send_bt);
        info_tv = (TextView) findViewById(R.id.info_tv);

        // 开启服务器
        ExecutorService exec = Executors.newCachedThreadPool();
        UDPServer server = new UDPServer();
        exec.execute(server);
        // 发送消息
        Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                info_tv.append(text);
            }
        };
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          new Thread(new Server()).start();
                UDPClient client = new UDPClient(msg_et.getText().toString());
                new Thread(client).start();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main5 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.administrator.mychatdemo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
//    public class Server implements Runnable {
//        @Override
//        public void run() {
//
//            try{
//
//
//
//            }catch(Exception ex){
//                ex.printStackTrace();
//            }

//            try {
//                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
//                updatetrack("\nServer: Start connecting\n");
//                DatagramSocket socket = new DatagramSocket(SERVERPORT,
//                        serverAddr);
//                byte[] buf = new byte[17];
//                DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                updatetrack("Server: Receiving\n");
//                socket.receive(packet);
//                updatetrack("Server: Message received: ‘"
//                        + new String(packet.getData()) + "’\n");
//                updatetrack("Server: Succeed!\n");
//            } catch (Exception e) {
//                updatetrack("Server: Error!\n");
//            }
//        }
//    }

    public void updatetrack(String s) {
        Message msg = new Message();
        String textTochange = s;
        msg.obj = textTochange;
        Handler.sendMessage(msg);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main5 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.administrator.mychatdemo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
