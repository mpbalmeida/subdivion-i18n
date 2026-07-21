package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionALTest {

    @Test
    public void testALSubdivisions() {
        SubdivisionCode.AL berat = SubdivisionCode.AL.AL_01;
        assertEquals("AL-01", berat.getCode());
        assertEquals("Berat", berat.getSubdivisionName());
        assertEquals("county", berat.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.AL);
        assertNotNull(subdivisions);
        assertEquals(12, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("AL-01", first.getCode());
        assertEquals("Berat", first.getSubdivisionName());

        Subdivision last = subdivisions[11];
        assertEquals("AL-12", last.getCode());
        assertEquals("Vlorë", last.getSubdivisionName());
        assertEquals("county", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AL berat = SubdivisionCode.AL.fromCode("AL-01");
        assertEquals(SubdivisionCode.AL.AL_01, berat);

        SubdivisionCode.AL vlore = SubdivisionCode.AL.fromCode("12");
        assertEquals(SubdivisionCode.AL.AL_12, vlore);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AL.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision tirane = SubdivisionCode.AL.fromName("Tiranë").orElseThrow();
        assertEquals(SubdivisionCode.AL.AL_11, tirane);

        Subdivision shkoder = SubdivisionCode.AL.fromName("shkodër").orElseThrow();
        assertEquals(SubdivisionCode.AL.AL_10, shkoder);

        assertTrue(SubdivisionCode.AL.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision dibra = SubdivisionCode.AL.find("09").orElseThrow();
        assertEquals(SubdivisionCode.AL.AL_09, dibra);

        Subdivision dibraFull = SubdivisionCode.AL.find("AL-09").orElseThrow();
        assertEquals(SubdivisionCode.AL.AL_09, dibraFull);

        Subdivision durres = SubdivisionCode.AL.find("Durrës").orElseThrow();
        assertEquals(SubdivisionCode.AL.AL_02, durres);

        assertTrue(SubdivisionCode.AL.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.AL.getCounties();
        assertEquals(12, counties.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.AL.wikipedia());
        assertFalse(SubdivisionCode.AL.wikipedia().isBlank());
        assertTrue(SubdivisionCode.AL.wikipedia().contains("ISO_3166-2:AL"));

        assertNotNull(SubdivisionCode.AL.dateAdded());
        assertFalse(SubdivisionCode.AL.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.AL.lastUpdated());
        assertFalse(SubdivisionCode.AL.lastUpdated().isBlank());
    }
}
