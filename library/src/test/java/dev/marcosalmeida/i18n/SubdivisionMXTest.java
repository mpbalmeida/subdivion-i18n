package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionMXTest {

    @Test
    public void testMXSubdivisions() {
        SubdivisionCode.MX agu = SubdivisionCode.MX.AGU;
        assertEquals("MX-AGU", agu.getCode());
        assertEquals("Aguascalientes", agu.getSubdivisionName());
        assertEquals("state", agu.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.MX);
        assertNotNull(subdivisions);
        assertEquals(32, subdivisions.length); // 31 states + 1 federal entity

        Subdivision aguascalientes = subdivisions[0];
        assertEquals("MX-AGU", aguascalientes.getCode());
        assertEquals("Aguascalientes", aguascalientes.getSubdivisionName());

        Subdivision zacatecas = subdivisions[31];
        assertEquals("MX-ZAC", zacatecas.getCode());
        assertEquals("Zacatecas", zacatecas.getSubdivisionName());
        assertEquals("state", zacatecas.getCategory());

        Subdivision cmx = SubdivisionCode.MX.CMX;
        assertEquals("MX-CMX", cmx.getCode());
        assertEquals("Ciudad de México", cmx.getSubdivisionName());
        assertEquals("federal entity", cmx.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.MX agu = SubdivisionCode.MX.fromCode("MX-AGU");
        assertEquals(SubdivisionCode.MX.AGU, agu);

        // Test lookup by subdivision part
        SubdivisionCode.MX zac = SubdivisionCode.MX.fromCode("ZAC");
        assertEquals(SubdivisionCode.MX.ZAC, zac);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.MX.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision aguascalientes = SubdivisionCode.MX.fromName("Aguascalientes").orElseThrow();
        assertEquals(SubdivisionCode.MX.AGU, aguascalientes);

        Subdivision zacatecas = SubdivisionCode.MX.fromName("zacatecas").orElseThrow();
        assertEquals(SubdivisionCode.MX.ZAC, zacatecas);

        assertTrue(SubdivisionCode.MX.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision zac = SubdivisionCode.MX.find("ZAC").orElseThrow();
        assertEquals(SubdivisionCode.MX.ZAC, zac);

        Subdivision zacFull = SubdivisionCode.MX.find("MX-ZAC").orElseThrow();
        assertEquals(SubdivisionCode.MX.ZAC, zacFull);

        Subdivision zacatecas = SubdivisionCode.MX.find("Zacatecas").orElseThrow();
        assertEquals(SubdivisionCode.MX.ZAC, zacatecas);

        assertTrue(SubdivisionCode.MX.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] states = SubdivisionCode.MX.getStates();
        assertEquals(31, states.length);

        Subdivision[] federalEntities = SubdivisionCode.MX.getFederalEntities();
        assertEquals(1, federalEntities.length);
        assertEquals(SubdivisionCode.MX.CMX, federalEntities[0]);
    }
}
