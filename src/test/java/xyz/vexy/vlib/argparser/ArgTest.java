package xyz.vexy.vlib.argparser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArgTest {
  @Test
  public void testHasAlias() {
    Arg arg = new Arg("thisIsAnArg", 'a');

    assertTrue(arg.hasAlias('a'));
  }

  @Test
  public void doesNotHaveAlias() {
    Arg arg = new Arg("thisIsAnArg", 'b');

    assertFalse(arg.hasAlias('a'));
  }

  @Test
  public void hasNoAlias() {
    assertFalse(new Arg("argWithNoAlias").hasAlias('\u0000'));
  }

  @Test
  public void nameCannotStartWithHyphen() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Arg("-name");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      new Arg("-name", 'n');
    });
  }

  @Test
  public void aliasCannotBeNullChar() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Arg("name", '\u0000');
    });
  }
}
