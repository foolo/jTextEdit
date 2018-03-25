package texteditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class SyntaxDetector {

	static String detectSyntax(org.fife.ui.rsyntaxtextarea.TextEditorPane textEditorPane) {
		String syntaxFromFilename = GetSyntaxForFilename(textEditorPane.getFileName());
		if (syntaxFromFilename != null) {
			return syntaxFromFilename;
		}
		String syntaxFromContent = GetSyntaxFromContent(textEditorPane.getText());
		if (syntaxFromContent != null) {
			return syntaxFromContent;
		}
		return SyntaxConstants.SYNTAX_STYLE_NONE;
	}

	static String getFirstLine(String s) {
		Matcher matcher = Pattern.compile("\\R").matcher(s);
		if (matcher.find()) {
			return s.substring(0, matcher.start());
		}
		return s;
	}

	static String findParser(String s) {
		Map<String, String> map = new HashMap<>();
		map.put("bash", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
		map.put("sh", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
		map.put("python", SyntaxConstants.SYNTAX_STYLE_PYTHON);
		map.put("ruby", SyntaxConstants.SYNTAX_STYLE_RUBY);
		String pattern = "(\\b" + String.join("\\b)|(\\b", map.keySet()) + "\\b)";
		Matcher matcher = Pattern.compile(pattern).matcher(s);
		if (matcher.find()) {
			String parser = matcher.group();
			return map.get(parser);
		}
		return null;
	}

	static String GetSyntaxFromContent(String text) {
		String firstLine = getFirstLine(text);
		System.out.println("texteditor.SyntaxDetector.GetSyntaxFromContent() " + firstLine);
		if (firstLine.startsWith("#!")) {
			String parser = findParser(firstLine);
			if (parser != null) {
				return parser;
			}
		}
		return null;
	}

	static String GetSyntaxForFilename(String filename) {
		switch (filename.toLowerCase()) {
			case "makefile":
				return SyntaxConstants.SYNTAX_STYLE_MAKEFILE;
			case "rakefile":
				return SyntaxConstants.SYNTAX_STYLE_RUBY;
			case "dockerfile":
				return SyntaxConstants.SYNTAX_STYLE_DOCKERFILE;
			case "hosts":
				return SyntaxConstants.SYNTAX_STYLE_HOSTS;
			case "sconstruct":
			case "sconscript":
				return SyntaxConstants.SYNTAX_STYLE_PYTHON;
			default:
				break;
		}

		String fileExtension = FilenameUtils.getExtension(filename);
		switch (fileExtension.toLowerCase()) {
			case "asm":
				return SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86;
			case "bat":
				return SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH;
			case "c":
				return SyntaxConstants.SYNTAX_STYLE_C;
			case "cpp":
			case "c++":
			case "cc":
			case "h":
			case "hpp":
				return SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
			case "clj":
				return SyntaxConstants.SYNTAX_STYLE_CLOJURE;
			case "cs":
				return SyntaxConstants.SYNTAX_STYLE_CSHARP;
			case "css":
				return SyntaxConstants.SYNTAX_STYLE_CSS;
			case "d":
				return SyntaxConstants.SYNTAX_STYLE_D;
			case "dtd":
				return SyntaxConstants.SYNTAX_STYLE_DTD;
			case "dart":
				return SyntaxConstants.SYNTAX_STYLE_DART;
			case "f":
			case "for":
			case "f90":
			case "f95":
				return SyntaxConstants.SYNTAX_STYLE_FORTRAN;
			case "groovy":
				return SyntaxConstants.SYNTAX_STYLE_GROOVY;
			case "htaccess":
				return SyntaxConstants.SYNTAX_STYLE_HTACCESS;
			case "html":
			case "htm":
				return SyntaxConstants.SYNTAX_STYLE_HTML;
			case "ini":
				return SyntaxConstants.SYNTAX_STYLE_INI;
			case "java":
				return SyntaxConstants.SYNTAX_STYLE_JAVA;
			case "js":
				return SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
			case "json":
				return SyntaxConstants.SYNTAX_STYLE_JSON;
			case "jshintrc":
				return SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS;
			case "jsp":
				return SyntaxConstants.SYNTAX_STYLE_JSP;
			case "tex":
			case "latex":
				return SyntaxConstants.SYNTAX_STYLE_LATEX;
			case "less":
				return SyntaxConstants.SYNTAX_STYLE_LESS;
			case "lisp":
				return SyntaxConstants.SYNTAX_STYLE_LISP;
			case "lua":
				return SyntaxConstants.SYNTAX_STYLE_LUA;
			case "mxml":
				return SyntaxConstants.SYNTAX_STYLE_MXML;
			case "nsi":
			case "nsh":
				return SyntaxConstants.SYNTAX_STYLE_NSIS;
			case "pas":
				return SyntaxConstants.SYNTAX_STYLE_DELPHI;
			case "perl":
				return SyntaxConstants.SYNTAX_STYLE_PERL;
			case "php":
				return SyntaxConstants.SYNTAX_STYLE_PHP;
			case "properties":
				return SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE;
			case "py":
				return SyntaxConstants.SYNTAX_STYLE_PYTHON;
			case "rb":
				return SyntaxConstants.SYNTAX_STYLE_RUBY;
			case "scala":
				return SyntaxConstants.SYNTAX_STYLE_SCALA;
			case "sh":
				return SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL;
			case "sql":
				return SyntaxConstants.SYNTAX_STYLE_SQL;
			case "tcl":
				return SyntaxConstants.SYNTAX_STYLE_TCL;
			case "ts":
				return SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT;
			case "vb":
				return SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC;
			case "xml":
				return SyntaxConstants.SYNTAX_STYLE_XML;
			case "yml":
				return SyntaxConstants.SYNTAX_STYLE_YAML;
			default:
				return null;
		}
	}

	static ArrayList<String> getAvailableSyntaxes() {
		ArrayList<String> result = new ArrayList<>();
		result.add(SyntaxConstants.SYNTAX_STYLE_NONE);
		result.add(SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);
		result.add(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
		result.add(SyntaxConstants.SYNTAX_STYLE_BBCODE);
		result.add(SyntaxConstants.SYNTAX_STYLE_C);
		result.add(SyntaxConstants.SYNTAX_STYLE_CLOJURE);
		result.add(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
		result.add(SyntaxConstants.SYNTAX_STYLE_CSHARP);
		result.add(SyntaxConstants.SYNTAX_STYLE_CSS);
		result.add(SyntaxConstants.SYNTAX_STYLE_D);
		result.add(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
		result.add(SyntaxConstants.SYNTAX_STYLE_DART);
		result.add(SyntaxConstants.SYNTAX_STYLE_DELPHI);
		result.add(SyntaxConstants.SYNTAX_STYLE_DTD);
		result.add(SyntaxConstants.SYNTAX_STYLE_FORTRAN);
		result.add(SyntaxConstants.SYNTAX_STYLE_GROOVY);
		result.add(SyntaxConstants.SYNTAX_STYLE_HOSTS);
		result.add(SyntaxConstants.SYNTAX_STYLE_HTACCESS);
		result.add(SyntaxConstants.SYNTAX_STYLE_HTML);
		result.add(SyntaxConstants.SYNTAX_STYLE_INI);
		result.add(SyntaxConstants.SYNTAX_STYLE_JAVA);
		result.add(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		result.add(SyntaxConstants.SYNTAX_STYLE_JSON);
		result.add(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
		result.add(SyntaxConstants.SYNTAX_STYLE_JSP);
		result.add(SyntaxConstants.SYNTAX_STYLE_LATEX);
		result.add(SyntaxConstants.SYNTAX_STYLE_LESS);
		result.add(SyntaxConstants.SYNTAX_STYLE_LISP);
		result.add(SyntaxConstants.SYNTAX_STYLE_LUA);
		result.add(SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
		result.add(SyntaxConstants.SYNTAX_STYLE_MXML);
		result.add(SyntaxConstants.SYNTAX_STYLE_NSIS);
		result.add(SyntaxConstants.SYNTAX_STYLE_PERL);
		result.add(SyntaxConstants.SYNTAX_STYLE_PHP);
		result.add(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
		result.add(SyntaxConstants.SYNTAX_STYLE_PYTHON);
		result.add(SyntaxConstants.SYNTAX_STYLE_RUBY);
		result.add(SyntaxConstants.SYNTAX_STYLE_SAS);
		result.add(SyntaxConstants.SYNTAX_STYLE_SCALA);
		result.add(SyntaxConstants.SYNTAX_STYLE_SQL);
		result.add(SyntaxConstants.SYNTAX_STYLE_TCL);
		result.add(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
		result.add(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
		result.add(SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
		result.add(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
		result.add(SyntaxConstants.SYNTAX_STYLE_XML);
		result.add(SyntaxConstants.SYNTAX_STYLE_YAML);
		return result;
	}
}
