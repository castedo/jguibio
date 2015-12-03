package jguibio;

import java.util.List;
import java.util.ArrayList;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Kernel
{
  List<Window> _windows = new ArrayList<Window>();
  JDialog _modalDialog;
  EventPrinter _printer;

  Kernel(EventPrinter ep) {
    _modalDialog = null;
    _printer = ep;
  }

  public void async_dispose(int windowIndex)
  {
    DisposeCommand cmd = new DisposeCommand();
    cmd.windowIndex = windowIndex;
    java.awt.EventQueue.invokeLater(cmd);
  }

  public void async_submit(int windowIndex, String button, List<String> values)
  {
    SubmitCommand cmd = new SubmitCommand();
    cmd.windowIndex = windowIndex;
    cmd.buttonText = button;
    cmd.values = values;
    java.awt.EventQueue.invokeLater(cmd);
  }

  private class DisposeCommand implements Runnable
  {
    int windowIndex;
    public void run() { dispose(windowIndex); }
  }

  private class SubmitCommand implements Runnable
  {
    int windowIndex; String buttonText; List<String> values;
    public void run() { submit(windowIndex, buttonText, values); }
  }

  synchronized
  private void dispose(int windowIndex) {
    Window w = null;
    if (windowIndex >= 0 && windowIndex < _windows.size()) {
      w = _windows.get(windowIndex);
      w.dispose();
    } else {
      Slog.it().severe("Window " + windowIndex + " invalid.");
    }
  }

  synchronized
  private void submit(int windowIndex, String buttonText, List<String> values) {
    Window w = null;
    if (windowIndex >= 0 && windowIndex < _windows.size()) {
      w = _windows.get(windowIndex);
    }
    if (w == null) {
      Slog.it().severe("Window " + windowIndex + " invalid.");
      return;
    }
    if (_modalDialog != null && _modalDialog != w) {
      _modalDialog.dispose();
      _modalDialog = null;
    }
    List<JTextField> fields = UIHelper.getTextFields(w);
    if (fields.size() < values.size()) {
      Slog.it().severe("Only " + fields.size() + " for " + values.size() + " fields");
      return;
    }
    for (int i = 0; i < values.size(); ++i) {
      fields.get(i).setText(values.get(i));
    }
    JButton but = UIHelper.getButton(w, buttonText);
    if (null == but) {
      Slog.it().severe("No button " + buttonText);
      return;
    }
    but.setEnabled(true);
    but.doClick();
  }
  
  synchronized
  public void windowOpened(Window w)
  {
    if (!_windows.contains(w)) {
      _windows.add(w);
      _printer.windowOpened(_windows.indexOf(w), w);
    }
    if (w instanceof JDialog) {
      JDialog jd = (JDialog)w;
      if (jd.isModal()) {
        _modalDialog = jd;
      }
    }
  }

  synchronized
  public void windowClosed(Window w)
  {
    if (_windows.contains(w)) {
      _printer.windowEvent("closed", _windows.indexOf(w));
    }
  }

  synchronized
  public void windowShown(Window w)
  {
    if (_windows.contains(w)) {
      _printer.windowEvent("shown", _windows.indexOf(w));
    }
  }

  synchronized
  public void windowHidden(Window w)
  {
    if (_windows.contains(w)) {
      _printer.windowEvent("hidden", _windows.indexOf(w));
    }
  }

}

