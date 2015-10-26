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

	static String NAME = "tracing"
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
				if (isNumber(heading)) println "<h5>${heading}</h5>"
				else if (heading != null) println "<h4>${heading}</h4>"
				println "<ul>"
				text.each { t -> println "<li>${t}</li>" }
				println "</ul>"
				if (image != null) println "<a href=\"${URL}/${image}\"><img class=\"aligncenter size-full\" src=\"${URL}/${image}\"/></a>"

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
				{ "<b>" + it.replace("\\textbf{","").replace("}", "") + "</b>" })
		s = s.replaceAll(/\\textit\{[^}]*}/,
				{ "<i>" + it.replace("\\textit{","").replace("}", "") + "</i>" })
		s = s.replaceAll(/\\texttt\{[^}]*}/,
			{ "<code>" + it.replace("\\texttt{","").replace("}", "") + "</code>" })
		s = s.replaceAll(/\\url\{[^}]*}/, {
			def url = it.replace("\\url{","").replace("}", "")
			"<a href=\"${url}\" target=\"_blank\">${url}</a>"
		})
		s = s.replaceAll(/.\$/, {  it.charAt(0) == '\\' ? "\$" : it.charAt(0) })
		s = s.replace("{", "").replace("}", "")
		s = s.replace("\\%", "%");
		s = s.replace("\\textgreater", "&gt;");
		s = s.trim()
	}

	static boolean isNumber(String s) {		
		try {
			Integer.parseInt(s)
			return true
		} catch (Exception e) {
			return false
		}
	}
}
