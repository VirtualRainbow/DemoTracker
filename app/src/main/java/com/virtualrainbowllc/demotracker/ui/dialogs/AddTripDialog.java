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

package com.virtualrainbowllc.demotracker.ui.dialogs;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.databinding.DialogDatepickerBinding;
import com.virtualrainbowllc.demotracker.databinding.DialogTripBinding;
import com.virtualrainbowllc.demotracker.tools.Utils;

import java.util.UUID;

public class AddTripDialog extends BaseDialog {

    private DialogTripBinding binding;

    private Context context;
    private Demo demo;

    private OnConfirmedListener<Trip> listener;

    private String dialogDateOut, dialogDateIn;

    public AddTripDialog(Context context, Demo demo) {
        super(context);
        this.context = context;
        this.demo = demo;
    }

    @Override
    protected ViewDataBinding getLayoutBinding() {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_trip, null, false);
    }

    @Override
    protected void setBinding(@NonNull ViewDataBinding binding) {
        this.binding = (DialogTripBinding) binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dialog_add_business_trip);

        addToAutoUnsubscribe(RxView.clicks(binding.dateOut).subscribe(click -> {
            MaterialDialog.Builder dateDialog = new MaterialDialog.Builder(context);
            DialogDatepickerBinding dateBinding = DialogDatepickerBinding.inflate(getLayoutInflater());
            dateDialog.customView(dateBinding.getRoot(), false)
                    .negativeText(android.R.string.cancel)
                    .positiveText(android.R.string.ok)
                    .onPositive((dialog2, which) -> {
                        dialogDateOut = Utils.getFormattedDateFromPicker(
                                dateBinding.datePicker.getMonth(),
                                dateBinding.datePicker.getDayOfMonth(),
                                dateBinding.datePicker.getYear());
                        binding.dateOut.setText(dialogDateOut);
                    }).build().show();
        }));

        addToAutoUnsubscribe(RxView.clicks(binding.dateIn).subscribe(click -> {
            MaterialDialog.Builder dateDialog = new MaterialDialog.Builder(context);
            DialogDatepickerBinding dateBinding = DialogDatepickerBinding.inflate(getLayoutInflater());
            dateDialog.customView(dateBinding.getRoot(), false)
                    .negativeText(android.R.string.cancel)
                    .positiveText(android.R.string.ok)
                    .onPositive((dialog2, which) -> {
                        dialogDateIn = Utils.getFormattedDateFromPicker(
                                dateBinding.datePicker.getMonth(),
                                dateBinding.datePicker.getDayOfMonth(),
                                dateBinding.datePicker.getYear());
                        binding.dateIn.setText(dialogDateIn);
                    }).build().show();
        }));

        addToAutoUnsubscribe(RxView.clicks(binding.confirm).subscribe(clicks -> {
            if (listener != null) {
                boolean error = false;
                if (dialogDateOut == null) {
                    error = true;
                    binding.dateOutTextLayout.setError(context.getString(R.string.err_msg_date_empty));
                }
                if (dialogDateIn == null) {
                    error = true;
                    binding.dateInTextLayout.setError(context.getString(R.string.err_msg_date_empty));
                }
                if (Utils.getStringFromET(binding.totalBusinessMiles).isEmpty()
                        || Utils.getIntFromET(binding.totalBusinessMiles) == 0) {
                    error = true;
                    binding.totalBusinessMilesTextLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                }
                if (!error) {
                    binding.dateOutTextLayout.setErrorEnabled(false);
                    binding.dateInTextLayout.setErrorEnabled(false);
                    binding.totalBusinessMilesTextLayout.setErrorEnabled(false);

                    Trip trip = new Trip(UUID.randomUUID().toString(), demo.getId(),
                            Utils.getDateFromString(dialogDateOut),
                            Utils.getDateFromString(dialogDateIn),
                            Utils.getStringFromET(binding.totalBusinessMiles));

                    listener.onConfirmed(trip);

                    if (isShowing()) {
                        dismiss();
                    }
                }
            }
        }));

        addToAutoUnsubscribe(RxView.clicks(binding.cancel).subscribe(clicks -> {
            if (isShowing()) {
                dismiss();
            }
        }));
    }

    public AddTripDialog setListener(@NonNull OnConfirmedListener<Trip> listener) {
        this.listener = listener;
        return this;
    }
}
