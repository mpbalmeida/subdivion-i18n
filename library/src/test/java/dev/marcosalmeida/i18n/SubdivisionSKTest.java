package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionSKTest {

    @Test
    public void testSKSubdivisions() {
        SubdivisionCode.SK bc = SubdivisionCode.SK.BC;
        assertEquals("SK-BC", bc.getCode());
        assertEquals("Banskobystrický kraj", bc.getSubdivisionName());
        assertEquals("region", bc.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.SK);
        assertNotNull(subdivisions);
        assertEquals(8, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("SK-BC", first.getCode());
        assertEquals("Banskobystrický kraj", first.getSubdivisionName());

        Subdivision last = subdivisions[7];
        assertEquals("SK-ZI", last.getCode());
        assertEquals("Žilinský kraj", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.SK tc = SubdivisionCode.SK.fromCode("SK-TC");
        assertEquals(SubdivisionCode.SK.TC, tc);

        SubdivisionCode.SK ki = SubdivisionCode.SK.fromCode("KI");
        assertEquals(SubdivisionCode.SK.KI, ki);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.SK.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision trnava = SubdivisionCode.SK.fromName("Trnavský kraj").orElseThrow();
        assertEquals(SubdivisionCode.SK.TA, trnava);

        Subdivision presov = SubdivisionCode.SK.fromName("prešovský kraj").orElseThrow();
        assertEquals(SubdivisionCode.SK.PV, presov);

        assertTrue(SubdivisionCode.SK.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision bl = SubdivisionCode.SK.find("BL").orElseThrow();
        assertEquals(SubdivisionCode.SK.BL, bl);

        Subdivision blFull = SubdivisionCode.SK.find("SK-BL").orElseThrow();
        assertEquals(SubdivisionCode.SK.BL, blFull);

        Subdivision nitra = SubdivisionCode.SK.find("Nitriansky kraj").orElseThrow();
        assertEquals(SubdivisionCode.SK.NI, nitra);

        assertTrue(SubdivisionCode.SK.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.SK.getRegions();
        assertEquals(8, regions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.SK.wikipedia());
        assertFalse(SubdivisionCode.SK.wikipedia().isBlank());
        assertTrue(SubdivisionCode.SK.wikipedia().contains("ISO_3166-2:SK"));

        assertNotNull(SubdivisionCode.SK.dateAdded());
        assertFalse(SubdivisionCode.SK.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.SK.lastUpdated());
        assertFalse(SubdivisionCode.SK.lastUpdated().isBlank());
    }
}
