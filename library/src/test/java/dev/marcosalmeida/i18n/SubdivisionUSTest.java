package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionUSTest {

    @Test
    public void testUSSubdivisions() {
        // Direct access to the enum constant
        SubdivisionCode.US al = SubdivisionCode.US.AL;
        assertEquals("US-AL", al.getCode());
        assertEquals("Alabama", al.getSubdivisionName());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.US);
        assertNotNull(subdivisions);
        assertEquals(57, subdivisions.length); // 50 states + DC + 6 outlying areas

        Subdivision alabama = subdivisions[0];
        assertEquals("US-AL", alabama.getCode());
        assertEquals("Alabama", alabama.getSubdivisionName());
        assertEquals("State", alabama.getCategory());

        Subdivision virginIslands = subdivisions[56];
        assertEquals("US-VI", virginIslands.getCode());
        assertEquals("Virgin Islands, U.S.", virginIslands.getSubdivisionName());
        assertEquals("Outlying area", virginIslands.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.US pr = SubdivisionCode.US.fromCode("US-PR");
        assertEquals(SubdivisionCode.US.PR, pr);

        // Test lookup by subdivision part
        SubdivisionCode.US al = SubdivisionCode.US.fromCode("AL");
        assertEquals(SubdivisionCode.US.AL, al);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.US.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision alabama = SubdivisionCode.US.fromName("Alabama").orElseThrow();
        assertEquals(SubdivisionCode.US.AL, alabama);

        Subdivision california = SubdivisionCode.US.fromName("california").orElseThrow();
        assertEquals(SubdivisionCode.US.CA, california);

        assertTrue(SubdivisionCode.US.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision al = SubdivisionCode.US.find("AL").orElseThrow();
        assertEquals(SubdivisionCode.US.AL, al);

        Subdivision alFull = SubdivisionCode.US.find("US-AL").orElseThrow();
        assertEquals(SubdivisionCode.US.AL, alFull);

        Subdivision alabama = SubdivisionCode.US.find("Alabama").orElseThrow();
        assertEquals(SubdivisionCode.US.AL, alabama);

        assertTrue(SubdivisionCode.US.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] states = SubdivisionCode.US.getStates();
        assertEquals(50, states.length);

        Subdivision[] districts = SubdivisionCode.US.getDistricts();
        assertEquals(1, districts.length);
        assertEquals(SubdivisionCode.US.DC, districts[0]);

        Subdivision[] outlyingAreas = SubdivisionCode.US.getOutlyingAreas();
        assertEquals(6, outlyingAreas.length);
    }

    @Test
    public void testGetSubdivisionCode() {
        assertEquals("AL", SubdivisionCode.US.AL.getSubdivisionCode());
        assertEquals("PR", SubdivisionCode.US.PR.getSubdivisionCode());
    }
}
