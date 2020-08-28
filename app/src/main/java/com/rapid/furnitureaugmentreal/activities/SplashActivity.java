package com.rapid.furnitureaugmentreal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.rapid.furnitureaugmentreal.R;
import com.rapid.furnitureaugmentreal.appConstants.AppConstants;
import com.rapid.furnitureaugmentreal.preferencehelper.PreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    Thread thread=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        thread=new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    thread.sleep(3000);

                    if(new PreferenceHelper(SplashActivity.this).getData(AppConstants.Userid).equalsIgnoreCase("")) {

                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    else {

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }


                }catch (Exception e)
                {

                }

            }
        });


        if(ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {

           // surfaceHolder.addCallback(SplashActivity.this);

            thread.start();
        }
        else {

            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.CAMERA},11);
        }




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {

            // surfaceHolder.addCallback(SplashActivity.this);

            thread.start();
        }
        else {

            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.CAMERA},11);
        }
    }
}
