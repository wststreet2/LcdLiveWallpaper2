package org.kamehamehaaa.android.livewallpaper.candies;

import org.kamehamehaaa.android.livewallpaper.engine.LCDLiveWallpaper;

public class EyeCandyPI extends EyeCandy {

	static int pi_small[][] = {
			{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0 },
			{ 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 },
			{ 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0 },
			{ 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 } };

	public EyeCandyPI() {
		init();
	}

	@Override
	public boolean[][] draw(boolean[][] matrix) {
		// TODO Auto-generated method stub
		if (LCDLiveWallpaper.getLCD_WIDTH() >= 40) {
			drawLarge(matrix);
		} else {
			drawSmall(matrix);
		}
		return matrix;
	}

	private void drawSmall(boolean[][] matrix) {
		int startw = (LCDLiveWallpaper.getLCD_WIDTH() / 2) - 10;
		int starth = (LCDLiveWallpaper.getLCD_HEIGHT() / 2) - 9;

		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 17; j++) {
				matrix[i + startw][j + starth] = pi_small[j][i]!=0?true:false;
			}

	}

	private void drawLarge(boolean[][] matrix) {
		// TODO Auto-generated method stub

		int startw = (LCDLiveWallpaper.getLCD_WIDTH() / 2) - 20;
		int starth = (LCDLiveWallpaper.getLCD_HEIGHT() / 2) - 18;

		for (int i = 0; i < 40; i+=2)
			for (int j = 0; j < 34; j+=2) {
				matrix[i + startw][j + starth] = pi_small[j/2][i/2]!=0?true:false;
				matrix[i + startw + 1][j + starth] = pi_small[j/2][i/2]!=0?true:false;
				matrix[i + startw][j + starth + 1] = pi_small[j/2][i/2]!=0?true:false;
				matrix[i + startw + 1][j + starth + 1] = pi_small[j/2][i/2]!=0?true:false;
			}
	}

	@Override
	public void init() {

	}

}
