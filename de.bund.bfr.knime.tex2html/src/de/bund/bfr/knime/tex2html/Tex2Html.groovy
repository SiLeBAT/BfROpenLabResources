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
		def data = [:]
		def currentText = []
		def currentImage = null

		for (def s : new File("${LOCAL_FOLDER}/${TEX_FILE}").readLines()) {
			s = s.trim()

			if (s.startsWith("\\includegraphics")) {
				currentImage = s.substring(s.indexOf("{")+1, s.indexOf("}"))
			} else if (s.startsWith("\\item")) {
				currentText.add(toHtml(s.replace("\\item", "").trim()))
			} else if (currentImage != null && !currentText.empty) {
				data.put(currentImage, currentText)
				currentImage = null
				currentText = []
			}
		}

		data.each { image, text ->
			println "<ul>"
			text.each { t -> println "<li>${t}</li>" }
			println "</ul>"
			println "<img class=\"aligncenter size-full\" src=\"${URL}/${image}\"/>"
		}
	}

	static String toHtml(String s) {
		s = s.replace("\$", "")
		s = s.replaceAll(/\\textbf\{[^}]*}/,
				{ "<b>" + it.replace("\\textbf{","").replace("}", "") + "</b>" })
		s = s.replaceAll(/\\textit\{[^}]*}/,
				{ "<i>" + it.replace("\\textit{","").replace("}", "") + "</i>" })
		s = s.replaceAll(/\\url\{[^}]*}/, {
			def url = it.replace("\\url{","").replace("}", "")
			"<a href=\"${url}\" target=\"_blank\">${url}</a>"
		})
	}
}