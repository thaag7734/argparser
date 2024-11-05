package xyz.vexy.vlib.argparser;

public class Kwarg extends Arg {
  private String value;

  public Kwarg(String name, char alias) {
    super(name, alias);
  }

  public Kwarg(String name) {
    super(name);
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}

/*
 *
 * i = 0 : j = 1
 * i = 1 : j = 3
 * i = 2 : j = 6;
 */
