package jguibio;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.LinkedList;
import com.google.gson.*;

public class UIHelper
{
  static JsonArray container2Json(Container c)
  {
    JsonArray ret = new JsonArray();
    if (c.isShowing()) {
      String text = null;
      if (c instanceof JLabel) {
        text = ((JLabel)c).getText();
      }
      if (c instanceof JTextComponent) {
        javax.swing.text.Document doc = ((JTextComponent)c).getDocument();
        try {
          text = doc.getText(0, doc.getLength());
        } catch (javax.swing.text.BadLocationException ignore) {}
      }
      if (text != null) {
        if (text.length() > 0) {
          ret.add(new JsonPrimitive(text));
        }
      } else {
        if (c.getComponents().length == 0) {
          if (c instanceof JPanel) {
            JsonObject img = new JsonObject();
            img.add("png", new JsonPrimitive(imageAsBase64Png(c)));
            ret.add(img);
          } else if (c instanceof JButton) {
            String txt = ((JButton)c).getText();
            if (txt != null && txt.length() > 0) {
              JsonObject btn = new JsonObject();
              btn.add("button", new JsonPrimitive(txt));
              ret.add(btn);
            }
          }
        } else {
          for (Component sub : c.getComponents()) {
            if (sub instanceof Container) {
              ret.addAll(container2Json((Container)sub));
            }
          }
        }
      }
    }
    return ret;
  }

  static String imageAsBase64Png(Component c)
  {
    if (c.getWidth() <= 0 || c.getHeight() <= 0) {
      return "";
    }
    BufferedImage img = new BufferedImage(c.getWidth(), c.getHeight(),
                                          BufferedImage.TYPE_INT_RGB);
    c.paint(img.getGraphics());
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    try {
      ImageIO.write(img, "png", buf);
    } catch (java.io.IOException ex) {
      return ex.toString();
    }
    return javax.xml.bind.DatatypeConverter.printBase64Binary(buf.toByteArray());
  }

  static List<JTextField> getTextFields(Container c)
  {
    List<JTextField> ret = new LinkedList<JTextField>();
    for (Component sub : c.getComponents()) {
      if (sub instanceof JTextField) {
        ret.add((JTextField)sub);
      } else if (sub instanceof Container) {
        ret.addAll(getTextFields((Container)sub));
      }
    }
    return ret;
  }

  static JButton getButton(Container c, String text)
  {
    if (c.isShowing()) {
      if (c.getComponents().length == 0) {
        if (c instanceof JButton) {
          JButton b = (JButton)c;
          String btxt = b.getText();
          if (btxt != null && btxt.equalsIgnoreCase(text)) { return b; }
        }
      } else {
        for (Component sub : c.getComponents()) {
          if (sub instanceof Container) {
            JButton b = getButton((Container)sub, text);
            if (b != null) { return b; }
          }
        }
      }
    }
    return null;
  }
}

