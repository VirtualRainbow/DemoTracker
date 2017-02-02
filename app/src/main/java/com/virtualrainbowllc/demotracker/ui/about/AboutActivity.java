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

package com.virtualrainbowllc.demotracker.ui.about;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.model.MaterialAboutTitleItem;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.virtualrainbowllc.demotracker.R;
import com.virtualrainbowllc.demotracker.databinding.DialogWebBinding;

import timber.log.Timber;

public class AboutActivity extends MaterialAboutActivity {

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("DemoTracker")
                .icon(R.mipmap.ic_launcher)
                .build());

        try {
            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(this,
                    new IconDrawable(this,
                            Iconify.IconValue.zmdi_info_outline)
                            .color(ContextCompat.getColor(this, R.color.dark_icons))
                            .sizeDp(48), "Version", false));

        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e.getMessage());
        }

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(new IconDrawable(this,
                        Iconify.IconValue.zmdi_time_restore)
                        .color(ContextCompat.getColor(this, R.color.dark_icons))
                        .sizeDp(48))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(this,
                        "Releases", "https://github.com/virtualrainbow/DemoTracker/releases", true, false))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(new IconDrawable(this,
                        Iconify.IconValue.zmdi_file_text)
                        .color(ContextCompat.getColor(this, R.color.dark_icons))
                        .sizeDp(48))
                .setOnClickListener(() -> {
                    DialogWebBinding binding = DialogWebBinding.inflate(getLayoutInflater());
                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title(R.string.dialog_license_title)
                            .customView(binding.getRoot(), true)
                            .positiveText(android.R.string.ok)
                            .build();

                    binding.webview.loadUrl("file:///android_asset/opensource.html");

                    dialog.show();
                })
                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("James Roberts")
                .subText("Fulton, MS")
                .icon(new IconDrawable(this,
                        Iconify.IconValue.zmdi_account_circle)
                        .color(ContextCompat.getColor(this, R.color.dark_icons))
                        .sizeDp(48))
                .build());

        authorCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(this,
                new IconDrawable(this,
                        Iconify.IconValue.zmdi_twitter)
                        .color(ContextCompat.getColor(this, R.color.dark_icons))
                        .sizeDp(48),
                "Follow me on twitter!",
                true,
                Uri.parse("http://twitter.com/JR_Roberts1")));

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Send feedback");

        convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(this,
                new IconDrawable(this,
                        Iconify.IconValue.zmdi_star)
                        .color(ContextCompat.getColor(this, R.color.dark_icons))
                        .sizeDp(48),
                "Rate this app",
                null
        ));
        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(this,
                new IconDrawable(this,
                        Iconify.IconValue.zmdi_email)
                        .color(ContextCompat.getColor(this, R.color.dark_icons))
                        .sizeDp(48),
                "Send an email",
                true,
                "virtualrainbowapps@gmail.com",
                "Question concerning DemoTracker"));

        return new MaterialAboutList.Builder()
                .addCard(appCardBuilder.build())
                .addCard(authorCardBuilder.build())
                .addCard(convenienceCardBuilder.build())
                .build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.demotracker_title_about);
    }
}
