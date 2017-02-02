/*
 * Copyright 2016 Virtual Rainbow, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtualrainbowllc.demotracker.ui.viewdemo;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.jakewharton.rxbinding.view.RxView;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.virtualrainbowllc.demotracker.AppComponent;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.adapters.TripAdapter;
import com.virtualrainbowllc.demotracker.data.DataModule;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.databinding.ActivityViewVehicleBinding;
import com.virtualrainbowllc.demotracker.tools.Constants;
import com.virtualrainbowllc.demotracker.tools.ItemClickSupport;
import com.virtualrainbowllc.demotracker.tools.Utils;
import com.virtualrainbowllc.demotracker.ui.common.BaseActivity;
import com.virtualrainbowllc.demotracker.ui.dialogs.AddTripDialog;
import com.virtualrainbowllc.demotracker.ui.dialogs.CheckInDemoDialog;
import com.virtualrainbowllc.demotracker.ui.dialogs.UpdateDemoDialog;
import com.virtualrainbowllc.demotracker.ui.dialogs.UpdateTripDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ViewDemoActivity extends BaseActivity implements ViewDemoContract.ViewDemoView,
        ItemClickSupport.OnItemClickListener {

    @Inject
    ViewDemoContract.ViewDemoPresenter presenter;

    @Inject
    SharedPreferences prefs;

    private ActivityViewVehicleBinding binding;
    private TripAdapter adapter;

    private Demo demo;
    private boolean isCheckedIn;
    private String id;
    private boolean deletingVehicle = false;

    @Override
    protected void setupComponent(AppComponent component) {
        DaggerViewDemoComponent.builder()
                .appComponent(component)
                .dataModule(new DataModule())
                .viewDemoPresenterModule(new ViewDemoPresenterModule(this))
                .build().inject(this);
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_vehicle);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                id = bundle.getString(Constants.ID);
            }
        } else {
            id = savedInstanceState.getString(Constants.ID);
        }

        presenter.onViewAttached();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.ID, id);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.adView != null) {
            binding.adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (binding.adView != null) {
            binding.adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (binding.adView != null) {
            binding.adView.destroy();
        }
        presenter.onViewDetached();
        super.onDestroy();
    }

    @Override
    public void closeRealm() {
        presenter.closeRealm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_vehicle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.businessTrip:
                presenter.addTripClicked();
                return true;
            case R.id.edit:
                presenter.editDemoClicked();
                return true;
            case R.id.delete:
                presenter.deleteDemoClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFirstRun() {
        showMaterialTapTarget(this, binding.fab, R.string.tap_target_view_demo_primary,
                R.string.tap_target_view_demo_secondary);
    }

    @Override
    public void editTapTargetFlags() {
        prefs.edit().putBoolean(Constants.FIRST_RUN_VIEW_DEMO, false).apply();
    }

    @Override
    public void setupBaseViews() {
        setupToolbar();
        setupRecyclerView();
        setupFab();
        setupAdView();


        if (prefs.getBoolean(Constants.FIRST_RUN_VIEW_DEMO, true)) {
            presenter.firstRun();
        }

        presenter.loadData(id);
    }

    @Override
    public void setupDemoViews(Demo demo) {
        this.demo = demo;
        isCheckedIn = demo.isCheckedIn();
        if (isCheckedIn) {
            binding.dateIn.setText(Utils.getDateStringFromDate(demo.getDateIn()));
            String mileageIn = demo.getMileageIn();
            binding.mileageIn.setText(String.format(getResources()
                    .getQuantityString(R.plurals.miles_suffix,
                            Integer.parseInt(mileageIn)), mileageIn));
        }

        List<String> nameParts = new ArrayList<>();
        nameParts.add(demo.getYear());
        nameParts.add(demo.getMake());
        nameParts.add(demo.getModel());
        binding.collapsingToolbarLayout.setTitle(Utils.buildString(nameParts));

        binding.idnumber.setText(demo.getId());
        binding.price.setText(String.format(getResources().getString(R.string.priceholder), demo.getPrice()));

        binding.dateOut.setText(Utils.getDateStringFromDate(demo.getDateOut()));
        String mileageOut = demo.getMileageOut();
        binding.mileageOut.setText(String.format(getResources()
                .getQuantityString(R.plurals.miles_suffix,
                        Integer.parseInt(mileageOut)), mileageOut));
    }

    @Override
    public void setTripItems(List<Trip> trips) {
        adapter.updateItems(trips);
    }

    @Override
    public void startCheckInDialog() {
        if (isCheckedIn) {
            alreadyCheckedIn();
            return;
        }
        CheckInDemoDialog dialog = new CheckInDemoDialog(this, demo);
        dialog.setListener(demo -> presenter.checkInDemo(demo)).show();
    }

    @Override
    public void startEditDialog() {
        UpdateDemoDialog dialog = new UpdateDemoDialog(this, demo);
        dialog.setListener(demo -> presenter.editDemo(demo)).show();
    }

    @Override
    public void startDeleteDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_delete_confirmation_title)
                .content(R.string.dialog_delete_confirmation_message)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    deletingVehicle = true;
                    presenter.deleteDemo(demo);
                }).show();
    }

    @Override
    public void startAddBusinessDialog() {
        AddTripDialog dialog = new AddTripDialog(this, demo);
        dialog.setListener(trip -> presenter.saveTrip(trip)).show();
    }

    @Override
    public void startEditOrDeleteBusinessDialog(Trip trip) {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_start_edit_or_delete_title)
                .items(R.array.dialog_start_edit_or_delete_items)
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            // edit
                            presenter.editTripItemClicked(trip);
                            break;
                        case 1:
                            // delete
                            presenter.deleteTripItemClicked(trip);
                            break;
                    }
                }).show();
    }

    @Override
    public void startEditBusinessDialog(Trip trip) {
        UpdateTripDialog dialog = new UpdateTripDialog(this, trip);
        dialog.setListener(editedTrip -> presenter.editTripItem(editedTrip)).show();
    }

    @Override
    public void startDeleteBusinessDialog(Trip trip) {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_delete_trip_confirmation_title)
                .content(R.string.dialog_delete_trip_confirmation_message)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> presenter.deleteTrip(trip)).show();
    }

    private void alreadyCheckedIn() {
        showSnackbar(binding.coordinator, getString(R.string.err_msg_already_checked_in));
    }

    @Override
    public void saveSuccess() {
        if (deletingVehicle) {
            showSnackbar(binding.coordinator, getString(R.string.delete_success));
            finish();
        } else {
            showSnackbar(binding.coordinator, getString(R.string.edit_success));
            presenter.loadData(demo.getId());
        }
    }

    @Override
    public void saveError() {
        if(deletingVehicle) {
            showSnackbar(binding.coordinator, getString(R.string.delete_error));
        } else {
            showSnackbar(binding.coordinator, getString(R.string.err_msg_edit));
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        ItemClickSupport.addTo(binding.recyclerView).setOnItemClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripAdapter(this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fab.setImageDrawable(new IconDrawable(this,
                Iconify.IconValue.zmdi_check)
                .color(ContextCompat.getColor(this, R.color.icons))
                .sizeDp(48));
        addToAutoUnsubscribe(RxView.clicks(binding.fab)
                .subscribe(clicks -> {
                    presenter.checkInButtonClicked();
                }));
    }

    private void setupAdView() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("9F54C222FEEF5529841D655040886EC1")
                .build();
        if (binding.adView != null) {
            binding.adView.loadAd(adRequest);
        }
    }

    @Override
    public void onItemClicked(int position) {
        presenter.tripItemClicked(adapter.getTrip(position));
    }
}
