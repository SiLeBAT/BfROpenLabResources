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

class AllTex2Html {

	static FOLDER = "../GitHubPages/documents"
	// static FOLDER = "/Users/ruegenm/git/BfROpenLabResources/GitHubPages/documents"
	static URL = "https://github.com/SiLeBAT/BfROpenLabResources/raw/master/GitHubPages/documents"
	

	static main(args) {
		for (File d : new File(FOLDER).listFiles())
			if (d.isDirectory())
				for (File f : d.listFiles())
					if (f.name.endsWith(".tex")) { // && !f.name.endsWith("_DE.tex")) {				
						createFile(f, "${URL}/${d.name}");
					}
	}
	
	/*
	static createDirIfNecessary(String dirName) {
		File theDir = new File(dirName);
		if (!theDir.exists()){
    		theDir.mkdir();
		}
	}
	*/

	static createFile(File f, String url) {
		String name = f.name[0..-5]
		File out = new File("${name}.html")
		String heading = null
		List<String> text = []
		String image = null		

		for (def s : f.readLines()) {
			s = s.trim()
			
			if (s.startsWith("\\section") || s.startsWith("\\subsection")) {
				s = s.substring(s.indexOf("{") + 1, s.indexOf("}")).trim()
				if (!s.empty) heading = s
			} else if (s.startsWith("\\item")) {
				text.add(toHtml(s.replace("\\item", "").trim()))
			} else if (s.startsWith("\\includegraphics")) {
				image = s.substring(s.indexOf("{")+1, s.indexOf("}"))
			} else if (s.startsWith("\\end{frame}")) {
				if (((CharSequence)heading).isInteger()) out << "<h5>${heading}</h5>\n"
				else if (heading != null) out << "<h4>${heading}</h4>\n"
				out << "<ul>\n"
				text.each { t -> out << "<li>${t}</li>\n" }
				out << "</ul>\n"
				if (image != null) out << "<a href=\"${url}/${image}\"><img class=\"aligncenter size-full\" src=\"${url}/${image}\"/></a>\n"

				heading = null
				text = []
				image = null
			}
		}
	}

	static String toHtml(String s) {
		s = " " + s + " "
		s = s.replaceAll(/\$[^_\$]+_[^_\$]+\$/, { String o -> "${o.split('_')[0][1..-1]}<sub>${o.split('_')[1][0..-2]}</sub>" })
		s = s.replaceAll(/\\textbf\{[^}]*}/, { String o -> "<b>${o[8..-2]}</b>" })
		s = s.replaceAll(/\\textit\{[^}]*}/, { String o -> "<i>${o[8..-2]}</i>" })
		s = s.replaceAll(/\\url\{[^}]*}/, { String o ->	"<a href=\"${o[4..-2]}\" target=\"_blank\">${o.length() > 45 ? o[4..40] + "..." : o[4..-2]}</a>" })
		s = s.replaceAll(/.\$/, { String o -> o.charAt(0) == '\\' ? "\$" : o.charAt(0) })
		s = s.replace("{", "").replace("}", "")
		s = s.replace("\\%", "%");
		s = s.replace("\\_", "_");

		return s.trim()
	}
}
