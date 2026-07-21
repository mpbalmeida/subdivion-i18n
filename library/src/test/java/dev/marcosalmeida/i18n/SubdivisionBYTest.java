package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBYTest {

    @Test
    public void testBYSubdivisions() {
        SubdivisionCode.BY brest = SubdivisionCode.BY.BR;
        assertEquals("BY-BR", brest.getCode());
        assertEquals("Brest", brest.getSubdivisionName());
        assertEquals("oblast", brest.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.BY);
        assertNotNull(subdivisions);
        assertEquals(7, subdivisions.length); // 6 oblasts + 1 city

        Subdivision first = subdivisions[0];
        assertEquals("BY-BR", first.getCode());
        assertEquals("Brest", first.getSubdivisionName());

        Subdivision last = subdivisions[6];
        assertEquals("BY-VI", last.getCode());
        assertEquals("Vitebsk", last.getSubdivisionName());
        assertEquals("oblast", last.getCategory());

        Subdivision minskCity = SubdivisionCode.BY.HM;
        assertEquals("BY-HM", minskCity.getCode());
        assertEquals("Minsk City", minskCity.getSubdivisionName());
        assertEquals("city", minskCity.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BY brest = SubdivisionCode.BY.fromCode("BY-BR");
        assertEquals(SubdivisionCode.BY.BR, brest);

        SubdivisionCode.BY gomel = SubdivisionCode.BY.fromCode("HO");
        assertEquals(SubdivisionCode.BY.HO, gomel);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BY.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision minskCity = SubdivisionCode.BY.fromName("Minsk City").orElseThrow();
        assertEquals(SubdivisionCode.BY.HM, minskCity);

        Subdivision grodno = SubdivisionCode.BY.fromName("grodno").orElseThrow();
        assertEquals(SubdivisionCode.BY.HR, grodno);

        assertTrue(SubdivisionCode.BY.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision ma = SubdivisionCode.BY.find("MA").orElseThrow();
        assertEquals(SubdivisionCode.BY.MA, ma);

        Subdivision maFull = SubdivisionCode.BY.find("BY-MA").orElseThrow();
        assertEquals(SubdivisionCode.BY.MA, maFull);

        Subdivision vitebsk = SubdivisionCode.BY.find("Vitebsk").orElseThrow();
        assertEquals(SubdivisionCode.BY.VI, vitebsk);

        assertTrue(SubdivisionCode.BY.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        // Belarus subdivisions have no parent-child hierarchy
        Subdivision brest = SubdivisionCode.BY.BR;
        assertFalse(brest.getParent().isPresent());

        Subdivision minskCity = SubdivisionCode.BY.HM;
        assertFalse(minskCity.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] oblasts = SubdivisionCode.BY.getOblasts();
        assertEquals(6, oblasts.length);

        Subdivision[] cities = SubdivisionCode.BY.getCities();
        assertEquals(1, cities.length);
        assertEquals("Minsk City", cities[0].getSubdivisionName());
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.BY.wikipedia());
        assertFalse(SubdivisionCode.BY.wikipedia().isBlank());
        assertTrue(SubdivisionCode.BY.wikipedia().contains("ISO_3166-2:BY"));

        assertNotNull(SubdivisionCode.BY.dateAdded());
        assertFalse(SubdivisionCode.BY.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.BY.lastUpdated());
        assertFalse(SubdivisionCode.BY.lastUpdated().isBlank());
    }
}
