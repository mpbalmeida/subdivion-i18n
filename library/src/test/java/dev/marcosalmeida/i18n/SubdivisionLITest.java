package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionLITest {

    @Test
    public void testLISubdivisions() {
        SubdivisionCode.LI balzers = SubdivisionCode.LI.LI_01;
        assertEquals("LI-01", balzers.getCode());
        assertEquals("Balzers", balzers.getSubdivisionName());
        assertEquals("commune", balzers.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.LI);
        assertNotNull(subdivisions);
        assertEquals(11, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("LI-01", first.getCode());
        assertEquals("Balzers", first.getSubdivisionName());

        Subdivision last = subdivisions[10];
        assertEquals("LI-11", last.getCode());
        assertEquals("Vaduz", last.getSubdivisionName());
        assertEquals("commune", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.LI balzers = SubdivisionCode.LI.fromCode("LI-01");
        assertEquals(SubdivisionCode.LI.LI_01, balzers);

        SubdivisionCode.LI vaduz = SubdivisionCode.LI.fromCode("11");
        assertEquals(SubdivisionCode.LI.LI_11, vaduz);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.LI.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision schaan = SubdivisionCode.LI.fromName("Schaan").orElseThrow();
        assertEquals(SubdivisionCode.LI.LI_07, schaan);

        Subdivision triesen = SubdivisionCode.LI.fromName("triesen").orElseThrow();
        assertEquals(SubdivisionCode.LI.LI_09, triesen);

        assertTrue(SubdivisionCode.LI.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision three = SubdivisionCode.LI.find("03").orElseThrow();
        assertEquals(SubdivisionCode.LI.LI_03, three);

        Subdivision threeFull = SubdivisionCode.LI.find("LI-03").orElseThrow();
        assertEquals(SubdivisionCode.LI.LI_03, threeFull);

        Subdivision ruggell = SubdivisionCode.LI.find("Ruggell").orElseThrow();
        assertEquals(SubdivisionCode.LI.LI_06, ruggell);

        assertTrue(SubdivisionCode.LI.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] communes = SubdivisionCode.LI.getCommunes();
        assertEquals(11, communes.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.LI.wikipedia());
        assertFalse(SubdivisionCode.LI.wikipedia().isBlank());
        assertTrue(SubdivisionCode.LI.wikipedia().contains("ISO_3166-2:LI"));

        assertNotNull(SubdivisionCode.LI.dateAdded());
        assertFalse(SubdivisionCode.LI.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.LI.lastUpdated());
        assertFalse(SubdivisionCode.LI.lastUpdated().isBlank());
    }
}
