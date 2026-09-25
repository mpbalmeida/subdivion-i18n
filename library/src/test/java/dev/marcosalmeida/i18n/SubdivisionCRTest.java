package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCRTest {

    @Test
    public void testCRSubdivisions() {
        SubdivisionCode.CR alajuela = SubdivisionCode.CR.A;
        assertEquals("CR-A", alajuela.getCode());
        assertEquals("Alajuela", alajuela.getSubdivisionName());
        assertEquals("province", alajuela.getCategory());

        SubdivisionCode.CR sanJose = SubdivisionCode.CR.SJ;
        assertEquals("CR-SJ", sanJose.getCode());
        assertEquals("San José", sanJose.getSubdivisionName());
        assertEquals("province", sanJose.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.CR);
        assertNotNull(subdivisions);
        assertEquals(7, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("CR-A", first.getCode());
        assertEquals("Alajuela", first.getSubdivisionName());

        Subdivision last = subdivisions[6];
        assertEquals("CR-SJ", last.getCode());
        assertEquals("San José", last.getSubdivisionName());
        assertEquals("province", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CR alajuela = SubdivisionCode.CR.fromCode("CR-A");
        assertEquals(SubdivisionCode.CR.A, alajuela);

        SubdivisionCode.CR sanJose = SubdivisionCode.CR.fromCode("SJ");
        assertEquals(SubdivisionCode.CR.SJ, sanJose);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision alajuela = SubdivisionCode.CR.fromName("Alajuela").orElseThrow();
        assertEquals(SubdivisionCode.CR.A, alajuela);

        Subdivision limon = SubdivisionCode.CR.fromName("limón").orElseThrow();
        assertEquals(SubdivisionCode.CR.L, limon);

        assertTrue(SubdivisionCode.CR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision puntarenas = SubdivisionCode.CR.find("P").orElseThrow();
        assertEquals(SubdivisionCode.CR.P, puntarenas);

        Subdivision puntarenasFull = SubdivisionCode.CR.find("CR-P").orElseThrow();
        assertEquals(SubdivisionCode.CR.P, puntarenasFull);

        Subdivision guanacaste = SubdivisionCode.CR.find("Guanacaste").orElseThrow();
        assertEquals(SubdivisionCode.CR.G, guanacaste);

        assertTrue(SubdivisionCode.CR.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.CR.getProvinces();
        assertEquals(7, provinces.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.CR.wikipedia());
        assertFalse(SubdivisionCode.CR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.CR.wikipedia().contains("ISO_3166-2:CR"));

        assertNotNull(SubdivisionCode.CR.dateAdded());
        assertFalse(SubdivisionCode.CR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.CR.lastUpdated());
        assertFalse(SubdivisionCode.CR.lastUpdated().isBlank());
    }
}
