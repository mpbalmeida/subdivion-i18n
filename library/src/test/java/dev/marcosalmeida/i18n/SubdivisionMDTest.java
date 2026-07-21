package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionMDTest {

    @Test
    public void testMDSubdivisions() {
        SubdivisionCode.MD an = SubdivisionCode.MD.AN;
        assertEquals("MD-AN", an.getCode());
        assertEquals("Anenii Noi", an.getSubdivisionName());
        assertEquals("district", an.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.MD);
        assertNotNull(subdivisions);
        assertEquals(37, subdivisions.length); // 32 districts + 3 cities + 1 autonomous territorial unit + 1 territorial unit

        Subdivision first = subdivisions[0];
        assertEquals("MD-AN", first.getCode());
        assertEquals("Anenii Noi", first.getSubdivisionName());

        Subdivision last = subdivisions[36];
        assertEquals("MD-UN", last.getCode());
        assertEquals("Ungheni", last.getSubdivisionName());
        assertEquals("district", last.getCategory());

        Subdivision chisinau = SubdivisionCode.MD.CU;
        assertEquals("MD-CU", chisinau.getCode());
        assertEquals("Chișinău", chisinau.getSubdivisionName());
        assertEquals("city", chisinau.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.MD cu = SubdivisionCode.MD.fromCode("MD-CU");
        assertEquals(SubdivisionCode.MD.CU, cu);

        // Test lookup by subdivision part
        SubdivisionCode.MD cuShort = SubdivisionCode.MD.fromCode("CU");
        assertEquals(SubdivisionCode.MD.CU, cuShort);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.MD.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision chisinau = SubdivisionCode.MD.fromName("Chișinău").orElseThrow();
        assertEquals(SubdivisionCode.MD.CU, chisinau);

        Subdivision balti = SubdivisionCode.MD.fromName("bălți").orElseThrow();
        assertEquals(SubdivisionCode.MD.BA, balti);

        assertTrue(SubdivisionCode.MD.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision cu = SubdivisionCode.MD.find("CU").orElseThrow();
        assertEquals(SubdivisionCode.MD.CU, cu);

        Subdivision cuFull = SubdivisionCode.MD.find("MD-CU").orElseThrow();
        assertEquals(SubdivisionCode.MD.CU, cuFull);

        Subdivision chisinau = SubdivisionCode.MD.find("Chișinău").orElseThrow();
        assertEquals(SubdivisionCode.MD.CU, chisinau);

        assertTrue(SubdivisionCode.MD.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] cities = SubdivisionCode.MD.getCities();
        assertEquals(3, cities.length);

        Subdivision[] districts = SubdivisionCode.MD.getDistricts();
        assertEquals(32, districts.length);

        Subdivision[] autonomousTerritorialUnits = SubdivisionCode.MD.getAutonomousTerritorialUnits();
        assertEquals(1, autonomousTerritorialUnits.length);
        assertEquals(SubdivisionCode.MD.GA, autonomousTerritorialUnits[0]);

        Subdivision[] territorialUnits = SubdivisionCode.MD.getTerritorialUnits();
        assertEquals(1, territorialUnits.length);
        assertEquals(SubdivisionCode.MD.SN, territorialUnits[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.MD.wikipedia());
        assertFalse(SubdivisionCode.MD.wikipedia().isBlank());

        assertNotNull(SubdivisionCode.MD.dateAdded());
        assertFalse(SubdivisionCode.MD.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.MD.lastUpdated());
        assertFalse(SubdivisionCode.MD.lastUpdated().isBlank());
    }
}
