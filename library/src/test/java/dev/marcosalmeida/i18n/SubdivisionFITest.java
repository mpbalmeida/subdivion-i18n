package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionFITest {

    @Test
    public void testFISubdivisions() {
        SubdivisionCode.FI aland = SubdivisionCode.FI.FI_01;
        assertEquals("FI-01", aland.getCode());
        assertEquals("Ahvenanmaan maakunta", aland.getSubdivisionName());
        assertEquals("region", aland.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.FI);
        assertNotNull(subdivisions);
        assertEquals(19, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("FI-01", first.getCode());
        assertEquals("Ahvenanmaan maakunta", first.getSubdivisionName());

        Subdivision last = subdivisions[18];
        assertEquals("FI-19", last.getCode());
        assertEquals("Varsinais-Suomi", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.FI uusimaa = SubdivisionCode.FI.fromCode("FI-18");
        assertEquals(SubdivisionCode.FI.FI_18, uusimaa);

        SubdivisionCode.FI lappi = SubdivisionCode.FI.fromCode("10");
        assertEquals(SubdivisionCode.FI.FI_10, lappi);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.FI.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision satakunta = SubdivisionCode.FI.fromName("Satakunta").orElseThrow();
        assertEquals(SubdivisionCode.FI.FI_17, satakunta);

        Subdivision uusimaa = SubdivisionCode.FI.fromName("uusimaa").orElseThrow();
        assertEquals(SubdivisionCode.FI.FI_18, uusimaa);

        assertTrue(SubdivisionCode.FI.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision kymenlaakso = SubdivisionCode.FI.find("09").orElseThrow();
        assertEquals(SubdivisionCode.FI.FI_09, kymenlaakso);

        Subdivision kymenlaaksoFull = SubdivisionCode.FI.find("FI-09").orElseThrow();
        assertEquals(SubdivisionCode.FI.FI_09, kymenlaaksoFull);

        Subdivision pirkanmaa = SubdivisionCode.FI.find("Pirkanmaa").orElseThrow();
        assertEquals(SubdivisionCode.FI.FI_11, pirkanmaa);

        assertTrue(SubdivisionCode.FI.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.FI.getRegions();
        assertEquals(19, regions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.FI.wikipedia());
        assertFalse(SubdivisionCode.FI.wikipedia().isBlank());
        assertTrue(SubdivisionCode.FI.wikipedia().contains("ISO_3166-2:FI"));

        assertNotNull(SubdivisionCode.FI.dateAdded());
        assertFalse(SubdivisionCode.FI.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.FI.lastUpdated());
        assertFalse(SubdivisionCode.FI.lastUpdated().isBlank());
    }
}
