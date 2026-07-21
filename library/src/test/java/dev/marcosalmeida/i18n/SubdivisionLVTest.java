package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionLVTest {

    @Test
    public void testLVSubdivisions() {
        SubdivisionCode.LV aizkraukle = SubdivisionCode.LV.LV_002;
        assertEquals("LV-002", aizkraukle.getCode());
        assertEquals("Aizkraukles novads", aizkraukle.getSubdivisionName());
        assertEquals("municipality", aizkraukle.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.LV);
        assertNotNull(subdivisions);
        assertEquals(43, subdivisions.length); // 36 municipalities + 7 state cities

        Subdivision first = subdivisions[0];
        assertEquals("LV-002", first.getCode());
        assertEquals("Aizkraukles novads", first.getSubdivisionName());

        Subdivision last = subdivisions[42];
        assertEquals("LV-VEN", last.getCode());
        assertEquals("Ventspils", last.getSubdivisionName());
        assertEquals("state city", last.getCategory());

        Subdivision riga = SubdivisionCode.LV.LV_RIX;
        assertEquals("LV-RIX", riga.getCode());
        assertEquals("Rīga", riga.getSubdivisionName());
        assertEquals("state city", riga.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.LV riga = SubdivisionCode.LV.fromCode("LV-RIX");
        assertEquals(SubdivisionCode.LV.LV_RIX, riga);

        // Test lookup by subdivision part
        SubdivisionCode.LV aizkraukle = SubdivisionCode.LV.fromCode("002");
        assertEquals(SubdivisionCode.LV.LV_002, aizkraukle);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.LV.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision riga = SubdivisionCode.LV.fromName("Rīga").orElseThrow();
        assertEquals(SubdivisionCode.LV.LV_RIX, riga);

        Subdivision cesu = SubdivisionCode.LV.fromName("cēsu novads").orElseThrow();
        assertEquals(SubdivisionCode.LV.LV_022, cesu);

        assertTrue(SubdivisionCode.LV.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision riga = SubdivisionCode.LV.find("RIX").orElseThrow();
        assertEquals(SubdivisionCode.LV.LV_RIX, riga);

        Subdivision rigaFull = SubdivisionCode.LV.find("LV-RIX").orElseThrow();
        assertEquals(SubdivisionCode.LV.LV_RIX, rigaFull);

        Subdivision rigaName = SubdivisionCode.LV.find("Rīga").orElseThrow();
        assertEquals(SubdivisionCode.LV.LV_RIX, rigaName);

        assertTrue(SubdivisionCode.LV.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] municipalities = SubdivisionCode.LV.getMunicipalities();
        assertEquals(36, municipalities.length);

        Subdivision[] stateCities = SubdivisionCode.LV.getStateCities();
        assertEquals(7, stateCities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.LV.wikipedia());
        assertFalse(SubdivisionCode.LV.wikipedia().isBlank());
        assertTrue(SubdivisionCode.LV.wikipedia().contains("ISO_3166-2:LV"));

        assertNotNull(SubdivisionCode.LV.dateAdded());
        assertFalse(SubdivisionCode.LV.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.LV.lastUpdated());
        assertFalse(SubdivisionCode.LV.lastUpdated().isBlank());
    }
}
