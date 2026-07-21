package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCodeTest {

    @Test
    public void testGetSubdivisions() {
        Subdivision[] au = SubdivisionCode.getSubdivisions(CountryCode.AU);
        assertNotNull(au);
        assertEquals(8, au.length);

        Subdivision[] us = SubdivisionCode.getSubdivisions(CountryCode.US);
        assertNotNull(us);
        assertTrue(us.length > 0);

        Subdivision[] br = SubdivisionCode.getSubdivisions(CountryCode.BR);
        assertNotNull(br);
        assertTrue(br.length > 0);

        Subdivision[] ca = SubdivisionCode.getSubdivisions(CountryCode.CA);
        assertNotNull(ca);
        assertEquals(13, ca.length);

        Subdivision[] mx = SubdivisionCode.getSubdivisions(CountryCode.MX);
        assertNotNull(mx);
        assertTrue(mx.length > 0);

        Subdivision[] ie = SubdivisionCode.getSubdivisions(CountryCode.IE);
        assertNotNull(ie);
        assertTrue(ie.length > 0);

        Subdivision[] it = SubdivisionCode.getSubdivisions(CountryCode.IT);
        assertNotNull(it);
        assertEquals(20, it.length);

        Subdivision[] nz = SubdivisionCode.getSubdivisions(CountryCode.NZ);
        assertNotNull(nz);
        assertEquals(17, nz.length);

        Subdivision[] de = SubdivisionCode.getSubdivisions(CountryCode.DE);
        assertNotNull(de);
        assertEquals(16, de.length);

        Subdivision[] at = SubdivisionCode.getSubdivisions(CountryCode.AT);
        assertNotNull(at);
        assertEquals(9, at.length);

        assertNull(SubdivisionCode.getSubdivisions(CountryCode.AF));
    }

    @Test
    public void testFromCode() {
        Subdivision auNsw = SubdivisionCode.fromCode("AU-NSW");
        assertNotNull(auNsw);
        assertEquals("New South Wales", auNsw.getSubdivisionName());

        Subdivision usAl = SubdivisionCode.fromCode("US-AL");
        assertNotNull(usAl);
        assertEquals("Alabama", usAl.getSubdivisionName());

        Subdivision it25 = SubdivisionCode.fromCode("IT-25");
        assertNotNull(it25);
        assertEquals("Lombardia", it25.getSubdivisionName());

        Subdivision caOn = SubdivisionCode.fromCode("CA-ON");
        assertNotNull(caOn);
        assertEquals("Ontario", caOn.getSubdivisionName());

        Subdivision brSp = SubdivisionCode.fromCode("BR-SP");
        assertNotNull(brSp);
        assertEquals("São Paulo", brSp.getSubdivisionName());

        Subdivision ieD = SubdivisionCode.fromCode("IE-D");
        assertNotNull(ieD);
        assertEquals("Dublin", ieD.getSubdivisionName());

        Subdivision deBy = SubdivisionCode.fromCode("DE-BY");
        assertNotNull(deBy);
        assertEquals("Bayern", deBy.getSubdivisionName());

        Subdivision at9 = SubdivisionCode.fromCode("AT-9");
        assertNotNull(at9);
        assertEquals("Wien", at9.getSubdivisionName());

        // Test lookup by subdivision part (returns the first match, which is BR-AL for "AL")
        Subdivision alShort = SubdivisionCode.fromCode("AL");
        assertNotNull(alShort);
        assertEquals("BR-AL", alShort.getCode());

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.fromCode("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.fromCode("US-XX"));
    }

    @Test
    public void testFromName() {
        Subdivision alabama = SubdivisionCode.fromName("Alabama").orElseThrow();
        assertEquals("US-AL", alabama.getCode());

        Subdivision saoPaulo = SubdivisionCode.fromName("são paulo").orElseThrow();
        assertEquals("BR-SP", saoPaulo.getCode());

        assertTrue(SubdivisionCode.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        // "AL" matches BR-AL first because BR is before US in the Stream
        Subdivision al = SubdivisionCode.find("AL").orElseThrow();
        assertEquals("BR-AL", al.getCode());

        Subdivision spFull = SubdivisionCode.find("BR-SP").orElseThrow();
        assertEquals("BR-SP", spFull.getCode());

        Subdivision mexico = SubdivisionCode.find("México").orElseThrow();
        assertEquals("MX-MEX", mexico.getCode());

        assertTrue(SubdivisionCode.find("Invalid").isEmpty());
    }

    @Test
    public void testGlobalFiltering() {
        Subdivision[] allStates = SubdivisionCode.getStates();
        // AT (9) + AU (6) + BR (26) + MX (31) + US "State" (50) = 122
        assertEquals(122, allStates.length);

        Subdivision[] allRegions = SubdivisionCode.getRegions();
        assertEquals(130, allRegions.length);

        Subdivision[] allProvinces = SubdivisionCode.getProvinces();
        assertEquals(168, allProvinces.length);

        Subdivision[] allCounties = SubdivisionCode.getCounties();
        assertEquals(134, allCounties.length);

        Subdivision[] allLands = SubdivisionCode.getLands();
        assertEquals(16, allLands.length);
    }
}
