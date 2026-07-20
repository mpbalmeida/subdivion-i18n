package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionITTest {

    @Test
    public void testITSubdivisions() {
        SubdivisionCode.IT lombardia = SubdivisionCode.IT.IT_25;
        assertEquals("IT-25", lombardia.getCode());
        assertEquals("Lombardia", lombardia.getSubdivisionName());
        assertEquals("region", lombardia.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.IT);
        assertNotNull(subdivisions);
        assertEquals(20, subdivisions.length);

        Subdivision abruzzo = subdivisions[0];
        assertEquals("IT-65", abruzzo.getCode());
        assertEquals("Abruzzo", abruzzo.getSubdivisionName());

        Subdivision veneto = subdivisions[19];
        assertEquals("IT-34", veneto.getCode());
        assertEquals("Veneto", veneto.getSubdivisionName());
        assertEquals("region", veneto.getCategory());

        Subdivision sardegna = SubdivisionCode.IT.IT_88;
        assertEquals("IT-88", sardegna.getCode());
        assertEquals("Sardegna", sardegna.getSubdivisionName());
        assertEquals("autonomous region", sardegna.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.IT lazio = SubdivisionCode.IT.fromCode("IT-62");
        assertEquals(SubdivisionCode.IT.IT_62, lazio);

        // Test lookup by subdivision part
        SubdivisionCode.IT sicilia = SubdivisionCode.IT.fromCode("82");
        assertEquals(SubdivisionCode.IT.IT_82, sicilia);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.IT.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision toscana = SubdivisionCode.IT.fromName("Toscana").orElseThrow();
        assertEquals(SubdivisionCode.IT.IT_52, toscana);

        Subdivision puglia = SubdivisionCode.IT.fromName("puglia").orElseThrow();
        assertEquals(SubdivisionCode.IT.IT_75, puglia);

        assertTrue(SubdivisionCode.IT.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision liguria = SubdivisionCode.IT.find("42").orElseThrow();
        assertEquals(SubdivisionCode.IT.IT_42, liguria);

        Subdivision liguriaFull = SubdivisionCode.IT.find("IT-42").orElseThrow();
        assertEquals(SubdivisionCode.IT.IT_42, liguriaFull);

        Subdivision marche = SubdivisionCode.IT.find("Marche").orElseThrow();
        assertEquals(SubdivisionCode.IT.IT_57, marche);

        assertTrue(SubdivisionCode.IT.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.IT.getRegions();
        assertEquals(15, regions.length);

        Subdivision[] autonomousRegions = SubdivisionCode.IT.getAutonomousRegions();
        assertEquals(5, autonomousRegions.length);
    }

    @Test
    public void testGetSubdivisionCode() {
        assertEquals("25", SubdivisionCode.IT.IT_25.getSubdivisionCode());
        assertEquals("82", SubdivisionCode.IT.IT_82.getSubdivisionCode());
    }
}
