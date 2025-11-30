package com.example.demo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerSms;
    ArrayList<SmsModel> smsList = new ArrayList<>();
    SmsAdapter smsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerSms = findViewById(R.id.recyclerSms);
        recyclerSms.setLayoutManager(new LinearLayoutManager(this));

        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, 100);
        } else {
            loadSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perm, @NonNull int[] grant) {
        if (requestCode == 100 && grant.length > 0 && grant[0] == PackageManager.PERMISSION_GRANTED) {
            loadSMS();
        } else {
            Toast.makeText(this, "Permission Required!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSMS() {
        smsList.clear();

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null, null, "date DESC");

        long todayStart = getTodayStartMillis();

        if (c != null) {
            while (c.moveToNext()) {

                String number = c.getString(c.getColumnIndexOrThrow("address"));
                String msg = c.getString(c.getColumnIndexOrThrow("body"));
                long timeMillis = c.getLong(c.getColumnIndexOrThrow("date"));

                String formattedTime;

                if (timeMillis >= todayStart) {
                    formattedTime = new SimpleDateFormat("HH:mm", Locale.getDefault())
                            .format(new Date(timeMillis));
                } else {
                    formattedTime = new SimpleDateFormat("dd MMM", Locale.getDefault())
                            .format(new Date(timeMillis));
                }

                smsList.add(new SmsModel(number, msg, formattedTime));
            }
            c.close();
        }

        smsAdapter = new SmsAdapter(this, smsList);
        recyclerSms.setAdapter(smsAdapter);
    }

    private long getTodayStartMillis() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Date date = sdf.parse(sdf.format(new Date()));
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }
}
