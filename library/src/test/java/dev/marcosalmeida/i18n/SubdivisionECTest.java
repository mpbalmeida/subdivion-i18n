package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionECTest {

    @Test
    public void testECSubdivisions() {
        SubdivisionCode.EC galapagos = SubdivisionCode.EC.W;
        assertEquals("EC-W", galapagos.getCode());
        assertEquals("Galápagos", galapagos.getSubdivisionName());
        assertEquals("province", galapagos.getCategory());

        SubdivisionCode.EC canar = SubdivisionCode.EC.F;
        assertEquals("EC-F", canar.getCode());
        assertEquals("Cañar", canar.getSubdivisionName());
        assertEquals("province", canar.getCategory());

        SubdivisionCode.EC pichincha = SubdivisionCode.EC.P;
        assertEquals("EC-P", pichincha.getCode());
        assertEquals("Pichincha", pichincha.getSubdivisionName());
        assertEquals("province", pichincha.getCategory());

        SubdivisionCode.EC santoDomingo = SubdivisionCode.EC.SD;
        assertEquals("EC-SD", santoDomingo.getCode());
        assertEquals("Santo Domingo de los Tsáchilas", santoDomingo.getSubdivisionName());
        assertEquals("province", santoDomingo.getCategory());

        SubdivisionCode.EC santaElena = SubdivisionCode.EC.SE;
        assertEquals("EC-SE", santaElena.getCode());
        assertEquals("Santa Elena", santaElena.getSubdivisionName());
        assertEquals("province", santaElena.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.EC);
        assertNotNull(subdivisions);
        assertEquals(24, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("EC-A", first.getCode());
        assertEquals("Azuay", first.getSubdivisionName());

        Subdivision last = subdivisions[23];
        assertEquals("EC-Z", last.getCode());
        assertEquals("Zamora Chinchipe", last.getSubdivisionName());
        assertEquals("province", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.EC galapagos = SubdivisionCode.EC.fromCode("EC-W");
        assertEquals(SubdivisionCode.EC.W, galapagos);

        SubdivisionCode.EC santoDomingo = SubdivisionCode.EC.fromCode("SD");
        assertEquals(SubdivisionCode.EC.SD, santoDomingo);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.EC.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision galapagos = SubdivisionCode.EC.fromName("Galápagos").orElseThrow();
        assertEquals(SubdivisionCode.EC.W, galapagos);

        Subdivision manabi = SubdivisionCode.EC.fromName("manabí").orElseThrow();
        assertEquals(SubdivisionCode.EC.M, manabi);

        assertTrue(SubdivisionCode.EC.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision loja = SubdivisionCode.EC.find("L").orElseThrow();
        assertEquals(SubdivisionCode.EC.L, loja);

        Subdivision lojaFull = SubdivisionCode.EC.find("EC-L").orElseThrow();
        assertEquals(SubdivisionCode.EC.L, lojaFull);

        Subdivision losRios = SubdivisionCode.EC.find("Los Ríos").orElseThrow();
        assertEquals(SubdivisionCode.EC.R, losRios);

        assertTrue(SubdivisionCode.EC.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.EC.getProvinces();
        assertEquals(24, provinces.length);
        assertEquals(SubdivisionCode.EC.A, provinces[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.EC.wikipedia());
        assertFalse(SubdivisionCode.EC.wikipedia().isBlank());
        assertTrue(SubdivisionCode.EC.wikipedia().contains("ISO_3166-2:EC"));

        assertNotNull(SubdivisionCode.EC.dateAdded());
        assertFalse(SubdivisionCode.EC.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.EC.lastUpdated());
        assertFalse(SubdivisionCode.EC.lastUpdated().isBlank());
    }
}
