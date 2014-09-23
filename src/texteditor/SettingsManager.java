package texteditor;

public class SettingsManager {

	String GetSyntaxForFileExtension(String fileExtension) {
		switch (fileExtension) {
			case "bat":
				return "text/bat";
			case "cpp":
			case "c++":
			case "cc":
				return "text/cpp";
			case "cs":
				return "text/cs";
			case "css":
				return "text/css";
			case "json":
				return "text/json";
			case "tex":
			case "latex":
				return "text/latex";
			case "perl":
				return "text/perl";
			case "php":
				return "text/php";
			case "py":
				return "text/python";
			case "rb":
				return "text/ruby";
			case "sh":
				return "text/unix";
			case "sql":
				return "text/sql";
			case "tcl":
				return "text/tcl";
			case "vb":
				return "text/vb";
			case "xml":
				return "text/xml";
			default:
				return "text/plain";
		}
	}
}
