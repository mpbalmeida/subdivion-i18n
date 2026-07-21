package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionDKTest {

    @Test
    public void testDKSubdivisions() {
        SubdivisionCode.DK nordjylland = SubdivisionCode.DK.DK_81;
        assertEquals("DK-81", nordjylland.getCode());
        assertEquals("Nordjylland", nordjylland.getSubdivisionName());
        assertEquals("region", nordjylland.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.DK);
        assertNotNull(subdivisions);
        assertEquals(5, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("DK-81", first.getCode());
        assertEquals("Nordjylland", first.getSubdivisionName());

        Subdivision last = subdivisions[4];
        assertEquals("DK-85", last.getCode());
        assertEquals("Sjælland", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.DK nordjylland = SubdivisionCode.DK.fromCode("DK-81");
        assertEquals(SubdivisionCode.DK.DK_81, nordjylland);

        SubdivisionCode.DK sjælland = SubdivisionCode.DK.fromCode("85");
        assertEquals(SubdivisionCode.DK.DK_85, sjælland);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.DK.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision nordjylland = SubdivisionCode.DK.fromName("Nordjylland").orElseThrow();
        assertEquals(SubdivisionCode.DK.DK_81, nordjylland);

        Subdivision hovedstaden = SubdivisionCode.DK.fromName("hovedstaden").orElseThrow();
        assertEquals(SubdivisionCode.DK.DK_84, hovedstaden);

        assertTrue(SubdivisionCode.DK.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision midtjylland = SubdivisionCode.DK.find("82").orElseThrow();
        assertEquals(SubdivisionCode.DK.DK_82, midtjylland);

        Subdivision midtjyllandFull = SubdivisionCode.DK.find("DK-82").orElseThrow();
        assertEquals(SubdivisionCode.DK.DK_82, midtjyllandFull);

        Subdivision sjaelland = SubdivisionCode.DK.find("Sjælland").orElseThrow();
        assertEquals(SubdivisionCode.DK.DK_85, sjaelland);

        assertTrue(SubdivisionCode.DK.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.DK.getRegions();
        assertEquals(5, regions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.DK.wikipedia());
        assertFalse(SubdivisionCode.DK.wikipedia().isBlank());
        assertTrue(SubdivisionCode.DK.wikipedia().contains("ISO_3166-2:DK"));

        assertNotNull(SubdivisionCode.DK.dateAdded());
        assertFalse(SubdivisionCode.DK.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.DK.lastUpdated());
        assertFalse(SubdivisionCode.DK.lastUpdated().isBlank());
    }
}
