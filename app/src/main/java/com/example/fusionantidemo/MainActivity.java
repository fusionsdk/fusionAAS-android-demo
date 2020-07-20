package com.example.fusionantidemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fusion.fusion_anti_addiction_sdk.FusionAntiSdk;
import com.fusion.fusion_anti_addiction_sdk.callback.IRealNameCallback;
import com.fusion.fusion_anti_addiction_sdk.core.FusionAntiMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG= "FUSION";

    private Button btnGetState;
    private Button btnGetPayState;
    private Button btnSendPay;
    private Button btnLogout;
    private EditText etUid;
    private EditText etItemId;
    private EditText etMoney;

    private String uid;
    private String money;
    private String itemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件

        btnGetState = findViewById(R.id.btnGetState);
        btnGetState.setOnClickListener(this);
        btnGetPayState = findViewById(R.id.btnGetPayState);
        btnGetPayState.setOnClickListener(this);
        btnSendPay = findViewById(R.id.btnSendPay);
        btnSendPay.setOnClickListener(this);
        btnLogout = findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(this);

        etUid = findViewById(R.id.etUid);
        etItemId = findViewById(R.id.etItemId);
        etMoney = findViewById(R.id.etMoney);

        //初始化sdk
        //初始化（不特殊配置）
//        FusionAntiSdk.getInstance().initSdk("1001",this);
        FusionAntiSdk.getInstance().initSdk("1001",this, FusionAntiMode.LanchType.MODE_DEFAULT, FusionAntiMode.ScreenType.SCREEN_MINI);
        //设置监听
        FusionAntiSdk.getInstance().setRealNameListener(new IRealNameCallback() {
            @Override
            public void AllowLogin() {
                Log.e(TAG,"AllowLogin call");
                Toast.makeText(MainActivity.this,"允许登录",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void ForbidLogin(String msg) {
                Log.e(TAG,"ForbidLogin call = "+msg);
                Toast.makeText(MainActivity.this,"不允许登录",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OffLine(String msg) {
                Log.e(TAG,"OffLine call = "+msg);
                Toast.makeText(MainActivity.this,"已经踢线",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void AllowPay() {
                Log.e(TAG,"AllowPay call");
                Toast.makeText(MainActivity.this,"允许支付",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void ForbidPay(String msg) {
                Log.e(TAG,"ForbidPay call = "+msg);
                Toast.makeText(MainActivity.this,"不允许支付",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void BindSuccess() {
                Log.e(TAG,"BindSuccess call");
                Toast.makeText(MainActivity.this,"实名制绑定成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void BindFail(String msg) {
                Log.e(TAG,"BindFail call = "+msg);
                Toast.makeText(MainActivity.this,"实名制绑定失败",Toast.LENGTH_SHORT).show();
            }
        });
        requestPermission();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FusionAntiSdk.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnGetState){
            uid = etUid.getText().toString();
            if (uid.length() < 1 || uid == null){
                Toast.makeText(this,"uid不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            FusionAntiSdk.getInstance().getRealNameState(uid);
        }else if(view.getId() == R.id.btnGetPayState){
            money = etMoney.getText().toString();
            if (uid.length() < 1 || uid == null){
                Toast.makeText(this,"uid不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if (money.length() < 1 || money == null){
                Toast.makeText(this,"money不能为空",Toast.LENGTH_SHORT).show();
                return;

            }
            FusionAntiSdk.getInstance().checkMoneyLimit(uid,Float.valueOf(money));
        }else if (view.getId() == R.id.btnSendPay){
            itemId = etItemId.getText().toString();
            if (uid.length() < 1 || uid == null){
                Toast.makeText(this,"uid不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if (money.length() < 1 || money == null){
                Toast.makeText(this,"money不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if (itemId.length() < 1 || itemId == null){
                Toast.makeText(this,"itemId不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            FusionAntiSdk.getInstance().sendCharge(uid,itemId,Float.valueOf(money));
        }else if (view.getId() == R.id.btnlogout){
            FusionAntiSdk.getInstance().logout();
        }
    }

    private void requestPermission() {

        Log.i(TAG,"requestPermission");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"checkSelfPermission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    101);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"checkSelfPermission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    102);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"checkSelfPermission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    103);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
