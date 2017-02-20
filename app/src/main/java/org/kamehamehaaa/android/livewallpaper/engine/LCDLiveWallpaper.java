package org.kamehamehaaa.android.livewallpaper.engine;

//import java.sql.Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.kamehamehaaa.android.livewallpaper.candies.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;

public class LCDLiveWallpaper extends WallpaperService {

    private FirebaseAnalytics mFirebaseAnalytics;

    private class MyWallpaperEngine extends Engine {
		private Handler mHandler = new Handler();
		private SurfaceHolder mSurfaceHolder = null;
		private Canvas mCanvas = null;
		private int width = 0;
		private int height = 0;
		private int pixelWidth = 0;
		private int pixelHeight = 0;
		private boolean[][] displayMatrix;
		private int refreshDelay = 1000 / framerate;
		private WriteClass wC = new WriteClass();
		float margin = 0.5f;
	
		

		private Runnable mDraw = new Runnable() {

			public void run() {
				drawFrame();
				refreshDelay = 1000 / framerate;
			}

		};

		private void drawBg(Canvas c) {
            
            
			try {
				c.drawRect(0, 0, width, height, bg);
			    
				
			    
			} catch (Exception e) {
				Log.e("LCDLiveWallpaper", "exception", e);
			}
		}

		private void drawFrame() {

			mCanvas = mSurfaceHolder.lockCanvas();
			drawBg(mCanvas);
			update();

			drawMatrix();
			try {
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			} catch (Exception e) {
				Log.e("LCDLiveWallpaper", "exception", e);
			}
			mHandler.postDelayed(mDraw, refreshDelay);

		}

		private void drawMatrix() throws IndexOutOfBoundsException {
			for (int i = 0; i < LCD_WIDTH; i++)
				for (int j = 0; j < LCD_HEIGHT; j++) {
					try {
						if (displayMatrix[i][j])
							drawPixel(i, j, displayMatrix[i][j]);
					} catch (Throwable e) {
						Log.e("LCDLiveWallpaper", "exception", e);
					}
				}

		}

		private void drawPixel(int x, int y, boolean value) {

			try {
				if (value) {
					
					mCanvas.drawRect((x * pixelWidth) + margin,
							(y * pixelHeight) + margin, (x * pixelWidth)
									+ pixelWidth - margin, (y * pixelHeight)
									+ pixelHeight - margin, onPixelPaint);
				}
			} catch (Exception e) {
				Log.e("LCDLiveWallpaper", "exception", e);
			}
		}

		public void init() {
			if (mCanvas == null && mSurfaceHolder == null)
				return;

			width = mCanvas.getWidth();
			height = mCanvas.getHeight();
			pixelWidth = width / LCD_WIDTH;
			pixelHeight = height / LCD_HEIGHT;
			WriteClass.dispTime = false;
			WriteClass.dispDate = false;
			WriteClass.clockType = "";
			WriteClass.blackBinary = true;
			// WriteClass.dateType = "european";
           
			initMatrix();

			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(context);

			String candySetting = sharedPref
					.getString("eye_candy", "waterfall");
			Boolean clockEnabled = sharedPref.getBoolean("show_clock", true);
			Boolean dateEnabled = sharedPref.getBoolean("show_date", false);
			String clockType = sharedPref.getString("clock_type", "decimal");
			Boolean bigClock = sharedPref.getBoolean("big_clock", false);
			Boolean blackClock = sharedPref.getBoolean("black_clock", false);
			WriteClass.dateType = sharedPref.getString("date_format",
					"dd/MM/yy");

			WriteClass.setTime(clockEnabled);
			WriteClass.setDate(dateEnabled);
			WriteClass.setClockType(clockType);
			WriteClass.blackBinary = blackClock;
			WriteClass.setBigBinary(bigClock);
			setEyeCandy(candySetting);
			setFramerate(sharedPref.getInt("framerate", 10));

			EyeCandyRandom.setDensity(sharedPref.getInt("random_pixel_density",
					50));
			EyeCandyWaterfall.setOverlapping(sharedPref.getBoolean(
					"waterfall_overlap", true));
			EyeCandyWaterfall.setAppearnceChance(100);
			EyeCandyWaterfall.setNrStrings(sharedPref.getInt(
					"waterfall_strings2", 100));
			if (sharedPref.getBoolean("use_custom_colors", false)) {
				setBgColor(sharedPref.getString("color", "0x999999"));
				setPxColor(sharedPref.getString("pixel_color", "0x333333"));
			} else {
				setColorSet(sharedPref.getString("color_set", "999999|333333"));
			}
		}

		@SuppressLint("NewApi")
		public void initMatrix() {
			final WindowManager w = (WindowManager) getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			final Display d = w.getDefaultDisplay();
			final DisplayMetrics m = new DisplayMetrics();
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;

			if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
				d.getRealMetrics(m);
			} else {
				d.getMetrics(m);
			}
			if (m.widthPixels >= 720) {
				LCD_WIDTH = m.widthPixels / 10;
				LCD_HEIGHT = m.heightPixels / 10;
			} else if (m.widthPixels >= 480) {
				LCD_WIDTH = m.widthPixels / 7;
				LCD_HEIGHT = m.heightPixels / 7;

			} else if (m.widthPixels > 320) {
				LCD_WIDTH = m.widthPixels / 5;
				LCD_HEIGHT = m.heightPixels / 5;

			} else {
				LCD_WIDTH = m.widthPixels / 4;
				LCD_HEIGHT = m.heightPixels / 4;

			}

			displayMatrix = new boolean[LCD_WIDTH][LCD_HEIGHT];
			for (int i = 0; i < LCD_WIDTH; i++)
				for (int j = 0; j < LCD_HEIGHT; j++)
					displayMatrix[i][j] = false;
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);

			mSurfaceHolder = surfaceHolder;
			mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);
			onPixelPaint = new Paint();
			onPixelPaint.setARGB(0xFF, 0x33, 0x33, 0x33);
			onPixelPaint.setStrokeWidth(0.5f);

		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			mSurfaceHolder = holder;
			mCanvas = holder.lockCanvas();
			init();
			initMatrix();
			try {
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			} catch (Exception e) {
				Log.e("LCDLiveWallpaper", "exception", e);
			}
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			mSurfaceHolder = holder;
			mCanvas = holder.lockCanvas();
			init();
			initMatrix();
			try {
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			} catch (Exception e) {
				Log.e("LCDLiveWallpaper", "exception", e);
			}

		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mHandler.removeCallbacks(mDraw);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);

		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if (visible)
				mHandler.postDelayed(mDraw, refreshDelay);
			else
				mHandler.removeCallbacks(mDraw);

		}

		@SuppressLint("SimpleDateFormat")
		private void update() {
			initMatrix(); // Called just to clean the matrix
            
			
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			SimpleDateFormat dfd = new SimpleDateFormat("dd.MM");
			Calendar cal = Calendar.getInstance();

			if (df.format(cal.getTime()).equals("03:14")) {
				new EyeCandyPI().draw(displayMatrix);
			} else if (df.format(cal.getTime()).equals("13:37")) {
				new EyeCandyLeet().draw(displayMatrix);
			} else if (dfd.format(cal.getTime()).equals("14.03")) {
				displayMatrix = eyeCandy.draw(displayMatrix);
				new EyeCandyPIDay().draw(displayMatrix);
				try {
					displayMatrix = wC.drawDateTime(displayMatrix);
				} catch (Throwable e) {
					Log.e("LCDLiveWallpaper", "exception", e);
				}
			} else if (dfd.format(cal.getTime()).equals("04.05")) {
				displayMatrix = eyeCandy.draw(displayMatrix);
				new EyeCandyVader().draw(displayMatrix);
				try {
					displayMatrix = wC.drawDateTime(displayMatrix);
				} catch (Throwable e) {
					Log.e("LCDLiveWallpaper", "exception", e);
				}
			} else if (eyeCandy != null) {
				displayMatrix = eyeCandy.draw(displayMatrix);
				try {
					displayMatrix = wC.drawDateTime(displayMatrix);
				} catch (Throwable e) {
					Log.e("LCDLiveWallpaper", "exception", e);
				}
			} else {
				try {
					displayMatrix = wC.drawDateTime(displayMatrix);
				} catch (Throwable e) {
					Log.e("LCDLiveWallpaper", "exception", e);
				}
			}

		}
		
	}

	// Static stuff goes here
	private static int LCD_WIDTH = 72;
	private static int LCD_HEIGHT = 128;
	private static EyeCandy eyeCandy = null;
	private static Context context = null;
	private static int framerate = 1;
	private static MyWallpaperEngine engine;
	private static Paint bg = new Paint();
	private static Paint onPixelPaint = null;
	private static boolean useCustomColors = false;
	public static int getLCD_WIDTH() {
		return LCD_WIDTH;
	}

	public static int getLCD_HEIGHT() {
		return LCD_HEIGHT;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		try {
			engine.initMatrix();
			eyeCandy.init();
		} catch (Exception e) {
			Log.e("LCDLiveWallpaper", "exception", e);
		}

	}

	@Override
	public Engine onCreateEngine() {
		context = getApplicationContext();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
		engine = new MyWallpaperEngine();
		return engine;
	}
	
	public static Context getContext() {
		return context;
	}

	public static void setEyeCandy(String name) {

		if (name.equalsIgnoreCase("none")) {
			eyeCandy = null;
		} else if (name.equalsIgnoreCase("checkerbox")) {
			eyeCandy = new EyeCandyFlip();
		} else if (name.equalsIgnoreCase("gradient")) {
			eyeCandy = new EyeCandyGradient();
		} else if (name.equalsIgnoreCase("random")) {
			eyeCandy = new EyeCandyRandom();
		} else if (name.equalsIgnoreCase("waterfall")) {
			eyeCandy = new EyeCandyWaterfall();
		} else {
			eyeCandy = null;
		}

	}

	public static void setFramerate(int value) {

		framerate = value > 0 ? value : 1;
	}

	public static void setBgColor(String string) {

		int r = 0x99;
		int g = 0xAA;
		int b = 0x99;
		try {
			if (string.length() == 8) {
				String color = string.split("[x|X]")[1];
				r = Integer.parseInt(color.substring(0, 2), 16);
				g = Integer.parseInt(color.substring(2, 4), 16);
				b = Integer.parseInt(color.substring(4, 6), 16);
			}
		} catch (Exception e) {
			Log.e("LCDLiveWallpaper", "exception", e);
		}
		bg.setARGB(0xFF, r, g, b);
	}

	public static void setPxColor(String string) {

		int r = 0x33;
		int g = 0x33;
		int b = 0x33;

		try {
			if (string.length() == 8) {
				String color = string.split("[x|X]")[1];
				r = Integer.parseInt(color.substring(0, 2), 16);
				g = Integer.parseInt(color.substring(2, 4), 16);
				b = Integer.parseInt(color.substring(4, 6), 16);
			}
		} catch (Exception e) {
			Log.e("LCDLiveWallpaper", "exception", e);
		}

		onPixelPaint.setARGB(0xFF, r, g, b);
	}

	public static void setColorSet(String string) {

		setBgColor("0x".concat(string.split("\\|")[0]));
		setPxColor("0x".concat(string.split("\\|")[1]));
	}

	public static void setUseCustomColors(boolean b) {
		useCustomColors = b;
	}

}
