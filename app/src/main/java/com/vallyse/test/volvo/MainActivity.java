package com.vallyse.test.volvo;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser == null){
                    startActivity(new Intent(MainActivity.this, Login.class));
                }else{
                    startActivity(new Intent(MainActivity.this, home.class));
                }
            }
        },SPLASH_TIME_OUT);


    }


    @Override
    protected void onStart() {
        super.onStart();
    }





}
