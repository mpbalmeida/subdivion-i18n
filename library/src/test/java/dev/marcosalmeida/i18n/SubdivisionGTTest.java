package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionGTTest {

    @Test
    public void testGTSubdivisions() {
        SubdivisionCode.GT guatemala = SubdivisionCode.GT.GT_01;
        assertEquals("GT-01", guatemala.getCode());
        assertEquals("Guatemala", guatemala.getSubdivisionName());
        assertEquals("department", guatemala.getCategory());

        SubdivisionCode.GT peten = SubdivisionCode.GT.GT_17;
        assertEquals("GT-17", peten.getCode());
        assertEquals("Petén", peten.getSubdivisionName());
        assertEquals("department", peten.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.GT);
        assertNotNull(subdivisions);
        assertEquals(22, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("GT-01", first.getCode());
        assertEquals("Guatemala", first.getSubdivisionName());

        Subdivision last = subdivisions[21];
        assertEquals("GT-22", last.getCode());
        assertEquals("Jutiapa", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.GT guatemala = SubdivisionCode.GT.fromCode("GT-01");
        assertEquals(SubdivisionCode.GT.GT_01, guatemala);

        SubdivisionCode.GT quetzaltenango = SubdivisionCode.GT.fromCode("09");
        assertEquals(SubdivisionCode.GT.GT_09, quetzaltenango);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.GT.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision peten = SubdivisionCode.GT.fromName("Petén").orElseThrow();
        assertEquals(SubdivisionCode.GT.GT_17, peten);

        Subdivision quiche = SubdivisionCode.GT.fromName("quiché").orElseThrow();
        assertEquals(SubdivisionCode.GT.GT_14, quiche);

        assertTrue(SubdivisionCode.GT.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision solola = SubdivisionCode.GT.find("07").orElseThrow();
        assertEquals(SubdivisionCode.GT.GT_07, solola);

        Subdivision sololaFull = SubdivisionCode.GT.find("GT-07").orElseThrow();
        assertEquals(SubdivisionCode.GT.GT_07, sololaFull);

        Subdivision huehuetenango = SubdivisionCode.GT.find("Huehuetenango").orElseThrow();
        assertEquals(SubdivisionCode.GT.GT_13, huehuetenango);

        assertTrue(SubdivisionCode.GT.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.GT.getDepartments();
        assertEquals(22, departments.length);
        assertEquals(SubdivisionCode.GT.GT_01, departments[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.GT.wikipedia());
        assertFalse(SubdivisionCode.GT.wikipedia().isBlank());
        assertTrue(SubdivisionCode.GT.wikipedia().contains("ISO_3166-2:GT"));

        assertNotNull(SubdivisionCode.GT.dateAdded());
        assertFalse(SubdivisionCode.GT.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.GT.lastUpdated());
        assertFalse(SubdivisionCode.GT.lastUpdated().isBlank());
    }
}
