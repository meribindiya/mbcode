package com.deecoders.meribindiya.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.Checksum;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.util.MyPref;
import com.deecoders.meribindiya.util.TinyDB;
import com.devspark.appmsg.AppMsg;
import com.google.gson.GsonBuilder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentOption extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.totalPrice)
    TextView totalPrice;
    @BindView(R.id.cash)
    RadioButton cash;
    @BindView(R.id.paytm)
    RadioButton paytm;
    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.proceed)
    Button proceed;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    ArrayList<ProductModel> models = new ArrayList<>();
    Checksum checksum;
    JSONObject checksumObject;
    String orderId, orderDate, orderTime;
    int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                if(group.getCheckedRadioButtonId() == -1){
                    AppMsg.makeText(PaymentOption.this, "Please select Payment Type!", AppMsg.STYLE_ALERT).show();
                    return;
                }
                if(group.getCheckedRadioButtonId()==R.id.cash){
                    sendCashOrderRequest();
                }
                else if(group.getCheckedRadioButtonId()==R.id.paytm){
                    getCheckSum();
                }
            }
        });

        orderDate=getIntent().getStringExtra("order_date");
        orderTime=getIntent().getStringExtra("order_time");
        addressId=getIntent().getIntExtra("address_id",0);

        TinyDB tinyDB = new TinyDB(this);
        models = tinyDB.getListObject("products", ProductModel.class);
        clearDirtyModels();

        int priceCount = 0;
        for (ProductModel model : models) {
            priceCount = priceCount + (model.getCount() * model.getPrice());
        }
        totalPrice.setText(priceCount+" INR");
    }

    private void clearDirtyModels() {
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getCount() == 0) {
                models.remove(i);
            }
        }
    }

    public void viewDetail(View view) {
        Constants.clickEffect(view);
        Intent intent = new Intent(this, OrderDetails.class);
        startActivity(intent);
    }

    private void getCheckSum() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("userId", MyPref.getId(this));
            object.put("email", MyPref.getEmail(this));
            int priceCount = 0;
            for (ProductModel model : models) {
                priceCount = priceCount + (model.getCount() * model.getPrice());
            }
            object.put("finalAmount", priceCount);
            object.put("mobile", Long.parseLong(MyPref.getMobile(this)));
            object.put("orderId", generateString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: " + object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.generateChecksum, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("generate", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                checksumObject = response.getJSONObject("object");
                                checksum = new GsonBuilder().create().fromJson(checksumObject.toString(), Checksum.class);
                                validateCheckSum();
                            } else {
                                Toast.makeText(PaymentOption.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(PaymentOption.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void validateCheckSum() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("userId", MyPref.getId(this));
            object.put("email", MyPref.getEmail(this));
            int priceCount = 0;
            for (ProductModel model : models) {
                priceCount = priceCount + (model.getCount() * model.getPrice());
            }
            object.put("finalAmount", priceCount);
            object.put("mobile", MyPref.getMobile(this));
            object.put("orderId", generateString());
            object.put("paramMap", checksumObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: " + object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.validateChecksum, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("validate", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                boolean value = response.getJSONObject("object").getBoolean("value");
                                if (value) {
                                    onStartTransaction(null);
                                } else {
                                    Toast.makeText(PaymentOption.this, "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PaymentOption.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(PaymentOption.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("CALLBACK_URL", "" + checksum.getCALLBACK_URL());
        paramMap.put("CHANNEL_ID", "" + checksum.getCHANNEL_ID());
        paramMap.put("CHECKSUMHASH", "" + checksum.getCHECKSUMHASH());
        paramMap.put("CUST_ID", "" + checksum.getCUST_ID());
        paramMap.put("INDUSTRY_TYPE_ID", "" + checksum.getINDUSTRY_TYPE_ID());
        paramMap.put("MID", "" + checksum.getMID());
        paramMap.put("ORDER_ID", "" + checksum.getORDER_ID());
        paramMap.put("TXN_AMOUNT", "" + checksum.getTXN_AMOUNT());
        paramMap.put("WEBSITE", "" + checksum.getWEBSITE());
        paramMap.put("EMAIL", "" + checksum.getEMAIL());
        paramMap.put("MOBILE_NO", "" + checksum.getMOBILE_NO());

        // testing
        /*paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=ORDS86991657");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("CHECKSUMHASH", "GlFsxtyLE9hr4Z/vhklhAyReRyYgY+OdfKppb1z4Pl3ePk0VGDWrOH5GLiZ5QC1wblkvXyFLo6DJap2nXFgFraLTBZWhnNNV4u6RC0xVHUM=");
        paramMap.put("CUST_ID", "CUST001");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("MID", "Meribi16353178366179");
        paramMap.put("ORDER_ID", "ORDS86991657");
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", "APPSTAGING");
        paramMap.put("EMAIL", ""+checksum.getEMAIL());
        paramMap.put("MOBILE_NO", ""+checksum.getMOBILE_NO());*/

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.e("tag", key + " : " + value);
        }

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Log.e("tag", "error: " + inErrorMessage);
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
                        progressBar.setVisibility(View.GONE);
                        Log.e("LOG", "Payment Transaction is successful " + inResponse);
                        String txnId = inResponse.getString("TXNID");
                        String txnDate = inResponse.getString("TXNDATE");
                        sendPaytmOrderRequest(txnId, txnDate);
                    }

                    @Override
                    public void networkNotAvailable() {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "networkNotAvailable");
                        Toast.makeText(PaymentOption.this, "Network problem!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "clientAuthenticationFailed: " + inErrorMessage);
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "onErrorLoadingWebPage: " + inErrorMessage);
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PaymentOption.this, "Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed!", Toast.LENGTH_LONG).show();
                    }

                });
    }

    private void sendPaytmOrderRequest(String txnId, String txnDate) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", Long.parseLong(MyPref.getMobile(this)));
            object.put("userid", Long.parseLong(MyPref.getId(this)));
            object.put("addressid", addressId);
            object.put("booking_date", orderDate);
            object.put("booking_time", orderTime);
            object.put("category_id", models.get(0).getCatid());
            object.put("online_source_id", checksum.getORDER_ID());
            object.put("online_source_txndate", txnDate);
            object.put("online_source_txnid", txnId);
            object.put("payment_source_id", "");
            object.put("payment_source_txndate", "");
            object.put("payment_source_txnid", "");
            object.put("pymnt_mode", 1);
            object.put("pymnt_source", "paytm");
            object.put("utf1", "");
            object.put("utf2", "");
            object.put("utf3", "");

            JSONArray allServices = new JSONArray();
            for (ProductModel model : models) {
                JSONObject service = new JSONObject();
                service.put("quantity", model.getCount());
                service.put("serviceid", model.getId());
                allServices.put(service);
            }
            object.put("orderServiceRequests", allServices);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: " + object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.createOrder, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                showAlert(PaymentOption.this);
                            } else {
                                Toast.makeText(PaymentOption.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PaymentOption.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private int generateString() {
        int orderId = 0;
        Random r = new Random();
        orderId = 100000000 + r.nextInt(100000000) + r.nextInt(100000000);
        return orderId;
    }

    private void sendCashOrderRequest() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", Long.parseLong(MyPref.getMobile(this)));
            object.put("userid", Long.parseLong(MyPref.getId(this)));
            object.put("addressid", addressId);
            object.put("booking_date", orderDate);
            object.put("booking_time", orderTime);
            object.put("category_id", models.get(0).getCatid());
            object.put("online_source_id", "");
            object.put("online_source_txndate", "");
            object.put("online_source_txnid", "");
            object.put("payment_source_id", "");
            object.put("payment_source_txndate", "");
            object.put("payment_source_txnid", "");
            object.put("pymnt_mode", 0);
            object.put("pymnt_source", "cod");
            //object.put("pymnt_mode", 1);
            //object.put("pymnt_source", "paytm");
            object.put("utf1", "");
            object.put("utf2", "");
            object.put("utf3", "");

            JSONArray allServices = new JSONArray();
            for (ProductModel model : models) {
                JSONObject service = new JSONObject();
                service.put("quantity", model.getCount());
                service.put("serviceid", model.getId());
                allServices.put(service);
            }
            object.put("orderServiceRequests", allServices);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("tag", "sending: " + object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.createOrder, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                showAlert(PaymentOption.this);
                            } else {
                                Toast.makeText(PaymentOption.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PaymentOption.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void showAlert(Context context) {
        finishAffinity();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        Intent cong = new Intent(this, Congrats.class);
        startActivity(cong);
    }

}
