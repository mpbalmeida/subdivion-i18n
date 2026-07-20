package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBRTest {

    @Test
    public void testBRSubdivisions() {
        SubdivisionCode.BR ac = SubdivisionCode.BR.AC;
        assertEquals("BR-AC", ac.getCode());
        assertEquals("Acre", ac.getSubdivisionName());
        assertEquals("state", ac.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.BR);
        assertNotNull(subdivisions);
        assertEquals(27, subdivisions.length); // 26 states + 1 federal district

        Subdivision acre = subdivisions[0];
        assertEquals("BR-AC", acre.getCode());
        assertEquals("Acre", acre.getSubdivisionName());

        Subdivision tocantins = subdivisions[26];
        assertEquals("BR-TO", tocantins.getCode());
        assertEquals("Tocantins", tocantins.getSubdivisionName());
        assertEquals("state", tocantins.getCategory());

        Subdivision df = SubdivisionCode.BR.DF;
        assertEquals("BR-DF", df.getCode());
        assertEquals("Distrito Federal", df.getSubdivisionName());
        assertEquals("federal district", df.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BR ac = SubdivisionCode.BR.fromCode("BR-AC");
        assertEquals(SubdivisionCode.BR.AC, ac);

        // Test lookup by subdivision part
        SubdivisionCode.BR sp = SubdivisionCode.BR.fromCode("SP");
        assertEquals(SubdivisionCode.BR.SP, sp);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision acre = SubdivisionCode.BR.fromName("Acre").orElseThrow();
        assertEquals(SubdivisionCode.BR.AC, acre);

        Subdivision saoPaulo = SubdivisionCode.BR.fromName("são paulo").orElseThrow();
        assertEquals(SubdivisionCode.BR.SP, saoPaulo);

        assertTrue(SubdivisionCode.BR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision sp = SubdivisionCode.BR.find("SP").orElseThrow();
        assertEquals(SubdivisionCode.BR.SP, sp);

        Subdivision spFull = SubdivisionCode.BR.find("BR-SP").orElseThrow();
        assertEquals(SubdivisionCode.BR.SP, spFull);

        Subdivision saoPaulo = SubdivisionCode.BR.find("São Paulo").orElseThrow();
        assertEquals(SubdivisionCode.BR.SP, saoPaulo);

        assertTrue(SubdivisionCode.BR.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] states = SubdivisionCode.BR.getStates();
        assertEquals(26, states.length);

        Subdivision[] federalDistricts = SubdivisionCode.BR.getFederalDistricts();
        assertEquals(1, federalDistricts.length);
        assertEquals(SubdivisionCode.BR.DF, federalDistricts[0]);
    }
}
