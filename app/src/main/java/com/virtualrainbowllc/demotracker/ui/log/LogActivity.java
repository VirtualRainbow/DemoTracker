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

package com.virtualrainbowllc.demotracker.ui.log;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.jakewharton.rxbinding.view.RxView;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.virtualrainbowllc.demotracker.AppComponent;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.adapters.LogAdapter;
import com.virtualrainbowllc.demotracker.data.DataModule;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.databinding.ActivityLogBinding;
import com.virtualrainbowllc.demotracker.tools.Constants;
import com.virtualrainbowllc.demotracker.tools.ItemClickSupport;
import com.virtualrainbowllc.demotracker.ui.about.AboutActivity;
import com.virtualrainbowllc.demotracker.ui.common.BaseActivity;
import com.virtualrainbowllc.demotracker.ui.dialogs.AddDemoDialog;
import com.virtualrainbowllc.demotracker.ui.viewdemo.ViewDemoActivity;

import java.util.List;

import javax.inject.Inject;

public class LogActivity extends BaseActivity implements LogContract.LogView, ItemClickSupport.OnItemClickListener {

    @Inject
    LogContract.LogPresenter presenter;

    @Inject
    SharedPreferences prefs;

    private ActivityLogBinding binding;
    private LogAdapter adapter;

    @Override
    protected void setupComponent(@NonNull AppComponent component) {
        DaggerLogComponent.builder()
                .appComponent(component)
                .dataModule(new DataModule())
                .logPresenterModule(new LogPresenterModule(this))
                .build().inject(this);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log);
        presenter.onViewAttached();
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
        getMenuInflater().inflate(R.menu.menu_log_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFirstRun() {
        showMaterialTapTarget(this, binding.fab, R.string.tap_target_log_primary,
                R.string.tap_target_log_secondary);
    }

    @Override
    public void editTapTargetFlags() {
        prefs.edit().putBoolean(Constants.FIRST_RUN_LOG, false).apply();
    }

    @Override
    public void setupBaseViews() {
        setSupportActionBar(binding.toolbar);
        setupRecyclerView();
        setupFab();
        setupAdView();
        if (prefs.getBoolean(Constants.FIRST_RUN_LOG, true)) {
            presenter.firstRun();
        }
        presenter.loadData();
    }

    @Override
    public void setItems(@NonNull List<Demo> demos) {
        adapter.updateItems(demos);
    }

    @Override
    public void startCheckOutDialog() {
        AddDemoDialog dialog = new AddDemoDialog(this);
        dialog.setListener(demo -> presenter.checkOutDemo(demo)).show();
    }

    @Override
    public void startViewDemoActivity(String id) {
        Intent i = new Intent(LogActivity.this, ViewDemoActivity.class);
        i.putExtra(Constants.ID, id);
        startActivity(i);
    }

    @Override
    public void checkOutDemoSuccess() {
        showSnackbar(binding.coordinator, "Checked out new vehicle successfully");
    }

    @Override
    public void checkOutDemoError() {
        showSnackbar(binding.coordinator, "Error checking out new vehicle");
    }

    private void setupRecyclerView() {
        ItemClickSupport.addTo(binding.recyclerView).setOnItemClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogAdapter(this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fab.setImageDrawable(new IconDrawable(this,
                Iconify.IconValue.zmdi_plus)
                .color(ContextCompat.getColor(this, R.color.icons))
                .sizeDp(48));
        addToAutoUnsubscribe(RxView.clicks(binding.fab).subscribe(click -> presenter.fabClicked()));
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
        presenter.itemClicked(adapter.getDemoId(position));
    }
}