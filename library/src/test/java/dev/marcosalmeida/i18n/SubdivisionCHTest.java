package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCHTest {

    @Test
    public void testCHSubdivisions() {
        SubdivisionCode.CH ag = SubdivisionCode.CH.AG;
        assertEquals("CH-AG", ag.getCode());
        assertEquals("Aargau", ag.getSubdivisionName());
        assertEquals("canton", ag.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.CH);
        assertNotNull(subdivisions);
        assertEquals(26, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("CH-AG", first.getCode());
        assertEquals("Aargau", first.getSubdivisionName());

        Subdivision last = subdivisions[25];
        assertEquals("CH-ZH", last.getCode());
        assertEquals("Zürich", last.getSubdivisionName());
        assertEquals("canton", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CH zh = SubdivisionCode.CH.fromCode("CH-ZH");
        assertEquals(SubdivisionCode.CH.ZH, zh);

        SubdivisionCode.CH be = SubdivisionCode.CH.fromCode("BE");
        assertEquals(SubdivisionCode.CH.BE, be);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CH.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision zurich = SubdivisionCode.CH.fromName("Zürich").orElseThrow();
        assertEquals(SubdivisionCode.CH.ZH, zurich);

        Subdivision geneve = SubdivisionCode.CH.fromName("genève").orElseThrow();
        assertEquals(SubdivisionCode.CH.GE, geneve);

        assertTrue(SubdivisionCode.CH.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision ge = SubdivisionCode.CH.find("GE").orElseThrow();
        assertEquals(SubdivisionCode.CH.GE, ge);

        Subdivision geFull = SubdivisionCode.CH.find("CH-GE").orElseThrow();
        assertEquals(SubdivisionCode.CH.GE, geFull);

        Subdivision ticino = SubdivisionCode.CH.find("Ticino").orElseThrow();
        assertEquals(SubdivisionCode.CH.TI, ticino);

        assertTrue(SubdivisionCode.CH.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] cantons = SubdivisionCode.CH.getCantons();
        assertEquals(26, cantons.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.CH.wikipedia());
        assertFalse(SubdivisionCode.CH.wikipedia().isBlank());
        assertTrue(SubdivisionCode.CH.wikipedia().contains("ISO_3166-2:CH"));

        assertNotNull(SubdivisionCode.CH.dateAdded());
        assertFalse(SubdivisionCode.CH.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.CH.lastUpdated());
        assertFalse(SubdivisionCode.CH.lastUpdated().isBlank());
    }
}
