package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionARTest {

    @Test
    public void testARSubdivisions() {
        SubdivisionCode.AR salta = SubdivisionCode.AR.A;
        assertEquals("AR-A", salta.getCode());
        assertEquals("Salta", salta.getSubdivisionName());
        assertEquals("province", salta.getCategory());

        SubdivisionCode.AR cordoba = SubdivisionCode.AR.X;
        assertEquals("AR-X", cordoba.getCode());
        assertEquals("Córdoba", cordoba.getSubdivisionName());
        assertEquals("province", cordoba.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.AR);
        assertNotNull(subdivisions);
        assertEquals(24, subdivisions.length); // 23 provinces + 1 city

        Subdivision first = subdivisions[0];
        assertEquals("AR-A", first.getCode());
        assertEquals("Salta", first.getSubdivisionName());

        Subdivision last = subdivisions[23];
        assertEquals("AR-Z", last.getCode());
        assertEquals("Santa Cruz", last.getSubdivisionName());
        assertEquals("province", last.getCategory());

        Subdivision ciudadAutonoma = SubdivisionCode.AR.C;
        assertEquals("AR-C", ciudadAutonoma.getCode());
        assertEquals("Ciudad Autónoma de Buenos Aires", ciudadAutonoma.getSubdivisionName());
        assertEquals("city", ciudadAutonoma.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AR entreRios = SubdivisionCode.AR.fromCode("AR-E");
        assertEquals(SubdivisionCode.AR.E, entreRios);

        // Test lookup by subdivision part
        SubdivisionCode.AR salta = SubdivisionCode.AR.fromCode("A");
        assertEquals(SubdivisionCode.AR.A, salta);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision tucuman = SubdivisionCode.AR.fromName("Tucumán").orElseThrow();
        assertEquals(SubdivisionCode.AR.T, tucuman);

        Subdivision neuquen = SubdivisionCode.AR.fromName("neuquén").orElseThrow();
        assertEquals(SubdivisionCode.AR.Q, neuquen);

        assertTrue(SubdivisionCode.AR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision rioNegro = SubdivisionCode.AR.find("R").orElseThrow();
        assertEquals(SubdivisionCode.AR.R, rioNegro);

        Subdivision rioNegroFull = SubdivisionCode.AR.find("AR-R").orElseThrow();
        assertEquals(SubdivisionCode.AR.R, rioNegroFull);

        Subdivision rioNegroByName = SubdivisionCode.AR.find("Río Negro").orElseThrow();
        assertEquals(SubdivisionCode.AR.R, rioNegroByName);

        assertTrue(SubdivisionCode.AR.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.AR.getProvinces();
        assertEquals(23, provinces.length);

        Subdivision[] cities = SubdivisionCode.AR.getCities();
        assertEquals(1, cities.length);
        assertEquals(SubdivisionCode.AR.C, cities[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.AR.wikipedia());
        assertFalse(SubdivisionCode.AR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.AR.wikipedia().contains("ISO_3166-2:AR"));

        assertNotNull(SubdivisionCode.AR.dateAdded());
        assertFalse(SubdivisionCode.AR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.AR.lastUpdated());
        assertFalse(SubdivisionCode.AR.lastUpdated().isBlank());
    }
}
