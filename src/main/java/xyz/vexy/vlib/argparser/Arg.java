package xyz.vexy.vlib.argparser;

public class Arg {
  private String name;
  private final String[] aliases;

  public Arg(String name, String[] aliases) {
    this.name = name;
    this.aliases = aliases;
  }

  public Arg(String name) {
    this.name = name;
    this.aliases = null;
  }

  public String getName() {
    return this.name;
  }

  public String[] getAliases() {
    return this.aliases;
  }
}
