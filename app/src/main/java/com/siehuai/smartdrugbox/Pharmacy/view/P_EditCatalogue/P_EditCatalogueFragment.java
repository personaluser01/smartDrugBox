package com.siehuai.smartdrugbox.Pharmacy.view.P_EditCatalogue;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.siehuai.smartdrugbox.Generic.common.Utils;
import com.siehuai.smartdrugbox.Generic.controller.RemoteDatabaseHelper.IDbOnDataChangeListener;
import com.siehuai.smartdrugbox.Generic.data.MenuResource.MenuResource;
import com.siehuai.smartdrugbox.Pharmacy.controller.P_MedicineDetailsMenuAdapter;
import com.siehuai.smartdrugbox.Pharmacy.controller.RemoteDatabaseHelper.P_MedicineDetailsRemoteHelper;
import com.siehuai.smartdrugbox.Pharmacy.data.MenuResource.P_MedicineDetailsMenuResource;
import com.siehuai.smartdrugbox.Pharmacy.data.P_MedicineDetails;
import com.siehuai.smartdrugbox.R;

import java.util.ArrayList;
import java.util.Collection;


public class P_EditCatalogueFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    P_MedicineDetailsMenuAdapter adapter;
    ProgressBar mProgressBar;


    public P_EditCatalogueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_p_edit_catalogue, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        hideProgressBar();
        setUpGridView(view);
        return view;
    }

    public void setUpGridView(View mView) {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        setRecycleViewLayoutManager();
        adapter = new P_MedicineDetailsMenuAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setResourceArrayList(setUpMenuResource());
        adapter.addObserver(new P_EditCatalogueClickListener(getActivity()));
    }

    public void setRecycleViewLayoutManager() {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public MenuResource setUpMenuResource() {
        final MenuResource resource = new P_MedicineDetailsMenuResource();
        ArrayList<P_MedicineDetails> list = new ArrayList<>();
        resource.setResourceList(list);
        showProgressBar();
        final P_MedicineDetailsRemoteHelper remoteHelper = P_MedicineDetailsRemoteHelper.getInstance();
        remoteHelper.findAll(new IDbOnDataChangeListener() {
            @Override
            public void onDataChange(Object data) {
                Collection<P_MedicineDetails> changedList = (Collection<P_MedicineDetails>) data;
                resource.setResourceList(Utils.convertCollectionToArrayList(changedList));
                adapter.setResourceArrayList(resource);
                adapter.notifyDataSetChanged();
                hideProgressBar();
            }
        });
        return resource;
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
