package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionNITest {

    @Test
    public void testNISubdivisions() {
        SubdivisionCode.NI costaCaribeNorte = SubdivisionCode.NI.AN;
        assertEquals("NI-AN", costaCaribeNorte.getCode());
        assertEquals("Costa Caribe Norte", costaCaribeNorte.getSubdivisionName());
        assertEquals("autonomous region", costaCaribeNorte.getCategory());

        SubdivisionCode.NI esteli = SubdivisionCode.NI.ES;
        assertEquals("NI-ES", esteli.getCode());
        assertEquals("Estelí", esteli.getSubdivisionName());
        assertEquals("department", esteli.getCategory());

        SubdivisionCode.NI rioSanJuan = SubdivisionCode.NI.SJ;
        assertEquals("NI-SJ", rioSanJuan.getCode());
        assertEquals("Río San Juan", rioSanJuan.getSubdivisionName());
        assertEquals("department", rioSanJuan.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.NI);
        assertNotNull(subdivisions);
        assertEquals(17, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("NI-AN", first.getCode());
        assertEquals("Costa Caribe Norte", first.getSubdivisionName());

        Subdivision last = subdivisions[16];
        assertEquals("NI-SJ", last.getCode());
        assertEquals("Río San Juan", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.NI boaco = SubdivisionCode.NI.fromCode("NI-BO");
        assertEquals(SubdivisionCode.NI.BO, boaco);

        SubdivisionCode.NI managua = SubdivisionCode.NI.fromCode("MN");
        assertEquals(SubdivisionCode.NI.MN, managua);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.NI.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision costaCaribeNorte = SubdivisionCode.NI.fromName("Costa Caribe Norte").orElseThrow();
        assertEquals(SubdivisionCode.NI.AN, costaCaribeNorte);

        Subdivision leon = SubdivisionCode.NI.fromName("león").orElseThrow();
        assertEquals(SubdivisionCode.NI.LE, leon);

        assertTrue(SubdivisionCode.NI.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision granada = SubdivisionCode.NI.find("GR").orElseThrow();
        assertEquals(SubdivisionCode.NI.GR, granada);

        Subdivision granadaFull = SubdivisionCode.NI.find("NI-GR").orElseThrow();
        assertEquals(SubdivisionCode.NI.GR, granadaFull);

        Subdivision matagalpa = SubdivisionCode.NI.find("Matagalpa").orElseThrow();
        assertEquals(SubdivisionCode.NI.MT, matagalpa);

        assertTrue(SubdivisionCode.NI.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.NI.getDepartments();
        assertEquals(15, departments.length);

        Subdivision[] autonomousRegions = SubdivisionCode.NI.getAutonomousRegions();
        assertEquals(2, autonomousRegions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.NI.wikipedia());
        assertFalse(SubdivisionCode.NI.wikipedia().isBlank());
        assertTrue(SubdivisionCode.NI.wikipedia().contains("ISO_3166-2:NI"));

        assertNotNull(SubdivisionCode.NI.dateAdded());
        assertFalse(SubdivisionCode.NI.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.NI.lastUpdated());
        assertFalse(SubdivisionCode.NI.lastUpdated().isBlank());
    }
}
