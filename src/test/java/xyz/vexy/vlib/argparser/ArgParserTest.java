package xyz.vexy.vlib.argparser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ArgParserTest {
  @Test
  public void testSingleParg() {
    ArgParser parser = new ArgParser(1);
    String[] args = { "thisIsAParg" };

    assertDoesNotThrow(() -> {
      parser.parse(args);
    });

    assertEquals(parser.pargs[0], "thisIsAParg");
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
      assertEquals(arg, args[i]);
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
}
