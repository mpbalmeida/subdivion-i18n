package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionBATest {

    @Test
    public void testBASubdivisions() {
        SubdivisionCode.BA fBiH = SubdivisionCode.BA.BIH;
        assertEquals("BA-BIH", fBiH.getCode());
        assertEquals("Federation of Bosnia and Herzegovina", fBiH.getSubdivisionName());
        assertEquals("entity", fBiH.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.BA);
        assertNotNull(subdivisions);
        assertEquals(3, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("BA-BIH", first.getCode());
        assertEquals("Federation of Bosnia and Herzegovina", first.getSubdivisionName());

        Subdivision last = subdivisions[2];
        assertEquals("BA-SRP", last.getCode());
        assertEquals("Republika Srpska", last.getSubdivisionName());
        assertEquals("entity", last.getCategory());

        Subdivision brcko = SubdivisionCode.BA.BRC;
        assertEquals("BA-BRC", brcko.getCode());
        assertEquals("Brčko District", brcko.getSubdivisionName());
        assertEquals("district with special status", brcko.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.BA srp = SubdivisionCode.BA.fromCode("BA-SRP");
        assertEquals(SubdivisionCode.BA.SRP, srp);

        SubdivisionCode.BA brc = SubdivisionCode.BA.fromCode("BRC");
        assertEquals(SubdivisionCode.BA.BRC, brc);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.BA.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision fBiH = SubdivisionCode.BA.fromName("Federation of Bosnia and Herzegovina").orElseThrow();
        assertEquals(SubdivisionCode.BA.BIH, fBiH);

        Subdivision brcko = SubdivisionCode.BA.fromName("brčko district").orElseThrow();
        assertEquals(SubdivisionCode.BA.BRC, brcko);

        assertTrue(SubdivisionCode.BA.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision bih = SubdivisionCode.BA.find("BIH").orElseThrow();
        assertEquals(SubdivisionCode.BA.BIH, bih);

        Subdivision bihFull = SubdivisionCode.BA.find("BA-BIH").orElseThrow();
        assertEquals(SubdivisionCode.BA.BIH, bihFull);

        Subdivision republikaSrpska = SubdivisionCode.BA.find("Republika Srpska").orElseThrow();
        assertEquals(SubdivisionCode.BA.SRP, republikaSrpska);

        assertTrue(SubdivisionCode.BA.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision fBiH = SubdivisionCode.BA.BIH;
        assertFalse(fBiH.getParent().isPresent());

        Subdivision srp = SubdivisionCode.BA.SRP;
        assertFalse(srp.getParent().isPresent());

        Subdivision brcko = SubdivisionCode.BA.BRC;
        assertFalse(brcko.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] entities = SubdivisionCode.BA.getEntities();
        assertEquals(2, entities.length);

        // Verify both entities are present
        boolean hasBiH = false;
        boolean hasSRP = false;
        for (Subdivision s : entities) {
            if (s.equals(SubdivisionCode.BA.BIH)) {
                hasBiH = true;
            }
            if (s.equals(SubdivisionCode.BA.SRP)) {
                hasSRP = true;
            }
        }
        assertTrue(hasBiH);
        assertTrue(hasSRP);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.BA.wikipedia());
        assertFalse(SubdivisionCode.BA.wikipedia().isBlank());
        assertTrue(SubdivisionCode.BA.wikipedia().contains("ISO_3166-2:BA"));

        assertNotNull(SubdivisionCode.BA.dateAdded());
        assertFalse(SubdivisionCode.BA.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.BA.lastUpdated());
        assertFalse(SubdivisionCode.BA.lastUpdated().isBlank());
    }
}
