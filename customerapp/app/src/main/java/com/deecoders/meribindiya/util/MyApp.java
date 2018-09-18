package com.deecoders.meribindiya.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.deecoders.meribindiya.R;
import com.squareup.otto.Bus;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.lang.reflect.Method;

/**
 * Created by saiful.nadeem on 3/21/2016.
 */

@ReportsCrashes(mailTo = "saif052m@gmail.com", customReportContent = {
        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class MyApp extends Application {
    private static MyBus bus = new MyBus();
    private static Application application;

    public static Bus getBus(){
        return bus;
    }

    public static Context getAppContext(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ACRA.init(this);
        getBus().register(this);
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
