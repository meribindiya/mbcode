package com.deecoders.meribindiya.constants;

import android.os.Handler;
import android.view.View;

public class Constants {
    public static final String newOrder = "new order";
    public static final String complete = "complete";
    public static final String cancel = "cancel";

    public static final String callPhone = "+918130520472";
    public static final String playstoreUrl = "https://play.google.com/store/apps/details?id=com.deecoders.meribindiya";

    public static final String M_ID = "Meribi16353178366179"; //Paytm Merchand Id we got it in paytm credentials
    public static final String CHANNEL_ID = "WEB"; //Paytm Channel Id, got it in paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential
    public static final String WEBSITE = "APP_STAGING";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    public static final String baseUrl = "http://live.meribindiya.com";
    //public static final String baseUrl = "http://127.0.0.1:8081";
    public static final String loginUrl = baseUrl+"/user/"; //+ phone number
    public static final String userUpdate = baseUrl+"/user/update";
    public static final String userRegister = baseUrl+"/user/register";
    public static final String userOtpVerification = baseUrl+"/user/otp/verification";
    public static final String userFcm = baseUrl+"/user/fcm";
    public static final String deleteAddress = baseUrl+"/user/address/delete/";//+ address id
    public static final String allAddresses = baseUrl+"/user/address/all/"; //+ userid
    public static final String addAddress = baseUrl+"/user/address/add";
    public static final String orderDetails = baseUrl+"/order/details/"; //+ orderid
    public static final String createOrder = baseUrl+"/order/create-order";
    public static final String allOrders = baseUrl+"/order/all/";//+ userid
    public static final String getSubcategories = baseUrl+"/sub-categories/";//+ categoryid
    public static final String getSubcategory = baseUrl+"/services/";//+ catid/subcatid
    public static final String getCategories = baseUrl+"/categories";
    public static final String banners = baseUrl+"/banners";
    public static final String generateChecksum = baseUrl+"/paytm/checksum/generate";
    public static final String validateChecksum = baseUrl+"/paytm/checksum/validate";
    public static final String time_slots = baseUrl+"/slots?date=";
    public static final String getWallet = baseUrl+"/user/wallet/";
    public static final String shareContact = baseUrl+"/refer/save/";
    public static final String getNetworkDetails = baseUrl+"/network/detail/";
    public static final String getActiveMembers = baseUrl+"/network/activeMembers/";

    public static final String getSharedContacts = baseUrl+"/refer/get";

    public static final String getProfileDetails = baseUrl+"/user/profile/";
    public static final String aliveUser = baseUrl+"/user/imAlive/";
    //refer/delete
    public static final String deleteReferContact = baseUrl+"/refer/delete/";

    public static final String getEarningDetails = baseUrl+"/earning/get/";



    public static void clickEffect(final View view){
        view.setAlpha(0.5f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setAlpha(1f);
            }
        }, 100);
    }

}
