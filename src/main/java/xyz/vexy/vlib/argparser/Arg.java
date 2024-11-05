package xyz.vexy.vlib.argparser;

public class Arg {
  private String name;
  private final char alias;

  public Arg(String name, char alias) {
    if (alias == '\u0000') {
      throw new IllegalArgumentException("Illegal alias for arg " + name + ": '\u0000'");
    } else if (alias == '-') {
      throw new IllegalArgumentException("Illegal alias for " + name + ": '-'");
    }

    if (name.startsWith("-")) {
      throw new IllegalArgumentException("Illegal name \"" + name + "\": name cannot start with \"-\"");
    }
    this.name = name;
    this.alias = alias;
  }

  public Arg(String name) {
    if (name.startsWith("-")) {
      throw new IllegalArgumentException("Illegal name \"" + name + "\": name cannot start with \"-\"");
    }

    this.name = name;
    this.alias = '\u0000';
  }

  public String getName() {
    return this.name;
  }

  public char getAlias() {
    return this.alias;
  }

  public boolean hasAlias(char alias) {
    if (this.alias == '\u0000')
      return false;

    if (alias == this.alias) {
      return true;
    }

    return false;
  }
}
