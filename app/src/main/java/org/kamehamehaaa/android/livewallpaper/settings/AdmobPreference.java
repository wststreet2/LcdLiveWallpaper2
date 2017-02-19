package org.kamehamehaaa.android.livewallpaper.settings;

import org.kamehamehaaa.android.livewallpaper.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdmobPreference extends Preference {

    public AdmobPreference(Context context) {
        super(context, null);
    }

    public AdmobPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        //override here to return the admob ad instead of a regular preference display
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.admob_preference, null);
        AdView adView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("8CF778710FC9EDD27F89354DE3172D50")
                .addTestDevice("14D2A4FD15D042D93BECEAED1B0AE343").build();
        adView.loadAd(adRequest);
        //MobileAds.initialize(getContext(), "ca-app-pub-2569572093580074/1608209542");

        return view;
    }

}
