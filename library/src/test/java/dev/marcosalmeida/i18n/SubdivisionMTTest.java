package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionMTTest {

    @Test
    public void testMTSubdivisions() {
        SubdivisionCode.MT attard = SubdivisionCode.MT.MT_01;
        assertEquals("MT-01", attard.getCode());
        assertEquals("Attard", attard.getSubdivisionName());
        assertEquals("local council", attard.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.MT);
        assertNotNull(subdivisions);
        assertEquals(68, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("MT-01", first.getCode());
        assertEquals("Attard", first.getSubdivisionName());

        Subdivision last = subdivisions[67];
        assertEquals("MT-68", last.getCode());
        assertEquals("Żurrieq", last.getSubdivisionName());
        assertEquals("local council", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.MT attard = SubdivisionCode.MT.fromCode("MT-01");
        assertEquals(SubdivisionCode.MT.MT_01, attard);

        SubdivisionCode.MT valletta = SubdivisionCode.MT.fromCode("60");
        assertEquals(SubdivisionCode.MT.MT_60, valletta);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.MT.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision valletta = SubdivisionCode.MT.fromName("Valletta").orElseThrow();
        assertEquals(SubdivisionCode.MT.MT_60, valletta);

        Subdivision zurrieq = SubdivisionCode.MT.fromName("żurrieq").orElseThrow();
        assertEquals(SubdivisionCode.MT.MT_68, zurrieq);

        assertTrue(SubdivisionCode.MT.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision zejtun = SubdivisionCode.MT.find("67").orElseThrow();
        assertEquals(SubdivisionCode.MT.MT_67, zejtun);

        Subdivision zejtunFull = SubdivisionCode.MT.find("MT-67").orElseThrow();
        assertEquals(SubdivisionCode.MT.MT_67, zejtunFull);

        Subdivision mdina = SubdivisionCode.MT.find("Mdina").orElseThrow();
        assertEquals(SubdivisionCode.MT.MT_29, mdina);

        assertTrue(SubdivisionCode.MT.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] localCouncils = SubdivisionCode.MT.getLocalCouncils();
        assertEquals(68, localCouncils.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.MT.wikipedia());
        assertFalse(SubdivisionCode.MT.wikipedia().isBlank());
        assertTrue(SubdivisionCode.MT.wikipedia().contains("ISO_3166-2:MT"));

        assertNotNull(SubdivisionCode.MT.dateAdded());
        assertFalse(SubdivisionCode.MT.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.MT.lastUpdated());
        assertFalse(SubdivisionCode.MT.lastUpdated().isBlank());
    }
}
