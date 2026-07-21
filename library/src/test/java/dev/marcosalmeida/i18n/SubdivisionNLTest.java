package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionNLTest {

    @Test
    public void testNLSubdivisions() {
        SubdivisionCode.NL dr = SubdivisionCode.NL.DR;
        assertEquals("NL-DR", dr.getCode());
        assertEquals("Drenthe", dr.getSubdivisionName());
        assertEquals("province", dr.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.NL);
        assertNotNull(subdivisions);
        assertEquals(18, subdivisions.length); // 12 provinces + 3 countries + 3 special municipalities

        Subdivision first = subdivisions[0];
        assertEquals("NL-AW", first.getCode());
        assertEquals("Aruba", first.getSubdivisionName());

        Subdivision last = subdivisions[17];
        assertEquals("NL-ZH", last.getCode());
        assertEquals("Zuid-Holland", last.getSubdivisionName());
        assertEquals("province", last.getCategory());

        // Verify special municipality
        Subdivision bonaire = SubdivisionCode.NL.BQ1;
        assertEquals("NL-BQ1", bonaire.getCode());
        assertEquals("Bonaire", bonaire.getSubdivisionName());
        assertEquals("special municipality", bonaire.getCategory());

        // Verify country
        Subdivision aruba = SubdivisionCode.NL.AW;
        assertEquals("NL-AW", aruba.getCode());
        assertEquals("Aruba", aruba.getSubdivisionName());
        assertEquals("country", aruba.getCategory());

        // Verify Fryslân with its West Frisian name
        Subdivision fryslan = SubdivisionCode.NL.FR;
        assertEquals("NL-FR", fryslan.getCode());
        assertEquals("Fryslân", fryslan.getSubdivisionName());

        // Verify Sint Maarten
        Subdivision sintMaarten = SubdivisionCode.NL.SX;
        assertEquals("NL-SX", sintMaarten.getCode());
        assertEquals("Sint Maarten", sintMaarten.getSubdivisionName());
        assertEquals("country", sintMaarten.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.NL dr = SubdivisionCode.NL.fromCode("NL-DR");
        assertEquals(SubdivisionCode.NL.DR, dr);

        SubdivisionCode.NL fl = SubdivisionCode.NL.fromCode("FL");
        assertEquals(SubdivisionCode.NL.FL, fl);

        SubdivisionCode.NL bq1 = SubdivisionCode.NL.fromCode("NL-BQ1");
        assertEquals(SubdivisionCode.NL.BQ1, bq1);

        SubdivisionCode.NL aw = SubdivisionCode.NL.fromCode("AW");
        assertEquals(SubdivisionCode.NL.AW, aw);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.NL.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision drenthe = SubdivisionCode.NL.fromName("Drenthe").orElseThrow();
        assertEquals(SubdivisionCode.NL.DR, drenthe);

        Subdivision aruba = SubdivisionCode.NL.fromName("aruba").orElseThrow();
        assertEquals(SubdivisionCode.NL.AW, aruba);

        Subdivision fryslan = SubdivisionCode.NL.fromName("Fryslân").orElseThrow();
        assertEquals(SubdivisionCode.NL.FR, fryslan);

        Subdivision sintEustatius = SubdivisionCode.NL.fromName("Sint Eustatius").orElseThrow();
        assertEquals(SubdivisionCode.NL.BQ3, sintEustatius);

        assertTrue(SubdivisionCode.NL.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision nb = SubdivisionCode.NL.find("NB").orElseThrow();
        assertEquals(SubdivisionCode.NL.NB, nb);

        Subdivision nbFull = SubdivisionCode.NL.find("NL-NB").orElseThrow();
        assertEquals(SubdivisionCode.NL.NB, nbFull);

        Subdivision groningen = SubdivisionCode.NL.find("Groningen").orElseThrow();
        assertEquals(SubdivisionCode.NL.GR, groningen);

        Subdivision curacao = SubdivisionCode.NL.find("Curaçao").orElseThrow();
        assertEquals(SubdivisionCode.NL.CW, curacao);

        assertTrue(SubdivisionCode.NL.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.NL.getProvinces();
        assertEquals(12, provinces.length);

        Subdivision[] countries = SubdivisionCode.NL.getCountries();
        assertEquals(3, countries.length);

        Subdivision[] specialMunicipalities = SubdivisionCode.NL.getSpecialMunicipalities();
        assertEquals(3, specialMunicipalities.length);

        // Verify a province is in the filtered results
        boolean hasZuidHolland = false;
        for (Subdivision s : provinces) {
            if (s.equals(SubdivisionCode.NL.ZH)) {
                hasZuidHolland = true;
                break;
            }
        }
        assertTrue(hasZuidHolland, "Zuid-Holland should be in provinces");

        // Verify constituent countries are in the filtered results
        boolean hasAruba = false;
        for (Subdivision s : countries) {
            if (s.equals(SubdivisionCode.NL.AW)) {
                hasAruba = true;
                break;
            }
        }
        assertTrue(hasAruba, "Aruba should be in countries");
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.NL.wikipedia());
        assertFalse(SubdivisionCode.NL.wikipedia().isBlank());
        assertTrue(SubdivisionCode.NL.wikipedia().contains("ISO_3166-2:NL"));

        assertNotNull(SubdivisionCode.NL.dateAdded());
        assertFalse(SubdivisionCode.NL.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.NL.lastUpdated());
        assertFalse(SubdivisionCode.NL.lastUpdated().isBlank());
    }
}
