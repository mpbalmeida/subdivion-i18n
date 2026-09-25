package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionPATest {

    @Test
    public void testPASubdivisions() {
        SubdivisionCode.PA bocasDelToro = SubdivisionCode.PA.PA_1;
        assertEquals("PA-1", bocasDelToro.getCode());
        assertEquals("Bocas del Toro", bocasDelToro.getSubdivisionName());
        assertEquals("province", bocasDelToro.getCategory());

        SubdivisionCode.PA panamaOeste = SubdivisionCode.PA.PA_10;
        assertEquals("PA-10", panamaOeste.getCode());
        assertEquals("Panamá Oeste", panamaOeste.getSubdivisionName());
        assertEquals("province", panamaOeste.getCategory());

        SubdivisionCode.PA ngabeBugle = SubdivisionCode.PA.PA_NB;
        assertEquals("PA-NB", ngabeBugle.getCode());
        assertEquals("Ngäbe-Buglé", ngabeBugle.getSubdivisionName());
        assertEquals("indigenous region", ngabeBugle.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.PA);
        assertNotNull(subdivisions);
        assertEquals(14, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("PA-1", first.getCode());
        assertEquals("Bocas del Toro", first.getSubdivisionName());

        Subdivision last = subdivisions[13];
        assertEquals("PA-NT", last.getCode());
        assertEquals("Naso Tjër Di", last.getSubdivisionName());
        assertEquals("indigenous region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.PA chiriqui = SubdivisionCode.PA.fromCode("PA-4");
        assertEquals(SubdivisionCode.PA.PA_4, chiriqui);

        SubdivisionCode.PA gunaYala = SubdivisionCode.PA.fromCode("KY");
        assertEquals(SubdivisionCode.PA.PA_KY, gunaYala);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.PA.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision cocle = SubdivisionCode.PA.fromName("Coclé").orElseThrow();
        assertEquals(SubdivisionCode.PA.PA_2, cocle);

        Subdivision embera = SubdivisionCode.PA.fromName("emberá").orElseThrow();
        assertEquals(SubdivisionCode.PA.PA_EM, embera);

        assertTrue(SubdivisionCode.PA.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision darien = SubdivisionCode.PA.find("5").orElseThrow();
        assertEquals(SubdivisionCode.PA.PA_5, darien);

        Subdivision veraguasFull = SubdivisionCode.PA.find("PA-9").orElseThrow();
        assertEquals(SubdivisionCode.PA.PA_9, veraguasFull);

        Subdivision herrera = SubdivisionCode.PA.find("Herrera").orElseThrow();
        assertEquals(SubdivisionCode.PA.PA_6, herrera);

        assertTrue(SubdivisionCode.PA.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.PA.getProvinces();
        assertEquals(10, provinces.length);

        Subdivision[] indigenousRegions = SubdivisionCode.PA.getIndigenousRegions();
        assertEquals(4, indigenousRegions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.PA.wikipedia());
        assertFalse(SubdivisionCode.PA.wikipedia().isBlank());
        assertTrue(SubdivisionCode.PA.wikipedia().contains("ISO_3166-2:PA"));

        assertNotNull(SubdivisionCode.PA.dateAdded());
        assertFalse(SubdivisionCode.PA.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.PA.lastUpdated());
        assertFalse(SubdivisionCode.PA.lastUpdated().isBlank());
    }
}
