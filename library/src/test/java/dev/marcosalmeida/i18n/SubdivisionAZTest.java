package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionAZTest {

    @Test
    public void testAZSubdivisions() {
        SubdivisionCode.AZ nx = SubdivisionCode.AZ.NX;
        assertEquals("AZ-NX", nx.getCode());
        assertEquals("Nakhchivan", nx.getSubdivisionName());
        assertEquals("autonomous republic", nx.getCategory());

        SubdivisionCode.AZ ba = SubdivisionCode.AZ.BA;
        assertEquals("AZ-BA", ba.getCode());
        assertEquals("Baku", ba.getSubdivisionName());
        assertEquals("municipality", ba.getCategory());

        SubdivisionCode.AZ abs = SubdivisionCode.AZ.ABS;
        assertEquals("AZ-ABS", abs.getCode());
        assertEquals("Absheron", abs.getSubdivisionName());
        assertEquals("rayon", abs.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.AZ);
        assertNotNull(subdivisions);
        assertEquals(78, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("AZ-ABS", first.getCode());
        assertEquals("Absheron", first.getSubdivisionName());

        Subdivision last = subdivisions[77];
        assertEquals("AZ-ZAR", last.getCode());
        assertEquals("Zardab", last.getSubdivisionName());
        assertEquals("rayon", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.AZ nx = SubdivisionCode.AZ.fromCode("AZ-NX");
        assertEquals(SubdivisionCode.AZ.NX, nx);

        SubdivisionCode.AZ ba = SubdivisionCode.AZ.fromCode("BA");
        assertEquals(SubdivisionCode.AZ.BA, ba);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.AZ.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision baku = SubdivisionCode.AZ.fromName("Baku").orElseThrow();
        assertEquals(SubdivisionCode.AZ.BA, baku);

        Subdivision sumgayit = SubdivisionCode.AZ.fromName("sumgayit").orElseThrow();
        assertEquals(SubdivisionCode.AZ.SM, sumgayit);

        assertTrue(SubdivisionCode.AZ.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision ba = SubdivisionCode.AZ.find("BA").orElseThrow();
        assertEquals(SubdivisionCode.AZ.BA, ba);

        Subdivision baFull = SubdivisionCode.AZ.find("AZ-BA").orElseThrow();
        assertEquals(SubdivisionCode.AZ.BA, baFull);

        Subdivision nakhchivan = SubdivisionCode.AZ.find("Nakhchivan").orElseThrow();
        assertEquals(SubdivisionCode.AZ.NX, nakhchivan);

        assertTrue(SubdivisionCode.AZ.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision nakhchivanCity = SubdivisionCode.AZ.NV;
        assertTrue(nakhchivanCity.getParent().isPresent());
        assertEquals(SubdivisionCode.AZ.NX, nakhchivanCity.getParent().get());

        Subdivision nakhchivan = SubdivisionCode.AZ.NX;
        assertFalse(nakhchivan.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] autonomousRepublics = SubdivisionCode.AZ.getAutonomousRepublics();
        assertEquals(1, autonomousRepublics.length);
        assertEquals(SubdivisionCode.AZ.NX, autonomousRepublics[0]);

        Subdivision[] municipalities = SubdivisionCode.AZ.getMunicipalities();
        assertEquals(11, municipalities.length);

        Subdivision[] rayons = SubdivisionCode.AZ.getRayons();
        assertEquals(66, rayons.length);

        Subdivision nakhchivan = SubdivisionCode.AZ.NX;
        Subdivision[] nakhchivanSubdivisions = SubdivisionCode.AZ.getByParent(nakhchivan);
        assertEquals(8, nakhchivanSubdivisions.length);

        // Verify Nakhchivan City is one of the subdivisions under Nakhchivan
        boolean hasNakhchivanCity = false;
        for (Subdivision s : nakhchivanSubdivisions) {
            if (s.equals(SubdivisionCode.AZ.NV)) {
                hasNakhchivanCity = true;
                break;
            }
        }
        assertTrue(hasNakhchivanCity);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.AZ.wikipedia());
        assertFalse(SubdivisionCode.AZ.wikipedia().isBlank());
        assertTrue(SubdivisionCode.AZ.wikipedia().contains("ISO_3166-2:AZ"));

        assertNotNull(SubdivisionCode.AZ.dateAdded());
        assertFalse(SubdivisionCode.AZ.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.AZ.lastUpdated());
        assertFalse(SubdivisionCode.AZ.lastUpdated().isBlank());
    }
}
