package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionGRTest {

    @Test
    public void testGRSubdivisions() {
        SubdivisionCode.GR mountAthos = SubdivisionCode.GR.GR_69;
        assertEquals("GR-69", mountAthos.getCode());
        assertEquals("Mount Athos", mountAthos.getSubdivisionName());
        assertEquals("self-governed part", mountAthos.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.GR);
        assertNotNull(subdivisions);
        assertEquals(14, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("GR-69", first.getCode());
        assertEquals("Mount Athos", first.getSubdivisionName());

        Subdivision last = subdivisions[13];
        assertEquals("GR-M", last.getCode());
        assertEquals("Crete", last.getSubdivisionName());
        assertEquals("administrative region", last.getCategory());

        Subdivision attica = SubdivisionCode.GR.GR_I;
        assertEquals("GR-I", attica.getCode());
        assertEquals("Attica", attica.getSubdivisionName());
        assertEquals("administrative region", attica.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.GR attica = SubdivisionCode.GR.fromCode("GR-I");
        assertEquals(SubdivisionCode.GR.GR_I, attica);

        SubdivisionCode.GR crete = SubdivisionCode.GR.fromCode("M");
        assertEquals(SubdivisionCode.GR.GR_M, crete);

        SubdivisionCode.GR mountAthos = SubdivisionCode.GR.fromCode("69");
        assertEquals(SubdivisionCode.GR.GR_69, mountAthos);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.GR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision epirus = SubdivisionCode.GR.fromName("Epirus").orElseThrow();
        assertEquals(SubdivisionCode.GR.GR_D, epirus);

        Subdivision mountAthos = SubdivisionCode.GR.fromName("mount athos").orElseThrow();
        assertEquals(SubdivisionCode.GR.GR_69, mountAthos);

        assertTrue(SubdivisionCode.GR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision crete = SubdivisionCode.GR.find("M").orElseThrow();
        assertEquals(SubdivisionCode.GR.GR_M, crete);

        Subdivision creteFull = SubdivisionCode.GR.find("GR-M").orElseThrow();
        assertEquals(SubdivisionCode.GR.GR_M, creteFull);

        Subdivision attica = SubdivisionCode.GR.find("Attica").orElseThrow();
        assertEquals(SubdivisionCode.GR.GR_I, attica);

        assertTrue(SubdivisionCode.GR.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] adminRegions = SubdivisionCode.GR.getAdministrativeRegions();
        assertEquals(13, adminRegions.length);

        Subdivision[] selfGovernedParts = SubdivisionCode.GR.getSelfGovernedParts();
        assertEquals(1, selfGovernedParts.length);
        assertEquals("Mount Athos", selfGovernedParts[0].getSubdivisionName());
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.GR.wikipedia());
        assertFalse(SubdivisionCode.GR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.GR.wikipedia().contains("ISO_3166-2:GR"));

        assertNotNull(SubdivisionCode.GR.dateAdded());
        assertFalse(SubdivisionCode.GR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.GR.lastUpdated());
        assertFalse(SubdivisionCode.GR.lastUpdated().isBlank());
    }
}
