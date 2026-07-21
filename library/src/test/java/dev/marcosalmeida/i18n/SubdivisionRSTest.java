package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionRSTest {

    @Test
    public void testRSSubdivisions() {
        SubdivisionCode.RS beograd = SubdivisionCode.RS.RS_00;
        assertEquals("RS-00", beograd.getCode());
        assertEquals("Belgrade", beograd.getSubdivisionName());
        assertEquals("city", beograd.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.RS);
        assertNotNull(subdivisions);
        assertEquals(32, subdivisions.length); // 2 autonomous provinces + 1 city + 29 districts

        Subdivision first = subdivisions[0];
        assertEquals("RS-00", first.getCode());
        assertEquals("Belgrade", first.getSubdivisionName());

        Subdivision last = subdivisions[31];
        assertEquals("RS-VO", last.getCode());
        assertEquals("Vojvodina", last.getSubdivisionName());
        assertEquals("autonomous province", last.getCategory());

        Subdivision vojvodina = SubdivisionCode.RS.RS_VO;
        assertEquals("RS-VO", vojvodina.getCode());
        assertEquals("Vojvodina", vojvodina.getSubdivisionName());
        assertEquals("autonomous province", vojvodina.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.RS vojvodina = SubdivisionCode.RS.fromCode("RS-VO");
        assertEquals(SubdivisionCode.RS.RS_VO, vojvodina);

        // Test lookup by subdivision part
        SubdivisionCode.RS beograd = SubdivisionCode.RS.fromCode("00");
        assertEquals(SubdivisionCode.RS.RS_00, beograd);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.RS.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision vojvodina = SubdivisionCode.RS.fromName("Vojvodina").orElseThrow();
        assertEquals(SubdivisionCode.RS.RS_VO, vojvodina);

        Subdivision beograd = SubdivisionCode.RS.fromName("belgrade").orElseThrow();
        assertEquals(SubdivisionCode.RS.RS_00, beograd);

        assertTrue(SubdivisionCode.RS.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision srem = SubdivisionCode.RS.find("07").orElseThrow();
        assertEquals(SubdivisionCode.RS.RS_07, srem);

        Subdivision sremFull = SubdivisionCode.RS.find("RS-07").orElseThrow();
        assertEquals(SubdivisionCode.RS.RS_07, sremFull);

        Subdivision sremName = SubdivisionCode.RS.find("Srem").orElseThrow();
        assertEquals(SubdivisionCode.RS.RS_07, sremName);

        assertTrue(SubdivisionCode.RS.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision beograd = SubdivisionCode.RS.RS_00;
        assertFalse(beograd.getParent().isPresent());

        Subdivision vojvodina = SubdivisionCode.RS.RS_VO;
        assertFalse(vojvodina.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] autonomousProvinces = SubdivisionCode.RS.getAutonomousProvinces();
        assertEquals(2, autonomousProvinces.length);

        Subdivision[] districts = SubdivisionCode.RS.getDistricts();
        assertEquals(29, districts.length);

        Subdivision[] cities = SubdivisionCode.RS.getCities();
        assertEquals(1, cities.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.RS.wikipedia());
        assertFalse(SubdivisionCode.RS.wikipedia().isBlank());
        assertTrue(SubdivisionCode.RS.wikipedia().contains("ISO_3166-2:RS"));

        assertNotNull(SubdivisionCode.RS.dateAdded());
        assertFalse(SubdivisionCode.RS.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.RS.lastUpdated());
        assertFalse(SubdivisionCode.RS.lastUpdated().isBlank());
    }
}
