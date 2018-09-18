package com.deecoders.meribindiya;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.activity.Home;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.util.MyPref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.GsonBuilder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MerchantActivity extends AppCompatActivity {
    Checksum checksum;
    JSONObject checksumObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantapp);

        getCheckSum();
    }

    private void getCheckSum() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", MyPref.getId(this));
            object.put("email", MyPref.getEmail(this));
            object.put("finalAmount", 50);
            object.put("mobile", Integer.parseInt(MyPref.getMobile(this)));
            object.put("orderId", generateString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: "+object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.generateChecksum, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("generate", ""+response.toString());
                        try {
                            String status = response.getString("status");
                            if(status.equals("success")) {
                                checksumObject = response.getJSONObject("object");
                                checksum =  new GsonBuilder().create().fromJson(checksumObject.toString(), Checksum.class);
                                validateCheckSum();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(MerchantActivity.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void validateCheckSum() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", MyPref.getId(this));
            object.put("email", MyPref.getEmail(this));
            object.put("finalAmount", 50);
            object.put("mobile", MyPref.getMobile(this));
            object.put("orderId", generateString());
            object.put("paramMap", checksumObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: "+object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.validateChecksum, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("validate", ""+response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if(status.equals("success")) {
                                boolean value = response.getJSONObject("object").getBoolean("value");
                                if(value){
                                    onStartTransaction(null);
                                }
                                else{
                                    Toast.makeText(MerchantActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MerchantActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(MerchantActivity.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("CALLBACK_URL", ""+checksum.getCALLBACK_URL());
        paramMap.put("CHANNEL_ID", ""+checksum.getCHANNEL_ID());
        paramMap.put("CHECKSUMHASH", ""+checksum.getCHECKSUMHASH());
        paramMap.put("CUST_ID", ""+checksum.getCUST_ID());
        paramMap.put("INDUSTRY_TYPE_ID", ""+checksum.getINDUSTRY_TYPE_ID());
        paramMap.put("MID", ""+checksum.getMID());
        paramMap.put("ORDER_ID", ""+checksum.getORDER_ID());
        paramMap.put("TXN_AMOUNT", ""+checksum.getTXN_AMOUNT());
        paramMap.put("WEBSITE", ""+checksum.getWEBSITE());

        for (Map.Entry<String,String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.e("tag", key+" : "+value);
        }

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Log.e("tag", "error: "+inErrorMessage);
                    }

					/*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.e("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.e("tag", "networkNotAvailable");
                        Toast.makeText(MerchantActivity.this, "Network problem!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Log.e("tag", "clientAuthenticationFailed: "+inErrorMessage);
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        Log.e("tag", "onErrorLoadingWebPage: "+inErrorMessage);
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(MerchantActivity.this,"Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.e("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed!", Toast.LENGTH_LONG).show();
                    }

                });
    }

    private int generateString() {
        int orderId = 0;
        Random r=new Random();
        orderId = 100000000 + r.nextInt(100000000) + r.nextInt(100000000);
        return orderId;
    }
}
