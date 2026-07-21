package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionSMTest {

    @Test
    public void testSMSubdivisions() {
        SubdivisionCode.SM acquaviva = SubdivisionCode.SM.SM_01;
        assertEquals("SM-01", acquaviva.getCode());
        assertEquals("Acquaviva", acquaviva.getSubdivisionName());
        assertEquals("municipality", acquaviva.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.SM);
        assertNotNull(subdivisions);
        assertEquals(9, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("SM-01", first.getCode());
        assertEquals("Acquaviva", first.getSubdivisionName());

        Subdivision last = subdivisions[8];
        assertEquals("SM-09", last.getCode());
        assertEquals("Serravalle", last.getSubdivisionName());
        assertEquals("municipality", last.getCategory());

        Subdivision citta = SubdivisionCode.SM.SM_07;
        assertEquals("SM-07", citta.getCode());
        assertEquals("Città di San Marino", citta.getSubdivisionName());
        assertEquals("municipality", citta.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.SM sm07 = SubdivisionCode.SM.fromCode("SM-07");
        assertEquals(SubdivisionCode.SM.SM_07, sm07);

        // Test lookup by subdivision part
        SubdivisionCode.SM sm09 = SubdivisionCode.SM.fromCode("09");
        assertEquals(SubdivisionCode.SM.SM_09, sm09);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.SM.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision borgoMaggiore = SubdivisionCode.SM.fromName("Borgo Maggiore").orElseThrow();
        assertEquals(SubdivisionCode.SM.SM_06, borgoMaggiore);

        Subdivision serravalle = SubdivisionCode.SM.fromName("serravalle").orElseThrow();
        assertEquals(SubdivisionCode.SM.SM_09, serravalle);

        assertTrue(SubdivisionCode.SM.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision s08 = SubdivisionCode.SM.find("08").orElseThrow();
        assertEquals(SubdivisionCode.SM.SM_08, s08);

        Subdivision s08Full = SubdivisionCode.SM.find("SM-08").orElseThrow();
        assertEquals(SubdivisionCode.SM.SM_08, s08Full);

        Subdivision chiesanuova = SubdivisionCode.SM.find("Chiesanuova").orElseThrow();
        assertEquals(SubdivisionCode.SM.SM_02, chiesanuova);

        assertTrue(SubdivisionCode.SM.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] municipalities = SubdivisionCode.SM.getMunicipalities();
        assertEquals(9, municipalities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.SM.wikipedia());
        assertFalse(SubdivisionCode.SM.wikipedia().isBlank());
        assertTrue(SubdivisionCode.SM.wikipedia().contains("ISO_3166-2:SM"));

        assertNotNull(SubdivisionCode.SM.dateAdded());
        assertFalse(SubdivisionCode.SM.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.SM.lastUpdated());
        assertFalse(SubdivisionCode.SM.lastUpdated().isBlank());
    }
}
