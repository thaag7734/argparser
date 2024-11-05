package xyz.vexy.vlib.argparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArgParser {
  public final ArrayList<Arg> args;
  public final String[] pargs;

  public ArgParser() {
    this.args = new ArrayList<>();
    this.pargs = null;
  }

  public ArgParser(int numPosArgs) {
    this.args = new ArrayList<>();
    this.pargs = new String[numPosArgs];
  }

  /* TODO throw an error if the arg's name/aliases are already registered */
  public void registerArg(Arg arg) {
    this.args.add(arg);
  }

  public void registerFlag(String name, String[] aliases) {
    this.registerArg(new Flarg(name, aliases));
  }

  public void registerFlag(String name) {
    this.registerArg(new Flarg(name));
  }

  public void registerKwarg(String name, String[] aliases) {
    this.registerArg(new Kwarg(name, aliases));
  }

  public void registerKwarg(String name) {
    this.registerArg(new Kwarg(name));
  }

  public Arg matchArg(String name) throws InvalidArgumentException {
    if (name.length() < 2 || !name.startsWith("-"))
      throw new InvalidArgumentException("Argument " + name + " is invalid");

    String strippedName;

    if (name.startsWith("--")) {
      strippedName = name.substring(2);
    } else if (name.startsWith("-")) {
      strippedName = name.substring(1);
    } else {
      /* this should only happen once we have parsed pargs */
      throw new InvalidArgumentException("Argument " + name + " is invalid");
    }

    for (Arg arg : this.args) {
      if (arg.getName() == strippedName || arg.hasAlias(strippedName)) {
        return arg;
      }
    }

    throw new InvalidArgumentException("Argument " + name + " is invalid");
  }

  public void parse(String[] args) throws InvalidArgumentException {
    int startIdx = 0;

    /* positional args */
    if (this.pargs.length > 0) {
      for (int i = 0; i < this.pargs.length; i++) {
        if (i == args.length) {
          throw new ArrayIndexOutOfBoundsException(
              "Expected "
                  + this.pargs.length
                  + " positional arguments but received "
                  + (i - 1));
        }

        this.pargs[i] = args[i];
        startIdx++;
      }
    }

    /* kwargs and flargs */
    for (int i = startIdx; i < args.length; i++) {
      // filter out some invalid arguments before looking through registered ones
      if (!(args[i].length() >= 2 && args[i].startsWith("-"))) {
        throw new InvalidArgumentException("Argument " + args[i] + " is invalid");
      }

      Arg arg = this.matchArg(args[i]);

      if (arg instanceof Kwarg) {
        Kwarg kwarg = (Kwarg) arg;

        kwarg.setValue(args[i + 1]);

        // don't try to parse the value of the kwarg
        i++;
      } else if (arg instanceof Flarg) {
        Flarg flarg = (Flarg) arg;

        flarg.set();
      } else {
        // something truly terrible has to happen to get here i think
        throw new InvalidArgumentException("Argument " + args[i] + " is invalid");
      }
    }
  }
}
