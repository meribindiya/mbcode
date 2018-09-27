package com.deecoders.meribindiya.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Bookings;
import com.deecoders.meribindiya.activity.EditProfile;
import com.deecoders.meribindiya.activity.Home;
import com.deecoders.meribindiya.activity.MyWalletActivity;
import com.deecoders.meribindiya.activity.Otp;
import com.deecoders.meribindiya.activity.Refer;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.listeners.ServiceDataListener;
import com.deecoders.meribindiya.model.UserModel;
import com.deecoders.meribindiya.util.CircularNetworkImageView;
import com.deecoders.meribindiya.util.MyPref;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainMenu extends Fragment {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.homePanel)
    LinearLayout homePanel;
    @BindView(R.id.bookingPanel)
    LinearLayout bookingPanel;
    @BindView(R.id.profilePanel)
    LinearLayout profilePanel;
    @BindView(R.id.walletPanel)
    LinearLayout walletPanel;
    @BindView(R.id.referPanel)
    LinearLayout referPanel;
    @BindView(R.id.privacyPanel)
    LinearLayout privacyPanel;
    @BindView(R.id.callUsPanel)
    LinearLayout callUsPanel;
    @BindView(R.id.ratePanel)
    LinearLayout ratePanel;
    @BindView(R.id.logoutPanel)
    LinearLayout logoutPanel;
    Unbinder unbinder;

    private ServiceDataListener<UserModel> serviceDataListener;


    public static MainMenu newInstance(String type) {
        MainMenu fragment = new MainMenu();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        getUserData();

        return view;
    }

    public void setServiceDataListener (ServiceDataListener<UserModel> serviceDataListener) {
        this.serviceDataListener = serviceDataListener;
    }

    private void getUserData() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.getProfileDetails
                + MyPref.getId(getActivity()), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "getUserData " + response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject jsonObject = response.getJSONObject("object");
                                Type objectType = new TypeToken<UserModel>() {
                                }.getType();
                                UserModel userModel = new GsonBuilder().create().fromJson(jsonObject.toString(), objectType);
                                serviceDataListener.onData(userModel);
                                if(null != userModel) {
                                    MyPref.setProfile(getActivity(), userModel);
                                    if(userModel.isActivated()) {
                                        referPanel.setVisibility(View.VISIBLE);
                                    } else {
                                        referPanel.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(getActivity()).add(req);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(MyPref.getGender(getActivity()).equals("male")){
            image.setImageResource(R.drawable.male);
        }
        else{
            image.setImageResource(R.drawable.female);
        }
        name.setText(MyPref.getName(getActivity()));
        phone.setText(MyPref.getMobile(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.homePanel, R.id.bookingPanel, R.id.profilePanel, R.id.walletPanel, R.id.referPanel, R.id.privacyPanel, R.id.callUsPanel, R.id.ratePanel, R.id.logoutPanel})
    public void onViewClicked(View view) {
        Constants.clickEffect(view);
        switch (view.getId()) {
            case R.id.homePanel:
                ((Home)getActivity()).drawerToggle();
                break;
            case R.id.bookingPanel:
                Intent intent1 = new Intent(getActivity(), Bookings.class);
                startActivity(intent1);
                break;
            case R.id.profilePanel:
                Intent intent2 = new Intent(getActivity(), EditProfile.class);
                startActivity(intent2);
                break;
            case R.id.walletPanel:
                Intent intent3 = new Intent(getActivity(), MyWalletActivity.class);
                startActivity(intent3);
                break;
            case R.id.referPanel:
                Intent intent4 = new Intent(getActivity(), Refer.class);
                startActivity(intent4);
                break;
            case R.id.privacyPanel:
                break;
            case R.id.callUsPanel:
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+Constants.callPhone));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(call);
                }
                break;
            case R.id.ratePanel:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.playstoreUrl));
                startActivity(i);
                break;
            case R.id.logoutPanel:
                showLogoutConfirmation();
                break;
        }
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Logout");
        alertBuilder.setMessage("Are you sure you want to Logout from application?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyPref.clearAllPrefs(getActivity());
                getActivity().finishAffinity();
                Intent intent = new Intent(getActivity(), Otp.class);
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.create().show();
    }
}
