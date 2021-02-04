/**
 * VotingSystem.java - Abstract class that contains all methods necessary for a type of vote
 *
 * Written by: Gabriel Lee (gnlee), Neil Patel (patel607), Philip Neff (neffx080), Ankith Bhat (bhatx050)
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.*;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;

abstract class VotingSystem {

  /**
   * Constructs report string to be printed and put in media report and audit
   *
   * @return A Report string, which will be displayed to Screen and included in the Media Report and Audit
   */
  abstract public String DisplayResults();

  /**
   *  Create Audit file with extensive information on election calculation
   * @param audit - the audit string that will be written to the audit file
   */
  public void GenerateAudit(String audit) {
    System.out.println("Generating Audit...");
    ExportReport(audit, "audit_");
  }


  /**
   * Create Media Report file with information on election that can be presented to the media
   *
   * @param report - the report string that will be written to the media report file
   */
  public void GenerateMediaReport(String report) {
    ExportReport(report, "media_report_");
    System.out.println("Media Report Created");
  }

  /**
   * Exports Appropriate file to appropriate filename
   *
   * Filename has Date Time values appended to filename to ensure files will not be overwritten
   *
   * @param givenString - The string to be written to the file
   * @param name - the type of file which will be the the prefix of the filename
   */
  public void ExportReport(String givenString, String name) {
    BufferedWriter bw = null;
    try {
      Date date = Calendar.getInstance().getTime();  
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
      String strDate = dateFormat.format(date);
      String filename = name + strDate + ".txt";
      File file = new File(filename);
      if (!file.exists()) {
        file.createNewFile();
      }

      FileWriter fw = new FileWriter(file);
      bw = new BufferedWriter(fw);
      bw.write(givenString);
      bw.close();
    } catch (Exception e) {
      System.out.println("Error in Export Media Report");
      e.printStackTrace();
    }
  }

  /**
   * Executes the voting algorithm to determine a winner.
   *
   * @return Returns the audit String
   */
  abstract public String RunVote ();
}
