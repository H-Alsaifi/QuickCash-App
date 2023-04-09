package com.example.g2_qc;

import com.example.g2_qc.user_profile.History.HistoryDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HistoryTest {

    private HistoryDetails historyDetails;

    @Before
    public void setUp() throws Exception {
        // Initialize objects needed for tests
        historyDetails = new HistoryDetails(1, 2, 3, 4, 5);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up after tests
        historyDetails = null;
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1, historyDetails.getTotalPosts());
        historyDetails.setTotalPosts(10);
        assertEquals(10, historyDetails.getTotalPosts());

        assertEquals(2, historyDetails.getPostsAsEmployer());
        historyDetails.setPostsAsEmployer(20);
        assertEquals(20, historyDetails.getPostsAsEmployer());

        assertEquals(3, historyDetails.getPostsAsEmployee());
        historyDetails.setPostsAsEmployee(30);
        assertEquals(30, historyDetails.getPostsAsEmployee());

        assertEquals(4, historyDetails.getTotalIncome());
        historyDetails.setTotalIncome(40);
        assertEquals(40, historyDetails.getTotalIncome());

        assertEquals(5, historyDetails.getAppliedJobs());
        historyDetails.setAppliedJobs(50);
        assertEquals(50, historyDetails.getAppliedJobs());


    }
}
