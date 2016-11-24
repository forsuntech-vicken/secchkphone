package com.forsuntech.secchkphone;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.forsuntech.secchkphone.svrmsg.OrgAppList;

public class MainActivity extends Activity {
    public static Handler handler = null;

    private Button btnGetAPPInfo=null;

    public static Activity mainAcitvity=null;
    OrgAppList info=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAcitvity=this;
        btnGetAPPInfo=(Button)findViewById(R.id.button1);
        info=new OrgAppList();
        // startService(new Intent(this, androidService.class));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                showToast(msg.what, msg);

                //
                // }
            }
        };


        btnGetAPPInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                info.getAppList();
            }
        });
    }

    private void showToast(int msg, Message b) {
        if (msg == 0) {
            Bundle bun = b.getData();
            String str = bun.get("key").toString();
            Toast.makeText(this, "指令：" + str, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "指令：" + msg, Toast.LENGTH_SHORT).show();
        }
    }





}
