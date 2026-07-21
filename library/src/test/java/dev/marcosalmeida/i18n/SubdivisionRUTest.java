package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionRUTest {

    @Test
    public void testRUSubdivisions() {
        SubdivisionCode.RU adygea = SubdivisionCode.RU.AD;
        assertEquals("RU-AD", adygea.getCode());
        assertEquals("Adygea", adygea.getSubdivisionName());
        assertEquals("republic", adygea.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.RU);
        assertNotNull(subdivisions);
        assertEquals(83, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("RU-AD", first.getCode());
        assertEquals("Adygea", first.getSubdivisionName());
        assertEquals("republic", first.getCategory());

        Subdivision last = subdivisions[82];
        assertEquals("RU-ZAB", last.getCode());
        assertEquals("Zabaykalsky Krai", last.getSubdivisionName());
        assertEquals("krai", last.getCategory());

        // Verify specific subdivisions across different categories
        SubdivisionCode.RU moscow = SubdivisionCode.RU.MOW;
        assertEquals("RU-MOW", moscow.getCode());
        assertEquals("Moscow", moscow.getSubdivisionName());
        assertEquals("Federal City", moscow.getCategory());

        SubdivisionCode.RU spb = SubdivisionCode.RU.SPE;
        assertEquals("RU-SPE", spb.getCode());
        assertEquals("Saint Petersburg", spb.getSubdivisionName());
        assertEquals("Federal City", spb.getCategory());

        SubdivisionCode.RU sakha = SubdivisionCode.RU.SA;
        assertEquals("RU-SA", sakha.getCode());
        assertEquals("Sakha", sakha.getSubdivisionName());
        assertEquals("republic", sakha.getCategory());

        SubdivisionCode.RU chukotka = SubdivisionCode.RU.CHU;
        assertEquals("RU-CHU", chukotka.getCode());
        assertEquals("Chukotka", chukotka.getSubdivisionName());
        assertEquals("Autonomous Okrug", chukotka.getCategory());

        SubdivisionCode.RU jewish = SubdivisionCode.RU.YEV;
        assertEquals("RU-YEV", jewish.getCode());
        assertEquals("Jewish Autonomous Oblast", jewish.getSubdivisionName());
        assertEquals("Autonomous Oblast", jewish.getCategory());
    }

    @Test
    public void testFromCode() {
        // Full code lookup
        SubdivisionCode.RU adygea = SubdivisionCode.RU.fromCode("RU-AD");
        assertEquals(SubdivisionCode.RU.AD, adygea);

        SubdivisionCode.RU moscow = SubdivisionCode.RU.fromCode("RU-MOW");
        assertEquals(SubdivisionCode.RU.MOW, moscow);

        SubdivisionCode.RU zab = SubdivisionCode.RU.fromCode("RU-ZAB");
        assertEquals(SubdivisionCode.RU.ZAB, zab);

        // Short code lookup
        SubdivisionCode.RU altaiKrai = SubdivisionCode.RU.fromCode("ALT");
        assertEquals(SubdivisionCode.RU.ALT, altaiKrai);

        SubdivisionCode.RU chechnya = SubdivisionCode.RU.fromCode("CE");
        assertEquals(SubdivisionCode.RU.CE, chechnya);

        SubdivisionCode.RU tatarstan = SubdivisionCode.RU.fromCode("TA");
        assertEquals(SubdivisionCode.RU.TA, tatarstan);

        SubdivisionCode.RU chukotka = SubdivisionCode.RU.fromCode("CHU");
        assertEquals(SubdivisionCode.RU.CHU, chukotka);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.RU.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision adygea = SubdivisionCode.RU.fromName("Adygea").orElseThrow();
        assertEquals(SubdivisionCode.RU.AD, adygea);

        // Case-insensitive
        Subdivision moscow = SubdivisionCode.RU.fromName("moscow").orElseThrow();
        assertEquals(SubdivisionCode.RU.MOW, moscow);

        Subdivision sakha = SubdivisionCode.RU.fromName("SAKHA").orElseThrow();
        assertEquals(SubdivisionCode.RU.SA, sakha);

        Subdivision tatarstan = SubdivisionCode.RU.fromName("Tatarstan").orElseThrow();
        assertEquals(SubdivisionCode.RU.TA, tatarstan);

        Subdivision jewish = SubdivisionCode.RU.fromName("Jewish Autonomous Oblast").orElseThrow();
        assertEquals(SubdivisionCode.RU.YEV, jewish);

        assertTrue(SubdivisionCode.RU.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        // Short code
        Subdivision ad = SubdivisionCode.RU.find("AD").orElseThrow();
        assertEquals(SubdivisionCode.RU.AD, ad);

        // Full code
        Subdivision adFull = SubdivisionCode.RU.find("RU-AD").orElseThrow();
        assertEquals(SubdivisionCode.RU.AD, adFull);

        // Short code (3-letter)
        Subdivision alt = SubdivisionCode.RU.find("ALT").orElseThrow();
        assertEquals(SubdivisionCode.RU.ALT, alt);

        // Full code (3-letter)
        Subdivision altFull = SubdivisionCode.RU.find("RU-ALT").orElseThrow();
        assertEquals(SubdivisionCode.RU.ALT, altFull);

        // By name
        Subdivision moscow = SubdivisionCode.RU.find("Moscow").orElseThrow();
        assertEquals(SubdivisionCode.RU.MOW, moscow);

        Subdivision stPetersburg = SubdivisionCode.RU.find("Saint Petersburg").orElseThrow();
        assertEquals(SubdivisionCode.RU.SPE, stPetersburg);

        Subdivision chukotka = SubdivisionCode.RU.find("Chukotka").orElseThrow();
        assertEquals(SubdivisionCode.RU.CHU, chukotka);

        Subdivision zab = SubdivisionCode.RU.find("Zabaykalsky Krai").orElseThrow();
        assertEquals(SubdivisionCode.RU.ZAB, zab);

        assertTrue(SubdivisionCode.RU.find("Invalid").isEmpty());
    }

    @Test
    public void testFiltering() {
        Subdivision[] republics = SubdivisionCode.RU.getRepublics();
        assertEquals(21, republics.length);

        Subdivision[] oblasts = SubdivisionCode.RU.getOblasts();
        assertEquals(46, oblasts.length);

        Subdivision[] krais = SubdivisionCode.RU.getKrais();
        assertEquals(9, krais.length);

        Subdivision[] autonomousOkrugs = SubdivisionCode.RU.getAutonomousOkrugs();
        assertEquals(4, autonomousOkrugs.length);

        Subdivision[] federalCities = SubdivisionCode.RU.getFederalCities();
        assertEquals(2, federalCities.length);

        Subdivision[] autonomousOblasts = SubdivisionCode.RU.getAutonomousOblasts();
        assertEquals(1, autonomousOblasts.length);
        assertEquals(SubdivisionCode.RU.YEV, autonomousOblasts[0]);

        // Verify a republic is in the republics array
        boolean hasTatarstan = false;
        for (Subdivision s : republics) {
            if (s.equals(SubdivisionCode.RU.TA)) {
                hasTatarstan = true;
                break;
            }
        }
        assertTrue(hasTatarstan);

        // Verify a federal city is in the federal cities array
        boolean hasMoscow = false;
        for (Subdivision s : federalCities) {
            if (s.equals(SubdivisionCode.RU.MOW)) {
                hasMoscow = true;
                break;
            }
        }
        assertTrue(hasMoscow);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.RU.wikipedia());
        assertFalse(SubdivisionCode.RU.wikipedia().isBlank());
        assertTrue(SubdivisionCode.RU.wikipedia().contains("ISO_3166-2:RU"));

        assertNotNull(SubdivisionCode.RU.dateAdded());
        assertFalse(SubdivisionCode.RU.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.RU.lastUpdated());
        assertFalse(SubdivisionCode.RU.lastUpdated().isBlank());
    }
}
