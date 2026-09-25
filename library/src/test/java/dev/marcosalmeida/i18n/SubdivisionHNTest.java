package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionHNTest {

    @Test
    public void testHNSubdivisions() {
        SubdivisionCode.HN atlantida = SubdivisionCode.HN.AT;
        assertEquals("HN-AT", atlantida.getCode());
        assertEquals("Atlántida", atlantida.getSubdivisionName());
        assertEquals("department", atlantida.getCategory());

        SubdivisionCode.HN franciscoMorazan = SubdivisionCode.HN.FM;
        assertEquals("HN-FM", franciscoMorazan.getCode());
        assertEquals("Francisco Morazán", franciscoMorazan.getSubdivisionName());
        assertEquals("department", franciscoMorazan.getCategory());

        SubdivisionCode.HN yoro = SubdivisionCode.HN.YO;
        assertEquals("HN-YO", yoro.getCode());
        assertEquals("Yoro", yoro.getSubdivisionName());
        assertEquals("department", yoro.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.HN);
        assertNotNull(subdivisions);
        assertEquals(18, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("HN-AT", first.getCode());
        assertEquals("Atlántida", first.getSubdivisionName());

        Subdivision last = subdivisions[17];
        assertEquals("HN-YO", last.getCode());
        assertEquals("Yoro", last.getSubdivisionName());
        assertEquals("department", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.HN atlantida = SubdivisionCode.HN.fromCode("HN-AT");
        assertEquals(SubdivisionCode.HN.AT, atlantida);

        SubdivisionCode.HN yoro = SubdivisionCode.HN.fromCode("YO");
        assertEquals(SubdivisionCode.HN.YO, yoro);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.HN.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision atlantida = SubdivisionCode.HN.fromName("Atlántida").orElseThrow();
        assertEquals(SubdivisionCode.HN.AT, atlantida);

        Subdivision intibuca = SubdivisionCode.HN.fromName("intibucá").orElseThrow();
        assertEquals(SubdivisionCode.HN.IN, intibuca);

        assertTrue(SubdivisionCode.HN.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision yoro = SubdivisionCode.HN.find("YO").orElseThrow();
        assertEquals(SubdivisionCode.HN.YO, yoro);

        Subdivision yoroFull = SubdivisionCode.HN.find("HN-YO").orElseThrow();
        assertEquals(SubdivisionCode.HN.YO, yoroFull);

        Subdivision lempira = SubdivisionCode.HN.find("Lempira").orElseThrow();
        assertEquals(SubdivisionCode.HN.LE, lempira);

        assertTrue(SubdivisionCode.HN.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] departments = SubdivisionCode.HN.getDepartments();
        assertEquals(18, departments.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.HN.wikipedia());
        assertFalse(SubdivisionCode.HN.wikipedia().isBlank());
        assertTrue(SubdivisionCode.HN.wikipedia().contains("ISO_3166-2:HN"));

        assertNotNull(SubdivisionCode.HN.dateAdded());
        assertFalse(SubdivisionCode.HN.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.HN.lastUpdated());
        assertFalse(SubdivisionCode.HN.lastUpdated().isBlank());
    }
}
