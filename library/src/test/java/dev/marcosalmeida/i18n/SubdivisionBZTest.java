package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBZTest {

    @Test
    public void testBZSubdivisions() {
        SubdivisionCode.BZ belize = SubdivisionCode.BZ.BZ;
        assertEquals("BZ-BZ", belize.getCode());
        assertEquals("Belize", belize.getSubdivisionName());
        assertEquals("district", belize.getCategory());

        SubdivisionCode.BZ cayo = SubdivisionCode.BZ.CY;
        assertEquals("BZ-CY", cayo.getCode());
        assertEquals("Cayo", cayo.getSubdivisionName());
        assertEquals("district", cayo.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.BZ);
        assertNotNull(subdivisions);
        assertEquals(6, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("BZ-BZ", first.getCode());
        assertEquals("Belize", first.getSubdivisionName());

        Subdivision last = subdivisions[5];
        assertEquals("BZ-TOL", last.getCode());
        assertEquals("Toledo", last.getSubdivisionName());
        assertEquals("district", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BZ belize = SubdivisionCode.BZ.fromCode("BZ-BZ");
        assertEquals(SubdivisionCode.BZ.BZ, belize);

        SubdivisionCode.BZ toledo = SubdivisionCode.BZ.fromCode("TOL");
        assertEquals(SubdivisionCode.BZ.TOL, toledo);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BZ.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision belize = SubdivisionCode.BZ.fromName("Belize").orElseThrow();
        assertEquals(SubdivisionCode.BZ.BZ, belize);

        Subdivision orangeWalk = SubdivisionCode.BZ.fromName("orange walk").orElseThrow();
        assertEquals(SubdivisionCode.BZ.OW, orangeWalk);

        assertTrue(SubdivisionCode.BZ.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision stannCreek = SubdivisionCode.BZ.find("SC").orElseThrow();
        assertEquals(SubdivisionCode.BZ.SC, stannCreek);

        Subdivision stannCreekFull = SubdivisionCode.BZ.find("BZ-SC").orElseThrow();
        assertEquals(SubdivisionCode.BZ.SC, stannCreekFull);

        Subdivision corozal = SubdivisionCode.BZ.find("Corozal").orElseThrow();
        assertEquals(SubdivisionCode.BZ.CZL, corozal);

        assertTrue(SubdivisionCode.BZ.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] districts = SubdivisionCode.BZ.getDistricts();
        assertEquals(6, districts.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.BZ.wikipedia());
        assertFalse(SubdivisionCode.BZ.wikipedia().isBlank());
        assertTrue(SubdivisionCode.BZ.wikipedia().contains("ISO_3166-2:BZ"));

        assertNotNull(SubdivisionCode.BZ.dateAdded());
        assertFalse(SubdivisionCode.BZ.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.BZ.lastUpdated());
        assertFalse(SubdivisionCode.BZ.lastUpdated().isBlank());
    }
}
