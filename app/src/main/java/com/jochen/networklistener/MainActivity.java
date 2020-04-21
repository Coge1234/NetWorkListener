package com.jochen.networklistener;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jochen.networklibrary.NetworkManager;
import com.jochen.networklibrary.annotation.NetworkChange;
import com.jochen.networklibrary.bean.NetworkType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getDefault().register(this);
    }

    @NetworkChange
    public void networkListener(NetworkType networkType) {
        Log.i("===jochen===", "MainActivity接收到的值：" + networkType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getDefault().unRegister(this);
    }
}
