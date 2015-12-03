package jguibio;

import java.awt.AWTEvent;
import java.awt.Toolkit;

public class Main
{
  public static void main(String[] args) throws Exception
  {
    Slog.logToRollingFiles();

    Kernel kern = new Kernel(new EventPrinter(System.out));
    new CommandThread(System.in, kern).start();
    System.setOut(System.err);

    long mask = AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK;
    Toolkit.getDefaultToolkit().addAWTEventListener(new UIListener(kern), mask);

    if (args.length > 0) {
      Class guiClass = Class.forName(args[0]);
      String[] guiArgs = java.util.Arrays.copyOfRange(args, 1, args.length);
      java.lang.reflect.Method meth = guiClass.getMethod("main", guiArgs.getClass());
      meth.invoke(null, new Object[]{guiArgs});
    } else {
      Slog.severe("No Java class specified in command line.");
    }
  }
}

