import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.*;
import java.io.*;
import java.util.Arrays;

public class CPLTest {

    @Test
    public void cplTest1() throws IOException {
        File file = new File("cpl_ballot1.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("CPL")) {
            CPL cpl = new CPL (reader);
            // Read ability to read and store data from csv file
            assertTrue(cpl.numOfParties == 4);
            assertTrue(cpl.numOfBallots == 13);
            assertTrue(cpl.numOfSeats == 7);
            assertTrue(cpl.numOfCandidates == 16);
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Deutsch,R]", "[Jones,G]", "[Wong,R]", "[Pike,D]", "[Smith,G]", "[Walters,R]",
                    "[Foster,D]"};
            assertEquals(expectedWinners, cpl.winners);
        } else {
            fail("Reader fail, should be type CPL");
        }
    }

    @Test
    public void cplTest2() throws IOException {
        File file = new File("cpl_majorityDemocrat.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("CPL")) {
            CPL cpl = new CPL (reader);
            // Read ability to read and store data from csv file
            assertTrue(cpl.numOfParties == 4);
            assertTrue(cpl.numOfBallots == 23);
            assertTrue(cpl.numOfSeats == 7);
            assertTrue(cpl.numOfCandidates == 16);
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Pike,D]", "[Foster,D]", "[Floyd,D]", "[Deutsch,R]", "[Jones,G]", "[Jones,D]", "[Wong,R]"};
            assertEquals(expectedWinners, cpl.winners);
        } else {
            fail("Reader fail, should be type CPL");
        }
    }

    @Test
    public void cplTest3() throws IOException {
        File file = new File("cpl_overwhelmingDemocrat.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("CPL")) {
            CPL cpl = new CPL (reader);
            // Read ability to read and store data from csv file
            assertTrue(cpl.numOfParties == 4);
            assertTrue(cpl.numOfBallots == 20);
            assertTrue(cpl.numOfSeats == 5);
            assertTrue(cpl.numOfCandidates == 16);
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Pike,D]", "[Foster,D]", "[Floyd,D]","[Jones,D]", "[Mallory,D]"};
            assertEquals(expectedWinners, cpl.winners);
        } else {
            fail("Reader fail, should be type CPL");
        }
    }

    @Test
    public void cplTest4() throws IOException {
        File file = new File("cpl_two_candidates_ties.csv");

        int candidateOneWins = 0;
        int candidateTwoWins = 0;

        for (int i =0; i < 1000; i++){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String type = reader.readLine();
            CPL cpl = new CPL (reader);
            // Read ability to read and store data from csv file
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] candidateOne = {"[Pike,D]"};
            String [] candidateTwo = {"[Deutsch,R]"};

            if (Arrays.equals(candidateOne,cpl.winners)) {
                candidateOneWins++;
            }
            else if (Arrays.equals(candidateTwo,cpl.winners)) {
                candidateTwoWins++;
            }
        }
        double ratio = (1.0*candidateOneWins)/(candidateOneWins+ candidateTwoWins);
        assertTrue(ratio > 0.45);
        assertTrue(ratio < 0.55);
    }

    @Test
    public void cplTest5() throws IOException {
        File file = new File("cpl_ballot_stress.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        long start = System.currentTimeMillis();
        CPL cpl = new CPL (reader);

        cpl.RunVote();
        // Test if CPL voting algorithm works as intended
        long end = System.currentTimeMillis();
        long fiveMInutes = 300000; // in milliseconds
        assertTrue((end-start) < fiveMInutes);

    }

    @Test
    public void cplTest6() throws IOException {
        File file = new File("cpl_ballot_stress.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("CPL")) {
            CPL cpl = new CPL (reader);
            // Read ability to read and store data from csv file
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Deutsch,R]", "[Jones,G]", "[Wong,R]", "[Pike,D]", "[Smith,G]", "[Walters,R]", "[Foster,D]"};
            assertEquals(expectedWinners, cpl.winners);
        } else {
            fail("Reader fail, should be type CPL");
        }
    }



    @Test
    public void cplTest7() throws IOException {
        File file = new File("cpl_just_one_person.csv");

        int candidateOneWins = 0;

        // Each time the algorithm is run, the results are the same
        for (int i =0; i < 1000; i++){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String type = reader.readLine();
            CPL cpl = new CPL (reader);
            // Read ability to read and store data from csv file
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] candidateOne = {"[Pike,D]"};

            if (Arrays.equals(candidateOne,cpl.winners)) {
                candidateOneWins++;
            }

        }
        assertTrue(candidateOneWins == 1000);
    }

    @Test
    public void cplTest8() throws IOException {
        File file = new File("cpl_ballot1.csv");

        // Each time the algorithm is run, the results are the same
        for (int i =0; i < 1000; i++){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String type = reader.readLine();

            CPL cpl = new CPL (reader);
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Deutsch,R]", "[Jones,G]", "[Wong,R]", "[Pike,D]", "[Smith,G]", "[Walters,R]",
                    "[Foster,D]"};
            assertEquals(expectedWinners, cpl.winners);

        }
    }

    @Test
    public void cplTest9() throws IOException {
        File file = new File("cpl_majorityDemocrat.csv");

        // Each time the algorithm is run, the results are the same
        for (int i = 0; i < 1000; i++){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String type = reader.readLine();

            CPL cpl = new CPL (reader);
            assertTrue(cpl.numOfParties == 4);
            assertTrue(cpl.numOfBallots == 23);
            assertTrue(cpl.numOfSeats == 7);
            assertTrue(cpl.numOfCandidates == 16);
            cpl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Pike,D]", "[Foster,D]", "[Floyd,D]", "[Deutsch,R]", "[Jones,G]", "[Jones,D]", "[Wong,R]"};
            assertEquals(expectedWinners, cpl.winners);
        }
    }
}