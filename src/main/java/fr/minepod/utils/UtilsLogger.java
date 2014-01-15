package fr.minepod.utils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class PrettyFormatter extends Formatter {   
	private static final MessageFormat messageFormat = new MessageFormat("{0}[{1}|{2}|{3,date,h:mm:ss}]: {4} \n");

	public PrettyFormatter() {
		super();
	}

	@Override public String format(LogRecord record) {
		Object[] arguments = new Object[6];
		arguments[0] = record.getLoggerName();
		arguments[1] = record.getLevel();
		arguments[2] = Thread.currentThread().getName();
		arguments[3] = new Date(record.getMillis());
		arguments[4] = record.getMessage();
		return messageFormat.format(arguments);
	}	
}

public class UtilsLogger {
	public java.util.logging.Logger SetLogger(String LogFile) throws SecurityException, IOException {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.INFO);
		if(LogFile != null) {
			FileHandler file = new FileHandler(LogFile);
			PrettyFormatter formatter = new PrettyFormatter();
			file.setFormatter(formatter);
			logger.addHandler(file);
		}
		return logger;
	}
}
