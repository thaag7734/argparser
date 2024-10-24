package xyz.vexy.vlib.argparser;

public class Flag extends Arg {
  private Boolean value;

  public Flag(String name) {
    super(name);

    this.value = false;
  }

  public Flag(String name, String[] aliases, Boolean defaultValue) {
    super(name, aliases);

    this.value = defaultValue;
  }

  public Flag(String name, Boolean defaultValue) {
    super(name);

    this.value = defaultValue;
  }

  public Boolean isSet() {
    return this.value;
  }

  public void set() {
    this.value = true;
  }

  public void unset() {
    this.value = false;
  }
}
