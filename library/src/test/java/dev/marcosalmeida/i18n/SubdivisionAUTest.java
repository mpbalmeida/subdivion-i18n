package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionAUTest {

    @Test
    public void testAUSubdivisions() {
        SubdivisionCode.AU nsw = SubdivisionCode.AU.NSW;
        assertEquals("AU-NSW", nsw.getCode());
        assertEquals("New South Wales", nsw.getSubdivisionName());
        assertEquals("state", nsw.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.AU);
        assertNotNull(subdivisions);
        assertEquals(8, subdivisions.length); // 6 states + 2 territories

        Subdivision act = subdivisions[0];
        assertEquals("AU-ACT", act.getCode());
        assertEquals("Australian Capital Territory", act.getSubdivisionName());
        assertEquals("territory", act.getCategory());

        Subdivision wa = subdivisions[7];
        assertEquals("AU-WA", wa.getCode());
        assertEquals("Western Australia", wa.getSubdivisionName());
        assertEquals("state", wa.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AU nsw = SubdivisionCode.AU.fromCode("AU-NSW");
        assertEquals(SubdivisionCode.AU.NSW, nsw);

        // Test lookup by subdivision part
        SubdivisionCode.AU act = SubdivisionCode.AU.fromCode("ACT");
        assertEquals(SubdivisionCode.AU.ACT, act);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AU.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision nsw = SubdivisionCode.AU.fromName("New South Wales").orElseThrow();
        assertEquals(SubdivisionCode.AU.NSW, nsw);

        Subdivision act = SubdivisionCode.AU.fromName("australian capital territory").orElseThrow();
        assertEquals(SubdivisionCode.AU.ACT, act);

        assertTrue(SubdivisionCode.AU.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision vic = SubdivisionCode.AU.find("VIC").orElseThrow();
        assertEquals(SubdivisionCode.AU.VIC, vic);

        Subdivision vicFull = SubdivisionCode.AU.find("AU-VIC").orElseThrow();
        assertEquals(SubdivisionCode.AU.VIC, vicFull);

        Subdivision victoria = SubdivisionCode.AU.find("Victoria").orElseThrow();
        assertEquals(SubdivisionCode.AU.VIC, victoria);

        assertTrue(SubdivisionCode.AU.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] states = SubdivisionCode.AU.getStates();
        assertEquals(6, states.length);

        Subdivision[] territories = SubdivisionCode.AU.getTerritories();
        assertEquals(2, territories.length);
        
        boolean hasAct = false;
        for (Subdivision s : territories) {
            if (s.equals(SubdivisionCode.AU.ACT)) {
                hasAct = true;
                break;
            }
        }
        assertTrue(hasAct);
    }

    @Test
    public void testGetSubdivisionCode() {
        assertEquals("NSW", SubdivisionCode.AU.NSW.getSubdivisionCode());
        assertEquals("ACT", SubdivisionCode.AU.ACT.getSubdivisionCode());
    }
}
