package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionDETest {

    @Test
    public void testDESubdivisions() {
        SubdivisionCode.DE bw = SubdivisionCode.DE.BW;
        assertEquals("DE-BW", bw.getCode());
        assertEquals("Baden-Württemberg", bw.getSubdivisionName());
        assertEquals("Land", bw.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.DE);
        assertNotNull(subdivisions);
        assertEquals(16, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("DE-BB", first.getCode());
        assertEquals("Brandenburg", first.getSubdivisionName());

        Subdivision last = subdivisions[15];
        assertEquals("DE-TH", last.getCode());
        assertEquals("Thüringen", last.getSubdivisionName());
        assertEquals("Land", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.DE bw = SubdivisionCode.DE.fromCode("DE-BW");
        assertEquals(SubdivisionCode.DE.BW, bw);

        SubdivisionCode.DE be = SubdivisionCode.DE.fromCode("BE");
        assertEquals(SubdivisionCode.DE.BE, be);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.DE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision bayern = SubdivisionCode.DE.fromName("Bayern").orElseThrow();
        assertEquals(SubdivisionCode.DE.BY, bayern);

        Subdivision berlin = SubdivisionCode.DE.fromName("berlin").orElseThrow();
        assertEquals(SubdivisionCode.DE.BE, berlin);

        assertTrue(SubdivisionCode.DE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision hh = SubdivisionCode.DE.find("HH").orElseThrow();
        assertEquals(SubdivisionCode.DE.HH, hh);

        Subdivision hhFull = SubdivisionCode.DE.find("DE-HH").orElseThrow();
        assertEquals(SubdivisionCode.DE.HH, hhFull);

        Subdivision bayern = SubdivisionCode.DE.find("Bayern").orElseThrow();
        assertEquals(SubdivisionCode.DE.BY, bayern);

        assertTrue(SubdivisionCode.DE.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] laender = SubdivisionCode.DE.getLands();
        assertEquals(16, laender.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.DE.wikipedia());
        assertFalse(SubdivisionCode.DE.wikipedia().isBlank());
        assertTrue(SubdivisionCode.DE.wikipedia().contains("ISO_3166-2:DE"));

        assertNotNull(SubdivisionCode.DE.dateAdded());
        assertFalse(SubdivisionCode.DE.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.DE.lastUpdated());
        assertFalse(SubdivisionCode.DE.lastUpdated().isBlank());
    }
}
