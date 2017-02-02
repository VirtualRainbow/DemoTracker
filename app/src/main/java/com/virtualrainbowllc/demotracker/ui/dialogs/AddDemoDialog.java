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
import com.virtualrainbowllc.demotracker.data.VehicleModels;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.databinding.DialogAddDemoBinding;
import com.virtualrainbowllc.demotracker.tools.Utils;

import java.util.Date;

public class AddDemoDialog extends BaseDialog {

    private DialogAddDemoBinding binding;

    private Context context;

    private String dialogYear, modelArray, dialogMake, dialogModel;

    private OnConfirmedListener<Demo> listener;

    public AddDemoDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected ViewDataBinding getLayoutBinding() {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_add_demo, null, false);
    }

    @Override
    protected void setBinding(@NonNull ViewDataBinding binding) {
        this.binding = (DialogAddDemoBinding) binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.dialog_check_out_vehicle);

        addToAutoUnsubscribe(RxView.clicks(binding.year).subscribe(click -> {
            new MaterialDialog.Builder(context)
                    .title("Choose year")
                    .items(R.array.years)
                    .itemsCallback((dialog2, v, which, text) -> {
                        dialogYear = text.toString();
                        binding.year.setText(dialogYear);
                    })
                    .positiveText(android.R.string.cancel).show();
        }));
        // disable long clicks to prevent pasting into single choice selections
        addToAutoUnsubscribe(RxView.longClicks(binding.year).subscribe());


        addToAutoUnsubscribe(RxView.clicks(binding.make).subscribe(click -> {
            new MaterialDialog.Builder(context)
                    .title("Choose make")
                    .items(R.array.makes)
                    .itemsCallback((dialog2, v, which, text) -> {
                        dialogMake = text.toString();
                        binding.make.setText(dialogMake);
                        binding.model.setEnabled(true);
                        modelArray = dialogMake.toLowerCase();
                    })
                    .positiveText(android.R.string.cancel).show();
        }));
        // disable long clicks to prevent pasting into single choice selections
        addToAutoUnsubscribe(RxView.longClicks(binding.make).subscribe());

        addToAutoUnsubscribe(RxView.clicks(binding.model).subscribe(click -> {
            new MaterialDialog.Builder(context)
                    .title("Choose model")
                    .items(VehicleModels.getModels().get(modelArray))
                    .itemsCallback((dialog2, v, which, text) -> {
                        dialogModel = text.toString();
                        binding.model.setText(dialogModel);
                    })
                    .positiveText(android.R.string.cancel)
                    .show();
        }));
        // disable long clicks to prevent pasting into single choice selections
        addToAutoUnsubscribe(RxView.longClicks(binding.model).subscribe());

        addToAutoUnsubscribe(RxView.clicks(binding.confirm).subscribe(clicks -> {
            if (listener != null) {
                boolean error = false;
                if (Utils.getStringFromET(binding.idnumber).isEmpty()) {
                    error = true;
                    binding.idnumberLayout.setError(context.getString(R.string.err_msg_id_empty));
                }
                if (Utils.getStringFromET(binding.price).isEmpty()) {
                    error = true;
                    binding.priceTextLayout.setError(context.getString(R.string.err_msg_price_empty));
                }
                if (Utils.getStringFromET(binding.mileageOut).isEmpty()) {
                    error = true;
                    binding.mileageOutTextLayout.setError(context.getString(R.string.err_msg_mileage_empty));
                }
                if (dialogYear == null) {
                    error = true;
                    binding.yearTextLayout.setError(context.getString(R.string.err_msg_year_empty));
                }
                if (dialogMake == null) {
                    error = true;
                    binding.makeTextLayout.setError(context.getString(R.string.err_msg_make_empty));
                }
                if (dialogModel == null) {
                    error = true;
                    binding.modelTextLayout.setError(context.getString(R.string.err_msg_model_empty));
                }
                if (!error) {
                    binding.idnumberLayout.setErrorEnabled(false);
                    binding.priceTextLayout.setErrorEnabled(false);
                    binding.mileageOutTextLayout.setErrorEnabled(false);
                    binding.yearTextLayout.setErrorEnabled(false);
                    binding.makeTextLayout.setErrorEnabled(false);
                    binding.modelTextLayout.setErrorEnabled(false);

                    Demo demo = new Demo(Utils.getStringFromET(binding.idnumber), false,
                            dialogYear, dialogMake, dialogModel, Utils.getStringFromET(binding.price),
                            new Date(), Utils.getStringFromET(binding.mileageOut));

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

    public AddDemoDialog setListener(@NonNull OnConfirmedListener<Demo> listener) {
        this.listener = listener;
        return this;
    }
}
