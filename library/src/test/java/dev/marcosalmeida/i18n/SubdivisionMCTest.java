package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionMCTest {

    @Test
    public void testMCSubdivisions() {
        SubdivisionCode.MC cl = SubdivisionCode.MC.CL;
        assertEquals("MC-CL", cl.getCode());
        assertEquals("La Colle", cl.getSubdivisionName());
        assertEquals("quarter", cl.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.MC);
        assertNotNull(subdivisions);
        assertEquals(17, subdivisions.length); // 17 quarters

        Subdivision first = subdivisions[0];
        assertEquals("MC-CL", first.getCode());
        assertEquals("La Colle", first.getSubdivisionName());

        Subdivision last = subdivisions[16];
        assertEquals("MC-VR", last.getCode());
        assertEquals("Vallon de la Rousse", last.getSubdivisionName());
        assertEquals("quarter", last.getCategory());

        Subdivision monteCarlo = SubdivisionCode.MC.MC;
        assertEquals("MC-MC", monteCarlo.getCode());
        assertEquals("Monte-Carlo", monteCarlo.getSubdivisionName());
        assertEquals("quarter", monteCarlo.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.MC mc = SubdivisionCode.MC.fromCode("MC-MC");
        assertEquals(SubdivisionCode.MC.MC, mc);

        // Test lookup by subdivision part
        SubdivisionCode.MC mcShort = SubdivisionCode.MC.fromCode("MC");
        assertEquals(SubdivisionCode.MC.MC, mcShort);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.MC.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision monteCarlo = SubdivisionCode.MC.fromName("Monte-Carlo").orElseThrow();
        assertEquals(SubdivisionCode.MC.MC, monteCarlo);

        Subdivision fontvieille = SubdivisionCode.MC.fromName("fontvieille").orElseThrow();
        assertEquals(SubdivisionCode.MC.FO, fontvieille);

        assertTrue(SubdivisionCode.MC.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision mc = SubdivisionCode.MC.find("MC").orElseThrow();
        assertEquals(SubdivisionCode.MC.MC, mc);

        Subdivision mcFull = SubdivisionCode.MC.find("MC-MC").orElseThrow();
        assertEquals(SubdivisionCode.MC.MC, mcFull);

        Subdivision monteCarlo = SubdivisionCode.MC.find("Monte-Carlo").orElseThrow();
        assertEquals(SubdivisionCode.MC.MC, monteCarlo);

        assertTrue(SubdivisionCode.MC.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] quarters = SubdivisionCode.MC.getQuarters();
        assertEquals(17, quarters.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.MC.wikipedia());
        assertFalse(SubdivisionCode.MC.wikipedia().isBlank());

        assertNotNull(SubdivisionCode.MC.dateAdded());
        assertFalse(SubdivisionCode.MC.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.MC.lastUpdated());
        assertFalse(SubdivisionCode.MC.lastUpdated().isBlank());
    }
}
