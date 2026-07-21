package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionUATest {

    @Test
    public void testUASubdivisions() {
        SubdivisionCode.UA crimea = SubdivisionCode.UA.UA_43;
        assertEquals("UA-43", crimea.getCode());
        assertEquals("Crimea", crimea.getSubdivisionName());
        assertEquals("republic", crimea.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.UA);
        assertNotNull(subdivisions);
        assertEquals(27, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("UA-05", first.getCode());
        assertEquals("Vinnytsia", first.getSubdivisionName());

        Subdivision last = subdivisions[26];
        assertEquals("UA-77", last.getCode());
        assertEquals("Chernivtsi", last.getSubdivisionName());
        assertEquals("region", last.getCategory());

        Subdivision kyiv = SubdivisionCode.UA.UA_30;
        assertEquals("UA-30", kyiv.getCode());
        assertEquals("Kyiv City", kyiv.getSubdivisionName());
        assertEquals("city", kyiv.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.UA crimea = SubdivisionCode.UA.fromCode("UA-43");
        assertEquals(SubdivisionCode.UA.UA_43, crimea);

        SubdivisionCode.UA kyiv = SubdivisionCode.UA.fromCode("30");
        assertEquals(SubdivisionCode.UA.UA_30, kyiv);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.UA.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision kyiv = SubdivisionCode.UA.fromName("Kyiv City").orElseThrow();
        assertEquals(SubdivisionCode.UA.UA_30, kyiv);

        Subdivision chernivtsi = SubdivisionCode.UA.fromName("chernivtsi").orElseThrow();
        assertEquals(SubdivisionCode.UA.UA_77, chernivtsi);

        assertTrue(SubdivisionCode.UA.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision zhytomyr = SubdivisionCode.UA.find("18").orElseThrow();
        assertEquals(SubdivisionCode.UA.UA_18, zhytomyr);

        Subdivision zhytomyrFull = SubdivisionCode.UA.find("UA-18").orElseThrow();
        assertEquals(SubdivisionCode.UA.UA_18, zhytomyrFull);

        Subdivision zaporizhzhia = SubdivisionCode.UA.find("Zaporizhzhia").orElseThrow();
        assertEquals(SubdivisionCode.UA.UA_23, zaporizhzhia);

        assertTrue(SubdivisionCode.UA.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.UA.getRegions();
        assertEquals(24, regions.length);

        Subdivision[] cities = SubdivisionCode.UA.getCities();
        assertEquals(2, cities.length);

        Subdivision[] republics = SubdivisionCode.UA.getRepublics();
        assertEquals(1, republics.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.UA.wikipedia());
        assertFalse(SubdivisionCode.UA.wikipedia().isBlank());
        assertTrue(SubdivisionCode.UA.wikipedia().contains("ISO_3166-2:UA"));

        assertNotNull(SubdivisionCode.UA.dateAdded());
        assertFalse(SubdivisionCode.UA.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.UA.lastUpdated());
        assertFalse(SubdivisionCode.UA.lastUpdated().isBlank());
    }
}
