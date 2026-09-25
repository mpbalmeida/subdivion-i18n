package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionPETest {

    @Test
    public void testPESubdivisions() {
        SubdivisionCode.PE amazonas = SubdivisionCode.PE.AMA;
        assertEquals("PE-AMA", amazonas.getCode());
        assertEquals("Amazonas", amazonas.getSubdivisionName());
        assertEquals("region", amazonas.getCategory());

        SubdivisionCode.PE lima = SubdivisionCode.PE.LIM;
        assertEquals("PE-LIM", lima.getCode());
        assertEquals("Lima", lima.getSubdivisionName());
        assertEquals("region", lima.getCategory());

        SubdivisionCode.PE limaMetropolitana = SubdivisionCode.PE.LMA;
        assertEquals("PE-LMA", limaMetropolitana.getCode());
        assertEquals("Municipalidad Metropolitana de Lima", limaMetropolitana.getSubdivisionName());
        assertEquals("municipality", limaMetropolitana.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.PE);
        assertNotNull(subdivisions);
        assertEquals(26, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("PE-AMA", first.getCode());
        assertEquals("Amazonas", first.getSubdivisionName());

        Subdivision last = subdivisions[25];
        assertEquals("PE-UCA", last.getCode());
        assertEquals("Ucayali", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.PE amazonas = SubdivisionCode.PE.fromCode("PE-AMA");
        assertEquals(SubdivisionCode.PE.AMA, amazonas);

        SubdivisionCode.PE limaMetropolitana = SubdivisionCode.PE.fromCode("LMA");
        assertEquals(SubdivisionCode.PE.LMA, limaMetropolitana);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.PE.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision limaMetropolitana = SubdivisionCode.PE.fromName("Municipalidad Metropolitana de Lima").orElseThrow();
        assertEquals(SubdivisionCode.PE.LMA, limaMetropolitana);

        Subdivision apurimac = SubdivisionCode.PE.fromName("apurímac").orElseThrow();
        assertEquals(SubdivisionCode.PE.APU, apurimac);

        assertTrue(SubdivisionCode.PE.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision ucayali = SubdivisionCode.PE.find("UCA").orElseThrow();
        assertEquals(SubdivisionCode.PE.UCA, ucayali);

        Subdivision ucayaliFull = SubdivisionCode.PE.find("PE-UCA").orElseThrow();
        assertEquals(SubdivisionCode.PE.UCA, ucayaliFull);

        Subdivision sanMartin = SubdivisionCode.PE.find("San Martín").orElseThrow();
        assertEquals(SubdivisionCode.PE.SAM, sanMartin);

        assertTrue(SubdivisionCode.PE.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.PE.getRegions();
        assertEquals(25, regions.length);
        assertEquals(SubdivisionCode.PE.AMA, regions[0]);

        Subdivision[] municipalities = SubdivisionCode.PE.getMunicipalities();
        assertEquals(1, municipalities.length);
        assertEquals(SubdivisionCode.PE.LMA, municipalities[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.PE.wikipedia());
        assertFalse(SubdivisionCode.PE.wikipedia().isBlank());
        assertTrue(SubdivisionCode.PE.wikipedia().contains("ISO_3166-2:PE"));

        assertNotNull(SubdivisionCode.PE.dateAdded());
        assertFalse(SubdivisionCode.PE.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.PE.lastUpdated());
        assertFalse(SubdivisionCode.PE.lastUpdated().isBlank());
    }
}
