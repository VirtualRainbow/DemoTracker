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
import com.jakewharton.rxbinding.widget.RxTextView;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.data.VehicleModels;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.databinding.DialogDatepickerBinding;
import com.virtualrainbowllc.demotracker.databinding.DialogUpdateDemoBinding;
import com.virtualrainbowllc.demotracker.tools.Utils;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

public class UpdateDemoDialog extends BaseDialog {

    private DialogUpdateDemoBinding binding;

    private Context context;
    private Demo demo;

    private OnConfirmedListener<Demo> listener;

    private String dialogYear, dialogMake, dialogModel, dialogDateOut, dialogDateIn,
            dialogMileageOut, dialogMileageIn, dialogPrice, modelArray;

    public UpdateDemoDialog(Context context, Demo demo) {
        super(context);
        this.context = context;
        this.demo = demo;
    }

    @Override
    protected ViewDataBinding getLayoutBinding() {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_update_demo, null, false);
    }

    @Override
    protected void setBinding(@NonNull ViewDataBinding binding) {
        this.binding = (DialogUpdateDemoBinding) binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dialog_edit_vehicle);

        boolean checkedIn = demo.isCheckedIn();

        binding.mileageIn.setFocusable(checkedIn);
        binding.mileageIn.setFocusableInTouchMode(checkedIn);
        binding.mileageIn.setLongClickable(checkedIn);

        binding.idnumber.setText(demo.getId());

        dialogYear = demo.getYear();
        binding.year.setText(dialogYear);
        addToAutoUnsubscribe(RxView.clicks(binding.year).subscribe(click -> {
            new MaterialDialog.Builder(context)
                    .title(R.string.dialog_choose_year)
                    .items(R.array.years)
                    .itemsCallback((dialog2, v, which, text) -> {
                        dialogYear = text.toString();
                        binding.year.setText(dialogYear);
                    })
                    .positiveText(android.R.string.cancel).show();
        }));

        dialogMake = demo.getMake();
        modelArray = dialogMake.toLowerCase();
        binding.make.setText(dialogMake);
        addToAutoUnsubscribe(RxView.clicks(binding.make).subscribe(click -> {
            new MaterialDialog.Builder(context)
                    .title(R.string.dialog_choose_make)
                    .items(R.array.makes)
                    .itemsCallback((dialog2, v, which, text) -> {
                        dialogMake = text.toString();
                        binding.make.setText(dialogMake);
                        modelArray = dialogMake.toLowerCase();
                    })
                    .positiveText(android.R.string.cancel).show();
        }));

        dialogModel = demo.getModel();
        binding.model.setText(dialogModel);
        addToAutoUnsubscribe(RxView.clicks(binding.model).subscribe(click -> {
            new MaterialDialog.Builder(context)
                    .title(R.string.dialog_choose_model)
                    .items(VehicleModels.getModels().get(modelArray))
                    .itemsCallback((dialog2, v, which, text) -> {
                        dialogModel = text.toString();
                        binding.model.setText(dialogModel);
                    })
                    .positiveText(android.R.string.cancel).show();
        }));

        dialogDateOut = Utils.getDateStringFromDate(demo.getDateOut());
        binding.dateOut.setText(dialogDateOut);
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

        if (checkedIn) {
            dialogDateIn = Utils.getDateStringFromDate(demo.getDateIn());
            binding.dateIn.setText(dialogDateIn);
            dialogMileageIn = demo.getMileageIn();
            binding.mileageIn.setText(dialogMileageIn);

            addToAutoUnsubscribe(RxTextView.textChanges(binding.mileageIn)
                    .debounce(500L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .skip(1)
                    .map(value -> {
                        if (Integer.parseInt(value.toString()) <= 0 || value.toString().isEmpty()) {
                            binding.mileageInTextLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                        } else if (Integer.parseInt(value.toString()) <
                                Integer.parseInt(demo.getMileageOut())) {
                            binding.mileageInTextLayout.setError(context.getString(R.string.err_msg_mileage_in_must_be_higher)
                                    + " " + demo.getMileageOut());
                        } else {
                            binding.mileageInTextLayout.setErrorEnabled(false);
                        }
                        return value;
                    })
                    .subscribe(value -> {
                        dialogMileageIn = value.toString();
                    }));
        }


        addToAutoUnsubscribe(RxView.clicks(binding.dateIn).subscribe(click -> {
            if (!checkedIn) {
                binding.dateInTextLayout.setError(context.getString(R.string.dialog_vehicle_not_checked_in));
                return;
            }

            MaterialDialog.Builder dateDialog = new MaterialDialog.Builder(context);
            DialogDatepickerBinding dateBinding = DialogDatepickerBinding.inflate(getLayoutInflater());
            dateDialog.customView(dateBinding.getRoot(), false)
                    .negativeText(android.R.string.cancel)
                    .positiveText(android.R.string.ok)
                    .onPositive((dialog2, which) -> {
                        if (checkedIn) {
                            dialogDateIn = Utils.getFormattedDateFromPicker(
                                    dateBinding.datePicker.getMonth(),
                                    dateBinding.datePicker.getDayOfMonth(),
                                    dateBinding.datePicker.getYear());
                            binding.dateIn.setText(dialogDateIn);
                        }
                    }).build().show();
        }));

        addToAutoUnsubscribe(RxView.clicks(binding.mileageIn).subscribe(click -> {
            if (!checkedIn) {
                binding.mileageInTextLayout.setError(context.getString(R.string.dialog_vehicle_not_checked_in));
            }
        }));

        dialogPrice = demo.getPrice();
        binding.price.setText(dialogPrice);
        addToAutoUnsubscribe(RxTextView.textChanges(binding.price)
                .debounce(500L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .skip(1)
                .map(value -> {
                    if (Integer.parseInt(value.toString()) <= 0 || value.toString().isEmpty()) {
                        binding.priceTextLayout.setError(context.getString(R.string.err_msg_price_empty));
                    } else {
                        binding.priceTextLayout.setErrorEnabled(false);
                    }
                    return value;
                })
                .subscribe(value -> {
                    dialogPrice = value.toString();
                }));

        dialogMileageOut = demo.getMileageOut();
        binding.mileageOut.setText(dialogMileageOut);
        addToAutoUnsubscribe(RxTextView.textChanges(binding.mileageOut)
                .debounce(500L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .skip(1)
                .map(value -> {
                    if (Integer.parseInt(value.toString()) <= 0 || value.toString().isEmpty()) {
                        binding.mileageOutTextLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                    } else {
                        binding.mileageOutTextLayout.setErrorEnabled(false);
                    }
                    return value;
                })
                .subscribe(value -> {
                    dialogMileageOut = value.toString();
                }));

        addToAutoUnsubscribe(RxView.clicks(binding.confirm).subscribe(clicks -> {
            if (listener != null) {
                boolean error = false;
                if (dialogMileageOut.isEmpty()) {
                    error = true;
                    binding.mileageOutTextLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                }
                if (checkedIn) {
                    if (dialogMileageIn.isEmpty()) {
                        error = true;
                        binding.mileageInTextLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                    }
                    if (Integer.parseInt(dialogMileageOut) > Integer.parseInt(dialogMileageIn)) {
                        error = true;
                        binding.mileageOutTextLayout.setError(context.getString(R.string.err_msg_mileage_in_must_be_higher));
                    }
                }
                if (!error) {
                    binding.mileageOutTextLayout.setErrorEnabled(false);
                    binding.mileageInTextLayout.setErrorEnabled(false);

                    listener.onConfirmed(demo);

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

    public UpdateDemoDialog setListener(@NonNull OnConfirmedListener<Demo> listener) {
        this.listener = listener;
        return this;
    }
}
