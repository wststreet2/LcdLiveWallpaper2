package org.kamehamehaaa.android.livewallpaper.candies;

import org.kamehamehaaa.android.livewallpaper.engine.LCDLiveWallpaper;

import android.util.Log;

class rect {
	public int x1, y1, x2, y2;
	public boolean color;
	public boolean splitType;

	public rect(int x1, int y1, int x2, int y2, boolean color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}
}

public class EyeCandyFlip extends EyeCandy {
	private boolean[][] flip;
	private rect[] rects;
	private int nrRects;
	private int width, height;
	private int totalflips;
	public boolean up;
	rect newRects[];
	int newRIndex;

	public EyeCandyFlip() {
		init();
	}

	public void init() {
		width = LCDLiveWallpaper.getLCD_WIDTH();
		height = LCDLiveWallpaper.getLCD_HEIGHT();

		flip = new boolean[width][height];
		rects = new rect[64];

		rects[0] = new rect(0, 0, width, height, true);

		nrRects = 1;
		rects[0].splitType = true; // urmeaza split pe orizontala

		totalflips = 0;

		up = true;
	}

	void addRect(int x1, int y1, int x2, int y2, boolean color,
			boolean splitType) {
		newRects[newRIndex] = new rect(x1, y1, x2, y2, color);
		newRects[newRIndex].splitType = splitType;

		newRIndex++;
	}

	private rect[] split() {
		int i;
		int newNrRects = nrRects * 2;
		newRects = null;
		newRects = new rect[newNrRects];
		newRIndex = 0;
		boolean color = false;

		for (i = 0; i < nrRects; i++) {
			if (rects[i].splitType == true) {
				if (i % 2 == 0) {
					color = !color;
				}
				addRect(rects[i].x1, rects[i].y1, rects[i].x2, rects[i].y2
						- (rects[i].y2 - rects[i].y1) / 2, color, false);
				addRect(rects[i].x1, rects[i].y2 - (rects[i].y2 - rects[i].y1)
						/ 2, rects[i].x2, rects[i].y2, !color, false);

			} else {
				if (i % 2 == 1) {
					color = !color;
				}
				addRect(rects[i].x1, rects[i].y1, rects[i].x2
						- (rects[i].x2 - rects[i].x1) / 2, rects[i].y2, !color,
						true);
				addRect(rects[i].x2 - (rects[i].x2 - rects[i].x1) / 2,
						rects[i].y1, rects[i].x2, rects[i].y2, color, true);
			}
			color = false;
		}

		nrRects = newRIndex;
		return newRects;
	}

	private rect[] unite() {
		int i, j = 0;
		int newNrRects = nrRects / 2;
		newRects = null;
		newRects = new rect[newNrRects];
		newRIndex = 0;
		boolean color = false;

		for (i = 0; i < nrRects - 1; i += 2) {

			if (rects[i].splitType == true) {
				addRect(rects[i].x1, rects[i].y1, rects[i + 1].x2,
						rects[i + 1].y2, color, false);
			}

			if (rects[i].splitType == false) {
				addRect(rects[i].x1, rects[i].y1, rects[i + 1].x2,
						rects[i + 1].y2, color, true);
			}

			// color = false;
			if (j % 2 == 0) {
				color = !color;
			}
			j++;
		}

		nrRects = newRIndex;
		return newRects;
	}

	public boolean[][] draw(boolean mat[][]) {
		try {
			int i, j, k;

			if (up) {
				if (totalflips < 9) {
					rects = split();
					totalflips++;
				} else {
					up = false;
				}
			} else {
				if (totalflips > 0) {
					rects = unite();
					totalflips--;
				} else {
					up = true;
				}
			}

			for (i = 0; i < width; i++) {
				for (j = 0; j < height; j++) {
					try {
						mat[i][j] = false;
					} catch (Error msg) {

					}
				}
			}

			boolean color = true;

			for (k = 0; k < nrRects; k++) {

				if (rects[k].color)
					for (i = rects[k].x1; i < rects[k].x2; i++) {
						for (j = rects[k].y1; j < rects[k].y2; j++) {

							try {
								mat[i][j] = color;

							} catch (Error msg) {
							}

						}

					}

			}

			
		} catch (Exception e) {

		}
		return mat;
	}
}
