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

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

class Tex2Markdown {

	static String NAME = "datacollecting_1"
	static String TEX_FILE = "../GitHubPages/documents/foodchainlab_${NAME}/${NAME}.tex"
	static String URL = "https://github.com/SiLeBAT/BfROpenLabResources/raw/master/GitHubPages/documents/foodchainlab_${NAME}"

	static main(args) {
		def heading = null
		def text = []
		def image = null

		for (def s : new File(TEX_FILE).readLines()) {
			s = s.trim()

			if (s.startsWith("\\section") || s.startsWith("\\subsection")) {
				s = s.substring(s.indexOf("{") + 1, s.indexOf("}")).trim()
				if (!s.empty) heading = s
			} else if (s.startsWith("\\item")) {
				text.add(toHtml(s.replace("\\item", "").trim()))
			} else if (s.startsWith("\\includegraphics")) {
				image = s.substring(s.indexOf("{")+1, s.indexOf("}"))
			} else if (s.startsWith("\\end{frame}")) {
				if (heading.isInteger()) println "<h2 class=\"tutorial-heading\">Step ${heading}</h2>"
				else if (heading != null) println "<h2 class=\"tutorial-heading\">${heading}</h2>"
				println ""
				text.each { t -> println " * ${t}" }
				println ""
				if (image != null) println "<a href=\"${URL}/${image}\"><img class=\"aligncenter\" src=\"${URL}/${image}\"/></a>\n"

				heading = null
				text = []
				image = null
			}
		}
	}

	static String toHtml(String s) {
		s = " " + s + " "
		s = s.replaceAll(/\$[^_\$]+_[^_\$]+\$/, {
			def i = it.indexOf("_")
			it.substring(1, i) + "<sub>" + it.substring(i+1, it.length()-1) + "</sub>"
		})
		s = s.replaceAll(/\\textbf\{[^}]*}/,
				{ "**" + it.replace("\\textbf{","").replace("}", "") + "**" })
		s = s.replaceAll(/\\textit\{[^}]*}/,
				{ "*" + it.replace("\\textit{","").replace("}", "") + "*" })
		s = s.replaceAll(/\\url\{[^}]*}/, {
			def url = it.replace("\\url{","").replace("}", "")
			def shortUrl = url.length() > 40 ? url.substring(0, 37) + "..." : url
			"[${shortUrl}](${url})"
		})
		s = s.replaceAll(/.\$/, {  it.charAt(0) == '\\' ? "\$" : it.charAt(0) })
		s = s.replace("{", "").replace("}", "")
		s = s.replace("\\%", "%");
		s = s.replace("\\_", "_");
		s = s.trim()
	}
}
