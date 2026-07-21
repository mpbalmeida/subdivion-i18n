package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionNOTest {

    @Test
    public void testNOSubdivisions() {
        SubdivisionCode.NO oslo = SubdivisionCode.NO.NO_03;
        assertEquals("NO-03", oslo.getCode());
        assertEquals("Oslo", oslo.getSubdivisionName());
        assertEquals("county", oslo.getCategory());

        SubdivisionCode.NO svalbard = SubdivisionCode.NO.NO_21;
        assertEquals("NO-21", svalbard.getCode());
        assertEquals("Svalbard", svalbard.getSubdivisionName());
        assertEquals("arctic region", svalbard.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.NO);
        assertNotNull(subdivisions);
        assertEquals(13, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("NO-03", first.getCode());
        assertEquals("Oslo", first.getSubdivisionName());

        Subdivision last = subdivisions[12];
        assertEquals("NO-54", last.getCode());
        assertEquals("Troms og Finnmark", last.getSubdivisionName());
        assertEquals("county", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.NO oslo = SubdivisionCode.NO.fromCode("NO-03");
        assertEquals(SubdivisionCode.NO.NO_03, oslo);

        SubdivisionCode.NO rogaland = SubdivisionCode.NO.fromCode("11");
        assertEquals(SubdivisionCode.NO.NO_11, rogaland);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.NO.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision vestland = SubdivisionCode.NO.fromName("Vestland").orElseThrow();
        assertEquals(SubdivisionCode.NO.NO_46, vestland);

        Subdivision trondelag = SubdivisionCode.NO.fromName("trøndelag").orElseThrow();
        assertEquals(SubdivisionCode.NO.NO_50, trondelag);

        assertTrue(SubdivisionCode.NO.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision tjueto = SubdivisionCode.NO.find("22").orElseThrow();
        assertEquals(SubdivisionCode.NO.NO_22, tjueto);

        Subdivision tjuetoFull = SubdivisionCode.NO.find("NO-22").orElseThrow();
        assertEquals(SubdivisionCode.NO.NO_22, tjuetoFull);

        Subdivision innlandet = SubdivisionCode.NO.find("Innlandet").orElseThrow();
        assertEquals(SubdivisionCode.NO.NO_34, innlandet);

        assertTrue(SubdivisionCode.NO.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.NO.getCounties();
        assertEquals(11, counties.length);

        Subdivision[] arcticRegions = SubdivisionCode.NO.getArcticRegions();
        assertEquals(2, arcticRegions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.NO.wikipedia());
        assertFalse(SubdivisionCode.NO.wikipedia().isBlank());
        assertTrue(SubdivisionCode.NO.wikipedia().contains("ISO_3166-2:NO"));

        assertNotNull(SubdivisionCode.NO.dateAdded());
        assertFalse(SubdivisionCode.NO.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.NO.lastUpdated());
        assertFalse(SubdivisionCode.NO.lastUpdated().isBlank());
    }
}
