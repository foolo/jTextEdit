package texteditor;

import java.io.*;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {

	private final Date dat = new Date();

	@Override
	public synchronized String format(LogRecord record) {
		dat.setTime(record.getMillis());
		String source;
		if (record.getSourceClassName() != null) {
			source = record.getSourceClassName();
			if (record.getSourceMethodName() != null) {
				source += " " + record.getSourceMethodName();
			}
		}
		else {
			source = record.getLoggerName();
		}
		String message = formatMessage(record);
		String throwable = "";
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}

		String logText = record.getLevel().getLocalizedName() + ", " + message + " --- " + dat + " @ " + source;
		System.out.println(logText);

		if (!throwable.isEmpty()) {
			System.err.println(throwable);

			logText += "\nThrowable:" + throwable;
		}

		return logText + "\n";
	}
}
