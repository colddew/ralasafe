/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class IOUtil {
	public static void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	public static void close(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}

	public static void write(String file, String content) throws IOException {
		OutputStreamWriter bos = new OutputStreamWriter(new FileOutputStream(
				file), "UTF-8");
		bos.write(content);
		bos.close();
	}

	public static String read(String file) throws IOException {
		StringBuffer content = new StringBuffer();
		char[] buff = new char[1024];
		InputStreamReader bis = new InputStreamReader(
				new FileInputStream(file), "UTF-8");

		int read = bis.read(buff);
		while (read != -1) {
			content.append(new String(buff, 0, read));
			read = bis.read(buff);
		}

		bis.close();

		return content.toString();
	}
}
