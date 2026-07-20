package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionNZTest {

    @Test
    public void testNZSubdivisions() {
        SubdivisionCode.NZ auk = SubdivisionCode.NZ.AUK;
        assertEquals("NZ-AUK", auk.getCode());
        assertEquals("Auckland", auk.getSubdivisionName());
        assertEquals("region", auk.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.NZ);
        assertNotNull(subdivisions);
        assertEquals(17, subdivisions.length); // 16 regions + 1 special island authority

        Subdivision auckland = subdivisions[0];
        assertEquals("NZ-AUK", auckland.getCode());
        assertEquals("Auckland", auckland.getSubdivisionName());

        Subdivision westCoast = subdivisions[16];
        assertEquals("NZ-WTC", westCoast.getCode());
        assertEquals("West Coast", westCoast.getSubdivisionName());
        assertEquals("region", westCoast.getCategory());

        Subdivision cit = SubdivisionCode.NZ.CIT;
        assertEquals("NZ-CIT", cit.getCode());
        assertEquals("Chatham Islands Territory", cit.getSubdivisionName());
        assertEquals("special island authority", cit.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.NZ auk = SubdivisionCode.NZ.fromCode("NZ-AUK");
        assertEquals(SubdivisionCode.NZ.AUK, auk);

        // Test lookup by subdivision part
        SubdivisionCode.NZ wko = SubdivisionCode.NZ.fromCode("WKO");
        assertEquals(SubdivisionCode.NZ.WKO, wko);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.NZ.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision auckland = SubdivisionCode.NZ.fromName("Auckland").orElseThrow();
        assertEquals(SubdivisionCode.NZ.AUK, auckland);

        Subdivision bayOfPlenty = SubdivisionCode.NZ.fromName("bay of plenty").orElseThrow();
        assertEquals(SubdivisionCode.NZ.BOP, bayOfPlenty);

        assertTrue(SubdivisionCode.NZ.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision wko = SubdivisionCode.NZ.find("WKO").orElseThrow();
        assertEquals(SubdivisionCode.NZ.WKO, wko);

        Subdivision wkoFull = SubdivisionCode.NZ.find("NZ-WKO").orElseThrow();
        assertEquals(SubdivisionCode.NZ.WKO, wkoFull);

        Subdivision waikato = SubdivisionCode.NZ.find("Waikato").orElseThrow();
        assertEquals(SubdivisionCode.NZ.WKO, waikato);

        assertTrue(SubdivisionCode.NZ.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.NZ.getRegions();
        assertEquals(16, regions.length);

        Subdivision[] specialIslandAuthorities = SubdivisionCode.NZ.getSpecialIslandAuthorities();
        assertEquals(1, specialIslandAuthorities.length);
        assertEquals(SubdivisionCode.NZ.CIT, specialIslandAuthorities[0]);
    }
}
