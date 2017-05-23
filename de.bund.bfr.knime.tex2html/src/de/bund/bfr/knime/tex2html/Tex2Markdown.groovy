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

	static String NAME = "overview"
	static String TEX_FILE = "../GitHubPages/documents/foodchainlab_${NAME}/${NAME}.tex"
	static String URL = "https://github.com/SiLeBAT/BfROpenLabResources/raw/master/GitHubPages/documents/foodchainlab_${NAME}"

	static main(args) {
		String heading = null
		List<String> text = []
		String image = null

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
				if (((CharSequence)heading).isInteger()) println "{% include heading.html text=\"Step ${heading}\" %}"
				else if (heading != null) println "{% include heading.html text=\"${heading}\" %}"
				println ""
				text.each { t -> println " * ${t}" }
				println ""
				if (image != null) println "{% include screenshot.html img=\"${URL}/${image}\" %}\n"

				heading = null
				text = []
				image = null
			}
		}
	}

	static String toHtml(String s) {
		s = " " + s + " "
		s = s.replaceAll(/\$[^_\$]+_[^_\$]+\$/, { String o -> "${o.split('_')[0][1..-1]}<sub>${o.split('_')[1][0..-2]}</sub>"})
		s = s.replaceAll(/\\textbf\{[^}]*}/, { String o -> "**${o[8..-2]}**" })
		s = s.replaceAll(/\\textit\{[^}]*}/, { String o -> "*${o[8..-2]}*" })
		s = s.replaceAll(/\\url\{[^}]*}/, { String o ->	"[${o.length() > 45 ? o[4..40] + "..." : o[4..-2]}](${o[4..-2]})"})
		s = s.replaceAll(/.\$/, { String o -> o.charAt(0) == '\\' ? "\$" : o.charAt(0) })
		s = s.replace("{", "").replace("}", "")
		s = s.replace("\\%", "%");
		s = s.replace("\\_", "_");
		
		return s.trim()		
	}
}
