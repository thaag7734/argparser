package xyz.vexy.vlib.argparser;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {
  public final Map<String, Kwarg> kwargs;
  public final Map<String, Flag> flags;
  public final String[] posArgs;

  public ArgParser() {
    this.kwargs = new HashMap<>();
    this.flags = new HashMap<>();
    this.posArgs = null;
  }

  public ArgParser(int numPosArgs) {
    this.posArgs = new String[numPosArgs];
    this.kwargs = new HashMap<>();
    this.flags = new HashMap<>();
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

  public void registerKwarg(String name, int numSubArgs, String[] aliases) {
    this.kwargs.put(name, new Kwarg(name, aliases, numSubArgs));
  }

  public void registerKwarg(String name, int numSubArgs) {
    this.kwargs.put(name, new Kwarg(name, numSubArgs));
  }
}
