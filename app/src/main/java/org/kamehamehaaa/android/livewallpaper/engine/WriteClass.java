package org.kamehamehaaa.android.livewallpaper.engine;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressLint("SimpleDateFormat")
public class WriteClass {
    private int cWidth = 5;

	public static boolean dispDate;
	public static boolean dispTime;
	public static String clockType;
	public static boolean blackBinary;
	public static boolean bigBinary;
	public static String dateType;

	public static void setBigBinary(boolean bigBinary) {
		if (LCDLiveWallpaper.getLCD_HEIGHT() >= 54)
			WriteClass.bigBinary = bigBinary;
		else
			WriteClass.bigBinary = false;
	}

	public static void setTime(boolean val) {
		dispTime = val;
	}

	public static void setDate(boolean val) {
		dispDate = val;
	}

	public static void setClockType(String cT) {
		clockType = cT;
	}

	public int[] getBinary(int number) {
		int[] b = new int[4];
		int i = 0;

		for (i = 0; i < 4; i++)
			b[i] = 0;

		i = 0;

		while (number > 0) // b[0] = lsb , b[3] = msb
		{
			b[i++] = number % 2;
			number = number / 2;
		}

		return b;
	}

	public boolean[][] drawH(boolean[][] displayMatrix2, int d1, int d2, int x,
			int lineNo, char option) // option selects draw for hour / minute /
										// second
	{
		int[] bin;
		int i = 0;
		int auxLineNo = lineNo;
		int auxX = x;
		int incr;
		boolean blackWhite; // 0 - black , 1 - white

		blackWhite = !blackBinary;

		if (!bigBinary) {
			incr = 2;
		} else {
			incr = 4;
		}

		if (d1 > 0) {
			bin = getBinary(d1);

			for (i = 0; i < 4; i++) {
				if (bin[i] == 1) {

					if (bigBinary) {
						displayMatrix2[auxX][auxLineNo - 1] = blackWhite;
						displayMatrix2[auxX - 1][auxLineNo] = blackWhite;
						displayMatrix2[auxX - 1][auxLineNo - 1] = blackWhite;
					}
					displayMatrix2[auxX][auxLineNo] = blackWhite;

				}

				if (option == 'h') {
					auxLineNo -= incr;
					auxX += incr;

				} else if (option == 'm') {

					auxLineNo -= incr;
				} else if (option == 's') {
					auxLineNo -= incr;
					auxX -= incr;
				}

			}
		}

		auxLineNo = lineNo;
		auxX = x + incr;

		if (d2 > 0) {
			bin = getBinary(d2);

			for (i = 0; i < 4; i++) {
				if (bin[i] == 1) {

					if (bigBinary) {
						displayMatrix2[auxX][auxLineNo - 1] = blackWhite;
						displayMatrix2[auxX - 1][auxLineNo] = blackWhite;
						displayMatrix2[auxX - 1][auxLineNo - 1] = blackWhite;
					}
					displayMatrix2[auxX][auxLineNo] = blackWhite;

				}

				if (option == 'h') {
					auxLineNo -= incr;
					auxX += incr;
				} else if (option == 'm') {
					auxLineNo -= incr;
				} else if (option == 's') {
					auxLineNo -= incr;
					auxX -= incr;
				}
			}
		}

		return displayMatrix2;
	}

	public boolean[][] drawBinaryWatch(boolean[][] displayMatrix2) {

		int i = 0, j = 0;
		int width, height;
		int x, lineNo; // left starting point at x , line at y = 40
		SimpleDateFormat df;
		String formattedDate = "";
		Calendar cal = Calendar.getInstance();
		df = new SimpleDateFormat("HH:mm:ss");

		if (!bigBinary) {
			lineNo = 47;
			width = 27;
			height = 11;
			x = (LCDLiveWallpaper.getLCD_WIDTH() / 2) - 14;

		} else {
			lineNo = 58;
			width = 54;
			height = 22;
			x = (LCDLiveWallpaper.getLCD_WIDTH() / 2) - 27;

		}

		formattedDate = df.format(cal.getTime());

		String[] values = formattedDate.split(":");

		for (i = x; i < x + width; i++) {
			for (j = lineNo; j > lineNo - height; j--) {
				displayMatrix2[i][j] = blackBinary;

				if (i == x || i == x + width - 1)
					displayMatrix2[i][j] = !blackBinary;

				if (j == lineNo || j == lineNo - height + 1)
					displayMatrix2[i][j] = !blackBinary;
			}
		}

		int d = 0, d1 = 0, d2 = 0;
		// pt ore

		d = Integer.parseInt(values[0]);
		if (d > 9) {
			d2 = d % 10;
			d = d / 10;
			d1 = d; // most significant digit
		} else {
			d2 = d;
		}

		// matrix = drawH(matrix,d1,d2,x + 2, lineNo - 2, 'h');
		if (!bigBinary) {
			displayMatrix2 = drawH(displayMatrix2, d1, d2, x + 2, lineNo - 2,
					'h');
			x = x + 12;
		} else {
			displayMatrix2 = drawH(displayMatrix2, d1, d2, x + 4, lineNo - 4,
					'h');
			x = x + 24;
		}

		// minute
		d = 0;
		d1 = 0;
		d2 = 0;
		d = Integer.parseInt(values[1]);

		if (d > 9) {
			d2 = d % 10;
			d = d / 10;
			d1 = d;
		} else {
			d2 = d;
		}

		// matrix = drawH(matrix,d1,d2,x , lineNo - 2,'m');
		if (!bigBinary) {
			displayMatrix2 = drawH(displayMatrix2, d1, d2, x, lineNo - 2, 'm');
			x = x + 10;
		} else {
			displayMatrix2 = drawH(displayMatrix2, d1, d2, x, lineNo - 4, 'm');
			x = x + 20;
		}

		// secunde
		d = 0;
		d1 = 0;
		d2 = 0;
		d = Integer.parseInt(values[2]);
		if (d > 9) {
			d2 = d % 10;
			d = d / 10;
			d1 = d;
		} else {
			d2 = d;
		}

		if (!bigBinary) {
			displayMatrix2 = drawH(displayMatrix2, d1, d2, x, lineNo - 2, 's');
		} else {
			displayMatrix2 = drawH(displayMatrix2, d1, d2, x, lineNo - 4, 's');
		}

		return displayMatrix2;
	}

	@SuppressLint("SimpleDateFormat")
	public boolean[][] drawDateTime(boolean[][] displayMatrix2) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(dateType); // european
																// default
		String formattedDate = "";

		int start = 0;

		if (dispDate) {

			start = (LCDLiveWallpaper.getLCD_WIDTH() / 2) - 23;
			formattedDate = df.format(cal.getTime());
			try {
				displayMatrix2 = writeLine(formattedDate, start, 33,
						displayMatrix2);
			} catch (Exception e) {
				Log.e("LCDLiveWallpaper", "exception", e);
			}
		}

		if (dispTime) {
			if (clockType.equalsIgnoreCase("Decimal")) {
				df = new SimpleDateFormat("HH:mm");
				start = (LCDLiveWallpaper.getLCD_WIDTH() / 2) - 14;
				formattedDate = df.format(cal.getTime());
				try {
					displayMatrix2 = writeLine(formattedDate, start, 25,
							displayMatrix2);
				} catch (Exception e) {
					Log.e("LCDLiveWallpaper", "exception", e);
				}
			} else if (clockType.equalsIgnoreCase("Binary")) {
				try {
					displayMatrix2 = drawBinaryWatch(displayMatrix2);
				} catch (Exception e) {
					Log.e("LCDLiveWallpaper", "exception", e);
				}
			}
		}

		return displayMatrix2;
	}

	boolean[][] setCharacter(char c, int x, int lineNo,
			boolean[][] displayMatrix) {
		int i = 0;

		for (int j = lineNo - 7; j <= lineNo + 1; j++)
			for (int k = x - 1; k <= x + 5; k++) {
				displayMatrix[k][j] = false;
			}

        int cHeight = 7;
        switch (c) {
		case '0':

			for (i = lineNo - 1; i > lineNo - cHeight + 1; i--) {
				displayMatrix[x][i] = true;
				displayMatrix[x + 4][i] = true;
			}

			for (i = x + 1; i < x + cWidth - 1; i++) {
				displayMatrix[i][lineNo] = true;
				displayMatrix[i][lineNo - 6] = true;
			}

			displayMatrix[x + 1][lineNo - 2] = true;
			displayMatrix[x + 2][lineNo - 3] = true;
			displayMatrix[x + 3][lineNo - 4] = true;

			break;

		case '1':
			displayMatrix[x + 1][lineNo] = true;
			displayMatrix[x + 3][lineNo] = true;

			for (i = lineNo; i >= lineNo - cHeight + 1; i--) {
				displayMatrix[x + 2][i] = true;
			}

			displayMatrix[x + 1][lineNo - cHeight + 2] = true;
			break;

		case '2':
			for (i = x; i < x + 5; i++)
				displayMatrix[i][lineNo] = true;

			for (i = x + 1; i < x + cWidth - 1; i++) {
				displayMatrix[i][lineNo - 6] = true;
			}

			displayMatrix[x + 1][lineNo - 1] = true;
			displayMatrix[x + 2][lineNo - 2] = true;
			displayMatrix[x + 3][lineNo - 3] = true;
			displayMatrix[x + 4][lineNo - 4] = true;
			displayMatrix[x + 4][lineNo - 5] = true;
			displayMatrix[x + 4][lineNo - 5] = true;
			displayMatrix[x][lineNo - 5] = true;

			break;

		case '3':
			for (i = x; i < x + 5; i++)
				displayMatrix[i][lineNo - 6] = true;

			for (i = x + 1; i < x + cWidth - 1; i++) {
				displayMatrix[i][lineNo] = true;
			}

			displayMatrix[x][lineNo - 1] = true;
			displayMatrix[x + 4][lineNo - 1] = true;
			displayMatrix[x + 4][lineNo - 2] = true;
			displayMatrix[x + 3][lineNo - 3] = true;
			displayMatrix[x + 2][lineNo - 4] = true;
			displayMatrix[x + 3][lineNo - 5] = true;

			break;

		case '4':
			for (i = x; i < x + 5; i++) {
				displayMatrix[i][lineNo - 2] = true;
			}

			for (i = lineNo; i >= lineNo - 6; i--) {
				displayMatrix[x + 3][i] = true;
			}

			displayMatrix[x][lineNo - 3] = true;
			displayMatrix[x + 1][lineNo - 4] = true;
			displayMatrix[x + 2][lineNo - 5] = true;

			break;

		case '5':
			for (i = x; i < x + 5; i++) {
				displayMatrix[i][lineNo - 6] = true;
			}

			for (i = x; i < x + 4; i++) {
				displayMatrix[i][lineNo - 4] = true;
			}

			for (i = x + 1; i < x + 4; i++) {
				displayMatrix[i][lineNo] = true;
			}

			for (i = lineNo - 1; i >= lineNo - 3; i--) {
				displayMatrix[x + 4][i] = true;
			}

			displayMatrix[x][lineNo - 5] = true;
			displayMatrix[x][lineNo - 1] = true;

			break;

		case '6':

			for (i = x + 1; i < x + 4; i++) {
				displayMatrix[i][lineNo] = true;
				displayMatrix[i][lineNo - 3] = true;
			}

			for (i = lineNo - 1; i >= lineNo - 4; i--) {
				displayMatrix[x][i] = true;
			}

			displayMatrix[x + 4][lineNo - 1] = true;
			displayMatrix[x + 4][lineNo - 2] = true;
			displayMatrix[x + 1][lineNo - 5] = true;
			displayMatrix[x + 2][lineNo - 6] = true;
			displayMatrix[x + 3][lineNo - 6] = true;

			break;

		case '7':
			for (i = x; i < x + 5; i++) {
				displayMatrix[i][lineNo - 6] = true;
			}

			for (i = lineNo - 4; i >= lineNo - 5; i--) {
				displayMatrix[x + 4][i] = true;
			}

			for (i = lineNo; i >= lineNo - 2; i--) {
				displayMatrix[x + 2][i] = true;
			}

			displayMatrix[x + 3][lineNo - 3] = true;

			break;

		case '8':
			for (i = x + 1; i < x + 4; i++) {
				displayMatrix[i][lineNo] = true;
				displayMatrix[i][lineNo - 3] = true;
				displayMatrix[i][lineNo - 6] = true;
			}

			for (i = lineNo - 1; i >= lineNo - 2; i--) {
				displayMatrix[x][i] = true;
				displayMatrix[x + 4][i] = true;
			}

			for (i = lineNo - 4; i >= lineNo - 5; i--) {
				displayMatrix[x][i] = true;
				displayMatrix[x + 4][i] = true;
			}

			break;

		case '9':
			for (i = x + 1; i < x + 4; i++) {
				displayMatrix[i][lineNo - 3] = true;
				displayMatrix[i][lineNo - 6] = true;
			}

			for (i = lineNo - 2; i >= lineNo - 5; i--) {
				displayMatrix[x + 4][i] = true;
			}

			displayMatrix[x][lineNo - 4] = true;
			displayMatrix[x][lineNo - 5] = true;

			displayMatrix[x + 1][lineNo] = true;
			displayMatrix[x + 2][lineNo] = true;
			displayMatrix[x + 3][lineNo - 1] = true;

			break;

		case ':':
			displayMatrix[x + 1][lineNo - 1] = true;
			displayMatrix[x + 1][lineNo - 2] = true;
			displayMatrix[x + 1][lineNo - 4] = true;
			displayMatrix[x + 1][lineNo - 5] = true;

			displayMatrix[x + 1][lineNo - 1] = true;
			displayMatrix[x + 1][lineNo - 2] = true;
			displayMatrix[x + 1][lineNo - 4] = true;
			displayMatrix[x + 1][lineNo - 5] = true;

			break;

		case '/':
			for (i = 0; i < 5; i++)
				displayMatrix[x + i][lineNo - i] = true;

			break;

		}
		return displayMatrix;
	}

	public boolean[][] writeLine(String s, int x, int lineNo,
			boolean[][] displayMatrix2)
	// lineNo e practic linia pe care va scrie, coordonata y cea mai de jos a
	// literei/numarului
	{
        boolean[][] displayMatrix = displayMatrix2;
		int i = 0;
		int spacing = 1;

		for (i = 0; i < s.length(); i++) {
			try {
				displayMatrix = setCharacter(s.charAt(i), x, lineNo,
                        displayMatrix);
			} catch (Exception ignored) {
			}
			x = x + cWidth + spacing;
		}

		return displayMatrix;
	}
}
