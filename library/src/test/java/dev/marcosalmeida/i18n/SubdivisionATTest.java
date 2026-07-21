package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionATTest {

    @Test
    public void testATSubdivisions() {
        SubdivisionCode.AT burgenland = SubdivisionCode.AT.AT_1;
        assertEquals("AT-1", burgenland.getCode());
        assertEquals("Burgenland", burgenland.getSubdivisionName());
        assertEquals("state", burgenland.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.AT);
        assertNotNull(subdivisions);
        assertEquals(9, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("AT-1", first.getCode());
        assertEquals("Burgenland", first.getSubdivisionName());

        Subdivision last = subdivisions[8];
        assertEquals("AT-9", last.getCode());
        assertEquals("Wien", last.getSubdivisionName());
        assertEquals("state", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AT wien = SubdivisionCode.AT.fromCode("AT-9");
        assertEquals(SubdivisionCode.AT.AT_9, wien);

        SubdivisionCode.AT tirol = SubdivisionCode.AT.fromCode("7");
        assertEquals(SubdivisionCode.AT.AT_7, tirol);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AT.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision salzburg = SubdivisionCode.AT.fromName("Salzburg").orElseThrow();
        assertEquals(SubdivisionCode.AT.AT_5, salzburg);

        Subdivision wien = SubdivisionCode.AT.fromName("wien").orElseThrow();
        assertEquals(SubdivisionCode.AT.AT_9, wien);

        assertTrue(SubdivisionCode.AT.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision acht = SubdivisionCode.AT.find("8").orElseThrow();
        assertEquals(SubdivisionCode.AT.AT_8, acht);

        Subdivision achtFull = SubdivisionCode.AT.find("AT-8").orElseThrow();
        assertEquals(SubdivisionCode.AT.AT_8, achtFull);

        Subdivision kaernten = SubdivisionCode.AT.find("Kärnten").orElseThrow();
        assertEquals(SubdivisionCode.AT.AT_2, kaernten);

        assertTrue(SubdivisionCode.AT.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] states = SubdivisionCode.AT.getStates();
        assertEquals(9, states.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.AT.wikipedia());
        assertFalse(SubdivisionCode.AT.wikipedia().isBlank());
        assertTrue(SubdivisionCode.AT.wikipedia().contains("ISO_3166-2:AT"));

        assertNotNull(SubdivisionCode.AT.dateAdded());
        assertFalse(SubdivisionCode.AT.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.AT.lastUpdated());
        assertFalse(SubdivisionCode.AT.lastUpdated().isBlank());
    }
}
