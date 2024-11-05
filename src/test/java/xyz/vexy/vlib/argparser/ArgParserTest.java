package xyz.vexy.vlib.argparser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class ArgParserTest {
  @Test
  public void testSingleParg() {
    ArgParser parser = new ArgParser(1);
    String[] args = { "thisIsAParg" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    assertEquals("thisIsAParg", parser.pargs[0]);
  }

  @Test
  public void testManyPargs() {
    ArgParser parser = new ArgParser(10);
    String[] args = { "parg0", "parg1", "parg2", "parg3", "parg4", "parg5", "parg6", "parg7", "parg8", "parg9" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    int i = 0;
    for (String arg : parser.pargs) {
      assertEquals(args[i], arg);
      i++;
    }
  }

  @Test
  public void testTooManyPargs() {
    ArgParser parser = new ArgParser(0);
    String[] args = { "invalidParg" };

    assertThrows(IllegalArgumentException.class, () -> {
      parser.parse(args);
    });
  }

  @Test
  public void testNotEnoughPargs() {
    ArgParser parser = new ArgParser(1);
    String[] args = {};

    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      parser.parse(args);
    });
  }

  @Test
  public void testSingleKwarg() {
    ArgParser parser = new ArgParser();
    parser.registerKwarg("thisIsAKwarg");

    String[] args = { "--thisIsAKwarg", "thisIsItsValue" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    String kwarg = parser.getKwarg("thisIsAKwarg");

    assertNotNull(kwarg);
    assertEquals("thisIsItsValue", kwarg);
  }

  @Test
  public void testSingleAliasedKwarg() {
    ArgParser parser = new ArgParser();

    assertDoesNotThrow(() -> {
      parser.registerKwarg("thisIsAKwargWithAnAlias", 'k');
    });

    String[] args = { "-k", "thisIsItsValue" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    String kwarg = parser.getKwarg("thisIsAKwargWithAnAlias");

    assertNotNull(kwarg);
    assertEquals("thisIsItsValue", kwarg);
  }

  @Test
  public void testManyKwargs() {
    ArgParser parser = new ArgParser();
    String[] kwargs = {
        "kwarg0",
        "kwarg1",
        "kwarg2",
        "kwarg3",
        "kwarg4",
        "kwarg5",
        "kwarg6",
        "kwarg7",
        "kwarg8",
        "kwarg9",
    };
    String[] args = new String[kwargs.length * 2];

    for (int i = 0; i < kwargs.length; i++) {
      args[i * 2] = "--" + kwargs[i];
      args[i * 2 + 1] = "value" + i;
    }

    for (String kwarg : kwargs) {
      assertDoesNotThrow(() -> {
        parser.registerKwarg(kwarg);
      });
    }

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    for (int i = 0; i < kwargs.length; i++) {
      assertEquals(args[i * 2 + 1], parser.getKwarg(kwargs[i]));
    }
  }

  @Test
  public void testManyAliasedKwargs() {
    ArgParser parser = new ArgParser();
    String[] args = new String[20];

    for (int i = 0; i < args.length / 2; i++) {
      char _char = String.valueOf(i).charAt(0);
      String argName = String.valueOf(i).repeat(3);

      assertDoesNotThrow(() -> {
        parser.registerKwarg(argName, _char);
      });

      args[i * 2] = "-" + i;
      args[i * 2 + 1] = "value" + i;
    }

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    for (int i = 0; i < args.length / 2; i++) {
      String argName = String.valueOf(i).repeat(3);

      String kwarg = parser.getKwarg(argName);

      assertNotNull(kwarg);
      assertEquals("value" + i, kwarg);
    }
  }

  @Test
  public void testInvalidKwarg() {
    ArgParser parser = new ArgParser();
    parser.registerKwarg("theCorrectKwargName");

    String[] args = { "--theIncorrectKwargName" };

    assertThrows(IllegalArgumentException.class, () -> {
      parser.parse(args);
    });
  }

  @Test
  public void testTakenKwargName() {
    ArgParser parser = new ArgParser();
    parser.registerKwarg("kwargName");

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerKwarg("kwargName");
    });
  }

  @Test
  public void testTakenKwargAlias() {
    ArgParser parser = new ArgParser();
    parser.registerKwarg("kwargName", 'k');

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerKwarg("otherKwargName", 'k');
    });
  }

  @Test
  public void testSingleFlarg() {
    ArgParser parser = new ArgParser();
    parser.registerFlarg("thisIsAFlarg");

    String[] args = { "-thisIsAFlarg" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    boolean flarg = parser.getFlarg("thisIsAFlarg");

    assertNotNull(flarg);
    assertTrue(flarg);
  }

  @Test
  public void testSingleAliasedFlarg() {
    ArgParser parser = new ArgParser();
    parser.registerFlarg("thisIsAFlargWithAnAlias", 'f');

    String[] args = { "-f" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });
  }

  @Test
  public void testManyFlargs() {
    ArgParser parser = new ArgParser();
    String[] args = new String[10];

    for (int i = 0; i < args.length; i++) {
      String argName = String.valueOf(i);

      parser.registerFlarg(argName);
      args[i] = "-" + argName;
    }

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    for (int i = 0; i < args.length; i++) {
      String argName = String.valueOf(i);
      boolean flarg = parser.getFlarg(argName);

      assertNotNull(flarg);
      assertTrue(flarg);
    }
  }

  @Test
  public void testManyAliasedFlargs() {
    ArgParser parser = new ArgParser();
    String[] args = new String[10];

    for (int i = 0; i < args.length; i++) {
      char _char = String.valueOf(i).charAt(0);
      String argName = String.valueOf(_char).repeat(3);

      assertDoesNotThrow(() -> {
        parser.registerFlarg(argName, _char);
      });

      args[i] = "-" + _char;
    }

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    for (int i = 0; i < args.length; i++) {
      String argName = String.valueOf(i).repeat(3);
      boolean flarg = parser.getFlarg(argName);

      assertNotNull(flarg);
      assertTrue(flarg);
    }
  }

  @Test
  public void testInvalidFlarg() {
    ArgParser parser = new ArgParser();
    parser.registerFlarg("theCorrectFlargName");

    String[] args = { "-theIncorrectFlargName" };

    assertThrows(IllegalArgumentException.class, () -> {
      parser.parse(args);
    });
  }

  @Test
  public void testTakenFlargName() {
    ArgParser parser = new ArgParser();
    parser.registerFlarg("flargName");

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerFlarg("flargName");
    });
  }

  @Test
  public void testTakenFlargAlias() {
    ArgParser parser = new ArgParser();
    parser.registerFlarg("flargName", 'f');

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerFlarg("otherFlargName", 'f');
    });
  }

  @Test
  public void testPargsWithKwargs() {
    String[] kwargs = new String[10];
    String[] pargs = new String[5];
    ArgParser parser = new ArgParser(pargs.length);

    for (int i = 0; i < parser.pargs.length; i++) {
      pargs[i] = "parg" + i;
    }

    for (int i = 0; i < kwargs.length / 2; i++) {
      String argName = String.valueOf(i);

      assertDoesNotThrow(() -> {
        parser.registerKwarg(argName);
      });

      kwargs[i * 2] = "--" + i;
      kwargs[i * 2 + 1] = "value" + i;
    }

    String[] args = Arrays.copyOf(pargs, pargs.length + kwargs.length);
    System.arraycopy(kwargs, 0, args, pargs.length, kwargs.length);

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    for (int i = 0; i < parser.pargs.length; i++) {
      assertNotNull(parser.pargs[i]);
      assertEquals("parg" + i, parser.pargs[i]);
    }

    for (int i = 0; i < kwargs.length / 2; i++) {
      String argName = String.valueOf(i);

      String kwarg = parser.getKwarg(argName);

      assertNotNull(kwarg);
      assertEquals("value" + i, kwarg);
    }
  }

  @Test
  public void testAllArgTypes() {
    String[] kwargs = new String[10];
    String[] flargs = new String[5];
    String[] pargs = new String[5];
    ArgParser parser = new ArgParser(pargs.length);

    for (int i = 0; i < parser.pargs.length; i++) {
      pargs[i] = "parg" + i;
    }

    for (int i = 0; i < kwargs.length / 2; i++) {
      String argName = "kwarg" + i;

      assertDoesNotThrow(() -> {
        parser.registerKwarg(argName);
      });

      kwargs[i * 2] = "--" + argName;
      kwargs[i * 2 + 1] = "value" + i;
    }

    for (int i = 0; i < flargs.length; i++) {
      String argName = "flarg" + i;

      assertDoesNotThrow(() -> {
        parser.registerFlarg(argName);
      });

      flargs[i] = "-" + argName;
    }

    String[] pargsAndKwargs = Arrays.copyOf(pargs, pargs.length + kwargs.length);
    System.arraycopy(kwargs, 0, pargsAndKwargs, pargs.length, kwargs.length);

    String[] args = Arrays.copyOf(pargsAndKwargs, pargsAndKwargs.length + flargs.length);
    System.arraycopy(flargs, 0, args, pargsAndKwargs.length, flargs.length);

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    for (int i = 0; i < parser.pargs.length; i++) {
      assertNotNull(parser.pargs[i]);
      assertEquals("parg" + i, parser.pargs[i]);
    }

    for (int i = 0; i < kwargs.length / 2; i++) {
      String argName = "kwarg" + i;

      String kwarg = parser.getKwarg(argName);

      assertNotNull(kwarg);
      assertEquals("value" + i, kwarg);
    }

    for (int i = 0; i < flargs.length; i++) {
      String argName = "flarg" + i;

      boolean flarg = parser.getFlarg(argName);

      assertNotNull(flarg);
      assertTrue(flarg);
    }
  }

  @Test
  public void testIllegalArgNames() {
    ArgParser parser = new ArgParser();

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerFlarg("-flargName");
    });
    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerFlarg("--flargName");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerKwarg("-kwargName");
    });
    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerKwarg("--kwargName");
    });
  }

  @Test
  public void testIllegalAliases() {
    ArgParser parser = new ArgParser();

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerFlarg("flargName", '-');
    });
    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerFlarg("otherFlargName", '\u0000');
    });

    assertThrows(IllegalArgumentException.class, () -> {
      parser.registerKwarg("kwargName", '-');
    });
  }
}
