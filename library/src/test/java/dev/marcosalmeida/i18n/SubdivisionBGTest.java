package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBGTest {

    @Test
    public void testBGSubdivisions() {
        SubdivisionCode.BG blagoevgrad = SubdivisionCode.BG.BG_01;
        assertEquals("BG-01", blagoevgrad.getCode());
        assertEquals("Blagoevgrad", blagoevgrad.getSubdivisionName());
        assertEquals("district", blagoevgrad.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.BG);
        assertNotNull(subdivisions);
        assertEquals(28, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("BG-01", first.getCode());
        assertEquals("Blagoevgrad", first.getSubdivisionName());

        Subdivision last = subdivisions[27];
        assertEquals("BG-28", last.getCode());
        assertEquals("Yambol", last.getSubdivisionName());
        assertEquals("district", last.getCategory());

        SubdivisionCode.BG sofiaStolitsa = SubdivisionCode.BG.BG_22;
        assertEquals("BG-22", sofiaStolitsa.getCode());
        assertEquals("Sofia (stolitsa)", sofiaStolitsa.getSubdivisionName());
        assertEquals("district", sofiaStolitsa.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BG blagoevgrad = SubdivisionCode.BG.fromCode("BG-01");
        assertEquals(SubdivisionCode.BG.BG_01, blagoevgrad);

        SubdivisionCode.BG varna = SubdivisionCode.BG.fromCode("03");
        assertEquals(SubdivisionCode.BG.BG_03, varna);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BG.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision plovdiv = SubdivisionCode.BG.fromName("Plovdiv").orElseThrow();
        assertEquals(SubdivisionCode.BG.BG_16, plovdiv);

        Subdivision varna = SubdivisionCode.BG.fromName("varna").orElseThrow();
        assertEquals(SubdivisionCode.BG.BG_03, varna);

        assertTrue(SubdivisionCode.BG.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision fifteen = SubdivisionCode.BG.find("15").orElseThrow();
        assertEquals(SubdivisionCode.BG.BG_15, fifteen);

        Subdivision fifteenFull = SubdivisionCode.BG.find("BG-15").orElseThrow();
        assertEquals(SubdivisionCode.BG.BG_15, fifteenFull);

        Subdivision yambol = SubdivisionCode.BG.find("Yambol").orElseThrow();
        assertEquals(SubdivisionCode.BG.BG_28, yambol);

        assertTrue(SubdivisionCode.BG.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] districts = SubdivisionCode.BG.getDistricts();
        assertEquals(28, districts.length);

        // Verify a known district is in the list
        boolean hasPlovdiv = false;
        for (Subdivision s : districts) {
            if (s.equals(SubdivisionCode.BG.BG_16)) {
                hasPlovdiv = true;
                break;
            }
        }
        assertTrue(hasPlovdiv);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.BG.wikipedia());
        assertFalse(SubdivisionCode.BG.wikipedia().isBlank());
        assertTrue(SubdivisionCode.BG.wikipedia().contains("ISO_3166-2:BG"));

        assertNotNull(SubdivisionCode.BG.dateAdded());
        assertFalse(SubdivisionCode.BG.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.BG.lastUpdated());
        assertFalse(SubdivisionCode.BG.lastUpdated().isBlank());
    }
}
