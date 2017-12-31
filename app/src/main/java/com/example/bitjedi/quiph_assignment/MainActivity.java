package com.example.bitjedi.quiph_assignment;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClickStartAccessibiltyService(View view){
        try{
            //start accessibilty service....
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
