package com.example.lining.fdclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lining on 2018/3/16.
 */

public class Login extends Activity {
    private String ipname;
    private Button btn_start;
    private Button btn_exit;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn_start = findViewById(R.id.btn_start);
        btn_exit = findViewById(R.id.btn_exit);
        editText = findViewById(R.id.edit_ip);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipname = editText.getText().toString().trim();
                if (ipname.equals("")) {
                    Toast.makeText(Login.this, "请输入服务端IP！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isIP(ipname)) {
                    Toast.makeText(Login.this, "请输入正确的IP地址！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle data = new Bundle();
                data.putString("ipname", ipname);
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });
    }

    public boolean isIP(String addr)
    {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
            return false;
        //判断IP格式和范围
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }
}
