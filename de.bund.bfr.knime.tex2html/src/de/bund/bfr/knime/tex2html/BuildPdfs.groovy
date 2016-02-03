/*******************************************************************************
 * Copyright (c) 2016 Federal Institute for Risk Assessment (BfR), Germany
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

class BuildPdfs {

	static String FOLDER = "../GitHubPages/documents"
	static String PDF_LATEX = "\"C:/Program Files (x86)/User/miktex29/miktex/bin/pdflatex.exe\""

	static main(args) {
		for (File d : new File(FOLDER).listFiles())
			if (d.isDirectory())
				for (File f : d.listFiles())
					if (f.name.endsWith(".tex")) {
						println PDF_LATEX + " " + f.getName()

						ProcessBuilder builder = new ProcessBuilder(PDF_LATEX, f.getName())

						builder.directory(d)

						Process process = builder.start()

						process.getInputStream().eachLine() { println it }
						process.waitFor()
						println ""
					}
	}
}
