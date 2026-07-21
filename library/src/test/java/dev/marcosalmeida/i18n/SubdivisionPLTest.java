package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionPLTest {

    @Test
    public void testPLSubdivisions() {
        SubdivisionCode.PL dolnoslaskie = SubdivisionCode.PL.PL_02;
        assertEquals("PL-02", dolnoslaskie.getCode());
        assertEquals("Dolnośląskie", dolnoslaskie.getSubdivisionName());
        assertEquals("Voivodship", dolnoslaskie.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.PL);
        assertNotNull(subdivisions);
        assertEquals(16, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("PL-02", first.getCode());
        assertEquals("Dolnośląskie", first.getSubdivisionName());

        Subdivision last = subdivisions[15];
        assertEquals("PL-32", last.getCode());
        assertEquals("Zachodniopomorskie", last.getSubdivisionName());
        assertEquals("Voivodship", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.PL zachodniopomorskie = SubdivisionCode.PL.fromCode("PL-32");
        assertEquals(SubdivisionCode.PL.PL_32, zachodniopomorskie);

        SubdivisionCode.PL dolnoslaskie = SubdivisionCode.PL.fromCode("02");
        assertEquals(SubdivisionCode.PL.PL_02, dolnoslaskie);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.PL.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision mazowieckie = SubdivisionCode.PL.fromName("Mazowieckie").orElseThrow();
        assertEquals(SubdivisionCode.PL.PL_14, mazowieckie);

        Subdivision lodzkie = SubdivisionCode.PL.fromName("łódzkie").orElseThrow();
        assertEquals(SubdivisionCode.PL.PL_10, lodzkie);

        assertTrue(SubdivisionCode.PL.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision pl24 = SubdivisionCode.PL.find("24").orElseThrow();
        assertEquals(SubdivisionCode.PL.PL_24, pl24);

        Subdivision pl24Full = SubdivisionCode.PL.find("PL-24").orElseThrow();
        assertEquals(SubdivisionCode.PL.PL_24, pl24Full);

        Subdivision slaskie = SubdivisionCode.PL.find("Śląskie").orElseThrow();
        assertEquals(SubdivisionCode.PL.PL_24, slaskie);

        assertTrue(SubdivisionCode.PL.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] voivodships = SubdivisionCode.PL.getVoivodships();
        assertEquals(16, voivodships.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.PL.wikipedia());
        assertFalse(SubdivisionCode.PL.wikipedia().isBlank());
        assertTrue(SubdivisionCode.PL.wikipedia().contains("ISO_3166-2:PL"));

        assertNotNull(SubdivisionCode.PL.dateAdded());
        assertFalse(SubdivisionCode.PL.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.PL.lastUpdated());
        assertFalse(SubdivisionCode.PL.lastUpdated().isBlank());
    }
}
