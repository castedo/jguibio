package jguibio;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;

public class UIListener implements AWTEventListener
{
  private Kernel _kernel;

  public UIListener(Kernel k) {
    _kernel = k;
  }

  public void eventDispatched(AWTEvent event) {
    if (event instanceof WindowEvent) {
      WindowEvent we = (WindowEvent)event;
      switch (we.getID()) {
        case WindowEvent.WINDOW_OPENED:
          _kernel.windowOpened(we.getWindow());
          break;
        case WindowEvent.WINDOW_CLOSED:
          _kernel.windowClosed(we.getWindow());
          break;
      }
    } else if (event instanceof ComponentEvent) {
      ComponentEvent ce = (ComponentEvent)event;
      Component c = ce.getComponent();
      switch (ce.getID()) {
        case ComponentEvent.COMPONENT_SHOWN:
          if (c instanceof Window) {
            _kernel.windowShown((Window)c);
          }
          break;
        case ComponentEvent.COMPONENT_HIDDEN:
          if (c instanceof Window) {
            _kernel.windowHidden((Window)c);
          }
          break;
      }
    }
  }

}
