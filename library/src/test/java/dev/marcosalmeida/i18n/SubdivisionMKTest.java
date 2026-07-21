package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionMKTest {

    @Test
    public void testMKSubdivisions() {
        SubdivisionCode.MK aerodrom = SubdivisionCode.MK.MK_801;
        assertEquals("MK-801", aerodrom.getCode());
        assertEquals("Aerodrom", aerodrom.getSubdivisionName());
        assertEquals("municipality", aerodrom.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.MK);
        assertNotNull(subdivisions);
        assertEquals(80, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("MK-101", first.getCode());
        assertEquals("Veles", first.getSubdivisionName());

        Subdivision last = subdivisions[79];
        assertEquals("MK-817", last.getCode());
        assertEquals("Šuto Orizari", last.getSubdivisionName());
        assertEquals("municipality", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.MK aerodrom = SubdivisionCode.MK.fromCode("MK-801");
        assertEquals(SubdivisionCode.MK.MK_801, aerodrom);

        SubdivisionCode.MK veles = SubdivisionCode.MK.fromCode("101");
        assertEquals(SubdivisionCode.MK.MK_101, veles);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.MK.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision aerodrom = SubdivisionCode.MK.fromName("Aerodrom").orElseThrow();
        assertEquals(SubdivisionCode.MK.MK_801, aerodrom);

        Subdivision veles = SubdivisionCode.MK.fromName("veles").orElseThrow();
        assertEquals(SubdivisionCode.MK.MK_101, veles);

        assertTrue(SubdivisionCode.MK.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision aerodromCode = SubdivisionCode.MK.find("801").orElseThrow();
        assertEquals(SubdivisionCode.MK.MK_801, aerodromCode);

        Subdivision aerodromFull = SubdivisionCode.MK.find("MK-801").orElseThrow();
        assertEquals(SubdivisionCode.MK.MK_801, aerodromFull);

        Subdivision aerodromName = SubdivisionCode.MK.find("Aerodrom").orElseThrow();
        assertEquals(SubdivisionCode.MK.MK_801, aerodromName);

        assertTrue(SubdivisionCode.MK.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] municipalities = SubdivisionCode.MK.getMunicipalities();
        assertEquals(80, municipalities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.MK.wikipedia());
        assertFalse(SubdivisionCode.MK.wikipedia().isBlank());
        assertTrue(SubdivisionCode.MK.wikipedia().contains("ISO_3166-2:MK"));

        assertNotNull(SubdivisionCode.MK.dateAdded());
        assertFalse(SubdivisionCode.MK.dateAdded().isBlank());
        assertEquals("2026-07-21", SubdivisionCode.MK.dateAdded());

        assertNotNull(SubdivisionCode.MK.lastUpdated());
        assertFalse(SubdivisionCode.MK.lastUpdated().isBlank());
        assertEquals("2026-07-21", SubdivisionCode.MK.lastUpdated());
    }
}
