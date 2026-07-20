package dev.marcosalmeida.i18n;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCATest {

    @Test
    public void testCASubdivisions() {
        SubdivisionCode.CA ab = SubdivisionCode.CA.AB;
        assertEquals("CA-AB", ab.getCode());
        assertEquals("Alberta", ab.getSubdivisionName());
        assertEquals("Province", ab.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(com.neovisionaries.i18n.CountryCode.CA);
        assertNotNull(subdivisions);
        assertEquals(13, subdivisions.length); // 10 provinces + 3 territories

        Subdivision alberta = subdivisions[0];
        assertEquals("CA-AB", alberta.getCode());
        assertEquals("Alberta", alberta.getSubdivisionName());

        Subdivision yukon = subdivisions[12];
        assertEquals("CA-YT", yukon.getCode());
        assertEquals("Yukon", yukon.getSubdivisionName());
        assertEquals("Territory", yukon.getCategory());

        Subdivision nt = SubdivisionCode.CA.NT;
        assertEquals("CA-NT", nt.getCode());
        assertEquals("Northwest Territories", nt.getSubdivisionName());
        assertEquals("Territory", nt.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.CA ab = SubdivisionCode.CA.fromCode("CA-AB");
        assertEquals(SubdivisionCode.CA.AB, ab);

        // Test lookup by subdivision part
        SubdivisionCode.CA qc = SubdivisionCode.CA.fromCode("QC");
        assertEquals(SubdivisionCode.CA.QC, qc);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.CA.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision alberta = SubdivisionCode.CA.fromName("Alberta").orElseThrow();
        assertEquals(SubdivisionCode.CA.AB, alberta);

        Subdivision quebec = SubdivisionCode.CA.fromName("quebec").orElseThrow();
        assertEquals(SubdivisionCode.CA.QC, quebec);

        assertTrue(SubdivisionCode.CA.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision qc = SubdivisionCode.CA.find("QC").orElseThrow();
        assertEquals(SubdivisionCode.CA.QC, qc);

        Subdivision qcFull = SubdivisionCode.CA.find("CA-QC").orElseThrow();
        assertEquals(SubdivisionCode.CA.QC, qcFull);

        Subdivision quebec = SubdivisionCode.CA.find("Quebec").orElseThrow();
        assertEquals(SubdivisionCode.CA.QC, quebec);

        assertTrue(SubdivisionCode.CA.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] provinces = SubdivisionCode.CA.getProvinces();
        assertEquals(10, provinces.length);

        Subdivision[] territories = SubdivisionCode.CA.getTerritories();
        assertEquals(3, territories.length);
        
        boolean hasYukon = false;
        for (Subdivision s : territories) {
            if (s.equals(SubdivisionCode.CA.YT)) {
                hasYukon = true;
                break;
            }
        }
        assertTrue(hasYukon);
    }
}
