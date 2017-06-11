package com.siehuai.smartdrugbox.Generic.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.siehuai.smartdrugbox.Pharmacy.controller.SubscribeToEventHelper;
import com.siehuai.smartdrugbox.Pharmacy.view.P_MainActivity;
import com.siehuai.smartdrugbox.R;
import com.siehuai.smartdrugbox.User.view.U_MainActivity;
import com.siehuai.smartdrugbox.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubscribeToEventHelper.unSubscribeTopic("medicineOrder");
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setUserBtnOnClick();
        setPharmacyBtnOnClick();
    }

    private void setUserBtnOnClick() {
        mBinding.btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, U_MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setPharmacyBtnOnClick() {
        mBinding.btnLoginPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P_MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
