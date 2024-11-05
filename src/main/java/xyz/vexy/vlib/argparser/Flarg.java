package xyz.vexy.vlib.argparser;

public class Flarg extends Arg {
  public boolean isSet = false;

  public Flarg(String name) {
    super(name);
  }

  public Flarg(String name, String[] aliases) {
    super(name, aliases);
  }

  public void set() {
    this.isSet = true;
  }
}
