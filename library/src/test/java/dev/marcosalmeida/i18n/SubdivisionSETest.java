package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionSETest {

    @Test
    public void testSESubdivisions() {
        SubdivisionCode.SE stockholm = SubdivisionCode.SE.AB;
        assertEquals("SE-AB", stockholm.getCode());
        assertEquals("Stockholms län", stockholm.getSubdivisionName());
        assertEquals("county", stockholm.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.SE);
        assertNotNull(subdivisions);
        assertEquals(21, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("SE-AB", first.getCode());
        assertEquals("Stockholms län", first.getSubdivisionName());

        Subdivision last = subdivisions[20];
        assertEquals("SE-Z", last.getCode());
        assertEquals("Jämtlands län", last.getSubdivisionName());
        assertEquals("county", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.SE skane = SubdivisionCode.SE.fromCode("SE-M");
        assertEquals(SubdivisionCode.SE.M, skane);

        SubdivisionCode.SE oerebro = SubdivisionCode.SE.fromCode("T");
        assertEquals(SubdivisionCode.SE.T, oerebro);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.SE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision vastraGotaland = SubdivisionCode.SE.fromName("Västra Götalands län").orElseThrow();
        assertEquals(SubdivisionCode.SE.O, vastraGotaland);

        Subdivision gotland = SubdivisionCode.SE.fromName("gotlands län").orElseThrow();
        assertEquals(SubdivisionCode.SE.I, gotland);

        assertTrue(SubdivisionCode.SE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision m = SubdivisionCode.SE.find("M").orElseThrow();
        assertEquals(SubdivisionCode.SE.M, m);

        Subdivision mFull = SubdivisionCode.SE.find("SE-M").orElseThrow();
        assertEquals(SubdivisionCode.SE.M, mFull);

        Subdivision dalarna = SubdivisionCode.SE.find("Dalarnas län").orElseThrow();
        assertEquals(SubdivisionCode.SE.W, dalarna);

        assertTrue(SubdivisionCode.SE.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.SE.getCounties();
        assertEquals(21, counties.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.SE.wikipedia());
        assertFalse(SubdivisionCode.SE.wikipedia().isBlank());
        assertTrue(SubdivisionCode.SE.wikipedia().contains("ISO_3166-2:SE"));

        assertNotNull(SubdivisionCode.SE.dateAdded());
        assertFalse(SubdivisionCode.SE.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.SE.lastUpdated());
        assertFalse(SubdivisionCode.SE.lastUpdated().isBlank());
    }
}
