package com.deecoders.meribindiya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Bookings;
import com.deecoders.meribindiya.adapter.BookingAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.BookingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingFrag extends Fragment {
    @BindView(R.id.noresult)
    TextView noresult;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Unbinder unbinder;
    String type;
    BookingAdapter bookingAdapter;
    ArrayList<BookingModel> ongoingModels = new ArrayList<>();
    ArrayList<BookingModel> historyModels = new ArrayList<>();

    public static BookingFrag newInstance() {
        BookingFrag fragment = new BookingFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type = getArguments().getString("type");

        for (BookingModel model : Bookings.models) {
            if (model.getOrderstatus().toLowerCase().equals(Constants.newOrder)) {
                String date = model.getBookingdate();
                String time = model.getBooking_time();
                try {
                    String [] arr = time.split(" to ");
                    if(arr.length==2) {
                        Date bDate = new SimpleDateFormat("dd-MM-yyyy hh a").parse(date + " " + arr[1]);
                        if (bDate.getTime() < System.currentTimeMillis()) {
                            historyModels.add(model);
                            continue;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ongoingModels.add(model);
            }
            else{
                historyModels.add(model);
            }
        }

        if(type.equals("ongoing")){
            bookingAdapter=new BookingAdapter(getActivity(), ongoingModels);
            listView.setAdapter(bookingAdapter);
            if(ongoingModels.size()==0){
                noresult.setVisibility(View.VISIBLE);
            }
            else{
                noresult.setVisibility(View.GONE);
            }
        }
        else{
            bookingAdapter=new BookingAdapter(getActivity(), historyModels);
            listView.setAdapter(bookingAdapter);
            if(historyModels.size()==0){
                noresult.setVisibility(View.VISIBLE);
            }
            else{
                noresult.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
