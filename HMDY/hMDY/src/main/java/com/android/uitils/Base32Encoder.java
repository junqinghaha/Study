package com.android.uitils;

public class Base32Encoder {

	private static final char[] encodeTable = {

	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3',
			'4', '5', '6', '7' };

	private static final byte[] decodeTable;
	static {
		decodeTable = new byte[128];
		for (int i = 0; i < decodeTable.length; i++) {
			decodeTable[i] = (byte) 0xFF;
		}

		for (int i = 0; i < encodeTable.length; i++) {

			decodeTable[(int) encodeTable[i]] = (byte) i;

			if (i < 24) {

				decodeTable[(int) Character.toLowerCase(encodeTable[i])] = (byte) i;
			}
		}
	}

	public static String encode(byte[] data) {

		char[] chars = new char[((data.length * 8) / 5)

		+ ((data.length % 5) != 0 ? 1 : 0)];
		for (int i = 0, j = 0, index = 0; i < chars.length; i++) {

			if (index > 3) {
				int b = data[j] & (0xFF >> index);

				index = (index + 5) % 8;

				b <<= index;

				if (j < data.length - 1) {

					b |= (data[j + 1] & 0xFF) >> (8 - index);
				}

				chars[i] = encodeTable[b];

				j++;
			}

			else {

				chars[i] = encodeTable[((data[j] >> (8 - (index + 5))) & 0x1F)];

				index = (index + 5) % 8;

				if (index == 0) {

					j++;
				}
			}
		}

		return new String(chars);

	}

	public static byte[] decode(String s) throws Exception

	{

		char[] stringData = s.toCharArray();

		byte[] data = new byte[(stringData.length * 5) / 8];

		for (int i = 0, j = 0, index = 0; i < stringData.length; i++) {

			int val;

			try {
				val = decodeTable[stringData[i]];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new Exception("Illegal character");
			}

			if (val == 0xFF) {
				throw new Exception("Illegal character");
			}

			if (index <= 3) {
				index = (index + 5) % 8;

				if (index == 0) {
					data[j++] |= val;
				} else {
					data[j] |= val << (8 - index);
				}
			}

			else {

				index = (index + 5) % 8;
				data[j++] |= (val >> index);

				if (j < data.length) {
					data[j] |= val << (8 - index);
				}
			}
		}

		return data;

	}
}