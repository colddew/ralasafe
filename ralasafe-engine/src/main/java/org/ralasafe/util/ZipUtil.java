/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil

{
	public static void zip(String zipFileName, String inputFile) throws Exception {

		zip(zipFileName, new File(inputFile));

	}

	public static void zip(String zipFileName, File inputFile) throws Exception {

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));

		zip(out, inputFile, "");

		out.close();

	}

	public static void unzip(String zipFileName, String outputDirectory)
			throws Exception {

		ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));

		ZipEntry z;

		while ((z = in.getNextEntry()) != null)

		{

			System.out.println("unziping " + z.getName());

			if (z.isDirectory())

			{

				String name = z.getName();

				name = name.substring(0, name.length() - 1);

				File f = new File(outputDirectory + File.separator + name);

				f.mkdir();

				System.out.println("mkdir " + outputDirectory + File.separator
						+ name);

			}

			else {

				File f = new File(outputDirectory + File.separator
						+ z.getName());

				f.createNewFile();

				FileOutputStream out = new FileOutputStream(f);

				int b;

				while ((b = in.read()) != -1)

					out.write(b);

				out.close();

			}

		}

		in.close();

	}

	public static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory())

		{

			File[] fl = f.listFiles();

			out.putNextEntry(new ZipEntry(base + "/"));

			base = base.length() == 0 ? "" : base + "/";

			for (int i = 0; i < fl.length; i++)

			{

				zip(out, fl[i], base + fl[i].getName());

			}

		}

		else

		{

			out.putNextEntry(new ZipEntry(base));

			FileInputStream in = new FileInputStream(f);

			int b;
			while ((b = in.read()) != -1)
				out.write(b);
			in.close();
		}
	}
}