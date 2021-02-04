/**
 * OPL.java - Contains the necessary methods and attributes to carry out a CPL vote. Extends the
 * Voting System class for the audit and media reports
 *
 * Written by: Gabriel Lee (gnlee), Neil Patel (patel607), Philip Neff (neffx080), Ankith Bhat (bhatx050)
 */

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class OPL extends VotingSystem {
  // protected variables accessible from tests
  protected int numOfSeats;
  protected int numOfBallots;
  protected int numOfCandidates;
  protected String [] candidates;
  protected String [] winners;
  protected String [] ballots;
  protected String report = "";
  protected String audit = "";
  protected int [] votes;
  protected int [] sortedVotes;
  private String[] ties;
  private int numOfTies;
  private double maxPartyVotes;

  /**
   * construct OPL object with information from top of input file
   *
   * @param reader - Reader that is at the second line of the input file. Reader is
   *               the buffered reader created in Driver
   */
  public OPL(BufferedReader reader) {
    try {
      numOfSeats = Integer.parseInt(reader.readLine());
      numOfBallots = Integer.parseInt(reader.readLine());
      numOfCandidates = Integer.parseInt(reader.readLine());
      // initialize all arrays to number of candidates
      candidates = new String [numOfCandidates];
      votes = new int [numOfCandidates];
      sortedVotes = new int [numOfCandidates];
      winners = new String [numOfSeats];
      for (int i = 0; i < numOfCandidates; i++) {
        candidates[i] = reader.readLine();
      }
      ballots = new String [numOfBallots];
      for (int i = 0; i < numOfBallots; i++) {
        ballots[i] = reader.readLine();
      }
    } catch (Exception e) {
      System.out.println("File not found");
    }
  }
  /**
   * Constructs report string to be printed and put in media report and audit
   *
   * @return A Report string, which will be displayed to Screen and included in the Media Report and Audit
   */
  public String DisplayResults() {
    report += "Results:\n";
    report += "Winners: ";
    for (String w : winners) {
      report += w + " ";
    }
    report += "\nVotes Received:\n";
    for (int i = 0; i < numOfCandidates; ++i) {
      report += "  " + candidates[i] + ": " + votes[i] + "\n";
    }
    report += "Type of Election: OPL\n";
    report += "Number of seats: "+ numOfSeats + "\n";
    report += "Number of ballots cast: "+ numOfBallots + "\n";
    report += "Number of candidates: "+ numOfCandidates + "\n";
    System.out.println(report);
    return report;
  }

  /**
   * Sorts the candidates in order from most votes to least votes
   *
   * @return Returns ordered list of candidates from most votes to least votes
   */
  private String[] SelectionSort() {
    String [] sortedCandidates = new String[numOfCandidates];
    // copy votes array into tempVotes
    int [] tempVotes = new int[numOfCandidates];
    for (int i = 0; i < numOfCandidates; i++){
      tempVotes[i] = votes[i];
    }

    // select sort algorithm, find spot with most votes and put at next spot in sortedCandidates
    for (int j = 0; j < numOfCandidates; j++) {
      int maxVote = -1;
      int maxIndex = -1;
      for (int i = 0; i < numOfCandidates; i++) {
        if (tempVotes[i] > maxVote) {
          maxIndex = i;
          maxVote = tempVotes[i];
        }
      }
      sortedCandidates[j] = candidates[maxIndex];
      // keeps track of the sorted candidates' votes by also adding to the sortedVotes array
      sortedVotes[j] = votes[maxIndex];
      tempVotes[maxIndex] = -1;
    }
    return sortedCandidates;
  }

  /**
   * Flips a coin to randomly pick a party or a candidate
   *
   * @param ties - List of Parties or Candidates currently in a tie
   * @param numOfTies Number of ties - The length of tiedParties
   * @return - A random party or random candidate
   */
  private String FlipCoin(String[] ties, int numOfTies){
    Random rand = new Random();
    int coinFlipResult = rand.nextInt(numOfTies);
    return ties[coinFlipResult];
  }

  /**
   * Creates dictionary with candidates and their votes
   *
   * @return Returns the dictionary
   */
  private Map<String, Double> assignPartyBallots(){
    // Intialize Dictionary: key: Party, value: # of Votes
    Map<String, Double> totalPartyBallots = new HashMap<String, Double>();

    // count votes into the votes array
    for (String ballot : ballots) {
      int index = ballot.indexOf("1");
      votes[index]++;
    }

    // iterate thru and parse each candidate
    for (int i = 0; i < numOfCandidates; i++) {
      String[] candidateData = candidates[i].split(",");
      String party = candidateData[1].replace("]","");
      // count how many votes each party has and store in totalPartyBallots dictionary
      if (!totalPartyBallots.containsKey(party)) totalPartyBallots.put(party, 0.0);
      totalPartyBallots.put(party, totalPartyBallots.get(party) + (double) votes[i]);
    }
    return totalPartyBallots;
  }


  /**
   * Creates list of votes received for each party
   *
   * @param totalPartyBallots dictionary of candidates and their votes
   * @return Returns list of votes received for each party
   */
  private String[] makePartyList(Map<String, Double> totalPartyBallots){
    String[] partyList = new String[totalPartyBallots.size()];
    int index = 0;
    audit += "Votes Received for Party:\n";
    for (String party: totalPartyBallots.keySet()){
      partyList[index] = party;
      index++;
      audit += "  " + party + ": " + (int) totalPartyBallots.get(party).doubleValue() + "\n";
    }
    return partyList;
  }


  /**
   * Creates dictionary with parties and their votes
   *
   * @param partyList List of parties
   * @return Returns dictionary of parties and their votes
   */
  private Map<String, Double> makePartyVotes(String[] partyList){
    // declare and initialize partyVotes dictionary, where the keys are the party names
    Map<String, Double> partyVotes = new HashMap<String, Double>();
    for (String party : partyList) partyVotes.put(party, 0.0);

    // iterate thru each candidate, add their votes to partyVotes dictionary
    for (int i = 0; i<candidates.length; i++) {
      String[] candidateData = candidates[i].split(",");
      String party = candidateData[1].replace("]", "");
      partyVotes.put(party, partyVotes.get(party) + votes[i]);
    }
    return partyVotes;
  }


  /**
   * Finds party with the highest number of votes
   *
   * @param partyList List of parties
   * @param partyVotes Dictionary of parties and their votes
   * @return Return the string that is the party with the highest number of votes
   */
  private String findMaxParty(String[] partyList, Map<String, Double> partyVotes){
    String maxParty = "";
    //choose party with highest number of votes, store that party and its number of votes
    for (String party : partyList) {
      double tempVotes = partyVotes.get(party);
      if (Double.compare(tempVotes, maxPartyVotes) > 0) {
        maxPartyVotes = tempVotes;
        maxParty = party;
      }
    }

    // iterate thru each party to check for ties between parties
    int numOfTies = 0;
    String[] ties = new String[partyList.length];
    for (String party : partyList) {
      double tempVotes = partyVotes.get(party);
      if (Double.compare(tempVotes, maxPartyVotes) == 0) {
        ties[numOfTies] = party;
        numOfTies++;
      }
    }
    // if there's a tie, flip a coin
    if (numOfTies>1) maxParty = FlipCoin(ties, numOfTies);
    return maxParty;
  }

  /**
   * Executes the voting algorithm to determine a winner.
   *
   * @return Returns the audit String
   */
  public String RunVote() {
    System.out.println("Running...");
    audit += "\nALGORITHM:\n";

    // dictionary where keys are the party names and values are the votes for the party
    Map<String, Double> totalPartyBallots = assignPartyBallots();
    // iterate thru the dictionary of parties to create an array of the party names in partyList
    String[] partyList = makePartyList(totalPartyBallots);
    // sort the list of candidates from most votes to least votes, stored in sortedCandidates
    String [] sortedCandidates = SelectionSort();
    // dictionary where keys are the party names and values are the votes for that party
    Map<String, Double> partyVotes = makePartyVotes(partyList);

    // find the quota
    double numSeats = (double) numOfSeats;
    double numBallots = (double) numOfBallots;
    double quota = numBallots/numSeats;

    // declare/initialize a dictionary partySeats with the key as the party and the value is the number of earned seats
    Map<String, Integer> partySeats = new HashMap<String, Integer>();
    for (String party : partyList) partySeats.put(party, 0);

    // repeat for each seat
    for (int i = 0; i < numOfSeats; i++) {

      ties = new String[numOfCandidates];
      numOfTies = 0;
      maxPartyVotes = Double.NEGATIVE_INFINITY;

      String maxParty = findMaxParty(partyList, partyVotes);

      //assign a seat, subtract the quota
      partySeats.put(maxParty, partySeats.get(maxParty) + 1);
      partyVotes.put(maxParty, partyVotes.get(maxParty) - quota);

      // check for ties between candidates in the winning party
      // 'ties' is an array of every candidate with the most number of votes for the maxParty
      ties = new String[numOfCandidates];
      numOfTies = 0;
      int increment = 0;
      int maxCandidateVotes = 0;
      // iterate thru each of the sorted candidates
      for (String candidate : sortedCandidates) {
        String[] candidateData = candidate.split(",");
        String party = candidateData[1].replace("]", "");
        // checks if the candidate is the right party and isn't already a winner
        if (party.equals(maxParty) && !Arrays.asList(winners).contains(candidate)) {
          if (numOfTies==0 || (numOfTies>0 && sortedVotes[increment]>=maxCandidateVotes)) {
            // if the candidate has the max number of votes, add them to the 'ties' array
            maxCandidateVotes = sortedVotes[increment];
            ties[numOfTies] = candidate;
            numOfTies++;
          }
        }
        increment++;
      }
      if (numOfTies==0) {
        i--; //not enough candidates for the number of seats
      } else if (numOfTies==1) {
        // only one person with max number of votes, aka, no tie and one winner
        winners[i] = ties[0];
        audit += "Round " + (i + 1) + ":\n";
        audit += "  Winner: " + winners[i] + " with the party + " + maxParty + " having " + (int) maxPartyVotes + "" + "\n";
        audit += "  and the candidate getting " + maxCandidateVotes + "\n\n";
      } else {
        //there's a tie, flip a coin
        winners[i] = FlipCoin(ties, numOfTies);
      }
    }
    // audit string to be used in Driver for GenerateAudit
    return audit;
  }
}
