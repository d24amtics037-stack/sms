package com.example.demo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SmsReader {
    public static ArrayList<SmsModel> getAllSms(Context context) {
        ArrayList<SmsModel> smsList = new ArrayList<>();

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {

                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                long timeMillis = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String time = sdf.format(new Date(timeMillis));

                smsList.add(new SmsModel(address, body, time));
            }
            cursor.close();
        }
        return smsList;
    }
}
