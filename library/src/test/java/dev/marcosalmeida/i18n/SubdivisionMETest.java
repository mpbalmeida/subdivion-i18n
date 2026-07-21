package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionMETest {

    @Test
    public void testMESubdivisions() {
        SubdivisionCode.ME andrijevica = SubdivisionCode.ME.ME_01;
        assertEquals("ME-01", andrijevica.getCode());
        assertEquals("Andrijevica", andrijevica.getSubdivisionName());
        assertEquals("municipality", andrijevica.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.ME);
        assertNotNull(subdivisions);
        assertEquals(25, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("ME-01", first.getCode());
        assertEquals("Andrijevica", first.getSubdivisionName());

        Subdivision last = subdivisions[24];
        assertEquals("ME-25", last.getCode());
        assertEquals("Zeta", last.getSubdivisionName());
        assertEquals("municipality", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.ME andrijevica = SubdivisionCode.ME.fromCode("ME-01");
        assertEquals(SubdivisionCode.ME.ME_01, andrijevica);

        SubdivisionCode.ME podgorica = SubdivisionCode.ME.fromCode("16");
        assertEquals(SubdivisionCode.ME.ME_16, podgorica);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.ME.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision podgorica = SubdivisionCode.ME.fromName("Podgorica").orElseThrow();
        assertEquals(SubdivisionCode.ME.ME_16, podgorica);

        Subdivision bar = SubdivisionCode.ME.fromName("bar").orElseThrow();
        assertEquals(SubdivisionCode.ME.ME_02, bar);

        assertTrue(SubdivisionCode.ME.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision zeta = SubdivisionCode.ME.find("25").orElseThrow();
        assertEquals(SubdivisionCode.ME.ME_25, zeta);

        Subdivision zetaFull = SubdivisionCode.ME.find("ME-25").orElseThrow();
        assertEquals(SubdivisionCode.ME.ME_25, zetaFull);

        Subdivision zetaName = SubdivisionCode.ME.find("Zeta").orElseThrow();
        assertEquals(SubdivisionCode.ME.ME_25, zetaName);

        assertTrue(SubdivisionCode.ME.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] municipalities = SubdivisionCode.ME.getMunicipalities();
        assertEquals(25, municipalities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.ME.wikipedia());
        assertFalse(SubdivisionCode.ME.wikipedia().isBlank());
        assertTrue(SubdivisionCode.ME.wikipedia().contains("ISO_3166-2:ME"));

        assertNotNull(SubdivisionCode.ME.dateAdded());
        assertFalse(SubdivisionCode.ME.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.ME.lastUpdated());
        assertFalse(SubdivisionCode.ME.lastUpdated().isBlank());
    }
}
