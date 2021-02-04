//package testing;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class DriverTerminal2 {


  @Test
  public void GetCorrectTerminalInput2() throws IOException {
    String filename = "opl_ballot1.csv";
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
