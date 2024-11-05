package xyz.vexy.vlib.argparser;

public class Kwarg extends Arg {
  private String value;

  public Kwarg(String name, String[] aliases) {
    super(name, aliases);
  }

  public Kwarg(String name) {
    super(name);
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue(String value) {
    return this.value;
  }
}
