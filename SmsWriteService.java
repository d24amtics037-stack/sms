package com.example.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SmsWriteService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
