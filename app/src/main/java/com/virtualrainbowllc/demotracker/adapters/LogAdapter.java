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

package com.virtualrainbowllc.demotracker.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.databinding.ListLogBinding;
import com.virtualrainbowllc.demotracker.tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private Context context;
    private List<Demo> demos = new ArrayList<>();

    public LogAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ListLogBinding binding = ListLogBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    public void updateItems(List<Demo> demos) {
        this.demos = demos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        boolean checkedIn = demos.get(position).isCheckedIn();
        holder.binding.vehicleId.setText(demos.get(position).getId());
        holder.binding.vehicleName.setText(demos.get(position).getDemoName());
        holder.binding.dateOut.setText(Utils.getDateStringFromDate(demos.get(position).getDateOut()));
        holder.binding.price.setText(String.format(context.getResources().getString(R.string.priceholder), demos.get(position).getPrice()));
        holder.binding.checked.setImageDrawable(new IconDrawable(context, Iconify.IconValue.zmdi_check_circle)
                .color(ContextCompat.getColor(context, checkedIn ? R.color.accent : R.color.primary_dark))
                .sizeDp(24));
        if (checkedIn) {
            holder.binding.dateIn.setText(Utils.getDateStringFromDate(demos.get(position).getDateIn()));
        }
    }

    @Override
    public int getItemCount() {
        return demos.size();
    }

    public String getDemoId(int position) {
        return demos.get(position).getId();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListLogBinding binding;

        ViewHolder(ListLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
