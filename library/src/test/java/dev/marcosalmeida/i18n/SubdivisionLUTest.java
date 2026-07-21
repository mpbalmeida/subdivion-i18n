package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionLUTest {

    @Test
    public void testLUSubdivisions() {
        SubdivisionCode.LU ca = SubdivisionCode.LU.CA;
        assertEquals("LU-CA", ca.getCode());
        assertEquals("Capellen", ca.getSubdivisionName());
        assertEquals("canton", ca.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.LU);
        assertNotNull(subdivisions);
        assertEquals(12, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("LU-CA", first.getCode());
        assertEquals("Capellen", first.getSubdivisionName());

        Subdivision last = subdivisions[11];
        assertEquals("LU-WI", last.getCode());
        assertEquals("Wiltz", last.getSubdivisionName());
        assertEquals("canton", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.LU ca = SubdivisionCode.LU.fromCode("LU-CA");
        assertEquals(SubdivisionCode.LU.CA, ca);

        SubdivisionCode.LU es = SubdivisionCode.LU.fromCode("ES");
        assertEquals(SubdivisionCode.LU.ES, es);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.LU.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision capellen = SubdivisionCode.LU.fromName("Capellen").orElseThrow();
        assertEquals(SubdivisionCode.LU.CA, capellen);

        Subdivision esch = SubdivisionCode.LU.fromName("esch-sur-alzette").orElseThrow();
        assertEquals(SubdivisionCode.LU.ES, esch);

        assertTrue(SubdivisionCode.LU.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision es = SubdivisionCode.LU.find("ES").orElseThrow();
        assertEquals(SubdivisionCode.LU.ES, es);

        Subdivision esFull = SubdivisionCode.LU.find("LU-ES").orElseThrow();
        assertEquals(SubdivisionCode.LU.ES, esFull);

        Subdivision diekirch = SubdivisionCode.LU.find("Diekirch").orElseThrow();
        assertEquals(SubdivisionCode.LU.DI, diekirch);

        assertTrue(SubdivisionCode.LU.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] cantons = SubdivisionCode.LU.getCantons();
        assertEquals(12, cantons.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.LU.wikipedia());
        assertFalse(SubdivisionCode.LU.wikipedia().isBlank());
        assertTrue(SubdivisionCode.LU.wikipedia().contains("ISO_3166-2:LU"));

        assertNotNull(SubdivisionCode.LU.dateAdded());
        assertFalse(SubdivisionCode.LU.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.LU.lastUpdated());
        assertFalse(SubdivisionCode.LU.lastUpdated().isBlank());
    }
}
