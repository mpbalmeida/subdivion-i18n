package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionHRTest {

    @Test
    public void testHRSubdivisions() {
        SubdivisionCode.HR zagreb = SubdivisionCode.HR.HR_01;
        assertEquals("HR-01", zagreb.getCode());
        assertEquals("Zagrebačka županija", zagreb.getSubdivisionName());
        assertEquals("county", zagreb.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.HR);
        assertNotNull(subdivisions);
        assertEquals(21, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("HR-01", first.getCode());
        assertEquals("Zagrebačka županija", first.getSubdivisionName());

        Subdivision last = subdivisions[20];
        assertEquals("HR-21", last.getCode());
        assertEquals("Grad Zagreb", last.getSubdivisionName());
        assertEquals("city", last.getCategory());

        Subdivision istarska = SubdivisionCode.HR.HR_18;
        assertEquals("HR-18", istarska.getCode());
        assertEquals("Istarska županija", istarska.getSubdivisionName());
        assertEquals("county", istarska.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.HR gradZagreb = SubdivisionCode.HR.fromCode("HR-21");
        assertEquals(SubdivisionCode.HR.HR_21, gradZagreb);

        SubdivisionCode.HR zagrebacka = SubdivisionCode.HR.fromCode("01");
        assertEquals(SubdivisionCode.HR.HR_01, zagrebacka);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.HR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision gradZagreb = SubdivisionCode.HR.fromName("Grad Zagreb").orElseThrow();
        assertEquals(SubdivisionCode.HR.HR_21, gradZagreb);

        Subdivision istarska = SubdivisionCode.HR.fromName("istarska županija").orElseThrow();
        assertEquals(SubdivisionCode.HR.HR_18, istarska);

        assertTrue(SubdivisionCode.HR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision jedan = SubdivisionCode.HR.find("01").orElseThrow();
        assertEquals(SubdivisionCode.HR.HR_01, jedan);

        Subdivision jedanFull = SubdivisionCode.HR.find("HR-01").orElseThrow();
        assertEquals(SubdivisionCode.HR.HR_01, jedanFull);

        Subdivision zagreb = SubdivisionCode.HR.find("Grad Zagreb").orElseThrow();
        assertEquals(SubdivisionCode.HR.HR_21, zagreb);

        assertTrue(SubdivisionCode.HR.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        Subdivision zagrebacka = SubdivisionCode.HR.HR_01;
        assertFalse(zagrebacka.getParent().isPresent());

        Subdivision gradZagreb = SubdivisionCode.HR.HR_21;
        assertFalse(gradZagreb.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] counties = SubdivisionCode.HR.getCounties();
        assertEquals(20, counties.length);

        Subdivision[] cities = SubdivisionCode.HR.getCities();
        assertEquals(1, cities.length);
        assertEquals("HR-21", cities[0].getCode());
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.HR.wikipedia());
        assertFalse(SubdivisionCode.HR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.HR.wikipedia().contains("ISO_3166-2:HR"));

        assertNotNull(SubdivisionCode.HR.dateAdded());
        assertFalse(SubdivisionCode.HR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.HR.lastUpdated());
        assertFalse(SubdivisionCode.HR.lastUpdated().isBlank());
    }
}
