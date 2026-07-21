package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionEETest {

    @Test
    public void testEESubdivisions() {
        SubdivisionCode.EE harjumaa = SubdivisionCode.EE.EE_37;
        assertEquals("EE-37", harjumaa.getCode());
        assertEquals("Harjumaa", harjumaa.getSubdivisionName());
        assertEquals("county", harjumaa.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.EE);
        assertNotNull(subdivisions);
        assertEquals(94, subdivisions.length); // 15 counties + 79 municipalities

        Subdivision firstCounty = subdivisions[0];
        assertEquals("EE-37", firstCounty.getCode());
        assertEquals("Harjumaa", firstCounty.getSubdivisionName());

        Subdivision lastMunicipality = subdivisions[93];
        assertEquals("EE-928", lastMunicipality.getCode());
        assertEquals("Väike-Maarja", lastMunicipality.getSubdivisionName());
        assertEquals("rural", lastMunicipality.getCategory());

        Subdivision tallinn = SubdivisionCode.EE.EE_784;
        assertEquals("EE-784", tallinn.getCode());
        assertEquals("Tallinn", tallinn.getSubdivisionName());
        assertEquals("urban", tallinn.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.EE harjumaa = SubdivisionCode.EE.fromCode("EE-37");
        assertEquals(SubdivisionCode.EE.EE_37, harjumaa);

        // Test lookup by subdivision part
        SubdivisionCode.EE tallinn = SubdivisionCode.EE.fromCode("784");
        assertEquals(SubdivisionCode.EE.EE_784, tallinn);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.EE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision tallinn = SubdivisionCode.EE.fromName("Tallinn").orElseThrow();
        assertEquals(SubdivisionCode.EE.EE_784, tallinn);

        Subdivision harjumaa = SubdivisionCode.EE.fromName("harjumaa").orElseThrow();
        assertEquals(SubdivisionCode.EE.EE_37, harjumaa);

        assertTrue(SubdivisionCode.EE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision tallinn = SubdivisionCode.EE.find("784").orElseThrow();
        assertEquals(SubdivisionCode.EE.EE_784, tallinn);

        Subdivision tallinnFull = SubdivisionCode.EE.find("EE-784").orElseThrow();
        assertEquals(SubdivisionCode.EE.EE_784, tallinnFull);

        Subdivision harjumaa = SubdivisionCode.EE.find("Harjumaa").orElseThrow();
        assertEquals(SubdivisionCode.EE.EE_37, harjumaa);

        assertTrue(SubdivisionCode.EE.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision tallinn = SubdivisionCode.EE.EE_784;
        assertTrue(tallinn.getParent().isPresent());
        assertEquals(SubdivisionCode.EE.EE_37, tallinn.getParent().get());

        Subdivision harjumaa = SubdivisionCode.EE.EE_37;
        assertFalse(harjumaa.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.EE.getCounties();
        assertEquals(15, counties.length);

        Subdivision[] rurals = SubdivisionCode.EE.getRurals();
        assertEquals(64, rurals.length);

        Subdivision[] urbans = SubdivisionCode.EE.getUrbans();
        assertEquals(15, urbans.length);

        Subdivision harjumaa = SubdivisionCode.EE.EE_37;
        Subdivision[] harjumaaSubs = SubdivisionCode.EE.getByParent(harjumaa);
        assertEquals(16, harjumaaSubs.length);

        // Verify one of the municipalities in Harjumaa
        boolean hasTallinn = false;
        for (Subdivision s : harjumaaSubs) {
            if (s.equals(SubdivisionCode.EE.EE_784)) {
                hasTallinn = true;
                break;
            }
        }
        assertTrue(hasTallinn);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.EE.wikipedia());
        assertFalse(SubdivisionCode.EE.wikipedia().isBlank());

        assertNotNull(SubdivisionCode.EE.dateAdded());
        assertFalse(SubdivisionCode.EE.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.EE.lastUpdated());
        assertFalse(SubdivisionCode.EE.lastUpdated().isBlank());
    }
}
