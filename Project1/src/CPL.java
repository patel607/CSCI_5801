/**
 * CPL.java - Contains the necessary methods and attributes to carry out a CPL vote. Extends the
 * Voting System class for the audit and media reports
 *
 * Written by: Gabriel Lee (gnlee), Neil Patel (patel607), Philip Neff (neffx080), Ankith Bhat (bhatx050)
 */

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class CPL extends VotingSystem {
  // protected variables accessible from tests
  protected String[][] candidate_ranking;
  protected int numOfParties;
  protected String [] partyList;
  protected int numOfSeats;
  protected int numOfBallots;
  protected int numOfCandidates;
  protected String [] candidates;
  protected String [] ballots;
  protected String [] winners;
  protected String report = "";
  protected String audit = "";
  protected int votes[];




  /**
   * construct CPL object with information from top of input file
   *
   * @param reader - Reader that is at the second line of the input file. Reader is
   *               the buffered reader created in Driver
   */
  public CPL(BufferedReader reader){
    try {
      //read file and extract info
      numOfParties = Integer.parseInt(reader.readLine());
      String parties = reader.readLine();
      // if at least one party in list, then cut off brackets of string
      if (parties.length() > 2) {
        parties = parties.substring(1, parties.length()-1);
      }
      partyList = parties.split(",");
      numOfSeats = Integer.parseInt(reader.readLine());
      numOfBallots = Integer.parseInt(reader.readLine());
      numOfCandidates = Integer.parseInt(reader.readLine());
      candidates = new String [numOfCandidates];
      // votes are designated to each party
      votes = new int[numOfParties];
      for (int i = 0; i < numOfCandidates; i++) {
        candidates[i] = reader.readLine();
      }
      ballots = new String [numOfBallots];
      for (int i = 0; i < numOfBallots; i++) {
        ballots[i] = reader.readLine();
      }
      // 2D array to rank candidates within their party
      candidate_ranking = new String[partyList.length][numOfCandidates];
      winners = new String[numOfSeats];
    } catch (Exception e) {
      System.out.println("File not Found in Constructor");
    }

  }



  /**
   * Constructs report string to be printed and put in media report and audit
   *
   * @return A Report string, which will be displayed to Screen and included in the Media Report and Audit
   */
  public String DisplayResults(){
    report += "Results:\n";
    report += "Winners: ";
    for (String w : winners) {
      report += w + " ";
    }
    report += "\nVotes Received:\n";
    for (int i = 0; i < numOfParties; ++i) { 
      report += "  " + partyList[i] + ": " + votes[i] + "\n"; 
    }
    report += "Type of Election: CPL\n";
    report += "Number of seats: "+ numOfSeats + "\n";
    report += "Number of ballots cast: "+ numOfBallots + "\n";
    report += "Number of candidates: "+ numOfCandidates + "\n";
    report += "Number of parties: " + numOfParties + "\n";
    report += "Parties:\n";
    for (String pl : partyList) {
      report += pl + " ";
    }

    System.out.println(report);
    // report string to be used in Driver for GenerateMediaReport
    return report;
  }

  /**
   * ReadBallots to count the votes per party necessary to display
   */
  private void ReadBallots() {
    for (String b : ballots) {
      ++votes[b.indexOf("1")];
    }
  }


  /**
   * Flips a coin to randomly pick a party
   *
   * @param tiedParties - List of parties that are currently in a tie
   * @param numOfTies - Number of ties - The length of tiedParties
   * @return - A random party or random candidate
   */
  private String FlipCoin(String[] tiedParties, int numOfTies){
    Random rand = new Random();
    int coinFlipResult = rand.nextInt(numOfTies);
    return tiedParties[coinFlipResult];
  }


  /**
   * iterate thru each candidate and parse the different parts of the string
   * then place each candidate in a 2d array based on party and ranking
   */
  private void updateCandidateRating(){
    for (String candidate : candidates) {
      String[] candidateData = candidate.split(",");
      candidateData[0] = candidateData[0].replace("[","");
      candidateData[2] = candidateData[2].replace("]","");

      int partyIndex = Arrays.asList(partyList).indexOf(candidateData[1]);
      int candidateIndex = Integer.parseInt(candidateData[2]) -1;
      candidate_ranking[partyIndex][candidateIndex] = "[" + candidateData[0] + "," + candidateData[1] + "]";
    }
  }


  /**
   * Determine winners in CPL vote
   *
   * @param partyBallots Dictionary of parties and their votes
   * @param partySeats Dictionary of parties and their seats
   * @param quota double containing the quota for this vote
   */
  private void FindWinners(Map<String, Double> partyBallots, Map<String, Integer> partySeats, double quota){
    // fill all the seats
    int seatsFilled = 0;
    while(seatsFilled != numOfSeats){
      double maxVotes = Double.NEGATIVE_INFINITY;
      String maxParty = "";

      // iterate thru each party to find the party that got the max number of votes
      for (String party: partyBallots.keySet()) {
        double partyVotes = partyBallots.get(party);
        if (Double.compare(partyVotes, maxVotes) > 0){
          maxVotes = partyVotes;
          maxParty = party;
        }
      }

      // iterate thru each party again to check for ties
      // stores every party that has the most number of votes in the ties array
      String[] ties = new String[numOfParties];
      int numOfTies = 0;
      for (String party: partyBallots.keySet()){
        double partyVotes = partyBallots.get(party);
        if (Double.compare(partyVotes, maxVotes) == 0) {
          ties[numOfTies] = party;
          numOfTies++;
        }
      }

      // if there are more than one parties with the max number of votes, then there's a tie, flip a coin
      if (numOfTies>1) {
        audit += "There is a tie between ";
        for (String t : ties) {
          audit += t + "/";
        }
        maxParty = FlipCoin(ties, numOfTies);
        audit += "\n" + maxParty + " wins coin flip.\n";
      }

      // TODO Return error if -1.0
      // return IllegalStateArgument

      // updates available ballots left for the party that won this round
      partyBallots.put(maxParty, partyBallots.get(maxParty) - quota);
      // adds one more seat to the party that won
      partySeats.put(maxParty, partySeats.get(maxParty) +1);

      // choose the winner from the 2d array
      int partyIndex = Arrays.asList(partyList).indexOf(maxParty);
      int candidateIndex = partySeats.get(maxParty) -1;
      String chosenCandidate = candidate_ranking[partyIndex][candidateIndex];

      // add a new winner to the assigned seats
      // if the winner is null, there are no more available candidates for the winning party, so the loop runs again
      if (chosenCandidate != null) {
        winners[seatsFilled] = chosenCandidate;
        seatsFilled++;
      }
      audit += "Round " + (seatsFilled + 1) + ":\n";
      audit += "Winner: "+chosenCandidate+" with party "+maxParty+" having "+(int)maxVotes+" votes remaining."+"\n\n";
    }
  }


  /**
   * Executes the voting algorithm to determine a winner.
   *
   * @return Returns the audit String
   */
  public String RunVote() {
    System.out.println("Running...");
    ReadBallots();
    audit += "\nALGORITHM:\n";

    // Declare and intialize Dictionary - key: Party, value: # of Votes for that party
    Map<String, Double> partyBallots = new HashMap<String, Double>();
    for (String party : partyList) {
      partyBallots.put(party, 0.0);
    }

    // count votes and add them to each party in partyBallot dictionary
    for (String ballot : ballots) {
      int index = ballot.indexOf("1");
      String party = partyList[index];
      partyBallots.put(party, partyBallots.get(party) + 1.0);
    }

    updateCandidateRating();

    double quota = (double) numOfBallots/(double) numOfSeats;

    // declare/initialize a dictionary partySeats to store how many seats each party will receive
    Map<String, Integer> partySeats = new HashMap<String, Integer>();
    for (String party : partyList) {
      partySeats.put(party, 0);
    }

    // given in the party data to get the winners
    FindWinners(partyBallots, partySeats, quota);

    // audit string to be used in Driver for GenerateAudit
    return audit;
  }
}