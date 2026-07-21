package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCYTest {

    @Test
    public void testCYSubdivisions() {
        SubdivisionCode.CY nicosia = SubdivisionCode.CY.CY_01;
        assertEquals("CY-01", nicosia.getCode());
        assertEquals("Nicosia", nicosia.getSubdivisionName());
        assertEquals("district", nicosia.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.CY);
        assertNotNull(subdivisions);
        assertEquals(6, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("CY-01", first.getCode());
        assertEquals("Nicosia", first.getSubdivisionName());

        Subdivision last = subdivisions[5];
        assertEquals("CY-06", last.getCode());
        assertEquals("Kyrenia", last.getSubdivisionName());
        assertEquals("district", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CY nicosia = SubdivisionCode.CY.fromCode("CY-01");
        assertEquals(SubdivisionCode.CY.CY_01, nicosia);

        SubdivisionCode.CY paphos = SubdivisionCode.CY.fromCode("05");
        assertEquals(SubdivisionCode.CY.CY_05, paphos);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CY.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision larnaca = SubdivisionCode.CY.fromName("Larnaca").orElseThrow();
        assertEquals(SubdivisionCode.CY.CY_03, larnaca);

        Subdivision limassol = SubdivisionCode.CY.fromName("limassol").orElseThrow();
        assertEquals(SubdivisionCode.CY.CY_02, limassol);

        assertTrue(SubdivisionCode.CY.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision four = SubdivisionCode.CY.find("04").orElseThrow();
        assertEquals(SubdivisionCode.CY.CY_04, four);

        Subdivision fourFull = SubdivisionCode.CY.find("CY-04").orElseThrow();
        assertEquals(SubdivisionCode.CY.CY_04, fourFull);

        Subdivision famagusta = SubdivisionCode.CY.find("Famagusta").orElseThrow();
        assertEquals(SubdivisionCode.CY.CY_04, famagusta);

        assertTrue(SubdivisionCode.CY.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] districts = SubdivisionCode.CY.getDistricts();
        assertEquals(6, districts.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.CY.wikipedia());
        assertFalse(SubdivisionCode.CY.wikipedia().isBlank());
        assertTrue(SubdivisionCode.CY.wikipedia().contains("ISO_3166-2:CY"));

        assertNotNull(SubdivisionCode.CY.dateAdded());
        assertFalse(SubdivisionCode.CY.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.CY.lastUpdated());
        assertFalse(SubdivisionCode.CY.lastUpdated().isBlank());
    }
}
