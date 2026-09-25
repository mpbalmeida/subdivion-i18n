package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCOTest {

    @Test
    public void testCOSubdivisions() {
        SubdivisionCode.CO amazonas = SubdivisionCode.CO.AMA;
        assertEquals("CO-AMA", amazonas.getCode());
        assertEquals("Amazonas", amazonas.getSubdivisionName());
        assertEquals("department", amazonas.getCategory());

        SubdivisionCode.CO bogota = SubdivisionCode.CO.DC;
        assertEquals("CO-DC", bogota.getCode());
        assertEquals("Distrito Capital de Bogotá", bogota.getSubdivisionName());
        assertEquals("capital district", bogota.getCategory());

        SubdivisionCode.CO sanAndres = SubdivisionCode.CO.SAP;
        assertEquals("CO-SAP", sanAndres.getCode());
        assertEquals("San Andrés, Providencia y Santa Catalina", sanAndres.getSubdivisionName());
        assertEquals("department", sanAndres.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.CO);
        assertNotNull(subdivisions);
        assertEquals(33, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("CO-AMA", first.getCode());
        assertEquals("Amazonas", first.getSubdivisionName());
        assertEquals("department", first.getCategory());

        Subdivision last = subdivisions[32];
        assertEquals("CO-VID", last.getCode());
        assertEquals("Vichada", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CO boyaca = SubdivisionCode.CO.fromCode("CO-BOY");
        assertEquals(SubdivisionCode.CO.BOY, boyaca);

        SubdivisionCode.CO cordoba = SubdivisionCode.CO.fromCode("COR");
        assertEquals(SubdivisionCode.CO.COR, cordoba);

        SubdivisionCode.CO bogota = SubdivisionCode.CO.fromCode("DC");
        assertEquals(SubdivisionCode.CO.DC, bogota);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CO.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision bogota = SubdivisionCode.CO.fromName("Distrito Capital de Bogotá").orElseThrow();
        assertEquals(SubdivisionCode.CO.DC, bogota);

        Subdivision choco = SubdivisionCode.CO.fromName("chocó").orElseThrow();
        assertEquals(SubdivisionCode.CO.CHO, choco);

        assertTrue(SubdivisionCode.CO.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision valle = SubdivisionCode.CO.find("VAC").orElseThrow();
        assertEquals(SubdivisionCode.CO.VAC, valle);

        Subdivision valleFull = SubdivisionCode.CO.find("CO-VAC").orElseThrow();
        assertEquals(SubdivisionCode.CO.VAC, valleFull);

        Subdivision quindio = SubdivisionCode.CO.find("Quindío").orElseThrow();
        assertEquals(SubdivisionCode.CO.QUI, quindio);

        assertTrue(SubdivisionCode.CO.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.CO.getDepartments();
        assertEquals(32, departments.length);
        assertEquals(SubdivisionCode.CO.AMA, departments[0]);

        Subdivision[] capitalDistricts = SubdivisionCode.CO.getCapitalDistricts();
        assertEquals(1, capitalDistricts.length);
        assertEquals(SubdivisionCode.CO.DC, capitalDistricts[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.CO.wikipedia());
        assertFalse(SubdivisionCode.CO.wikipedia().isBlank());
        assertTrue(SubdivisionCode.CO.wikipedia().contains("ISO_3166-2:CO"));

        assertNotNull(SubdivisionCode.CO.dateAdded());
        assertFalse(SubdivisionCode.CO.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.CO.lastUpdated());
        assertFalse(SubdivisionCode.CO.lastUpdated().isBlank());
    }
}
