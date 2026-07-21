package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionGLTest {

    @Test
    public void testGLSubdivisions() {
        SubdivisionCode.GL avannaata = SubdivisionCode.GL.AV;
        assertEquals("GL-AV", avannaata.getCode());
        assertEquals("Avannaata", avannaata.getSubdivisionName());
        assertEquals("municipality", avannaata.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.GL);
        assertNotNull(subdivisions);
        assertEquals(5, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("GL-AV", first.getCode());
        assertEquals("Avannaata", first.getSubdivisionName());

        Subdivision last = subdivisions[4];
        assertEquals("GL-SM", last.getCode());
        assertEquals("Sermersooq", last.getSubdivisionName());
        assertEquals("municipality", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.GL av = SubdivisionCode.GL.fromCode("GL-AV");
        assertEquals(SubdivisionCode.GL.AV, av);

        SubdivisionCode.GL ku = SubdivisionCode.GL.fromCode("KU");
        assertEquals(SubdivisionCode.GL.KU, ku);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.GL.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision sermersooq = SubdivisionCode.GL.fromName("Sermersooq").orElseThrow();
        assertEquals(SubdivisionCode.GL.SM, sermersooq);

        Subdivision qeqqata = SubdivisionCode.GL.fromName("qeqqata").orElseThrow();
        assertEquals(SubdivisionCode.GL.QE, qeqqata);

        assertTrue(SubdivisionCode.GL.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision av = SubdivisionCode.GL.find("AV").orElseThrow();
        assertEquals(SubdivisionCode.GL.AV, av);

        Subdivision avFull = SubdivisionCode.GL.find("GL-AV").orElseThrow();
        assertEquals(SubdivisionCode.GL.AV, avFull);

        Subdivision kujalleq = SubdivisionCode.GL.find("Kujalleq").orElseThrow();
        assertEquals(SubdivisionCode.GL.KU, kujalleq);

        assertTrue(SubdivisionCode.GL.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] municipalities = SubdivisionCode.GL.getMunicipalities();
        assertEquals(5, municipalities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.GL.wikipedia());
        assertFalse(SubdivisionCode.GL.wikipedia().isBlank());
        assertTrue(SubdivisionCode.GL.wikipedia().contains("ISO_3166-2:GL"));

        assertNotNull(SubdivisionCode.GL.dateAdded());
        assertFalse(SubdivisionCode.GL.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.GL.lastUpdated());
        assertFalse(SubdivisionCode.GL.lastUpdated().isBlank());
    }
}
