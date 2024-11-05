package xyz.vexy.vlib.argparser;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {
  public final Map<String, Flarg> flargs = new HashMap<String, Flarg>();
  public final Map<String, Kwarg> kwargs = new HashMap<String, Kwarg>();
  public final String[] pargs;

  public ArgParser() {
    this.pargs = null;
  }

  public ArgParser(int numPosArgs) {
    this.pargs = new String[numPosArgs];
  }

  public String getKwarg(String argName) {
    Kwarg kwarg = this.kwargs.get(argName);

    if (kwarg != null) {
      return kwarg.getValue();
    }

    return null;
  }

  public Boolean getFlarg(String argName) {
    Flarg flarg = this.flargs.get(argName);

    if (flarg != null) {
      return flarg.isSet;
    }

    return null;
  }

  public void registerArg(Arg arg) throws IllegalArgumentException {
    if (this.kwargs.get(arg.getName()) != null) {
      throw new IllegalArgumentException("Arg name " + arg.getName() + " cannot be assigned twice");
    }
    if (this.flargs.get(arg.getName()) != null) {
      throw new IllegalArgumentException("Arg name " + arg.getName() + " cannot be assigned twice");
    }

    /* begin 'there is definitely a better way to do this' block */
    for (Kwarg kwarg : this.kwargs.values()) {
      if (arg.getName().length() == 1 && kwarg.getAlias() == arg.getName().charAt(0)) {
        throw new IllegalArgumentException(
            "Arg name " + arg.getName() + " cannot be registered since an alias of the same name already exists");
      }

      if (arg.getAlias() != '\u0000' && arg.getAlias() == kwarg.getAlias()) {
        throw new IllegalArgumentException(
            "Alias " + arg.getAlias() + " cannot be registered twice");
      }
    }

    for (Flarg flarg : this.flargs.values()) {
      if (arg.getName().length() == 1 && flarg.getAlias() == arg.getName().charAt(0)) {
        throw new IllegalArgumentException(
            "Arg name " + arg.getName() + " cannot be registered since an alias of the same name already exists");
      }

      if (arg.getAlias() != '\u0000' && arg.getAlias() == flarg.getAlias()) {
        throw new IllegalArgumentException(
            "Alias " + arg.getAlias() + " cannot be registered twice");
      }
    }
    /* end 'there is definitely be a better way to do this' block */

    if (arg instanceof Kwarg) {
      this.kwargs.put(arg.getName(), (Kwarg) arg);
    } else if (arg instanceof Flarg) {
      this.flargs.put(arg.getName(), (Flarg) arg);
    }
  }

  public void registerFlarg(String name, char alias) {
    this.registerArg(new Flarg(name, alias));
  }

  public void registerFlarg(String name) {
    this.registerArg(new Flarg(name));
  }

  public void registerKwarg(String name, char alias) {
    this.registerArg(new Kwarg(name, alias));
  }

  public void registerKwarg(String name) {
    this.registerArg(new Kwarg(name));
  }

  public Arg matchArg(String name) throws IllegalArgumentException {
    if (name.length() < 2 || !name.startsWith("-"))
      throw new IllegalArgumentException("Argument " + name + " is invalid");

    String strippedName;

    if (name.startsWith("--")) {
      strippedName = name.substring(2);
      Kwarg kwarg = this.kwargs.get(strippedName);

      if (kwarg != null) {
        return kwarg;
      }
    } else if (name.startsWith("-")) {
      strippedName = name.substring(1);
      Flarg flarg = flargs.get(strippedName);

      if (flarg != null) {
        return flarg;
      }

      if (strippedName.length() == 1) {
        for (Kwarg kwarg : this.kwargs.values()) {
          if (kwarg.getAlias() == strippedName.charAt(0)) {
            return kwarg;
          }
        }

        for (Flarg f : this.flargs.values()) {
          if (f.getAlias() == strippedName.charAt(0)) {
            return f;
          }
        }
      }
    } else {
      /*
       * this should only happen once we have parsed pargs, so an arg with no leading
       * '-' is invalid
       */
      throw new IllegalArgumentException("Argument " + name + " is invalid");
    }

    throw new IllegalArgumentException("Argument " + name + " is invalid");
  }

  public void parse(String[] args) throws IllegalArgumentException {
    int startIdx = 0;

    /* positional args */
    if (this.pargs != null && this.pargs.length > 0) {
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
      if (args[i] == null || !(args[i].length() >= 2 && args[i].startsWith("-"))) {
        throw new IllegalArgumentException("Argument " + args[i] + " is invalid");
      }

      Arg arg = this.matchArg(args[i]);

      if (arg instanceof Kwarg) {
        Kwarg kwarg = (Kwarg) arg;

        kwarg.setValue(args[i + 1]);

        this.kwargs.put(kwarg.getName(), kwarg);

        // don't try to parse the value of the kwarg
        i++;
      } else if (arg instanceof Flarg) {
        Flarg flarg = (Flarg) arg;

        flarg.set();
      } else {
        // something truly terrible has to happen to get here i think
        throw new IllegalArgumentException("Argument " + args[i] + " is invalid");
      }
    }
  }
}
