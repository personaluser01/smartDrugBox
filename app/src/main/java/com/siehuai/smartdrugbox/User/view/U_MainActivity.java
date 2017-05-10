package com.siehuai.smartdrugbox.User.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.siehuai.smartdrugbox.Generic.controller.PostsDatabaseHelper;
import com.siehuai.smartdrugbox.Generic.controller.RemoteDatabaseHelper.RemoteDbFactory;
import com.siehuai.smartdrugbox.Generic.data.NetworkAddress;
import com.siehuai.smartdrugbox.R;
import com.siehuai.smartdrugbox.User.controller.RemoteDatabaseHelper.MedicineBoxDetailsRemoteHelper;
import com.siehuai.smartdrugbox.User.controller.RemoteDatabaseHelper.MedicineDetailsRemoteHelper;
import com.siehuai.smartdrugbox.User.data.AlarmData;
import com.siehuai.smartdrugbox.User.view.MedicineBox.MedicineBoxActivity;
import com.siehuai.smartdrugbox.User.view.UserViewMedicine.UserViewMedicineActivity;
import com.siehuai.smartdrugbox.databinding.ActivityUserMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class U_MainActivity extends AppCompatActivity {

    ActivityUserMainBinding mBinding;
    MedicineDetailsRemoteHelper mMedicineDbHelper;
    MedicineBoxDetailsRemoteHelper mMedicineBoxDetailsRemoteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);

        mMedicineDbHelper = new MedicineDetailsRemoteHelper();

        PostsDatabaseHelper postsDbHelper = PostsDatabaseHelper.getInstance(this);
        
        getAllMedicineBoxData();

        initAlarmData(postsDbHelper);

        setReminderBtn();

        setOrderMedicineBtn();

        activateAlarm();

    }

    public void setReminderBtn() {

        mBinding.btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder();
            }
        });
    }

    public void setOrderMedicineBtn() {
        mBinding.btnOrderMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderMedicine();
            }
        });
    }

    public void setReminder() {
        Intent intent = new Intent(U_MainActivity.this, UserSetReminderActivity.class);
        startActivity(intent);
    }

    public void orderMedicine() {
        Intent intent = new Intent(U_MainActivity.this, UserViewMedicineTabActivity.class);
        startActivity(intent);
    }

    public void activateAlarm() {

        mBinding.btnTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(U_MainActivity.this);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkAddress.ESP8266_BUZ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("U_MainActivity", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Hello world", "/BUZ");
                        return params;
                    }
                };
                // Add the request to the RequestQueue.

                queue.add(stringRequest);

            }
        });
    }

    public void initAlarmData(PostsDatabaseHelper helper) {

        ArrayList<AlarmData> alarmArrayList = helper.getAllAlarmFrmDb();

        helper.updateAlarmInLocal(alarmArrayList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return !itemClickHandler(item) && super.onOptionsItemSelected(item);
    }

    public boolean itemClickHandler(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.option_setMedicine):
                userSetMedicine();
                return true;
            case (R.id.option_viewMedicine):
                userViewMedicine();
                return true;
            default:
                return false;
        }
    }

    public void userSetMedicine() {
        Intent intent = new Intent(U_MainActivity.this, MedicineBoxActivity.class);
        startActivity(intent);
    }

    public void userViewMedicine() {
        Intent intent = new Intent(U_MainActivity.this, UserViewMedicineActivity.class);
        startActivity(intent);
    }

    private void getAllMedicineBoxData() {
        mMedicineBoxDetailsRemoteHelper = (MedicineBoxDetailsRemoteHelper) RemoteDbFactory.createRemoteDbHelper(RemoteDbFactory.RemoteDataType.MedicineBoxDetails);
        mMedicineBoxDetailsRemoteHelper.read();
    }

}