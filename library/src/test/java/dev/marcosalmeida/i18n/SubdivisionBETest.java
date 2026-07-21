package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBETest {

    @Test
    public void testBESubdivisions() {
        SubdivisionCode.BE c = SubdivisionCode.BE.BRU;
        assertEquals("BE-BRU", c.getCode());
        assertEquals("Brussels", c.getSubdivisionName());
        assertEquals("region", c.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.BE);
        assertNotNull(subdivisions);
        assertEquals(13, subdivisions.length); // 3 regions + 10 provinces

        Subdivision brussels = subdivisions[0];
        assertEquals("BE-BRU", brussels.getCode());
        assertEquals("Brussels", brussels.getSubdivisionName());

        Subdivision namur = subdivisions[12];
        assertEquals("BE-WNA", namur.getCode());
        assertEquals("Namur", namur.getSubdivisionName());
        assertEquals("province", namur.getCategory());

        Subdivision antwerp = SubdivisionCode.BE.VAN;
        assertEquals("BE-VAN", antwerp.getCode());
        assertEquals("Antwerp", antwerp.getSubdivisionName());
        assertEquals("province", antwerp.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BE c = SubdivisionCode.BE.fromCode("BE-BRU");
        assertEquals(SubdivisionCode.BE.BRU, c);

        // Test lookup by subdivision part
        SubdivisionCode.BE d = SubdivisionCode.BE.fromCode("BRU");
        assertEquals(SubdivisionCode.BE.BRU, d);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision brussels = SubdivisionCode.BE.fromName("Brussels").orElseThrow();
        assertEquals(SubdivisionCode.BE.BRU, brussels);

        Subdivision flanders = SubdivisionCode.BE.fromName("flanders").orElseThrow();
        assertEquals(SubdivisionCode.BE.VLG, flanders);

        assertTrue(SubdivisionCode.BE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision d = SubdivisionCode.BE.find("BRU").orElseThrow();
        assertEquals(SubdivisionCode.BE.BRU, d);

        Subdivision dFull = SubdivisionCode.BE.find("BE-BRU").orElseThrow();
        assertEquals(SubdivisionCode.BE.BRU, dFull);

        Subdivision brussels = SubdivisionCode.BE.find("Brussels").orElseThrow();
        assertEquals(SubdivisionCode.BE.BRU, brussels);

        assertTrue(SubdivisionCode.BE.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision antwerp = SubdivisionCode.BE.VAN;
        assertTrue(antwerp.getParent().isPresent());
        assertEquals(SubdivisionCode.BE.VLG, antwerp.getParent().get());

        Subdivision flanders = SubdivisionCode.BE.VLG;
        assertFalse(flanders.getParent().isPresent());

        Subdivision brussels = SubdivisionCode.BE.BRU;
        assertFalse(brussels.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.BE.getRegions();
        assertEquals(3, regions.length);

        Subdivision[] provinces = SubdivisionCode.BE.getProvinces();
        assertEquals(10, provinces.length);

        Subdivision flanders = SubdivisionCode.BE.VLG;
        Subdivision[] flandersProvinces = SubdivisionCode.BE.getByParent(flanders);
        assertEquals(5, flandersProvinces.length);

        // Verify one of the provinces in Flanders
        boolean hasAntwerp = false;
        for (Subdivision s : flandersProvinces) {
            if (s.equals(SubdivisionCode.BE.VAN)) {
                hasAntwerp = true;
                break;
            }
        }
        assertTrue(hasAntwerp);

        Subdivision wallonia = SubdivisionCode.BE.WAL;
        Subdivision[] walloniaProvinces = SubdivisionCode.BE.getByParent(wallonia);
        assertEquals(5, walloniaProvinces.length);
    }

    @Test
    public void testAuditFields() {
        Subdivision s = SubdivisionCode.BE.BRU;
        assertNotNull(s.wikipedia());
        assertFalse(s.wikipedia().isBlank());
        assertNotNull(s.dateAdded());
        assertFalse(s.dateAdded().isBlank());
        assertNotNull(s.lastUpdated());
        assertFalse(s.lastUpdated().isBlank());
    }
}
