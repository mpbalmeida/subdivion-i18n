package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionHUTest {

    @Test
    public void testHUSubdivisions() {
        SubdivisionCode.HU ba = SubdivisionCode.HU.BA;
        assertEquals("HU-BA", ba.getCode());
        assertEquals("Baranya", ba.getSubdivisionName());
        assertEquals("county", ba.getCategory());

        SubdivisionCode.HU bu = SubdivisionCode.HU.BU;
        assertEquals("HU-BU", bu.getCode());
        assertEquals("Budapest", bu.getSubdivisionName());
        assertEquals("capital city", bu.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.HU);
        assertNotNull(subdivisions);
        assertEquals(43, subdivisions.length); // 1 capital + 19 counties + 23 cities with county rights

        Subdivision first = subdivisions[0];
        assertEquals("HU-BA", first.getCode());
        assertEquals("Baranya", first.getSubdivisionName());

        Subdivision last = subdivisions[42];
        assertEquals("HU-ZE", last.getCode());
        assertEquals("Zalaegerszeg", last.getSubdivisionName());
        assertEquals("city with county rights", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.HU bu = SubdivisionCode.HU.fromCode("HU-BU");
        assertEquals(SubdivisionCode.HU.BU, bu);

        SubdivisionCode.HU de = SubdivisionCode.HU.fromCode("DE");
        assertEquals(SubdivisionCode.HU.DE, de);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.HU.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision budapest = SubdivisionCode.HU.fromName("Budapest").orElseThrow();
        assertEquals(SubdivisionCode.HU.BU, budapest);

        Subdivision debrecen = SubdivisionCode.HU.fromName("debrecen").orElseThrow();
        assertEquals(SubdivisionCode.HU.DE, debrecen);

        assertTrue(SubdivisionCode.HU.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision ba = SubdivisionCode.HU.find("BA").orElseThrow();
        assertEquals(SubdivisionCode.HU.BA, ba);

        Subdivision baFull = SubdivisionCode.HU.find("HU-BA").orElseThrow();
        assertEquals(SubdivisionCode.HU.BA, baFull);

        Subdivision baranya = SubdivisionCode.HU.find("Baranya").orElseThrow();
        assertEquals(SubdivisionCode.HU.BA, baranya);

        assertTrue(SubdivisionCode.HU.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        // No parent-child relationships in HU subdivisions
        SubdivisionCode.HU ba = SubdivisionCode.HU.BA;
        assertFalse(ba.getParent().isPresent());

        SubdivisionCode.HU bu = SubdivisionCode.HU.BU;
        assertFalse(bu.getParent().isPresent());

        SubdivisionCode.HU ze = SubdivisionCode.HU.ZE;
        assertFalse(ze.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.HU.getCounties();
        assertEquals(19, counties.length);

        boolean hasBaranya = false;
        boolean hasPest = false;
        for (Subdivision s : counties) {
            if (s.equals(SubdivisionCode.HU.BA)) hasBaranya = true;
            if (s.equals(SubdivisionCode.HU.PE)) hasPest = true;
        }
        assertTrue(hasBaranya);
        assertTrue(hasPest);

        // Verify no capital city or city with county rights in counties
        for (Subdivision s : counties) {
            assertNotEquals(SubdivisionCode.HU.BU, s);
            assertNotEquals(SubdivisionCode.HU.DE, s);
        }

        Subdivision[] capitalCities = SubdivisionCode.HU.getCapitalCitys();
        assertEquals(1, capitalCities.length);
        assertEquals("HU-BU", capitalCities[0].getCode());

        Subdivision[] citiesWithCountyRights = SubdivisionCode.HU.getCityWithCountyRightss();
        assertEquals(23, citiesWithCountyRights.length);

        boolean hasDebrecen = false;
        boolean hasPecs = false;
        for (Subdivision s : citiesWithCountyRights) {
            if (s.equals(SubdivisionCode.HU.DE)) hasDebrecen = true;
            if (s.equals(SubdivisionCode.HU.PS)) hasPecs = true;
        }
        assertTrue(hasDebrecen);
        assertTrue(hasPecs);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.HU.wikipedia());
        assertFalse(SubdivisionCode.HU.wikipedia().isBlank());
        assertTrue(SubdivisionCode.HU.wikipedia().contains("ISO_3166-2:HU"));

        assertNotNull(SubdivisionCode.HU.dateAdded());
        assertFalse(SubdivisionCode.HU.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.HU.lastUpdated());
        assertFalse(SubdivisionCode.HU.lastUpdated().isBlank());
    }
}
