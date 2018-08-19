package com.meribindiyaemployee.meribindiyaemployee.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meribindiyaemployee.meribindiyaemployee.R;
import com.meribindiyaemployee.meribindiyaemployee.adaptors.JobServiceAdaptor;
import com.meribindiyaemployee.meribindiyaemployee.responses.CommonResponse;
import com.meribindiyaemployee.meribindiyaemployee.responses.CustomerOrder;
import com.meribindiyaemployee.meribindiyaemployee.services.ServiceBuilder;
import com.meribindiyaemployee.meribindiyaemployee.services.Services;
import com.meribindiyaemployee.meribindiyaemployee.utils.Constants;
import com.meribindiyaemployee.meribindiyaemployee.utils.GPSTracker;
import com.meribindiyaemployee.meribindiyaemployee.utils.PreferenceHandler;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailsActivity extends AppCompatActivity {

    private static final Logger LOGGER = Logger.getLogger("JobDetailActivity.class");

    private static final String FAILED = "failed";

    private static final String SUCCESS = "success";

    @BindView(R.id.customername)
    TextView customeName;
    @BindView(R.id.mobile)
    ImageView customerMobile;
    @BindView(R.id.address)
    TextView customerAddress;
    @BindView(R.id.total)
    TextView totalAmount;

    @BindView((R.id.job_detail_layout))
    LinearLayout jobDetailLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.order_status_change)
    Button actionButton;

    @BindView(R.id.view_address)
    TextView view_address_on_map;


    private Long ordeId;
    private Integer orderStatus;
    private String address;

    String sourceLatitude, sourceLongitude;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Long orderid = getIntent().getLongExtra("orderid", 0);
        this.ordeId = orderid;
        LOGGER.info("Order id :::" + orderid);
        fetchData(orderid);
        actionButton.setEnabled(false);

        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {
            LOGGER.info(gpsTracker.latitude + " " + gpsTracker.latitude);
            sourceLatitude = String.valueOf(gpsTracker.latitude);
            sourceLongitude = String.valueOf(gpsTracker.longitude);
        }


    }

    public void fetchData(Long orderid) {
        Services services = ServiceBuilder.buildService(Services.class);
        Call<CommonResponse> call = services.getOrderDetailByOrderId(orderid);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if (FAILED.equalsIgnoreCase(commonResponse.getStatus())) {
                    Toast.makeText(JobDetailsActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String jsonContentResponse = gson.toJson(commonResponse.getObject());
                    final CustomerOrder customerOrder = gson.fromJson(jsonContentResponse, CustomerOrder.class);

                    JobDetailsActivity.this.ordeId = customerOrder.getId();
                    actionButton.setEnabled(true);

                    if (customerOrder.getOrder_status_code() == 1 || customerOrder.getOrder_status_code() == 2) {
                        JobDetailsActivity.this.orderStatus = 2;
                        actionButton.setText("Start Job");
                        actionButton.setEnabled(true);

                    } else if (customerOrder.getOrder_status_code() == 4) {
                        JobDetailsActivity.this.orderStatus = 3;
                        actionButton.setText("Stop Job");
                        actionButton.setEnabled(true);

                    } else {
                        actionButton.setText(customerOrder.getOrderstatus());
                        actionButton.setBackgroundColor(Color.GRAY);
                        actionButton.setEnabled(false);
                    }
                    JobDetailsActivity.this.address = customerOrder.getAddress().getAddress() + "," + customerOrder.getAddress().getBuildingName() + "," + customerOrder.getAddress().getLandmark();
                    customerAddress.setText(JobDetailsActivity.this.address);
                    customeName.setText(customerOrder.getUserName().toUpperCase());

                    customerMobile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + customerOrder.getMobile()));
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(callIntent);
                                } else {
                                    ActivityCompat.requestPermissions(JobDetailsActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            0);

                                }
                            } else {
                                Toast.makeText(JobDetailsActivity.this, "Kindly update you android version to use this feature.", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    totalAmount.setText(String.valueOf(customerOrder.getTotal()));
                    RecyclerView recyclerView = findViewById(R.id.service_item_detail);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobDetailsActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    JobServiceAdaptor jobServiceAdaptor = new JobServiceAdaptor(JobDetailsActivity.this, customerOrder.getCustomerOrderServices());
                    recyclerView.setAdapter(jobServiceAdaptor);
                    jobServiceAdaptor.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    jobDetailLayout.setVisibility(View.VISIBLE);
                    actionButton.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @OnClick(R.id.order_status_change)
    public void changeOrderStatus() {
        progressBar.setVisibility(View.VISIBLE);
        Services services = ServiceBuilder.buildService(Services.class);
        services.changeOrderStatus(this.ordeId, this.orderStatus).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if ("success".equalsIgnoreCase(commonResponse.getStatus())) {
                    fetchData(ordeId);
                } else if ("failed".equalsIgnoreCase(commonResponse.getStatus())) {
                    Toast.makeText(JobDetailsActivity.this, commonResponse.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(JobDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.view_address})
    public void openMap() {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            LOGGER.info(gpsTracker.latitude + " " + gpsTracker.latitude);
            sourceLatitude = String.valueOf(gpsTracker.latitude);
            sourceLongitude = String.valueOf(gpsTracker.longitude);
        }
        mapNavigate(JobDetailsActivity.this.address);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

    }

    public void mapNavigate(String address) {
        try {

            if (TextUtils.isEmpty(sourceLatitude) || sourceLatitude == null || TextUtils.isEmpty(sourceLongitude) || sourceLongitude == null) {
                Toast.makeText(getApplicationContext(), "Please press again to find view location in map.", Toast.LENGTH_LONG).show();
                return;
            }
            double lat = Double.parseDouble(sourceLatitude);
            double lon = Double.parseDouble(sourceLongitude);

            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());

            try {
                double latitude = 0;
                double longitude = 0;
                List<Address> listAddresses = geocoder.getFromLocationName(address, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    // Here we are finding , whatever we want our marker to show when
                    latitude = listAddresses.get(0).getLatitude();
                    longitude = listAddresses.get(0).getLongitude();
                }
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + lat + "," + lon + "&daddr=" + latitude + "," + longitude));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
