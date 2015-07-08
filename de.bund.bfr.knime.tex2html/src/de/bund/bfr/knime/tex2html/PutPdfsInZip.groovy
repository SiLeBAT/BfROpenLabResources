/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.tex2html

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class PutPdfsInZip {

	static String FOLDER = "../GitHubPages/documents"
	static String FILE_EN = "tutorials.zip"
	static String FILE_DE = "tutorials_DE.zip"

	static main(args) {
		def folder = new File(FOLDER)
		def zipEN = new ZipOutputStream(new FileOutputStream("${FOLDER}/${FILE_EN}"))
		def zipDE = new ZipOutputStream(new FileOutputStream("${FOLDER}/${FILE_DE}"))

		for (File d : folder.listFiles())
			if (d.isDirectory())
				for (File f : d.listFiles())
					if (f.name.endsWith(".pdf")) {
						def zip = f.name.endsWith("_DE.pdf") ? zipDE : zipEN

						zip.putNextEntry(new ZipEntry(f.name))
						zip << new FileInputStream(f)
						zip.closeEntry()
					}		

		zipEN.close()
		zipDE.close()
	}
}
