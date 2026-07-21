package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionFRTest {

    @Test
    public void testFRSubdivisions() {
        SubdivisionCode.FR corse = SubdivisionCode.FR.FR_20R;
        assertEquals("FR-20R", corse.getCode());
        assertEquals("Corse", corse.getSubdivisionName());
        assertEquals("metropolitan collectivity with special status", corse.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.FR);
        assertNotNull(subdivisions);
        assertEquals(124, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("FR-20R", first.getCode());
        assertEquals("Corse", first.getSubdivisionName());

        Subdivision last = subdivisions[123];
        assertEquals("FR-95", last.getCode());
        assertEquals("Val-d'Oise", last.getSubdivisionName());
        assertEquals("metropolitan department", last.getCategory());

        Subdivision alsace = SubdivisionCode.FR.FR_6AE;
        assertEquals("FR-6AE", alsace.getCode());
        assertEquals("Alsace", alsace.getSubdivisionName());
        assertEquals("European collectivity", alsace.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.FR ara = SubdivisionCode.FR.fromCode("FR-ARA");
        assertEquals(SubdivisionCode.FR.FR_ARA, ara);

        SubdivisionCode.FR idf = SubdivisionCode.FR.fromCode("IDF");
        assertEquals(SubdivisionCode.FR.FR_IDF, idf);

        SubdivisionCode.FR a01 = SubdivisionCode.FR.fromCode("01");
        assertEquals(SubdivisionCode.FR.FR_01, a01);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.FR.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision auvergne = SubdivisionCode.FR.fromName("Auvergne-Rhône-Alpes").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_ARA, auvergne);

        Subdivision paris = SubdivisionCode.FR.fromName("paris").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_75C, paris);

        Subdivision gironde = SubdivisionCode.FR.fromName("Gironde").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_33, gironde);

        assertTrue(SubdivisionCode.FR.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision bre = SubdivisionCode.FR.find("BRE").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_BRE, bre);

        Subdivision breFull = SubdivisionCode.FR.find("FR-BRE").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_BRE, breFull);

        Subdivision bretagne = SubdivisionCode.FR.find("Bretagne").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_BRE, bretagne);

        Subdivision lyon = SubdivisionCode.FR.find("Métropole de Lyon").orElseThrow();
        assertEquals(SubdivisionCode.FR.FR_69M, lyon);

        assertTrue(SubdivisionCode.FR.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        // Metropolitan region has no parent
        Subdivision occitanie = SubdivisionCode.FR.FR_OCC;
        assertFalse(occitanie.getParent().isPresent());

        // Department has a region parent
        Subdivision gironde = SubdivisionCode.FR.FR_33;
        assertTrue(gironde.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_NAQ, gironde.getParent().get());

        // Corsica departments have FR-20R parent
        Subdivision corseDuSud = SubdivisionCode.FR.FR_2A;
        assertTrue(corseDuSud.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_20R, corseDuSud.getParent().get());

        Subdivision hauteCorse = SubdivisionCode.FR.FR_2B;
        assertTrue(hauteCorse.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_20R, hauteCorse.getParent().get());

        // Alsace (European collectivity) has Grand Est parent
        Subdivision alsace = SubdivisionCode.FR.FR_6AE;
        assertTrue(alsace.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_GES, alsace.getParent().get());

        // Bas-Rhin and Haut-Rhin have Alsace parent
        Subdivision basRhin = SubdivisionCode.FR.FR_67;
        assertTrue(basRhin.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_6AE, basRhin.getParent().get());

        Subdivision hautRhin = SubdivisionCode.FR.FR_68;
        assertTrue(hautRhin.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_6AE, hautRhin.getParent().get());

        // Métropole de Lyon has Auvergne-Rhône-Alpes parent
        Subdivision lyon = SubdivisionCode.FR.FR_69M;
        assertTrue(lyon.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_ARA, lyon.getParent().get());

        // Paris has Île-de-France parent
        Subdivision paris = SubdivisionCode.FR.FR_75C;
        assertTrue(paris.getParent().isPresent());
        assertEquals(SubdivisionCode.FR.FR_IDF, paris.getParent().get());

        // Overseas collectivity has no parent
        Subdivision pf = SubdivisionCode.FR.FR_PF;
        assertFalse(pf.getParent().isPresent());
    }

    @Test
    public void testFiltering() {
        Subdivision[] regions = SubdivisionCode.FR.getMetropolitanRegions();
        assertEquals(12, regions.length);

        Subdivision[] departments = SubdivisionCode.FR.getMetropolitanDepartments();
        assertEquals(95, departments.length);

        Subdivision[] specialMetro = SubdivisionCode.FR.getMetropolitanCollectivityWithSpecialStatuss();
        assertEquals(3, specialMetro.length);

        Subdivision[] overseasDeptColl = SubdivisionCode.FR.getOverseasDepartmentalCollectivities();
        assertEquals(3, overseasDeptColl.length);

        Subdivision[] overseasUnique = SubdivisionCode.FR.getOverseasUniqueTerritorialCollectivities();
        assertEquals(2, overseasUnique.length);

        Subdivision[] overseasColl = SubdivisionCode.FR.getOverseasCollectivities();
        assertEquals(5, overseasColl.length);

        Subdivision[] dependencies = SubdivisionCode.FR.getDependencies();
        assertEquals(1, dependencies.length);
        assertEquals("FR-CP", dependencies[0].getCode());

        Subdivision[] overseasSpecial = SubdivisionCode.FR.getOverseasCollectivityWithSpecialStatuss();
        assertEquals(1, overseasSpecial.length);
        assertEquals("FR-NC", overseasSpecial[0].getCode());

        Subdivision[] overseasTerr = SubdivisionCode.FR.getOverseasTerritories();
        assertEquals(1, overseasTerr.length);
        assertEquals("FR-TF", overseasTerr[0].getCode());

        Subdivision[] europeanColl = SubdivisionCode.FR.getEuropeanCollectivities();
        assertEquals(1, europeanColl.length);
        assertEquals("FR-6AE", europeanColl[0].getCode());

        // Test getByParent for Nouvelle-Aquitaine region
        Subdivision naq = SubdivisionCode.FR.FR_NAQ;
        Subdivision[] naqChildren = SubdivisionCode.FR.getByParent(naq);
        assertEquals(12, naqChildren.length);

        boolean hasGironde = false;
        boolean hasPyreneesAtlantiques = false;
        for (Subdivision s : naqChildren) {
            if (s.equals(SubdivisionCode.FR.FR_33)) hasGironde = true;
            if (s.equals(SubdivisionCode.FR.FR_64)) hasPyreneesAtlantiques = true;
        }
        assertTrue(hasGironde);
        assertTrue(hasPyreneesAtlantiques);

        // Test getByParent for Grand Est region (should include FR-6AE, but not its children)
        Subdivision ges = SubdivisionCode.FR.FR_GES;
        Subdivision[] gesChildren = SubdivisionCode.FR.getByParent(ges);
        assertEquals(9, gesChildren.length);

        boolean hasAlsace = false;
        for (Subdivision s : gesChildren) {
            if (s.equals(SubdivisionCode.FR.FR_6AE)) hasAlsace = true;
        }
        assertTrue(hasAlsace);

        // Test getByParent for Alsace (should include Bas-Rhin and Haut-Rhin)
        Subdivision alsace = SubdivisionCode.FR.FR_6AE;
        Subdivision[] alsaceChildren = SubdivisionCode.FR.getByParent(alsace);
        assertEquals(2, alsaceChildren.length);

        boolean hasBasRhin = false;
        boolean hasHautRhin = false;
        for (Subdivision s : alsaceChildren) {
            if (s.equals(SubdivisionCode.FR.FR_67)) hasBasRhin = true;
            if (s.equals(SubdivisionCode.FR.FR_68)) hasHautRhin = true;
        }
        assertTrue(hasBasRhin);
        assertTrue(hasHautRhin);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.FR.wikipedia());
        assertFalse(SubdivisionCode.FR.wikipedia().isBlank());
        assertTrue(SubdivisionCode.FR.wikipedia().contains("ISO_3166-2:FR"));

        assertNotNull(SubdivisionCode.FR.dateAdded());
        assertFalse(SubdivisionCode.FR.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.FR.lastUpdated());
        assertFalse(SubdivisionCode.FR.lastUpdated().isBlank());
    }
}
