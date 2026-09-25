package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionGYTest {

    @Test
    public void testGYSubdivisions() {
        SubdivisionCode.GY barimaWaini = SubdivisionCode.GY.BA;
        assertEquals("GY-BA", barimaWaini.getCode());
        assertEquals("Barima-Waini", barimaWaini.getSubdivisionName());
        assertEquals("region", barimaWaini.getCategory());

        SubdivisionCode.GY essequiboIslands = SubdivisionCode.GY.ES;
        assertEquals("GY-ES", essequiboIslands.getCode());
        assertEquals("Essequibo Islands-West Demerara", essequiboIslands.getSubdivisionName());
        assertEquals("region", essequiboIslands.getCategory());

        SubdivisionCode.GY upperTakutu = SubdivisionCode.GY.UT;
        assertEquals("GY-UT", upperTakutu.getCode());
        assertEquals("Upper Takutu-Upper Essequibo", upperTakutu.getSubdivisionName());
        assertEquals("region", upperTakutu.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.GY);
        assertNotNull(subdivisions);
        assertEquals(10, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("GY-BA", first.getCode());
        assertEquals("Barima-Waini", first.getSubdivisionName());

        Subdivision last = subdivisions[9];
        assertEquals("GY-UT", last.getCode());
        assertEquals("Upper Takutu-Upper Essequibo", last.getSubdivisionName());
        assertEquals("region", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.GY barimaWaini = SubdivisionCode.GY.fromCode("GY-BA");
        assertEquals(SubdivisionCode.GY.BA, barimaWaini);

        SubdivisionCode.GY potaroSiparuni = SubdivisionCode.GY.fromCode("PT");
        assertEquals(SubdivisionCode.GY.PT, potaroSiparuni);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.GY.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision essequiboIslands = SubdivisionCode.GY.fromName("Essequibo Islands-West Demerara").orElseThrow();
        assertEquals(SubdivisionCode.GY.ES, essequiboIslands);

        Subdivision upperDemerara = SubdivisionCode.GY.fromName("upper demerara-berbice").orElseThrow();
        assertEquals(SubdivisionCode.GY.UD, upperDemerara);

        assertTrue(SubdivisionCode.GY.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision demeraraMahaica = SubdivisionCode.GY.find("DE").orElseThrow();
        assertEquals(SubdivisionCode.GY.DE, demeraraMahaica);

        Subdivision demeraraMahaicaFull = SubdivisionCode.GY.find("GY-DE").orElseThrow();
        assertEquals(SubdivisionCode.GY.DE, demeraraMahaicaFull);

        Subdivision pomeroonSupenaam = SubdivisionCode.GY.find("Pomeroon-Supenaam").orElseThrow();
        assertEquals(SubdivisionCode.GY.PM, pomeroonSupenaam);

        assertTrue(SubdivisionCode.GY.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.GY.getRegions();
        assertEquals(10, regions.length);
        assertEquals(SubdivisionCode.GY.BA, regions[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.GY.wikipedia());
        assertFalse(SubdivisionCode.GY.wikipedia().isBlank());
        assertTrue(SubdivisionCode.GY.wikipedia().contains("ISO_3166-2:GY"));

        assertNotNull(SubdivisionCode.GY.dateAdded());
        assertFalse(SubdivisionCode.GY.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.GY.lastUpdated());
        assertFalse(SubdivisionCode.GY.lastUpdated().isBlank());
    }
}
