package com.example.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for(SmsMessage sms: messages){
                String sender = sms.getOriginatingAddress();
                String body = sms.getMessageBody();
                Log.d("SMSAPP","From:"+sender+" Msg:"+body);
            }
        }
    }
}
