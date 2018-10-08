package com.deecoders.meribindiya.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.ContactAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.ContactModel;
import com.deecoders.meribindiya.util.MyPref;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareContact extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.shareContactBtn)
    Button shareContactBtn;

    @BindView(R.id.contactList)
    ListView mContactsList;

    @BindView(R.id.searchEditText)
    EditText searchEditText;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ArrayList<ContactModel> contactList = new ArrayList<>();

    private String SHARE_SMS_BODY  = "Hi Friends,\n" +
            "I called a beautician at my home from MeriBindiya At HOME SALON.\n" +
            "\n" +
            "The pricing was very affordable and the quality of service was excelent.\n" +
            "\n" +
            "I really love the services & recommend all to avail the services at least once.\n" +
            "\n" +
            "Install Meribindiya App and Get Rs 100 in your wallet. \n" +
            "\n" +
            "Download MeriBindiya app now - https://tinyurl.com/appmeribindiya";
    private ContactAdapter contactAdapter;
    private boolean permissionDenied = false;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_contact);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        shareContactBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(null != contactAdapter.getSelectedList() && contactAdapter.getSelectedList().size() > 0) {
                    shareContacts();
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contactAdapter != null)
                    contactAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionDenied && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            showContactPermissionRationaleAlert();
        } else {
            permissionDenied = false;
            loadContacts ();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != mAlertDialog) {
            mAlertDialog.dismiss();
        }
    }

    /*private void askPermission() {
        // Here, thisActivity is the current activity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getContactNames();
            contactAdapter = new ContactAdapter(ShareContact.this, contactList);
            mContactsList.setAdapter(contactAdapter);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                loadContacts();
            }
        }
    }*/

    public void composeSmsMessage(String message, List<ContactModel> contactModels) {

        String contacts = "";
        for(ContactModel model : contactModels) {
            if(contacts.equalsIgnoreCase("")){
                contacts = model.getPhoneNumber();
            } else {
                contacts = contacts + "," + model.getPhoneNumber();
            }
        }

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+contacts));
        smsIntent.putExtra("sms_body", SHARE_SMS_BODY);
        startActivity(smsIntent);
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }


    private void showContactPermissionRationaleAlert () {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Permission Denied");
        alertBuilder.setMessage("Read Contact permission is necessary to refer contacts. Please allow us to read it.");
        alertBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton("EXIT ANYWAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        mAlertDialog = alertBuilder.create();
        alertBuilder.show();
    }

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Android version is lesser than 6.0 or the permission is already granted.
        contactAdapter = new ContactAdapter(ShareContact.this, contactList);
        mContactsList.setAdapter(contactAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permissionDenied = false;
                } else {
                    permissionDenied = true;
                }
                break;
            }
        }


    }

    private void loadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            return;

        } else {
            progressBar.setVisibility(View.VISIBLE);
            LoadContacts loadContacts = new LoadContacts();
            loadContacts.execute();
        }
    }

    /**
     * Read the name of all the contacts.
     * @return a list of names.
     */
    private ArrayList<ContactModel> getContactNames() {

        // Get the ContentResolver
        ContentResolver cr = getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

        String lastNumber = "";
        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (null != cursor && cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) ==1) {
                    if(!lastNumber.equals(phoneNo)) {
                        lastNumber = phoneNo;
                        ContactModel contactModel = new ContactModel(id, name, phoneNo);
                        contactList.add(contactModel);
                    }

                }

            } while (cursor.moveToNext());
        }
        // Close the curosor
        cursor.close();

        return contactList;
    }

    private void shareContacts () {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        JSONArray mobileNumbers = new JSONArray();

        try {
            object.put("userId", MyPref.getId(this));
            Iterator<ContactModel> contactIterator = contactAdapter.getSelectedList().iterator();

            while(contactIterator.hasNext()) {
                ContactModel contact = contactIterator.next();
                mobileNumbers.put(contact.getPhoneNumber());
            }
            object.put("mobileNumbers", mobileNumbers);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("tag", "sharing contact numbers ");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.shareContact, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("share contact info", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                finish();
                                composeSmsMessage("hello", contactAdapter.getSelectedList());
                                Toast.makeText(ShareContact.this, R.string.refer_contact_success_msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShareContact.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShareContact.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private class LoadContacts extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            getContactNames();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            showContacts();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}