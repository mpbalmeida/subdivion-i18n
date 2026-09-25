package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBOTest {

    @Test
    public void testBOSubdivisions() {
        SubdivisionCode.BO c = SubdivisionCode.BO.C;
        assertEquals("BO-C", c.getCode());
        assertEquals("Cochabamba", c.getSubdivisionName());
        assertEquals("department", c.getCategory());

        SubdivisionCode.BO p = SubdivisionCode.BO.P;
        assertEquals("BO-P", p.getCode());
        assertEquals("Potosí", p.getSubdivisionName());
        assertEquals("department", p.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.BO);
        assertNotNull(subdivisions);
        assertEquals(9, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("BO-B", first.getCode());
        assertEquals("El Beni", first.getSubdivisionName());

        Subdivision last = subdivisions[8];
        assertEquals("BO-T", last.getCode());
        assertEquals("Tarija", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BO b = SubdivisionCode.BO.fromCode("BO-B");
        assertEquals(SubdivisionCode.BO.B, b);

        SubdivisionCode.BO t = SubdivisionCode.BO.fromCode("T");
        assertEquals(SubdivisionCode.BO.T, t);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BO.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision cochabamba = SubdivisionCode.BO.fromName("Cochabamba").orElseThrow();
        assertEquals(SubdivisionCode.BO.C, cochabamba);

        Subdivision elBeni = SubdivisionCode.BO.fromName("el beni").orElseThrow();
        assertEquals(SubdivisionCode.BO.B, elBeni);

        assertTrue(SubdivisionCode.BO.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision santaCruz = SubdivisionCode.BO.find("S").orElseThrow();
        assertEquals(SubdivisionCode.BO.S, santaCruz);

        Subdivision santaCruzFull = SubdivisionCode.BO.find("BO-S").orElseThrow();
        assertEquals(SubdivisionCode.BO.S, santaCruzFull);

        Subdivision potosi = SubdivisionCode.BO.find("Potosí").orElseThrow();
        assertEquals(SubdivisionCode.BO.P, potosi);

        assertTrue(SubdivisionCode.BO.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.BO.getDepartments();
        assertEquals(9, departments.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.BO.wikipedia());
        assertFalse(SubdivisionCode.BO.wikipedia().isBlank());
        assertTrue(SubdivisionCode.BO.wikipedia().contains("ISO_3166-2:BO"));

        assertNotNull(SubdivisionCode.BO.dateAdded());
        assertFalse(SubdivisionCode.BO.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.BO.lastUpdated());
        assertFalse(SubdivisionCode.BO.lastUpdated().isBlank());
    }
}
