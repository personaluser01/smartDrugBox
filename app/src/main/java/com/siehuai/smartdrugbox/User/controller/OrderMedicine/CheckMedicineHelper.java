package com.siehuai.smartdrugbox.User.controller.OrderMedicine;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siehuai.smartdrugbox.Generic.controller.HTTPHelper.IResponseReturnListener;
import com.siehuai.smartdrugbox.Generic.data.FirebaseCloudFunctions.HttpFunctionURL;
import com.siehuai.smartdrugbox.User.data.MedicineDetails;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CheckMedicineHelper {

    RequestQueue mRequestQueue;
    ObjectMapper objectMapper = new ObjectMapper();

    public CheckMedicineHelper(RequestQueue requestQueue) {
        mRequestQueue = requestQueue;

    }

    public void sendRequestToViewPharmacyMenu(final String medicineName, final IResponseReturnListener listener) {

        String url = HttpFunctionURL.GET_PHARMACY_WITH_MEDICINE;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<LinkedHashMap> linkedHashMapList = objectMapper.readValue(response, ArrayList.class);
                            ArrayList<MedicineDetails> medicineDetailsList = new ArrayList<>();
                            for (LinkedHashMap hashMap : linkedHashMapList) {
                                medicineDetailsList.add(convertLinkedHashMapToMedicineDetails(hashMap));
                            }
                            listener.onResponseComplete(medicineDetailsList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String requestBody = "{\"medicineName\":\"" + medicineName + "\"}";
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        mRequestQueue.add(stringRequest);
    }

    private MedicineDetails convertLinkedHashMapToMedicineDetails(LinkedHashMap<String, Object> linkedHashMap) {
        return objectMapper.convertValue(linkedHashMap, new TypeReference<MedicineDetails>() {});
    }

}
