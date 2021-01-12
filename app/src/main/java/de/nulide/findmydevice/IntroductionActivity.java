package de.nulide.findmydevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.nulide.findmydevice.data.Settings;
import de.nulide.findmydevice.data.WhiteList;
import de.nulide.findmydevice.data.io.IO;
import de.nulide.findmydevice.utils.Permission;

public class IntroductionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewInfoText;
    private Button buttonNext;
    private Button buttonGivePermission;

    private int position = 0;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        IO.context = this;
        settings = IO.read(Settings.class, IO.settingsFileName);

        textViewInfoText = findViewById(R.id.textViewInfoText);
        buttonGivePermission = findViewById(R.id.buttonGivePermission);
        buttonGivePermission.setOnClickListener(this);
        buttonGivePermission.setVisibility(View.INVISIBLE);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        updateViews();

    }

    public void updateViews(){
        switch(position){
            case 0:
                textViewInfoText.setText(getString(R.string.Introduction));
                break;
            case 1:
                buttonGivePermission.setVisibility(View.VISIBLE);
                textViewInfoText.setText(getString(R.string.Permission_SMS));
                if(Permission.checkSMSPermission(this)){
                    buttonNext.setEnabled(true);
                    buttonGivePermission.setTextColor(Color.GREEN);
                }else{
                    buttonNext.setEnabled(false);
                    buttonGivePermission.setTextColor(Color.RED);
                }
                break;
            case 2:
                textViewInfoText.setText(getString(R.string.Permission_CONTACTS));
                if(Permission.checkContactsPermission(this)){
                    buttonNext.setEnabled(true);
                    buttonGivePermission.setTextColor(Color.GREEN);
                }else{
                    buttonNext.setEnabled(false);
                    buttonGivePermission.setTextColor(Color.RED);
                }
                break;
            case 3:
                textViewInfoText.setText(getString(R.string.Permission_GPS));
                if(Permission.checkGPSPermission(this)){
                    buttonGivePermission.setTextColor(Color.GREEN);
                }else{
                    buttonGivePermission.setTextColor(Color.RED);
                }
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textViewInfoText.setText(getString(R.string.Permission_DND));
                    if(Permission.checkDNDPermission(this)){
                        buttonGivePermission.setTextColor(Color.GREEN);
                    }else{
                        buttonGivePermission.setTextColor(Color.RED);
                    }
                }else{
                    position++;
                    updateViews();
                }
                break;
            case 5:
                textViewInfoText.setText(getString(R.string.Permission_DEVICE_ADMIN));
                if(Permission.checkDeviceAdminPermission(this)){
                    buttonGivePermission.setTextColor(Color.GREEN);
                }else{
                    buttonGivePermission.setTextColor(Color.RED);
                }
                break;
            case 6:
                settings.setIntroductionPassed(true);
                Intent myIntent = new Intent(this, MainActivity.class);
                finish();
                startActivity(myIntent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonGivePermission){
            switch(position){
                case 0:

                    break;
                case 1:
                    Permission.requestSMSPermission(this);
                    break;
                case 2:
                    Permission.requestContactPermission(this);
                    break;
                case 3:
                    Permission.requestGPSPermission(this);
                    break;
                case 4:
                    Permission.requestDNDPermission(this);
                    break;
                case 5:
                    Permission.requestDeviceAdminPermission(this);
                    break;
            }
        }else{
            position++;
            updateViews();
        }

    }
}