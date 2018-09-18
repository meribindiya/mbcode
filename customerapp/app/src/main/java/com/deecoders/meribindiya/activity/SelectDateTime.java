package com.deecoders.meribindiya.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.DateAdapter;
import com.deecoders.meribindiya.adapter.TimeSlotAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.DateModel;
import com.deecoders.meribindiya.model.TimeSlotModel;
import com.deecoders.meribindiya.view.ExpandableHeightGridView;
import com.devspark.appmsg.AppMsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectDateTime extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.selectAddress)
    Button selectAddress;
    @BindView(R.id.dateTitle)
    TextView dateTitle;
    @BindView(R.id.calendarImg)
    ImageView calendarImg;
    @BindView(R.id.gridView)
    ExpandableHeightGridView gridView;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.proceed)
    Button proceed;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    int addressId;
    TimeSlotAdapter timeSlotAdapter;
    ArrayList<TimeSlotModel> timeSlotModels = new ArrayList<>();
    @BindView(R.id.dateSelected)
    TextView dateSelected;
    @BindView(R.id.selectDate)
    LinearLayout selectDate;
    @BindView(R.id.panelList)
    GridView panelList;
    DateAdapter dateAdapter;
    ArrayList<DateModel> dateModels = new ArrayList<>();
    @BindView(R.id.noTimeSlots)
    TextView noTimeSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_time);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(calendarImg.getDrawable(), ContextCompat.getColor(this, R.color.welcome));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        setDatePanels(calendar);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                showDatePicker();
            }
        });
    }

    private void setDatePanels(Calendar calendar) {
        dateModels.clear();
        dateTitle.setText(new SimpleDateFormat("MMMM yyyy").format(System.currentTimeMillis()));

        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                true));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                false));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                false));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                false));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                false));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                false));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateModels.add(new DateModel(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("dd").format(calendar.getTimeInMillis()),
                new SimpleDateFormat("EEE").format(calendar.getTimeInMillis()),
                false));

        dateAdapter = new DateAdapter(this, dateModels);
        panelList.setAdapter(dateAdapter);
        setTimeSlots();
    }

    public void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDatePanels(calendar);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    public void setTimeSlots() {
        timeSlotModels.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if (min > 10) {
            hour = hour + 2;
        } else {
            hour = hour + 1;
        }

        if (hour < 9) {
            hour = 9;
        }

        String selectedDate = dateAdapter.selectedModel.getCompleteDate();
        try {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            if (!selectedDate.equals(currentDate)) {
                hour = 9;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("tag", "hour: " + hour);

        for (int i = hour; i < 18; i++) {
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, i);
            String left = new SimpleDateFormat("hh a").format(calendar.getTimeInMillis());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            String right = new SimpleDateFormat("hh a").format(calendar.getTimeInMillis());
            timeSlotModels.add(new TimeSlotModel(left + " to " + right));
        }

        timeSlotAdapter = new TimeSlotAdapter(this, timeSlotModels);
        gridView.setAdapter(timeSlotAdapter);
        gridView.setExpanded(true);
        if(timeSlotModels.size()==0){
            noTimeSlots.setVisibility(View.VISIBLE);
        }
        else{
            noTimeSlots.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.back, R.id.selectAddress, R.id.proceed})
    public void onViewClicked(View view) {
        Constants.clickEffect(view);
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.selectAddress:
                addAddress();
                break;
            case R.id.proceed:
                if (address.getText().toString().isEmpty()) {
                    AppMsg.makeText(this, "No Address Selected!", AppMsg.STYLE_ALERT).show();
                    return;
                }
                if (dateAdapter.selectedModel == null) {
                    AppMsg.makeText(this, "No Date Selected!", AppMsg.STYLE_ALERT).show();
                    return;
                }
                if (timeSlotAdapter.getSelectedModel() == null) {
                    AppMsg.makeText(this, "No Time Slot Selected!", AppMsg.STYLE_ALERT).show();
                    return;
                }

                Log.e("tag", "date: " + dateAdapter.selectedModel.getCompleteDate());
                Log.e("tag", "timeSlot: " + timeSlotAdapter.getSelectedModel().getTime());

                Intent intent = new Intent(this, PaymentOption.class);
                intent.putExtra("order_date", dateAdapter.selectedModel.getCompleteDate());
                intent.putExtra("order_time", timeSlotAdapter.getSelectedModel().getTime());
                intent.putExtra("address_id", addressId);
                startActivity(intent);
                break;
        }
    }

    public void addAddress() {
        Intent intent = new Intent(this, Addresses.class);
        intent.putExtra("from", "SelectDateTime");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                addressId = data.getIntExtra("address_id", 0);
                if (data.getStringExtra("address") != null) {
                    address.setText(data.getStringExtra("address"));
                    selectAddress.setText("Change");
                }
            }
        }
    }

    /*private void getTimeSlots() {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        String url = Constants.time_slots;
        Log.e("tag", "url: " + url);
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("success", response.toString());
                progressBar.setVisibility(View.GONE);
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray arr = response.getJSONArray("object");
                        String[] values = new String[arr.length()];
                        for (int i = 0; i < arr.length(); i++) {
                            values[i] = arr.getString(i);
                        }
                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Cart.this, android.R.layout.simple_spinner_dropdown_item, values);
                        //orderTime.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Cart.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyLibrary.getInstance(this).addToRequestQueue(customRequest, "", false);
    }*/
}
