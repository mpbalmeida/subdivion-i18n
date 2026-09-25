package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionVETest {

    @Test
    public void testVESubdivisions() {
        SubdivisionCode.VE capital = SubdivisionCode.VE.A;
        assertEquals("VE-A", capital.getCode());
        assertEquals("Distrito Capital", capital.getSubdivisionName());
        assertEquals("capital district", capital.getCategory());

        SubdivisionCode.VE laGuaira = SubdivisionCode.VE.X;
        assertEquals("VE-X", laGuaira.getCode());
        assertEquals("La Guaira", laGuaira.getSubdivisionName());
        assertEquals("state", laGuaira.getCategory());

        SubdivisionCode.VE federal = SubdivisionCode.VE.W;
        assertEquals("VE-W", federal.getCode());
        assertEquals("Dependencias Federales", federal.getSubdivisionName());
        assertEquals("federal dependency", federal.getCategory());

        SubdivisionCode.VE merida = SubdivisionCode.VE.L;
        assertEquals("VE-L", merida.getCode());
        assertEquals("Mérida", merida.getSubdivisionName());
        assertEquals("state", merida.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.VE);
        assertNotNull(subdivisions);
        assertEquals(25, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("VE-A", first.getCode());
        assertEquals("Distrito Capital", first.getSubdivisionName());
        assertEquals("capital district", first.getCategory());

        Subdivision last = subdivisions[24];
        assertEquals("VE-Z", last.getCode());
        assertEquals("Amazonas", last.getSubdivisionName());
        assertEquals("state", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.VE merida = SubdivisionCode.VE.fromCode("VE-L");
        assertEquals(SubdivisionCode.VE.L, merida);

        SubdivisionCode.VE zulia = SubdivisionCode.VE.fromCode("V");
        assertEquals(SubdivisionCode.VE.V, zulia);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.VE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision nuevaEsparta = SubdivisionCode.VE.fromName("Nueva Esparta").orElseThrow();
        assertEquals(SubdivisionCode.VE.O, nuevaEsparta);

        Subdivision merida = SubdivisionCode.VE.fromName("mérida").orElseThrow();
        assertEquals(SubdivisionCode.VE.L, merida);

        assertTrue(SubdivisionCode.VE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision monagas = SubdivisionCode.VE.find("VE-N").orElseThrow();
        assertEquals(SubdivisionCode.VE.N, monagas);

        Subdivision sucre = SubdivisionCode.VE.find("Sucre").orElseThrow();
        assertEquals(SubdivisionCode.VE.R, sucre);

        assertTrue(SubdivisionCode.VE.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] states = SubdivisionCode.VE.getStates();
        assertEquals(23, states.length);
        assertEquals(SubdivisionCode.VE.B, states[0]);

        Subdivision[] capitalDistricts = SubdivisionCode.VE.getCapitalDistricts();
        assertEquals(1, capitalDistricts.length);
        assertEquals(SubdivisionCode.VE.A, capitalDistricts[0]);

        Subdivision[] federalDependencies = SubdivisionCode.VE.getFederalDependencies();
        assertEquals(1, federalDependencies.length);
        assertEquals(SubdivisionCode.VE.W, federalDependencies[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.VE.wikipedia());
        assertFalse(SubdivisionCode.VE.wikipedia().isBlank());
        assertTrue(SubdivisionCode.VE.wikipedia().contains("ISO_3166-2:VE"));

        assertNotNull(SubdivisionCode.VE.dateAdded());
        assertFalse(SubdivisionCode.VE.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.VE.lastUpdated());
        assertFalse(SubdivisionCode.VE.lastUpdated().isBlank());
    }
}
