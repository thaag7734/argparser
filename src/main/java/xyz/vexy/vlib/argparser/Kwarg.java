package xyz.vexy.vlib.argparser;

public class Kwarg extends Arg {
  private final String[] subArgs;

  public Kwarg(String name, String[] aliases, int numSubArgs) {
    super(name, aliases);

    this.subArgs = new String[numSubArgs];
  }

  public Kwarg(String name, int numSubArgs) {
    super(name);

    this.subArgs = new String[numSubArgs];
  }

  public String[] getSubArgs() {
    return this.subArgs;
  }

  public int desiredSubArgs() {
    return this.subArgs.length;
  }

  public void addSubArg(String subArg) {
    for (int i = 0; i < this.subArgs.length; i++) {
      if (this.subArgs[i] == null) {
        this.subArgs[i] = subArg;
        return;

        // if the final subarg to this kwarg is already populated
      } else if (i == this.subArgs.length - 1) {
        throw new ArrayIndexOutOfBoundsException(
            "Expected argument or end of command but got '" + subArg + "'");
      }
    }
  }
}
