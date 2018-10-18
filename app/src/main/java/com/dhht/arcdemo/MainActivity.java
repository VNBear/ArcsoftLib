package com.dhht.arcdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

import static com.dhht.arcdemo.FaceActivity.KEY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

            }
        });

        findViewById(R.id.btRegisterFace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(KEY, 0);
                startActivity(FaceActivity.class, bundle);
            }
        });

        findViewById(R.id.btComparison).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(KEY, 1);
                startActivity(FaceActivity.class, bundle);
            }
        });

        findViewById(R.id.btSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(KEY, 2);
                startActivity(FaceActivity.class, bundle);
            }
        });
    }

    private void startActivity(Class c, Bundle data) {
        Intent intent = new Intent(this, c);
        intent.putExtras(data);
        startActivity(intent);
    }
}
