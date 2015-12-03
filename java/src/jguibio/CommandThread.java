package jguibio;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import com.google.gson.*;

public class CommandThread extends Thread
{
  private BufferedReader _in;
  private JsonParser _json = new JsonParser();
  private Kernel _kern;

  CommandThread(InputStream in, Kernel kern) throws IOException {
    super("CommandThread");
    _in = new BufferedReader(new InputStreamReader(in));
    _kern = kern;
  }

  public void run() {
    while (readCommand()) {}
  }

  private boolean readCommand() {
    try {
      String line = _in.readLine();
      if (line == null || line.length() == 0) return false;
      JsonElement root = _json.parse(line);
      if (!root.isJsonArray()) {
        Slog.it().severe("Not a JSON array: " + line);
        return false;
      }
      JsonArray a = root.getAsJsonArray();
      if (a.size() == 0) return false; 
      String[] params = new String[a.size()];
      for (int i = 0; i < a.size(); ++i) {
        if (!a.get(i).isJsonPrimitive()) return false;
        params[i] = a.get(i).getAsString();
      }
      return handleCommand(params);
    } catch (JsonSyntaxException ex) {
      Slog.severe(ex);
    } catch (IOException ex) {
      Slog.severe(ex);
    }
    return false;
  }

  private boolean handleCommand(String[] params) {
    if (params == null || params.length < 2) {
      Slog.it().severe("Command too short");
      return false;
    }
    String cmd = params[0];
    int windowIndex;
    try {
      windowIndex = Integer.parseInt(params[1]);
    } catch (NumberFormatException ex) {
      Slog.severe(ex);
      return false;
    }
    if (cmd.equalsIgnoreCase("dispose")) {
      _kern.async_dispose(windowIndex);
      return true;
    }
    if (cmd.equalsIgnoreCase("submit")) {
      if (params.length < 3) {
        Slog.it().severe("Command submit too short");
        return false;
      }
      String buttonText = params[2];
      List<String> values = new LinkedList<String>();
      for (int i = 3; i < params.length; ++i) {
        values.add(params[i]);
      }
      _kern.async_submit(windowIndex, buttonText, values);
      return true;
    }
    Slog.it().severe("Unknown command " + cmd);
    return false;
  }

}

