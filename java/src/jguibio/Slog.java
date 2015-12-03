package jguibio;

import java.util.logging.*;

public class Slog
{
  public static void logToRollingFiles() throws Exception
  {
    LogManager logManager = LogManager.getLogManager();
    logManager.reset();
    // log file max size 256K, 4 rolling files, append-on-open
    Handler fileHandler = new FileHandler("slog", 1 << 18, 4, true);
    fileHandler.setFormatter(new SimpleFormatter());
    Logger.getLogger("").addHandler(fileHandler);
  }

  public static void severe(String msg)
  {
    s_logger.log(Level.SEVERE, msg);
  }

  public static void severe(Throwable e)
  {
    s_logger.log(Level.SEVERE, "", e);
  }

  public static void warning(Throwable e)
  {
    s_logger.log(Level.WARNING, "", e);
  }

  public static Logger it()
  {
    return s_logger;
  }

  private static Logger s_logger = Logger.getLogger(Slog.class.getCanonicalName());
}
