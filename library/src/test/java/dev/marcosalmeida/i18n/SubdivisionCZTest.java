package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCZTest {

    @Test
    public void testCZSubdivisions() {
        SubdivisionCode.CZ praha = SubdivisionCode.CZ.CZ_10;
        assertEquals("CZ-10", praha.getCode());
        assertEquals("Praha, Hlavní město", praha.getSubdivisionName());
        assertEquals("capital city", praha.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.CZ);
        assertNotNull(subdivisions);
        assertEquals(90, subdivisions.length); // 14 top-level + 76 districts

        Subdivision first = subdivisions[0];
        assertEquals("CZ-10", first.getCode());
        assertEquals("Praha, Hlavní město", first.getSubdivisionName());

        Subdivision last = subdivisions[89];
        assertEquals("CZ-806", last.getCode());
        assertEquals("Ostrava-město", last.getSubdivisionName());
        assertEquals("district", last.getCategory());

        Subdivision stredocesky = SubdivisionCode.CZ.CZ_20;
        assertEquals("CZ-20", stredocesky.getCode());
        assertEquals("Středočeský kraj", stredocesky.getSubdivisionName());
        assertEquals("region", stredocesky.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CZ praha = SubdivisionCode.CZ.fromCode("CZ-10");
        assertEquals(SubdivisionCode.CZ.CZ_10, praha);

        // Test lookup by subdivision part
        SubdivisionCode.CZ stredocesky = SubdivisionCode.CZ.fromCode("20");
        assertEquals(SubdivisionCode.CZ.CZ_20, stredocesky);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CZ.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision praha = SubdivisionCode.CZ.fromName("Praha, Hlavní město").orElseThrow();
        assertEquals(SubdivisionCode.CZ.CZ_10, praha);

        Subdivision ostrava = SubdivisionCode.CZ.fromName("ostrava-město").orElseThrow();
        assertEquals(SubdivisionCode.CZ.CZ_806, ostrava);

        assertTrue(SubdivisionCode.CZ.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision deset = SubdivisionCode.CZ.find("10").orElseThrow();
        assertEquals(SubdivisionCode.CZ.CZ_10, deset);

        Subdivision desetFull = SubdivisionCode.CZ.find("CZ-10").orElseThrow();
        assertEquals(SubdivisionCode.CZ.CZ_10, desetFull);

        Subdivision praha = SubdivisionCode.CZ.find("Praha, Hlavní město").orElseThrow();
        assertEquals(SubdivisionCode.CZ.CZ_10, praha);

        assertTrue(SubdivisionCode.CZ.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision benesov = SubdivisionCode.CZ.CZ_201;
        assertTrue(benesov.getParent().isPresent());
        assertEquals(SubdivisionCode.CZ.CZ_20, benesov.getParent().get());

        Subdivision praha = SubdivisionCode.CZ.CZ_10;
        assertFalse(praha.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.CZ.getRegions();
        assertEquals(13, regions.length);

        Subdivision[] districts = SubdivisionCode.CZ.getDistricts();
        assertEquals(76, districts.length);

        Subdivision stredocesky = SubdivisionCode.CZ.CZ_20;
        Subdivision[] stredoceskeDistricts = SubdivisionCode.CZ.getByParent(stredocesky);
        assertEquals(12, stredoceskeDistricts.length);

        // Verify one of the districts in Central Bohemia
        boolean hasBenesov = false;
        for (Subdivision s : stredoceskeDistricts) {
            if (s.equals(SubdivisionCode.CZ.CZ_201)) {
                hasBenesov = true;
                break;
            }
        }
        assertTrue(hasBenesov);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.CZ.wikipedia());
        assertFalse(SubdivisionCode.CZ.wikipedia().isBlank());
        assertTrue(SubdivisionCode.CZ.wikipedia().contains("ISO_3166-2:CZ"));

        assertNotNull(SubdivisionCode.CZ.dateAdded());
        assertFalse(SubdivisionCode.CZ.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.CZ.lastUpdated());
        assertFalse(SubdivisionCode.CZ.lastUpdated().isBlank());
    }
}
