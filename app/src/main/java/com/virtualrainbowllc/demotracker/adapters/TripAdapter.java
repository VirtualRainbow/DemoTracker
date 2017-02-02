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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.databinding.ListTripsBinding;
import com.virtualrainbowllc.demotracker.tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context context;
    private List<Trip> trips = new ArrayList<>();

    public TripAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ListTripsBinding binding = ListTripsBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    public void updateItems(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (trips.get(position).getDateOut() != null) {
            holder.binding.dateOut.setText(Utils.getDateStringFromDate(trips.get(position)
                    .getDateOut()));
        }
        if (trips.get(position).getDateIn() != null) {
            holder.binding.dateIn.setText(Utils.getDateStringFromDate(trips.get(position)
                    .getDateIn()));
        }
        String miles = trips.get(position).getTotalBusinessMiles();
        holder.binding.totalBusinessMiles.setText(String.format(context.getResources()
                        .getQuantityString(R.plurals.miles_suffix, Integer.parseInt(miles)),
                miles));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public Trip getTrip(int position) {
        return trips.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListTripsBinding binding;

        ViewHolder(ListTripsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

