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
}
