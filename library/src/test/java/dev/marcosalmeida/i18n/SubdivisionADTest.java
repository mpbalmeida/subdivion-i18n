package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionADTest {

    @Test
    public void testADSubdivisions() {
        SubdivisionCode.AD canillo = SubdivisionCode.AD.AD_02;
        assertEquals("AD-02", canillo.getCode());
        assertEquals("Canillo", canillo.getSubdivisionName());
        assertEquals("parish", canillo.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.AD);
        assertNotNull(subdivisions);
        assertEquals(7, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("AD-02", first.getCode());
        assertEquals("Canillo", first.getSubdivisionName());

        Subdivision last = subdivisions[6];
        assertEquals("AD-08", last.getCode());
        assertEquals("Escaldes-Engordany", last.getSubdivisionName());
        assertEquals("parish", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AD andorraLaVella = SubdivisionCode.AD.fromCode("AD-07");
        assertEquals(SubdivisionCode.AD.AD_07, andorraLaVella);

        SubdivisionCode.AD ordino = SubdivisionCode.AD.fromCode("05");
        assertEquals(SubdivisionCode.AD.AD_05, ordino);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AD.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision encamp = SubdivisionCode.AD.fromName("Encamp").orElseThrow();
        assertEquals(SubdivisionCode.AD.AD_03, encamp);

        Subdivision laMassana = SubdivisionCode.AD.fromName("la massana").orElseThrow();
        assertEquals(SubdivisionCode.AD.AD_04, laMassana);

        assertTrue(SubdivisionCode.AD.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision cinq = SubdivisionCode.AD.find("05").orElseThrow();
        assertEquals(SubdivisionCode.AD.AD_05, cinq);

        Subdivision cinqFull = SubdivisionCode.AD.find("AD-05").orElseThrow();
        assertEquals(SubdivisionCode.AD.AD_05, cinqFull);

        Subdivision santJulia = SubdivisionCode.AD.find("Sant Julià de Lòria").orElseThrow();
        assertEquals(SubdivisionCode.AD.AD_06, santJulia);

        assertTrue(SubdivisionCode.AD.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision escaldesEngordany = SubdivisionCode.AD.AD_08;
        assertTrue(escaldesEngordany.getParent().isPresent());
        assertEquals(SubdivisionCode.AD.AD_07, escaldesEngordany.getParent().get());

        Subdivision canillo = SubdivisionCode.AD.AD_02;
        assertFalse(canillo.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] parishes = SubdivisionCode.AD.getParishs();
        assertEquals(7, parishes.length);

        // Test getByParent
        Subdivision[] childrenOfAndorraLaVella = SubdivisionCode.AD.getByParent(SubdivisionCode.AD.AD_07);
        assertEquals(1, childrenOfAndorraLaVella.length);
        assertEquals(SubdivisionCode.AD.AD_08, childrenOfAndorraLaVella[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.AD.wikipedia());
        assertFalse(SubdivisionCode.AD.wikipedia().isBlank());
        assertTrue(SubdivisionCode.AD.wikipedia().contains("ISO_3166-2:AD"));

        assertNotNull(SubdivisionCode.AD.dateAdded());
        assertFalse(SubdivisionCode.AD.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.AD.lastUpdated());
        assertFalse(SubdivisionCode.AD.lastUpdated().isBlank());
    }
}
