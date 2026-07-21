package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionSITest {

    @Test
    public void testSISubdivisions() {
        SubdivisionCode.SI ajdovscina = SubdivisionCode.SI.SI_001;
        assertEquals("SI-001", ajdovscina.getCode());
        assertEquals("Ajdovščina", ajdovscina.getSubdivisionName());
        assertEquals("municipality", ajdovscina.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.SI);
        assertNotNull(subdivisions);
        assertEquals(212, subdivisions.length); // 200 municipalities + 12 urban municipalities

        Subdivision first = subdivisions[0];
        assertEquals("SI-001", first.getCode());
        assertEquals("Ajdovščina", first.getSubdivisionName());

        Subdivision last = subdivisions[211];
        assertEquals("SI-213", last.getCode());
        assertEquals("Ankaran", last.getSubdivisionName());
        assertEquals("municipality", last.getCategory());

        Subdivision celje = SubdivisionCode.SI.SI_011;
        assertEquals("SI-011", celje.getCode());
        assertEquals("Celje", celje.getSubdivisionName());
        assertEquals("urban municipality", celje.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.SI ajdovscina = SubdivisionCode.SI.fromCode("SI-001");
        assertEquals(SubdivisionCode.SI.SI_001, ajdovscina);

        SubdivisionCode.SI celje = SubdivisionCode.SI.fromCode("011");
        assertEquals(SubdivisionCode.SI.SI_011, celje);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.SI.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision ajdovscina = SubdivisionCode.SI.fromName("Ajdovščina").orElseThrow();
        assertEquals(SubdivisionCode.SI.SI_001, ajdovscina);

        Subdivision ankaran = SubdivisionCode.SI.fromName("ankaran").orElseThrow();
        assertEquals(SubdivisionCode.SI.SI_213, ankaran);

        assertTrue(SubdivisionCode.SI.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision si001 = SubdivisionCode.SI.find("001").orElseThrow();
        assertEquals(SubdivisionCode.SI.SI_001, si001);

        Subdivision si001Full = SubdivisionCode.SI.find("SI-001").orElseThrow();
        assertEquals(SubdivisionCode.SI.SI_001, si001Full);

        Subdivision ankaran = SubdivisionCode.SI.find("Ankaran").orElseThrow();
        assertEquals(SubdivisionCode.SI.SI_213, ankaran);

        assertTrue(SubdivisionCode.SI.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] municipalities = SubdivisionCode.SI.getMunicipalities();
        assertEquals(200, municipalities.length);

        Subdivision[] urbanMunicipalities = SubdivisionCode.SI.getUrbanMunicipalities();
        assertEquals(12, urbanMunicipalities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.SI.wikipedia());
        assertFalse(SubdivisionCode.SI.wikipedia().isBlank());
        assertTrue(SubdivisionCode.SI.wikipedia().contains("ISO_3166-2:SI"));

        assertNotNull(SubdivisionCode.SI.dateAdded());
        assertFalse(SubdivisionCode.SI.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.SI.lastUpdated());
        assertFalse(SubdivisionCode.SI.lastUpdated().isBlank());
    }
}
