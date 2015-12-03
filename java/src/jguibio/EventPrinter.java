package jguibio;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JDialog;
import com.google.gson.*;

public class EventPrinter
{
  private PrintWriter _out;

  EventPrinter(PrintStream out) throws IOException {
    _out = new PrintWriter(out, true);
  }

  void windowOpened(int wid, Window w) {
    String title = null;
    JsonArray content = null;
    if (w instanceof JFrame) {
      JFrame jf = (JFrame)w;
      title = jf.getTitle();
      content = UIHelper.container2Json(jf.getContentPane());
    } else if (w instanceof JDialog) {
      JDialog jd = (JDialog)w;
      title = jd.getTitle();
      content = UIHelper.container2Json(jd.getContentPane());
    }
    JsonObject extract = new JsonObject();
    if (title != null) {
      extract.addProperty("title", title);
    }
    if (content != null && content.size() > 0) {
      extract.add("content", content);
    }
    JsonObject obj = new JsonObject();
    obj.addProperty("wid", wid);
    obj.addProperty("event", "opened");
    obj.add("extract", extract);
    _out.println(obj.toString());
  }

  void windowEvent(String event, int wid) {
    JsonObject obj = new JsonObject();
    obj.addProperty("wid", wid);
    obj.addProperty("event", event);
    _out.println(obj.toString());
  }

}

