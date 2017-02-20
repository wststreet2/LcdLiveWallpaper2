package org.kamehamehaaa.android.livewallpaper.settings;

import java.util.Calendar;

import org.kamehamehaaa.android.livewallpaper.R;
import org.kamehamehaaa.android.livewallpaper.candies.EyeCandyRandom;
import org.kamehamehaaa.android.livewallpaper.candies.EyeCandyWaterfall;
import org.kamehamehaaa.android.livewallpaper.engine.LCDLiveWallpaper;
import org.kamehamehaaa.android.livewallpaper.engine.WriteClass;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

@SuppressWarnings("deprecation")
public class LCDLiveWallpaperSettings extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private static final String PREFS_NAME = "LcdLiveWallpaperSettings";

	public static String getPrefsName() {
		return PREFS_NAME;
	}

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	protected void onResume() {
		super.onResume();

		String today = (String) DateFormat.format("dd", Calendar.getInstance());
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(LCDLiveWallpaper.getContext());

		//if (today.equals(sharedPref.getString("lastDonate", "-1")) == false) {
//			Log.d("LCDLiveWallpaper",
//					today + "  " + sharedPref.getString("lastDonate", "-1"));
//			sharedPref.edit().putString("lastDonate", today).commit();
//			final Dialog d = new Dialog(this);
//			d.setTitle(getString(R.string.donate_dialog_title));
//			d.setContentView(R.layout.dialogview);
//			((Button) d.findViewById(R.id.nobutton))
//					.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							d.hide();
//						}
//					});
//			((Button) d.findViewById(R.id.maybebutton))
//					.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							d.hide();
//							Intent i = new Intent(
//									Intent.ACTION_VIEW,
//									Uri.parse("https://play.google.com/store/apps/details?id=org.kamehamehaaa.android.livewallpaperpro"));
//							startActivity(i);
//						}
//					});
//			((Button) d.findViewById(R.id.yesbutton))
//					.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							d.hide();
//							Intent i = new Intent(
//									Intent.ACTION_VIEW,
//									Uri.parse("https://play.google.com/store/apps/details?id=org.kamehamehaaa.android.livewallpaperpro"));
//							startActivity(i);
//						}
//					});
//			d.show();
		//}

		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (key.equals("eye_candy")) {
			LCDLiveWallpaper.setEyeCandy(sharedPreferences.getString(key,
					"waterfall"));
		} else if (key.equals("show_clock")) {
			WriteClass.setTime(sharedPreferences.getBoolean(key, true));
		} else if (key.equals("show_date")) {
			WriteClass.setDate(sharedPreferences.getBoolean(key, false));
		} else if (key.equals("framerate")) {
			LCDLiveWallpaper.setFramerate(sharedPreferences.getInt(key, 10));
		} else if (key.equals("clock_type")) {
			// getString returneaza "decimal" sau "binary"
			WriteClass
					.setClockType(sharedPreferences.getString(key, "decimal"));
		} else if (key.equals("color")) {
			if (sharedPreferences.getBoolean("use_custom_colors", false))
				LCDLiveWallpaper.setBgColor(sharedPreferences.getString(key,
						"0x99AA99"));
		} else if (key.equals("pixel_color")) {
			if (sharedPreferences.getBoolean("use_custom_colors", false))
				LCDLiveWallpaper.setPxColor(sharedPreferences.getString(key,
						"0x333333"));
		} else if (key.equals("big_clock")) {

			WriteClass.bigBinary = sharedPreferences.getBoolean(key, false);
		} else if (key.equals("black_clock")) {
			WriteClass.blackBinary = sharedPreferences.getBoolean(key, false);

		} else if (key.equals("date_format")) {

			WriteClass.dateType = sharedPreferences.getString(key, "dd/MM/yy");
		} else if (key.equals("random_pixel_density")) {
			EyeCandyRandom.setDensity(sharedPreferences.getInt(key, 50));
		} else if (key.equals("waterfall_overlap")) {
			EyeCandyWaterfall.setOverlapping(sharedPreferences.getBoolean(key,
					true));
		} else if (key.equals("waterfall_chance")) {
			// EyeCandyWaterfall.setAppearnceChance(sharedPreferences.getInt(key,
			// 100));
			EyeCandyWaterfall.setAppearnceChance(100);
		} else if (key.equals("waterfall_strings2")) {
			EyeCandyWaterfall.setNrStrings(sharedPreferences.getInt(key, 100));
		} else if (key.equals("use_custom_colors")) {

			LCDLiveWallpaper.setUseCustomColors(sharedPreferences.getBoolean(
					key, false));
			if (sharedPreferences.getBoolean(key, false)) {
				LCDLiveWallpaper.setPxColor(sharedPreferences.getString(
						"pixel_color", "0x333333"));
				LCDLiveWallpaper.setBgColor(sharedPreferences.getString(
						"color", "0x99AA99"));
			} else {
				LCDLiveWallpaper.setColorSet(sharedPreferences.getString(
						"color_set", "999999|333333"));
			}
		} else if (key.equals("color_set")) {
			if (!sharedPreferences.getBoolean("use_custom_colors", false))
				LCDLiveWallpaper.setColorSet(sharedPreferences.getString(key,
						"999999|333333"));
		}
	}

}
