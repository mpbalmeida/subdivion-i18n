package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionIETest {

    @Test
    public void testIESubdivisions() {
        SubdivisionCode.IE c = SubdivisionCode.IE.C;
        assertEquals("IE-C", c.getCode());
        assertEquals("Connaught", c.getSubdivisionName());
        assertEquals("province", c.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.IE);
        assertNotNull(subdivisions);
        assertEquals(30, subdivisions.length); // 4 provinces + 26 counties

        Subdivision connaught = subdivisions[0];
        assertEquals("IE-C", connaught.getCode());
        assertEquals("Connaught", connaught.getSubdivisionName());

        Subdivision wicklow = subdivisions[29];
        assertEquals("IE-WW", wicklow.getCode());
        assertEquals("Wicklow", wicklow.getSubdivisionName());
        assertEquals("county", wicklow.getCategory());

        Subdivision dublin = SubdivisionCode.IE.D;
        assertEquals("IE-D", dublin.getCode());
        assertEquals("Dublin", dublin.getSubdivisionName());
        assertEquals("county", dublin.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.IE c = SubdivisionCode.IE.fromCode("IE-C");
        assertEquals(SubdivisionCode.IE.C, c);

        // Test lookup by subdivision part
        SubdivisionCode.IE d = SubdivisionCode.IE.fromCode("D");
        assertEquals(SubdivisionCode.IE.D, d);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.IE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision connaught = SubdivisionCode.IE.fromName("Connaught").orElseThrow();
        assertEquals(SubdivisionCode.IE.C, connaught);

        Subdivision dublin = SubdivisionCode.IE.fromName("dublin").orElseThrow();
        assertEquals(SubdivisionCode.IE.D, dublin);

        assertTrue(SubdivisionCode.IE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision d = SubdivisionCode.IE.find("D").orElseThrow();
        assertEquals(SubdivisionCode.IE.D, d);

        Subdivision dFull = SubdivisionCode.IE.find("IE-D").orElseThrow();
        assertEquals(SubdivisionCode.IE.D, dFull);

        Subdivision dublin = SubdivisionCode.IE.find("Dublin").orElseThrow();
        assertEquals(SubdivisionCode.IE.D, dublin);

        assertTrue(SubdivisionCode.IE.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision dublin = SubdivisionCode.IE.D;
        assertTrue(dublin.getParent().isPresent());
        assertEquals(SubdivisionCode.IE.L, dublin.getParent().get());

        Subdivision leinster = SubdivisionCode.IE.L;
        assertFalse(leinster.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.IE.getProvinces();
        assertEquals(4, provinces.length);

        Subdivision[] counties = SubdivisionCode.IE.getCounties();
        assertEquals(26, counties.length);

        Subdivision leinster = SubdivisionCode.IE.L;
        Subdivision[] leinsterCounties = SubdivisionCode.IE.getByParent(leinster);
        assertEquals(12, leinsterCounties.length);

        // Verify one of the counties in Leinster
        boolean hasDublin = false;
        for (Subdivision s : leinsterCounties) {
            if (s.equals(SubdivisionCode.IE.D)) {
                hasDublin = true;
                break;
            }
        }
        assertTrue(hasDublin);
    }
}
