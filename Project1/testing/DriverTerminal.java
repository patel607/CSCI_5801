//package testing;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.*;
import java.io.*;

public class DriverTerminal {

  @Test
  public void GetCorrectTerminalInput() throws IOException {
    String filename = "cpl_ballot1.csv";
    InputStream stdin = System.in;
    Driver d;
    try {
      System.setIn(new ByteArrayInputStream(filename.getBytes()));
      d = new Driver();
      d.GetTerminalInput(true);
    } finally {
      System.setIn(stdin);
    }
    assertEquals(filename, d.input);

  }

}
