package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Button;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    Button btnDefaultSms, btnInbox, btnCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        btnDefaultSms = findViewById(R.id.btnDefaultSms);
        btnInbox = findViewById(R.id.btnInbox);
        btnCompose = findViewById(R.id.btnCompose);

        btnDefaultSms.setOnClickListener(v -> requestDefaultSmsApp());

        btnInbox.setOnClickListener(v -> {
            startActivity(new Intent(this, InboxActivity.class));
        });

        btnCompose.setOnClickListener(v -> {
            startActivity(new Intent(this, ComposeActivity.class));
        });
    }

    private void requestDefaultSmsApp() {
        String myPackage = getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
            if (!roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                startActivityForResult(intent, 100);
                return;
            }
        } else {
            String defaultSms = Telephony.Sms.getDefaultSmsPackage(this);
            if (!defaultSms.equals(myPackage)) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackage);
                startActivityForResult(intent, 100);
                return;
            }
        }
        Toast.makeText(this, "Already Default SMS App", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            Toast.makeText(this,"App is now Default SMS App",Toast.LENGTH_LONG).show();
        }
    }
}
