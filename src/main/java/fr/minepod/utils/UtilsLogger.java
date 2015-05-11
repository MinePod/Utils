package fr.minepod.utils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class PrettyFormatter extends Formatter {
  private static final MessageFormat messageFormat = new MessageFormat(
      "[{0}|{1,date,h:mm:ss}]: {2} \n");

  public PrettyFormatter() {
    super();
  }

  @Override
  public String format(LogRecord record) {
    Object[] arguments = new Object[3];

    arguments[0] = record.getLevel();
    arguments[1] = new Date(record.getMillis());
    arguments[2] = record.getMessage();

    return messageFormat.format(arguments);
  }
}


public class UtilsLogger {
  public static Logger setLogger(String LogFile) throws SecurityException, IOException {
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    logger.setLevel(Level.INFO);

    PrettyFormatter formatter = new PrettyFormatter();
    if (LogFile != null) {
      FileHandler file = new FileHandler(LogFile);
      file.setFormatter(formatter);
      logger.addHandler(file);
    }
    
    ConsoleHandler console = new ConsoleHandler();
    console.setFormatter(formatter);
    logger.addHandler(console);

    return logger;
  }
}
