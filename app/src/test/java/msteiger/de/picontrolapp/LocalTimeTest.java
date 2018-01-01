package msteiger.de.picontrolapp;

import org.junit.Test;

import msteiger.de.picontrolapp.dummy.LocalTime;

import static org.junit.Assert.assertEquals;

public class LocalTimeTest {

    @Test
    public void testNoOctals() {
        assertEquals("09:34", new LocalTime("09:34:22").toString());
    }
}
