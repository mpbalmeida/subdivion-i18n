package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionESTest {

    @Test
    public void testESSubdivisions() {
        SubdivisionCode.ES an = SubdivisionCode.ES.AN;
        assertEquals("ES-AN", an.getCode());
        assertEquals("Andalucía", an.getSubdivisionName());
        assertEquals("Autonomous Community", an.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.ES);
        assertNotNull(subdivisions);
        assertEquals(69, subdivisions.length); // 17 autonomous communities + 2 autonomous cities + 50 provinces

        Subdivision first = subdivisions[0];
        assertEquals("ES-AN", first.getCode());
        assertEquals("Andalucía", first.getSubdivisionName());
        assertEquals("Autonomous Community", first.getCategory());

        Subdivision last = subdivisions[68];
        assertEquals("ES-ZA", last.getCode());
        assertEquals("Zamora", last.getSubdivisionName());
        assertEquals("province", last.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.ES an = SubdivisionCode.ES.fromCode("ES-AN");
        assertEquals(SubdivisionCode.ES.AN, an);

        // Test lookup by subdivision part
        SubdivisionCode.ES ml = SubdivisionCode.ES.fromCode("ML");
        assertEquals(SubdivisionCode.ES.ML, ml);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.ES.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision andalucia = SubdivisionCode.ES.fromName("Andalucía").orElseThrow();
        assertEquals(SubdivisionCode.ES.AN, andalucia);

        Subdivision barcelona = SubdivisionCode.ES.fromName("barcelona").orElseThrow();
        assertEquals(SubdivisionCode.ES.B, barcelona);

        assertTrue(SubdivisionCode.ES.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision an = SubdivisionCode.ES.find("AN").orElseThrow();
        assertEquals(SubdivisionCode.ES.AN, an);

        Subdivision anFull = SubdivisionCode.ES.find("ES-AN").orElseThrow();
        assertEquals(SubdivisionCode.ES.AN, anFull);

        Subdivision almeria = SubdivisionCode.ES.find("Almería").orElseThrow();
        assertEquals(SubdivisionCode.ES.AL, almeria);

        assertTrue(SubdivisionCode.ES.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        // Provinces have autonomous community parents
        Subdivision alicante = SubdivisionCode.ES.A;
        assertTrue(alicante.getParent().isPresent());
        assertEquals(SubdivisionCode.ES.VC, alicante.getParent().get());

        Subdivision alava = SubdivisionCode.ES.VI;
        assertTrue(alava.getParent().isPresent());
        assertEquals(SubdivisionCode.ES.PV, alava.getParent().get());

        Subdivision madrid = SubdivisionCode.ES.M;
        assertTrue(madrid.getParent().isPresent());
        assertEquals(SubdivisionCode.ES.MD, madrid.getParent().get());

        Subdivision sevilla = SubdivisionCode.ES.SE;
        assertTrue(sevilla.getParent().isPresent());
        assertEquals(SubdivisionCode.ES.AN, sevilla.getParent().get());

        Subdivision zaragoza = SubdivisionCode.ES.Z;
        assertTrue(zaragoza.getParent().isPresent());
        assertEquals(SubdivisionCode.ES.AR, zaragoza.getParent().get());

        // Autonomous communities have no parent
        Subdivision andalucia = SubdivisionCode.ES.AN;
        assertFalse(andalucia.getParent().isPresent());

        Subdivision catalunya = SubdivisionCode.ES.CT;
        assertFalse(catalunya.getParent().isPresent());

        Subdivision paisVasco = SubdivisionCode.ES.PV;
        assertFalse(paisVasco.getParent().isPresent());

        // Autonomous cities have no parent
        Subdivision ceuta = SubdivisionCode.ES.CE;
        assertFalse(ceuta.getParent().isPresent());

        Subdivision melilla = SubdivisionCode.ES.ML;
        assertFalse(melilla.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        // Category methods
        Subdivision[] communities = SubdivisionCode.ES.getAutonomousCommunities();
        assertEquals(17, communities.length);

        Subdivision[] cities = SubdivisionCode.ES.getAutonomousCities();
        assertEquals(2, cities.length);

        Subdivision[] provinces = SubdivisionCode.ES.getProvinces();
        assertEquals(50, provinces.length);

        // Verify cities contains Ceuta and Melilla
        boolean hasCeuta = false;
        boolean hasMelilla = false;
        for (Subdivision s : cities) {
            if (s.equals(SubdivisionCode.ES.CE)) {
                hasCeuta = true;
            }
            if (s.equals(SubdivisionCode.ES.ML)) {
                hasMelilla = true;
            }
        }
        assertTrue(hasCeuta);
        assertTrue(hasMelilla);

        // getByParent for Andalucía (8 provinces: AL, CA, CO, GR, H, J, MA, SE)
        Subdivision andalucia = SubdivisionCode.ES.AN;
        Subdivision[] andaluciaProvinces = SubdivisionCode.ES.getByParent(andalucia);
        assertEquals(8, andaluciaProvinces.length);

        // getByParent for Cataluña (4 provinces: B, GI, L, T)
        Subdivision catalunya = SubdivisionCode.ES.CT;
        Subdivision[] catalunyaProvinces = SubdivisionCode.ES.getByParent(catalunya);
        assertEquals(4, catalunyaProvinces.length);

        // getByParent for País Vasco (3 provinces: BI, SS, VI)
        Subdivision paisVasco = SubdivisionCode.ES.PV;
        Subdivision[] paisVascoProvinces = SubdivisionCode.ES.getByParent(paisVasco);
        assertEquals(3, paisVascoProvinces.length);

        // getByParent for Castilla y León (9 provinces: AV, BU, LE, P, SA, SG, SO, VA, ZA)
        Subdivision castillaLeon = SubdivisionCode.ES.CL;
        Subdivision[] castillaLeonProvinces = SubdivisionCode.ES.getByParent(castillaLeon);
        assertEquals(9, castillaLeonProvinces.length);

        // getByParent for autonomous city should return empty
        Subdivision ceuta = SubdivisionCode.ES.CE;
        Subdivision[] ceutaChildren = SubdivisionCode.ES.getByParent(ceuta);
        assertEquals(0, ceutaChildren.length);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.ES.wikipedia());
        assertFalse(SubdivisionCode.ES.wikipedia().isBlank());
        assertTrue(SubdivisionCode.ES.wikipedia().contains("ISO_3166-2:ES"));

        assertNotNull(SubdivisionCode.ES.dateAdded());
        assertFalse(SubdivisionCode.ES.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.ES.lastUpdated());
        assertFalse(SubdivisionCode.ES.lastUpdated().isBlank());
    }
}
