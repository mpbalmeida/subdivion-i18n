package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionUYTest {

    @Test
    public void testUYSubdivisions() {
        SubdivisionCode.UY artigas = SubdivisionCode.UY.AR;
        assertEquals("UY-AR", artigas.getCode());
        assertEquals("Artigas", artigas.getSubdivisionName());
        assertEquals("department", artigas.getCategory());

        SubdivisionCode.UY paysandu = SubdivisionCode.UY.PA;
        assertEquals("UY-PA", paysandu.getCode());
        assertEquals("Paysandú", paysandu.getSubdivisionName());
        assertEquals("department", paysandu.getCategory());

        SubdivisionCode.UY treintaYTres = SubdivisionCode.UY.TT;
        assertEquals("UY-TT", treintaYTres.getCode());
        assertEquals("Treinta y Tres", treintaYTres.getSubdivisionName());
        assertEquals("department", treintaYTres.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.UY);
        assertNotNull(subdivisions);
        assertEquals(19, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("UY-AR", first.getCode());
        assertEquals("Artigas", first.getSubdivisionName());

        Subdivision last = subdivisions[18];
        assertEquals("UY-TT", last.getCode());
        assertEquals("Treinta y Tres", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.UY artigas = SubdivisionCode.UY.fromCode("UY-AR");
        assertEquals(SubdivisionCode.UY.AR, artigas);

        SubdivisionCode.UY rivera = SubdivisionCode.UY.fromCode("RV");
        assertEquals(SubdivisionCode.UY.RV, rivera);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.UY.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision rioNegro = SubdivisionCode.UY.fromName("Río Negro").orElseThrow();
        assertEquals(SubdivisionCode.UY.RN, rioNegro);

        Subdivision montevideo = SubdivisionCode.UY.fromName("montevideo").orElseThrow();
        assertEquals(SubdivisionCode.UY.MO, montevideo);

        assertTrue(SubdivisionCode.UY.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision rocha = SubdivisionCode.UY.find("RO").orElseThrow();
        assertEquals(SubdivisionCode.UY.RO, rocha);

        Subdivision rochaFull = SubdivisionCode.UY.find("UY-RO").orElseThrow();
        assertEquals(SubdivisionCode.UY.RO, rochaFull);

        Subdivision tacuarembo = SubdivisionCode.UY.find("Tacuarembó").orElseThrow();
        assertEquals(SubdivisionCode.UY.TA, tacuarembo);

        assertTrue(SubdivisionCode.UY.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.UY.getDepartments();
        assertEquals(19, departments.length);
        assertEquals(SubdivisionCode.UY.AR, departments[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.UY.wikipedia());
        assertFalse(SubdivisionCode.UY.wikipedia().isBlank());
        assertTrue(SubdivisionCode.UY.wikipedia().contains("ISO_3166-2:UY"));

        assertNotNull(SubdivisionCode.UY.dateAdded());
        assertFalse(SubdivisionCode.UY.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.UY.lastUpdated());
        assertFalse(SubdivisionCode.UY.lastUpdated().isBlank());
    }
}
