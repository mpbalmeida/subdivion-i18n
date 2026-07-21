package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionISTest {

    @Test
    public void testISSubdivisions() {
        SubdivisionCode.IS capitalRegion = SubdivisionCode.IS.IS_1;
        assertEquals("IS-1", capitalRegion.getCode());
        assertEquals("Höfuðborgarsvæði", capitalRegion.getSubdivisionName());
        assertEquals("region", capitalRegion.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.IS);
        assertNotNull(subdivisions);
        assertEquals(72, subdivisions.length); // 8 regions + 64 municipalities

        Subdivision first = subdivisions[0];
        assertEquals("IS-1", first.getCode());
        assertEquals("Höfuðborgarsvæði", first.getSubdivisionName());

        Subdivision last = subdivisions[71];
        assertEquals("IS-VOP", last.getCode());
        assertEquals("Vopnafjarðarhreppur", last.getSubdivisionName());
        assertEquals("municipality", last.getCategory());

        Subdivision reykjavik = SubdivisionCode.IS.IS_RKV;
        assertEquals("IS-RKV", reykjavik.getCode());
        assertEquals("Reykjavíkurborg", reykjavik.getSubdivisionName());
        assertEquals("municipality", reykjavik.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.IS c = SubdivisionCode.IS.fromCode("IS-1");
        assertEquals(SubdivisionCode.IS.IS_1, c);

        // Test lookup by subdivision part
        SubdivisionCode.IS rkv = SubdivisionCode.IS.fromCode("RKV");
        assertEquals(SubdivisionCode.IS.IS_RKV, rkv);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.IS.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision capitalRegion = SubdivisionCode.IS.fromName("Höfuðborgarsvæði").orElseThrow();
        assertEquals(SubdivisionCode.IS.IS_1, capitalRegion);

        Subdivision reykjavik = SubdivisionCode.IS.fromName("reykjavíkurborg").orElseThrow();
        assertEquals(SubdivisionCode.IS.IS_RKV, reykjavik);

        assertTrue(SubdivisionCode.IS.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision is1 = SubdivisionCode.IS.find("1").orElseThrow();
        assertEquals(SubdivisionCode.IS.IS_1, is1);

        Subdivision is1Full = SubdivisionCode.IS.find("IS-1").orElseThrow();
        assertEquals(SubdivisionCode.IS.IS_1, is1Full);

        Subdivision reykjavik = SubdivisionCode.IS.find("Reykjavíkurborg").orElseThrow();
        assertEquals(SubdivisionCode.IS.IS_RKV, reykjavik);

        assertTrue(SubdivisionCode.IS.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision reykjavik = SubdivisionCode.IS.IS_RKV;
        assertTrue(reykjavik.getParent().isPresent());
        assertEquals(SubdivisionCode.IS.IS_1, reykjavik.getParent().get());

        Subdivision capitalRegion = SubdivisionCode.IS.IS_1;
        assertFalse(capitalRegion.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.IS.getRegions();
        assertEquals(8, regions.length);

        Subdivision[] municipalities = SubdivisionCode.IS.getMunicipalities();
        assertEquals(64, municipalities.length);

        Subdivision capitalRegion = SubdivisionCode.IS.IS_1;
        Subdivision[] capitalRegionMunicipalities = SubdivisionCode.IS.getByParent(capitalRegion);
        assertEquals(7, capitalRegionMunicipalities.length);

        // Verify one of the municipalities in Capital Region
        boolean hasReykjavik = false;
        for (Subdivision s : capitalRegionMunicipalities) {
            if (s.equals(SubdivisionCode.IS.IS_RKV)) {
                hasReykjavik = true;
                break;
            }
        }
        assertTrue(hasReykjavik);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.IS.wikipedia());
        assertFalse(SubdivisionCode.IS.wikipedia().isBlank());
        assertTrue(SubdivisionCode.IS.wikipedia().contains("ISO_3166-2:IS"));

        assertNotNull(SubdivisionCode.IS.dateAdded());
        assertFalse(SubdivisionCode.IS.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.IS.lastUpdated());
        assertFalse(SubdivisionCode.IS.lastUpdated().isBlank());
    }
}
