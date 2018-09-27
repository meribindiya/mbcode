package com.deecoders.meribindiya.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class MySMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            SmsMessage smsMessage;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                smsMessage = msgs[0];
            } else {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
            }
            //String numberMNS = smsMessage.getDisplayOriginatingAddress();
            String message = smsMessage.getMessageBody();
            Log.e("tag", "sms: "+message);
            if(message.contains("OTP")){
                String code = message.substring(0, 4);
                if(TextUtils.isDigitsOnly(code)){
                    Intent intent1 = new Intent("otp_sms");
                    intent1.putExtra("code", ""+code);
                    context.sendBroadcast(intent1);
                }
            }
        }
    }
}
