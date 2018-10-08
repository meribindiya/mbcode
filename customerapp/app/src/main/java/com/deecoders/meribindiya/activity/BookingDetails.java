package com.deecoders.meribindiya.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.BookingDetailModel;
import com.deecoders.meribindiya.model.CustomerOrderServiceModel;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingDetails extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.bookingId)
    TextView bookingId;
    @BindView(R.id.bookedOn)
    TextView bookedOn;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.circle1)
    View circle1;
    @BindView(R.id.circle2)
    View circle2;
    @BindView(R.id.circle3)
    View circle3;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.appointmentOn)
    TextView appointmentOn;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.totalPrice)
    TextView totalPrice;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.payOnline)
    Button payOnline;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String orderId_;
    BookingDetailModel bookingDetailModel;
    @BindView(R.id.expertName)
    TextView expertName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.paymentStatus)
    TextView paymentStatus;

    @BindView(R.id.paidWithWallet)
    TextView paidWithWallet;

    @BindView(R.id.netAmountPaid)
    TextView netAmountPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        orderId_ = getIntent().getStringExtra("order_id");
        scrollView.setVisibility(View.GONE);
        getBookingsDetails();
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    private void getBookingsDetails() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.orderDetails + "/" + orderId_, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        if (progressBar == null)
                            return;
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject object = response.getJSONObject("object");
                                bookingDetailModel = new GsonBuilder().create().fromJson(object.toString(), BookingDetailModel.class);
                                showBookingData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                if (progressBar == null)
                    return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingDetails.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(BookingDetails.this).add(req);
    }

    private void showBookingData() {
        address.setText(bookingDetailModel.getAddress().getAddress());
        bookedOn.setText("Booked On: " + bookingDetailModel.getBookedat());
        appointmentOn.setText(bookingDetailModel.getBookingdate() + ", " + bookingDetailModel.getBooking_time());
        bookingId.setText("Booking ID: " + orderId_);
        if (bookingDetailModel.getOrderstatus().toLowerCase().contains("new order")) {
            status.setText("Order Confirmed");
            status.setTextColor(getResources().getColor(R.color.green1));
            showStep(0);
        } else if (bookingDetailModel.getOrderstatus().toLowerCase().contains("complete")) {
            status.setText("Order Completed");
            status.setTextColor(getResources().getColor(R.color.green1));
            showStep(1);
        } else if (bookingDetailModel.getOrderstatus().toLowerCase().contains("cancel")) {
            status.setText("Order Cancelled");
            status.setTextColor(getResources().getColor(R.color.red));
            showStep(2);
        }
        totalPrice.setText(bookingDetailModel.getTotal() + " INR");
        ArrayList<CustomerOrderServiceModel> models = bookingDetailModel.getCustomerOrderServices();
        for (CustomerOrderServiceModel model : models) {
            View view = LayoutInflater.from(this).inflate(R.layout.service_item, null, false);
            TextView name = view.findViewById(R.id.name);
            TextView quantity = view.findViewById(R.id.quantity);
            TextView singlePrice = view.findViewById(R.id.singlePrice);
            TextView duration = view.findViewById(R.id.duration);
            name.setText(model.getServiceName());
            quantity.setText(model.getQuantity() + "X");
            singlePrice.setText(model.getService_amount() + " INR");
            duration.setText(model.getMinutes() + " (Mins)");
            container.addView(view);
        }

        paidWithWallet.setText(bookingDetailModel.getPaidWithWallet() + " INR");
        netAmountPaid.setText((bookingDetailModel.getTotal() - bookingDetailModel.getPaidWithWallet()) + " INR");
    }

    private void showStep(int step) {
        if (step == 0) {
            line1.setBackgroundColor(getResources().getColor(R.color.gray1));
            line2.setBackgroundColor(getResources().getColor(R.color.gray1));
            circle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
            circle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_circle));
            circle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_circle));
        } else if (step == 1) {
            line1.setBackgroundColor(getResources().getColor(R.color.green1));
            line2.setBackgroundColor(getResources().getColor(R.color.green1));
            circle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
            circle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
            circle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
        } else if (step == 2) {
            line1.setBackgroundColor(getResources().getColor(R.color.gray1));
            line2.setBackgroundColor(getResources().getColor(R.color.gray1));
            circle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_circle));
            circle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_circle));
            circle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_circle));
        } else {
            line1.setBackgroundColor(getResources().getColor(R.color.green1));
            line2.setBackgroundColor(getResources().getColor(R.color.gray1));
            circle1.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
            circle2.setBackgroundDrawable(getResources().getDrawable(R.drawable.tick));
            circle3.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_circle));
        }
    }

    private String getDuration() {
        String duration = "0 min";
        int minutes = 0;
        ArrayList<CustomerOrderServiceModel> models = bookingDetailModel.getCustomerOrderServices();
        for (CustomerOrderServiceModel model : models) {
            minutes = minutes + model.getMinutes();
        }
        duration = getTimeFromSecond(minutes * 60);
        return duration;
    }

    private String getTimeFromSecond(long seconds) {
        if (seconds < 60) {
            return seconds + "sec";
        } else if (seconds < 3600) {
            return "" + (seconds / 60) + "min";
        } else if (seconds < 86400) {
            return "" + (seconds / 3600) + "H";
        } else if (seconds < 604800) {
            return "" + (seconds / 86400) + "D";
        } else if (seconds < 2419200) {
            return "" + (seconds / 604800) + "W";
        } else if (seconds < 29030400) {
            return "" + (seconds / 2419200) + "M";
        } else {
            return "" + (seconds / 29030400) + "Y";
        }
    }

    @OnClick({R.id.back, R.id.payOnline})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.payOnline:
                Intent intent = new Intent(this, PaymentOption.class);
                startActivity(intent);
                break;
        }
    }
}
