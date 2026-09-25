package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionPYTest {

    @Test
    public void testPYSubdivisions() {
        SubdivisionCode.PY concepcion = SubdivisionCode.PY.PY_1;
        assertEquals("PY-1", concepcion.getCode());
        assertEquals("Concepción", concepcion.getSubdivisionName());
        assertEquals("department", concepcion.getCategory());

        SubdivisionCode.PY neembucu = SubdivisionCode.PY.PY_12;
        assertEquals("PY-12", neembucu.getCode());
        assertEquals("Ñeembucú", neembucu.getSubdivisionName());
        assertEquals("department", neembucu.getCategory());

        SubdivisionCode.PY asuncion = SubdivisionCode.PY.PY_ASU;
        assertEquals("PY-ASU", asuncion.getCode());
        assertEquals("Asunción", asuncion.getSubdivisionName());
        assertEquals("capital", asuncion.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.PY);
        assertNotNull(subdivisions);
        assertEquals(18, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("PY-1", first.getCode());
        assertEquals("Concepción", first.getSubdivisionName());

        Subdivision last = subdivisions[17];
        assertEquals("PY-ASU", last.getCode());
        assertEquals("Asunción", last.getSubdivisionName());
        assertEquals("capital", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.PY altoParana = SubdivisionCode.PY.fromCode("PY-10");
        assertEquals(SubdivisionCode.PY.PY_10, altoParana);

        SubdivisionCode.PY asuncion = SubdivisionCode.PY.fromCode("ASU");
        assertEquals(SubdivisionCode.PY.PY_ASU, asuncion);

        SubdivisionCode.PY boqueron = SubdivisionCode.PY.fromCode("PY-19");
        assertEquals(SubdivisionCode.PY.PY_19, boqueron);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.PY.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision neembucu = SubdivisionCode.PY.fromName("Ñeembucú").orElseThrow();
        assertEquals(SubdivisionCode.PY.PY_12, neembucu);

        Subdivision asuncion = SubdivisionCode.PY.fromName("asunción").orElseThrow();
        assertEquals(SubdivisionCode.PY.PY_ASU, asuncion);

        assertTrue(SubdivisionCode.PY.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision altoParaguay = SubdivisionCode.PY.find("PY-16").orElseThrow();
        assertEquals(SubdivisionCode.PY.PY_16, altoParaguay);

        Subdivision altoParaguayByName = SubdivisionCode.PY.find("Alto Paraguay").orElseThrow();
        assertEquals(SubdivisionCode.PY.PY_16, altoParaguayByName);

        Subdivision presidenteHayes = SubdivisionCode.PY.find("Presidente Hayes").orElseThrow();
        assertEquals(SubdivisionCode.PY.PY_15, presidenteHayes);

        assertTrue(SubdivisionCode.PY.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.PY.getDepartments();
        assertEquals(17, departments.length);
        assertEquals(SubdivisionCode.PY.PY_1, departments[0]);

        Subdivision[] capitals = SubdivisionCode.PY.getCapitals();
        assertEquals(1, capitals.length);
        assertEquals(SubdivisionCode.PY.PY_ASU, capitals[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.PY.wikipedia());
        assertFalse(SubdivisionCode.PY.wikipedia().isBlank());
        assertTrue(SubdivisionCode.PY.wikipedia().contains("ISO_3166-2:PY"));

        assertNotNull(SubdivisionCode.PY.dateAdded());
        assertFalse(SubdivisionCode.PY.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.PY.lastUpdated());
        assertFalse(SubdivisionCode.PY.lastUpdated().isBlank());
    }
}
