package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCLTest {

    @Test
    public void testCLSubdivisions() {
        SubdivisionCode.CL aysen = SubdivisionCode.CL.AI;
        assertEquals("CL-AI", aysen.getCode());
        assertEquals("Aysén del General Carlos Ibáñez del Campo", aysen.getSubdivisionName());
        assertEquals("region", aysen.getCategory());

        SubdivisionCode.CL nuble = SubdivisionCode.CL.NB;
        assertEquals("CL-NB", nuble.getCode());
        assertEquals("Ñuble", nuble.getSubdivisionName());
        assertEquals("region", nuble.getCategory());

        SubdivisionCode.CL metropolitana = SubdivisionCode.CL.RM;
        assertEquals("CL-RM", metropolitana.getCode());
        assertEquals("Región Metropolitana de Santiago", metropolitana.getSubdivisionName());
        assertEquals("region", metropolitana.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.CL);
        assertNotNull(subdivisions);
        assertEquals(16, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("CL-AI", first.getCode());
        assertEquals("Aysén del General Carlos Ibáñez del Campo", first.getSubdivisionName());

        Subdivision last = subdivisions[15];
        assertEquals("CL-VS", last.getCode());
        assertEquals("Valparaíso", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CL aysen = SubdivisionCode.CL.fromCode("CL-AI");
        assertEquals(SubdivisionCode.CL.AI, aysen);

        SubdivisionCode.CL valparaiso = SubdivisionCode.CL.fromCode("VS");
        assertEquals(SubdivisionCode.CL.VS, valparaiso);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CL.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision santiago = SubdivisionCode.CL.fromName("Región Metropolitana de Santiago").orElseThrow();
        assertEquals(SubdivisionCode.CL.RM, santiago);

        Subdivision tarapaca = SubdivisionCode.CL.fromName("tarapacá").orElseThrow();
        assertEquals(SubdivisionCode.CL.TA, tarapaca);

        assertTrue(SubdivisionCode.CL.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision biobio = SubdivisionCode.CL.find("BI").orElseThrow();
        assertEquals(SubdivisionCode.CL.BI, biobio);

        Subdivision biobioFull = SubdivisionCode.CL.find("CL-BI").orElseThrow();
        assertEquals(SubdivisionCode.CL.BI, biobioFull);

        Subdivision losRios = SubdivisionCode.CL.find("Los Ríos").orElseThrow();
        assertEquals(SubdivisionCode.CL.LR, losRios);

        assertTrue(SubdivisionCode.CL.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.CL.getRegions();
        assertEquals(16, regions.length);
        assertEquals(SubdivisionCode.CL.AI, regions[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.CL.wikipedia());
        assertFalse(SubdivisionCode.CL.wikipedia().isBlank());
        assertTrue(SubdivisionCode.CL.wikipedia().contains("ISO_3166-2:CL"));

        assertNotNull(SubdivisionCode.CL.dateAdded());
        assertFalse(SubdivisionCode.CL.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.CL.lastUpdated());
        assertFalse(SubdivisionCode.CL.lastUpdated().isBlank());
    }
}
