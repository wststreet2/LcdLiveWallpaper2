package org.kamehamehaaa.android.livewallpaper.candies;

import java.util.Random;

import org.kamehamehaaa.android.livewallpaper.engine.LCDLiveWallpaper;

public class EyeCandyRandom extends EyeCandy {

	private Random r;
	private static byte density = 50;

	public EyeCandyRandom() {
		init();
	}

	@Override
	public boolean[][] draw(boolean[][] matrix) {

		for (int i = 0; i < LCDLiveWallpaper.getLCD_WIDTH(); i++)
			for (int j = 0; j < LCDLiveWallpaper.getLCD_HEIGHT(); j++) {
				if (r.nextInt(101) <= density)
					matrix[i][j] = true;
			}

		return matrix;
	}

	@Override
	public void init() {
		r = new Random();
	}

	public static void setDensity(int val) {
		density = (byte)val;
	}

}
