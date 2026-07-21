package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionGETest {

    @Test
    public void testGESubdivisions() {
        SubdivisionCode.GE abkhazia = SubdivisionCode.GE.AB;
        assertEquals("GE-AB", abkhazia.getCode());
        assertEquals("Abkhazia", abkhazia.getSubdivisionName());
        assertEquals("autonomous republic", abkhazia.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.GE);
        assertNotNull(subdivisions);
        assertEquals(12, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("GE-AB", first.getCode());
        assertEquals("Abkhazia", first.getSubdivisionName());

        Subdivision last = subdivisions[11];
        assertEquals("GE-TB", last.getCode());
        assertEquals("Tbilisi", last.getSubdivisionName());
        assertEquals("city", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.GE ab = SubdivisionCode.GE.fromCode("GE-AB");
        assertEquals(SubdivisionCode.GE.AB, ab);

        SubdivisionCode.GE aj = SubdivisionCode.GE.fromCode("AJ");
        assertEquals(SubdivisionCode.GE.AJ, aj);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.GE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision tbilisi = SubdivisionCode.GE.fromName("Tbilisi").orElseThrow();
        assertEquals(SubdivisionCode.GE.TB, tbilisi);

        Subdivision adjara = SubdivisionCode.GE.fromName("adjara").orElseThrow();
        assertEquals(SubdivisionCode.GE.AJ, adjara);

        assertTrue(SubdivisionCode.GE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision tb = SubdivisionCode.GE.find("TB").orElseThrow();
        assertEquals(SubdivisionCode.GE.TB, tb);

        Subdivision tbFull = SubdivisionCode.GE.find("GE-TB").orElseThrow();
        assertEquals(SubdivisionCode.GE.TB, tbFull);

        Subdivision guria = SubdivisionCode.GE.find("Guria").orElseThrow();
        assertEquals(SubdivisionCode.GE.GU, guria);

        assertTrue(SubdivisionCode.GE.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.GE.getRegions();
        assertEquals(9, regions.length);

        Subdivision[] autonomousRepublics = SubdivisionCode.GE.getAutonomousRepublics();
        assertEquals(2, autonomousRepublics.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.GE.wikipedia());
        assertFalse(SubdivisionCode.GE.wikipedia().isBlank());
        assertTrue(SubdivisionCode.GE.wikipedia().contains("ISO_3166-2:GE"));

        assertNotNull(SubdivisionCode.GE.dateAdded());
        assertFalse(SubdivisionCode.GE.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.GE.lastUpdated());
        assertFalse(SubdivisionCode.GE.lastUpdated().isBlank());
    }
}
