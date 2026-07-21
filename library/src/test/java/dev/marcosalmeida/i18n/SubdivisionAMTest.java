package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionAMTest {

    @Test
    public void testAMSubdivisions() {
        SubdivisionCode.AM ag = SubdivisionCode.AM.AG;
        assertEquals("AM-AG", ag.getCode());
        assertEquals("Aragatsotn", ag.getSubdivisionName());
        assertEquals("region", ag.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.AM);
        assertNotNull(subdivisions);
        assertEquals(11, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("AM-AG", first.getCode());
        assertEquals("Aragatsotn", first.getSubdivisionName());

        Subdivision last = subdivisions[10];
        assertEquals("AM-VD", last.getCode());
        assertEquals("Vayots Dzor", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AM vd = SubdivisionCode.AM.fromCode("AM-VD");
        assertEquals(SubdivisionCode.AM.VD, vd);

        SubdivisionCode.AM er = SubdivisionCode.AM.fromCode("ER");
        assertEquals(SubdivisionCode.AM.ER, er);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AM.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision shirak = SubdivisionCode.AM.fromName("Shirak").orElseThrow();
        assertEquals(SubdivisionCode.AM.SH, shirak);

        Subdivision yerevan = SubdivisionCode.AM.fromName("yerevan").orElseThrow();
        assertEquals(SubdivisionCode.AM.ER, yerevan);

        assertTrue(SubdivisionCode.AM.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision lo = SubdivisionCode.AM.find("LO").orElseThrow();
        assertEquals(SubdivisionCode.AM.LO, lo);

        Subdivision loFull = SubdivisionCode.AM.find("AM-LO").orElseThrow();
        assertEquals(SubdivisionCode.AM.LO, loFull);

        Subdivision lori = SubdivisionCode.AM.find("Lori").orElseThrow();
        assertEquals(SubdivisionCode.AM.LO, lori);

        assertTrue(SubdivisionCode.AM.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.AM.getRegions();
        assertEquals(10, regions.length);

        Subdivision[] cities = SubdivisionCode.AM.getCitys();
        assertEquals(1, cities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.AM.wikipedia());
        assertFalse(SubdivisionCode.AM.wikipedia().isBlank());
        assertTrue(SubdivisionCode.AM.wikipedia().contains("ISO_3166-2:AM"));

        assertNotNull(SubdivisionCode.AM.dateAdded());
        assertFalse(SubdivisionCode.AM.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.AM.lastUpdated());
        assertFalse(SubdivisionCode.AM.lastUpdated().isBlank());
    }
}
