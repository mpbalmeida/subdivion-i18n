package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionSVTest {

    @Test
    public void testSVSubdivisions() {
        SubdivisionCode.SV ahuachapan = SubdivisionCode.SV.AH;
        assertEquals("SV-AH", ahuachapan.getCode());
        assertEquals("Ahuachapán", ahuachapan.getSubdivisionName());
        assertEquals("department", ahuachapan.getCategory());

        SubdivisionCode.SV sanSalvador = SubdivisionCode.SV.SS;
        assertEquals("SV-SS", sanSalvador.getCode());
        assertEquals("San Salvador", sanSalvador.getSubdivisionName());
        assertEquals("department", sanSalvador.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.SV);
        assertNotNull(subdivisions);
        assertEquals(14, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("SV-AH", first.getCode());
        assertEquals("Ahuachapán", first.getSubdivisionName());

        Subdivision last = subdivisions[13];
        assertEquals("SV-US", last.getCode());
        assertEquals("Usulután", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.SV ahuachapan = SubdivisionCode.SV.fromCode("SV-AH");
        assertEquals(SubdivisionCode.SV.AH, ahuachapan);

        SubdivisionCode.SV cabanas = SubdivisionCode.SV.fromCode("CA");
        assertEquals(SubdivisionCode.SV.CA, cabanas);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.SV.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision sanSalvador = SubdivisionCode.SV.fromName("San Salvador").orElseThrow();
        assertEquals(SubdivisionCode.SV.SS, sanSalvador);

        Subdivision usulutan = SubdivisionCode.SV.fromName("usulután").orElseThrow();
        assertEquals(SubdivisionCode.SV.US, usulutan);

        assertTrue(SubdivisionCode.SV.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision laLibertad = SubdivisionCode.SV.find("LI").orElseThrow();
        assertEquals(SubdivisionCode.SV.LI, laLibertad);

        Subdivision laLibertadFull = SubdivisionCode.SV.find("SV-LI").orElseThrow();
        assertEquals(SubdivisionCode.SV.LI, laLibertadFull);

        Subdivision morazan = SubdivisionCode.SV.find("Morazán").orElseThrow();
        assertEquals(SubdivisionCode.SV.MO, morazan);

        assertTrue(SubdivisionCode.SV.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.SV.getDepartments();
        assertEquals(14, departments.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.SV.wikipedia());
        assertFalse(SubdivisionCode.SV.wikipedia().isBlank());
        assertTrue(SubdivisionCode.SV.wikipedia().contains("ISO_3166-2:SV"));

        assertNotNull(SubdivisionCode.SV.dateAdded());
        assertFalse(SubdivisionCode.SV.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.SV.lastUpdated());
        assertFalse(SubdivisionCode.SV.lastUpdated().isBlank());
    }
}
