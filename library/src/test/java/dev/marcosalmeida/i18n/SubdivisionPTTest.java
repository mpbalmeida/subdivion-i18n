package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionPTTest {

    @Test
    public void testPTSubdivisions() {
        SubdivisionCode.PT aveiro = SubdivisionCode.PT.PT_01;
        assertEquals("PT-01", aveiro.getCode());
        assertEquals("Aveiro", aveiro.getSubdivisionName());
        assertEquals("district", aveiro.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.PT);
        assertNotNull(subdivisions);
        assertEquals(20, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("PT-01", first.getCode());
        assertEquals("Aveiro", first.getSubdivisionName());

        Subdivision last = subdivisions[19];
        assertEquals("PT-30", last.getCode());
        assertEquals("Região Autónoma da Madeira", last.getSubdivisionName());
        assertEquals("autonomous region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.PT aveiro = SubdivisionCode.PT.fromCode("PT-01");
        assertEquals(SubdivisionCode.PT.PT_01, aveiro);

        SubdivisionCode.PT lisboa = SubdivisionCode.PT.fromCode("11");
        assertEquals(SubdivisionCode.PT.PT_11, lisboa);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.PT.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision porto = SubdivisionCode.PT.fromName("Porto").orElseThrow();
        assertEquals(SubdivisionCode.PT.PT_13, porto);

        Subdivision lisboa = SubdivisionCode.PT.fromName("lisboa").orElseThrow();
        assertEquals(SubdivisionCode.PT.PT_11, lisboa);

        assertTrue(SubdivisionCode.PT.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision p11 = SubdivisionCode.PT.find("11").orElseThrow();
        assertEquals(SubdivisionCode.PT.PT_11, p11);

        Subdivision p11Full = SubdivisionCode.PT.find("PT-11").orElseThrow();
        assertEquals(SubdivisionCode.PT.PT_11, p11Full);

        Subdivision lisboa = SubdivisionCode.PT.find("Lisboa").orElseThrow();
        assertEquals(SubdivisionCode.PT.PT_11, lisboa);

        assertTrue(SubdivisionCode.PT.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] districts = SubdivisionCode.PT.getDistricts();
        assertEquals(18, districts.length);

        Subdivision[] autonomousRegions = SubdivisionCode.PT.getAutonomousRegions();
        assertEquals(2, autonomousRegions.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.PT.wikipedia());
        assertFalse(SubdivisionCode.PT.wikipedia().isBlank());
        assertTrue(SubdivisionCode.PT.wikipedia().contains("ISO_3166-2:PT"));

        assertNotNull(SubdivisionCode.PT.dateAdded());
        assertFalse(SubdivisionCode.PT.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.PT.lastUpdated());
        assertFalse(SubdivisionCode.PT.lastUpdated().isBlank());
    }
}
