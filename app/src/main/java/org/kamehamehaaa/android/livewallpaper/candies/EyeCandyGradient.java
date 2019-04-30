package org.kamehamehaaa.android.livewallpaper.candies;

import java.util.Random;

import org.kamehamehaaa.android.livewallpaper.engine.LCDLiveWallpaper;

public class EyeCandyGradient extends EyeCandy {

	private int[][] gradient;
    private int width, height;

	public EyeCandyGradient() {
		init();
	}

	@Override
	public final boolean[][] draw(boolean[][] matrix) {
        boolean[][] gradCopy = new boolean[width][height];

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				gradCopy[j][i] = gradient[j][i] == 1;

		return gradCopy;
	}

	@Override
	public void init() {
		width = LCDLiveWallpaper.getLCD_WIDTH();
		height = LCDLiveWallpaper.getLCD_HEIGHT();
		gradient = new int[width][height];

		drawGradient();
		ditherGradient();
	}

	private void drawGradient() {
		int margin = 5;
		for (int i = 0; i < margin; i++)
			for (int j = 0; j < width; j++)
				gradient[j][i] = 0;

		for (int i = height - (2*margin); i < height; i++)
			for (int j = 0; j < width; j++)
				gradient[j][i] = 255;

		for (int i = margin; i < (height - margin) + 1; i++) {
			for (int j = 0; j < width; j++)
				gradient[j][i] = (int) (i * (255.0 / height));
		}

	}

	private void ditherGradient() {
		Random r = new Random();

		for (int i = 1; i <= width; i += 3) {
			for (int j = 1; j <= height; j += 3) {
				int original = 0;
				if (i < width && j < height)
					original = gradient[i][j];
				for (int k = i - 1; k <= i + 1; k++)
					for (int l = j - 1; l <= j + 1; l++) {
						if (k < width && l < height) {
							if (r.nextInt(100) < 100 * (original / 255.0))
								gradient[k][l] = 0;
							else
								gradient[k][l] = 1;
						}
					}
			}
		}

	}
}