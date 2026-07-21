package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionLTTest {

    @Test
    public void testLTSubdivisions() {
        SubdivisionCode.LT alytusCounty = SubdivisionCode.LT.AL;
        assertEquals("LT-AL", alytusCounty.getCode());
        assertEquals("Alytus County", alytusCounty.getSubdivisionName());
        assertEquals("county", alytusCounty.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.LT);
        assertNotNull(subdivisions);
        assertEquals(70, subdivisions.length); // 10 counties + 60 municipalities

        Subdivision first = subdivisions[0];
        assertEquals("LT-AL", first.getCode());
        assertEquals("Alytus County", first.getSubdivisionName());

        Subdivision last = subdivisions[69];
        assertEquals("LT-60", last.getCode());
        assertEquals("Zarasai", last.getSubdivisionName());
        assertEquals("district municipality", last.getCategory());

        Subdivision vilniusMiestas = SubdivisionCode.LT.LT_57;
        assertEquals("LT-57", vilniusMiestas.getCode());
        assertEquals("Vilniaus miestas", vilniusMiestas.getSubdivisionName());
        assertEquals("city municipality", vilniusMiestas.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.LT alytus = SubdivisionCode.LT.fromCode("LT-AL");
        assertEquals(SubdivisionCode.LT.AL, alytus);

        // Test lookup by subdivision part
        SubdivisionCode.LT vilnius = SubdivisionCode.LT.fromCode("VL");
        assertEquals(SubdivisionCode.LT.VL, vilnius);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.LT.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision sauliaiCounty = SubdivisionCode.LT.fromName("Šiauliai County").orElseThrow();
        assertEquals(SubdivisionCode.LT.SA, sauliaiCounty);

        Subdivision kaunasCounty = SubdivisionCode.LT.fromName("kaunas county").orElseThrow();
        assertEquals(SubdivisionCode.LT.KU, kaunasCounty);

        assertTrue(SubdivisionCode.LT.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision al = SubdivisionCode.LT.find("AL").orElseThrow();
        assertEquals(SubdivisionCode.LT.AL, al);

        Subdivision alFull = SubdivisionCode.LT.find("LT-AL").orElseThrow();
        assertEquals(SubdivisionCode.LT.AL, alFull);

        Subdivision alytusCounty = SubdivisionCode.LT.find("Alytus County").orElseThrow();
        assertEquals(SubdivisionCode.LT.AL, alytusCounty);

        assertTrue(SubdivisionCode.LT.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision vilniusMiestas = SubdivisionCode.LT.LT_57;
        assertTrue(vilniusMiestas.getParent().isPresent());
        assertEquals(SubdivisionCode.LT.VL, vilniusMiestas.getParent().get());

        Subdivision vilniusCounty = SubdivisionCode.LT.VL;
        assertFalse(vilniusCounty.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.LT.getCountys();
        assertEquals(10, counties.length);

        Subdivision[] cityMunicipalities = SubdivisionCode.LT.getCityMunicipalitys();
        assertEquals(7, cityMunicipalities.length);

        Subdivision[] districtMunicipalities = SubdivisionCode.LT.getDistrictMunicipalitys();
        assertEquals(44, districtMunicipalities.length);

        Subdivision[] municipalities = SubdivisionCode.LT.getMunicipalitys();
        assertEquals(9, municipalities.length);

        Subdivision vilniusCounty = SubdivisionCode.LT.VL;
        Subdivision[] vilniusCountyMunicipalities = SubdivisionCode.LT.getByParent(vilniusCounty);
        assertEquals(8, vilniusCountyMunicipalities.length);

        // Verify one of the municipalities in Vilnius County
        boolean hasVilniusMiestas = false;
        for (Subdivision s : vilniusCountyMunicipalities) {
            if (s.equals(SubdivisionCode.LT.LT_57)) {
                hasVilniusMiestas = true;
                break;
            }
        }
        assertTrue(hasVilniusMiestas);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.LT.wikipedia());
        assertFalse(SubdivisionCode.LT.wikipedia().isBlank());
        assertTrue(SubdivisionCode.LT.wikipedia().contains("ISO_3166-2:LT"));

        assertNotNull(SubdivisionCode.LT.dateAdded());
        assertFalse(SubdivisionCode.LT.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.LT.lastUpdated());
        assertFalse(SubdivisionCode.LT.lastUpdated().isBlank());
    }
}
