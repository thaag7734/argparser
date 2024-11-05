package xyz.vexy.vlib.argparser;

public class Flarg extends Arg {
  public boolean isSet = false;

  public Flarg(String name) {
    super(name);
  }

  public Flarg(String name, char alias) {
    super(name, alias);
  }

  public void set() {
    this.isSet = true;
  }
}
