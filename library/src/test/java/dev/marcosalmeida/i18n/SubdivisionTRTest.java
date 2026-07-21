package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionTRTest {

    @Test
    public void testTRSubdivisions() {
        SubdivisionCode.TR adana = SubdivisionCode.TR.TR_01;
        assertEquals("TR-01", adana.getCode());
        assertEquals("Adana", adana.getSubdivisionName());
        assertEquals("province", adana.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.TR);
        assertNotNull(subdivisions);
        assertEquals(81, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("TR-01", first.getCode());
        assertEquals("Adana", first.getSubdivisionName());

        Subdivision last = subdivisions[80];
        assertEquals("TR-81", last.getCode());
        assertEquals("Düzce", last.getSubdivisionName());
        assertEquals("province", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.TR duzce = SubdivisionCode.TR.fromCode("TR-81");
        assertEquals(SubdivisionCode.TR.TR_81, duzce);

        SubdivisionCode.TR adana = SubdivisionCode.TR.fromCode("01");
        assertEquals(SubdivisionCode.TR.TR_01, adana);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.TR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision duzce = SubdivisionCode.TR.fromName("Düzce").orElseThrow();
        assertEquals(SubdivisionCode.TR.TR_81, duzce);

        Subdivision adana = SubdivisionCode.TR.fromName("adana").orElseThrow();
        assertEquals(SubdivisionCode.TR.TR_01, adana);

        assertTrue(SubdivisionCode.TR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision duzce = SubdivisionCode.TR.find("81").orElseThrow();
        assertEquals(SubdivisionCode.TR.TR_81, duzce);

        Subdivision duzceFull = SubdivisionCode.TR.find("TR-81").orElseThrow();
        assertEquals(SubdivisionCode.TR.TR_81, duzceFull);

        Subdivision duzceName = SubdivisionCode.TR.find("Düzce").orElseThrow();
        assertEquals(SubdivisionCode.TR.TR_81, duzceName);

        assertTrue(SubdivisionCode.TR.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.TR.getProvinces();
        assertEquals(81, provinces.length);

        // Verify first and last provinces
        assertEquals(SubdivisionCode.TR.TR_01, provinces[0]);
        assertEquals(SubdivisionCode.TR.TR_81, provinces[80]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.TR.wikipedia());
        assertFalse(SubdivisionCode.TR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.TR.wikipedia().contains("ISO_3166-2:TR"));

        assertNotNull(SubdivisionCode.TR.dateAdded());
        assertFalse(SubdivisionCode.TR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.TR.lastUpdated());
        assertFalse(SubdivisionCode.TR.lastUpdated().isBlank());
    }
}
