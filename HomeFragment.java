package com.example.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    RecyclerView recyclerSms;
    ArrayList<SmsModel> smsList = new ArrayList<>();
    SmsAdapter smsAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main, container, false);

        recyclerSms = v.findViewById(R.id.recyclerSms);
        recyclerSms.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        checkPermissionAndLoad();

        return v;
    }

    // ----------------- CHECK PERMISSION ---------------------
    private void checkPermissionAndLoad() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_SMS}, 200);

        } else {
            loadSMS();
        }
    }

    // ----------------- PERMISSION RESULT ---------------------
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            loadSMS();

        } else {
            Toast.makeText(getContext(), "SMS Permission Required", Toast.LENGTH_SHORT).show();
        }
    }

    // ----------------- LOAD SMS INSIDE FRAGMENT ---------------------
    private void loadSMS() {

        smsList.clear();

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = requireContext().getContentResolver().query(uri, null, null, null, "date DESC");

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

        smsAdapter = new SmsAdapter(getContext(), smsList);
        recyclerSms.setAdapter(smsAdapter);
    }

    private long getTodayStartMillis() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date date = sdf.parse(sdf.format(new Date()));
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }
}
