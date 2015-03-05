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

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

class Tex2Html {

	static String LOCAL_FOLDER = "../GitHubPages/documents/foodchainlab_installation"
	static String URL = "https://github.com/SiLeBAT/BfROpenLabResources/raw/master/GitHubPages/documents/foodchainlab_installation"
	static String TEX_FILE = "installation.tex"

	static main(args) {
		def itemParsed = false

		for (def s : new File("${LOCAL_FOLDER}/${TEX_FILE}").readLines()) {
			s = s.trim()

			if (s.startsWith("\\includegraphics")) {
				def image = s.substring(s.indexOf("{")+1, s.indexOf("}"))

				if (itemParsed) {
					println "</ul>"
				}

				println "<img class=\"aligncenter size-full\" src=\"${URL}/${image}\"/>"
				itemParsed = false
			} else if (s.startsWith("\\item")) {
				def text = s.replace("\\item", "").trim()

				if (!itemParsed) {
					println "<ul>"
				}

				println "<li>${text}</li>"
				itemParsed = true
			}
		}
	}
}
