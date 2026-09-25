package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionSRTest {

    @Test
    public void testSRSubdivisions() {
        SubdivisionCode.SR brokopondo = SubdivisionCode.SR.BR;
        assertEquals("SR-BR", brokopondo.getCode());
        assertEquals("Brokopondo", brokopondo.getSubdivisionName());
        assertEquals("district", brokopondo.getCategory());

        SubdivisionCode.SR paramaribo = SubdivisionCode.SR.PM;
        assertEquals("SR-PM", paramaribo.getCode());
        assertEquals("Paramaribo", paramaribo.getSubdivisionName());
        assertEquals("district", paramaribo.getCategory());

        SubdivisionCode.SR wanica = SubdivisionCode.SR.WA;
        assertEquals("SR-WA", wanica.getCode());
        assertEquals("Wanica", wanica.getSubdivisionName());
        assertEquals("district", wanica.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.SR);
        assertNotNull(subdivisions);
        assertEquals(10, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("SR-BR", first.getCode());
        assertEquals("Brokopondo", first.getSubdivisionName());

        Subdivision last = subdivisions[9];
        assertEquals("SR-WA", last.getCode());
        assertEquals("Wanica", last.getSubdivisionName());
        assertEquals("district", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.SR brokopondo = SubdivisionCode.SR.fromCode("SR-BR");
        assertEquals(SubdivisionCode.SR.BR, brokopondo);

        SubdivisionCode.SR saramacca = SubdivisionCode.SR.fromCode("SA");
        assertEquals(SubdivisionCode.SR.SA, saramacca);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.SR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision paramaribo = SubdivisionCode.SR.fromName("Paramaribo").orElseThrow();
        assertEquals(SubdivisionCode.SR.PM, paramaribo);

        Subdivision brokopondo = SubdivisionCode.SR.fromName("brokopondo").orElseThrow();
        assertEquals(SubdivisionCode.SR.BR, brokopondo);

        assertTrue(SubdivisionCode.SR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision nickerie = SubdivisionCode.SR.find("NI").orElseThrow();
        assertEquals(SubdivisionCode.SR.NI, nickerie);

        Subdivision nickerieFull = SubdivisionCode.SR.find("SR-NI").orElseThrow();
        assertEquals(SubdivisionCode.SR.NI, nickerieFull);

        Subdivision sipaliwini = SubdivisionCode.SR.find("Sipaliwini").orElseThrow();
        assertEquals(SubdivisionCode.SR.SI, sipaliwini);

        assertTrue(SubdivisionCode.SR.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] districts = SubdivisionCode.SR.getDistricts();
        assertEquals(10, districts.length);
        assertEquals(SubdivisionCode.SR.BR, districts[0]);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.SR.wikipedia());
        assertFalse(SubdivisionCode.SR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.SR.wikipedia().contains("ISO_3166-2:SR"));

        assertNotNull(SubdivisionCode.SR.dateAdded());
        assertFalse(SubdivisionCode.SR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.SR.lastUpdated());
        assertFalse(SubdivisionCode.SR.lastUpdated().isBlank());
    }
}
