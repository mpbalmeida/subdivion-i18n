package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionROTest {

    @Test
    public void testROSubdivisions() {
        SubdivisionCode.RO ab = SubdivisionCode.RO.AB;
        assertEquals("RO-AB", ab.getCode());
        assertEquals("Alba", ab.getSubdivisionName());
        assertEquals("department", ab.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.RO);
        assertNotNull(subdivisions);
        assertEquals(42, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("RO-AB", first.getCode());
        assertEquals("Alba", first.getSubdivisionName());

        Subdivision last = subdivisions[41];
        assertEquals("RO-VS", last.getCode());
        assertEquals("Vaslui", last.getSubdivisionName());
        assertEquals("department", last.getCategory());

        Subdivision bucuresti = SubdivisionCode.RO.B;
        assertEquals("RO-B", bucuresti.getCode());
        assertEquals("București", bucuresti.getSubdivisionName());
        assertEquals("municipality", bucuresti.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.RO ab = SubdivisionCode.RO.fromCode("RO-AB");
        assertEquals(SubdivisionCode.RO.AB, ab);

        SubdivisionCode.RO b = SubdivisionCode.RO.fromCode("B");
        assertEquals(SubdivisionCode.RO.B, b);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.RO.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision cluj = SubdivisionCode.RO.fromName("Cluj").orElseThrow();
        assertEquals(SubdivisionCode.RO.CJ, cluj);

        Subdivision bucuresti = SubdivisionCode.RO.fromName("bucurești").orElseThrow();
        assertEquals(SubdivisionCode.RO.B, bucuresti);

        assertTrue(SubdivisionCode.RO.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision b = SubdivisionCode.RO.find("B").orElseThrow();
        assertEquals(SubdivisionCode.RO.B, b);

        Subdivision bFull = SubdivisionCode.RO.find("RO-B").orElseThrow();
        assertEquals(SubdivisionCode.RO.B, bFull);

        Subdivision timis = SubdivisionCode.RO.find("Timiș").orElseThrow();
        assertEquals(SubdivisionCode.RO.TM, timis);

        assertTrue(SubdivisionCode.RO.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.RO.getDepartments();
        assertEquals(41, departments.length);

        Subdivision[] municipalities = SubdivisionCode.RO.getMunicipalities();
        assertEquals(1, municipalities.length);
        assertEquals(SubdivisionCode.RO.B, municipalities[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.RO.wikipedia());
        assertFalse(SubdivisionCode.RO.wikipedia().isBlank());
        assertTrue(SubdivisionCode.RO.wikipedia().contains("ISO_3166-2:RO"));

        assertNotNull(SubdivisionCode.RO.dateAdded());
        assertFalse(SubdivisionCode.RO.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.RO.lastUpdated());
        assertFalse(SubdivisionCode.RO.lastUpdated().isBlank());
    }
}
