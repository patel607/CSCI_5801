/**
 * Driver.java - the main program that will drive the voting system
 *
 * Written by: Gabriel Lee (gnlee), Neil Patel (patel607), Philip Neff (neffx080), Ankith Bhat (bhatx050)
 */

import java.io.*;
import java.util.Scanner;

class Driver {
  protected static String input = "";
  protected static OPL opl;
  protected static CPL cpl;
  protected static Scanner scan = new Scanner(System.in);
  protected static boolean media;



  /**
   * Read input from user using Scanner for start-up and media report prompts
   *
   * @param first - if true, means we will run the initial GetTerminalInput to get
   *              the File name
   *              If False, it means we are looking for input as to whether a
   *              Media Report should be generated or not.
   */
  public static void GetTerminalInput(boolean first) {
    if (first) {
      System.out.println("Please enter file name to run election analysis!:");
      while (true) {
        input = scan.nextLine();
        File inputfile = new File(input);
        // valid file names must be between 5 and 255 characters long
        // check if file exists if name is valid
        // lastly, file must also end in csv
        if (input.length() < 4 || input.length() > 255) {
          System.out.println("Please enter a valid file name (too long or too short)");
        } else if (!inputfile.exists()) {
          System.out.println("Please enter a valid file name (doesn't exist)");
        } else if (!input.substring(input.length() - 3).equals("csv")) {
          System.out.println("Please enter a valid file name (not a csv)");
        } else {
          break;
        }
      }
    }
    // method used once again to ask if media report is wanted
    // will toggle the media boolean if report is desired
    else {
      System.out.println("Do you want a media report? (Y/N)");
      while (true) {
        input = scan.nextLine();
        // ensure Y/y/N/n input from user
        if (input.toUpperCase().equals("Y")) {
          media = true;
          break;
        } else if (input.toUpperCase().equals("N")) {
          break;
        } else {
          System.out.println("Please enter Y or N.");
        }
      }
    }
  }

  // MAIN METHOD

  /**
   * Main Method.
   *
   * @param args
   */
  public static void main(String[] args) {
    GetTerminalInput(true);
    try {
      File file = new File(input);
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String type = reader.readLine();
      System.out.println(type);
      // Create proper object depending on input file
      if (type.equals("CPL")) {
        cpl = new CPL(reader);
        String audit = cpl.RunVote();
        String report = cpl.DisplayResults();
        cpl.GenerateAudit(report + audit);
        // false terminal input parameter to toggle media boolean
        GetTerminalInput(false);
        if (media) {
          cpl.GenerateMediaReport(report);
        }
      } else if (type.equals("OPL")) {
        opl = new OPL(reader);
        String audit = opl.RunVote();
        String report = opl.DisplayResults();
        opl.GenerateAudit(report + audit);
        GetTerminalInput(false);
        if (media) {
          opl.GenerateMediaReport(report);
        }
        }
    } catch (Exception e){
      System.out.println("File not found.\nPlease enter a valid file name. (main)");
    }
  }
}
