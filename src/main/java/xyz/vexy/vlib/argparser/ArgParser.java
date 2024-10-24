package xyz.vexy.vlib.argparser;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {
  public Map<String, Kwarg> kwargs;
  public Map<String, Flag> flags;
  public String[] posArgs;

  public ArgParser() {
    this.kwargs = new HashMap<>();
    this.flags = new HashMap<>();
    this.posArgs = null;
  }

  public ArgParser(int numPosArgs) {
    this.posArgs = new String[numPosArgs];
  }

  public void registerFlag(String name) {
    this.flags.put(name, new Flag(name));
  }

  public void registerFlag(String name, String[] aliases, Boolean defaultValue) {
    this.flags.put(name, new Flag(name, defaultValue));
  }

  public void registerFlag(String name, Boolean defaultValue) {
    this.flags.put(name, new Flag(name, defaultValue));
  }
}
