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

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.databinding.DialogCheckInDemoBinding;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

public class CheckInDemoDialog extends BaseDialog {

    private DialogCheckInDemoBinding binding;

    private Context context;
    private Demo demo;

    private String dialogMileageIn;

    private OnConfirmedListener<Demo> listener;

    public CheckInDemoDialog(Context context, Demo demo) {
        super(context);
        this.context = context;
        this.demo = demo;
    }

    @Override
    protected ViewDataBinding getLayoutBinding() {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_check_in_demo, null, false);
    }

    @Override
    protected void setBinding(@NonNull ViewDataBinding binding) {
        this.binding = (DialogCheckInDemoBinding) binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dialog_check_in_vehicle);

        addToAutoUnsubscribe(RxTextView.textChanges(binding.mileageIn)
                .debounce(500L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .skip(1)
                .map(value -> {
                    if (Integer.parseInt(value.toString()) <= 0) {
                        binding.mileageInLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                    } else if (Integer.parseInt(value.toString()) <
                            Integer.parseInt(demo.getMileageOut())) {
                        binding.mileageInLayout.setError(context.getString(R.string.err_msg_mileage_in_must_be_higher)
                                + " " + demo.getMileageOut());
                    } else {
                        binding.mileageInLayout.setErrorEnabled(false);
                    }
                    return value;
                })
                .subscribe(value -> {
                    dialogMileageIn = value.toString();
                }));

        addToAutoUnsubscribe(RxView.clicks(binding.confirm).subscribe(clicks -> {
            if (listener != null) {
                if (dialogMileageIn == null) {
                    binding.mileageInLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                } else {
                    binding.mileageInLayout.setErrorEnabled(false);

                    demo.setMileageIn(dialogMileageIn);
                    demo.setCheckedIn(true);

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

    public CheckInDemoDialog setListener(@NonNull OnConfirmedListener<Demo> listener) {
        this.listener = listener;
        return this;
    }
}
