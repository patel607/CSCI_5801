import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class OPLTest {

    @Test
    public void oplTest1() throws IOException {
        File file = new File("opl_ballot1.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("OPL")) {
            OPL opl = new OPL (reader);
            // Read ability to read and store data from csv file
            assertTrue(opl.numOfBallots == 9);
            assertTrue(opl.numOfSeats == 3);
            assertTrue(opl.numOfCandidates == 6);
            opl.RunVote();
            // Test if OPL voting algorithm works as intended
            String [] expectedWinners = {"[Pike,D]", "[Borg,R]","[Foster,D]"};
            assertEquals(expectedWinners, opl.winners);
            opl.DisplayResults();
        } else {
            fail("Reader fail, should be type OPL");
        }
    }

    @Test
    public void oplTest2() throws IOException {
        File file = new File("opl_majorityDemocrat.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("OPL")) {
            OPL opl = new OPL (reader);
            // Read ability to read and store data from csv file
            assertTrue(opl.numOfBallots == 29);
            assertTrue(opl.numOfSeats == 3);
            assertTrue(opl.numOfCandidates == 6);
            opl.RunVote();
            // Test if CPL voting algorithm works as intended
            String[] expectedWinners = {"[Pike,D]", "[Foster,D]", "[Deutsch,R]"};
            assertEquals(expectedWinners, opl.winners);
        } else {
            fail("Reader fail, should be type CPL");
        }
    }

    @Test
    public void oplTest3() throws IOException {
        File file = new File("opl_two_candidates_ties.csv");

        int candidateOneWins = 0;
        int candidateTwoWins = 0;

        for (int i =0; i < 1000; i++){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String type = reader.readLine();
            OPL opl = new OPL (reader);
            // Read ability to read and store data from csv file
            opl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] candidateOne = {"[Pike,D]"};
            String [] candidateTwo = {"[Deutsch,R]"};

            if (Arrays.equals(candidateOne,opl.winners)) {
                candidateOneWins++;
            }
            else if (Arrays.equals(candidateTwo,opl.winners)) {
                candidateTwoWins++;
            }
        }
        double ratio = (1.0*candidateOneWins)/(candidateOneWins+ candidateTwoWins);
        assertTrue(ratio > 0.45);
        assertTrue(ratio < 0.55);
    }

    @Test
    public void oplTest4() throws IOException {
        File file = new File("opl_only_one_vote.csv");

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();
        OPL opl = new OPL (reader);
        // Read ability to read and store data from csv file
        opl.RunVote();
        // Test if CPL voting algorithm works as intended
        String candidateOne = "[Pike,D]";
        String candidateTwo = "[Foster,D]";

        assertEquals(candidateOne, opl.winners[0]);
        assertEquals(candidateTwo, opl.winners[1]);
    }

    @Test
    public void oplTest5() throws IOException {
        File file = new File("opl_ballot_stress.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        long start = System.currentTimeMillis();
        OPL opl = new OPL (reader);

        opl.RunVote();
        // Test if CPL voting algorithm works as intended
        long end = System.currentTimeMillis();
        long fiveMInutes = 300000; // in milliseconds
        assertTrue((end-start) < fiveMInutes);
    }

    @Test
    public void oplTest6() throws IOException {
        File file = new File("opl_ballot_stress.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();

        if (type.equals("OPL")) {
            OPL opl = new OPL (reader);
            // Read ability to read and store data from csv file
            opl.RunVote();
            // Test if CPL voting algorithm works as intended
            String [] expectedWinners = {"[Pike,D]", "[Borg,R]","[Foster,D]"};
            assertEquals(expectedWinners, opl.winners);
        } else {
            fail("Reader fail, should be type CPL");
        }
    }

    @Test
    public void oplTest7() throws IOException {
        File file = new File("opl_just_one_person.csv");

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String type = reader.readLine();
        OPL opl = new OPL (reader);
        // Read ability to read and store data from csv file
        opl.RunVote();
        // Test if CPL voting algorithm works as intended
        String [] expectedWinners = {"[Pike,D]"};

        assertEquals(expectedWinners, opl.winners);
    }

}

// [Pike,D] [Borg,R] [Foster,D]