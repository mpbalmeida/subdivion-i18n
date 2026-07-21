package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionGBTest {

    @Test
    public void testGBSubdivisions() {
        SubdivisionCode.GB ukm = SubdivisionCode.GB.UKM;
        assertEquals("GB-UKM", ukm.getCode());
        assertEquals("United Kingdom", ukm.getSubdivisionName());
        assertEquals("grouping", ukm.getCategory());

        Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.GB);
        assertNotNull(subdivisions);
        assertEquals(224, subdivisions.length);

        Subdivision first = subdivisions[0];
        assertEquals("GB-UKM", first.getCode());
        assertEquals("United Kingdom", first.getSubdivisionName());

        Subdivision last = subdivisions[223];
        assertEquals("GB-NMD", last.getCode());
        assertEquals("Newry, Mourne and Down", last.getSubdivisionName());
        assertEquals("district", last.getCategory());

        Subdivision eng = SubdivisionCode.GB.ENG;
        assertEquals("GB-ENG", eng.getCode());
        assertEquals("England", eng.getSubdivisionName());
        assertEquals("country", eng.getCategory());

        Subdivision sct = SubdivisionCode.GB.SCT;
        assertEquals("GB-SCT", sct.getCode());
        assertEquals("Scotland", sct.getSubdivisionName());
        assertEquals("country", sct.getCategory());
    }

    @Test
    public void testFromCode() {
        SubdivisionCode.GB eng = SubdivisionCode.GB.fromCode("GB-ENG");
        assertEquals(SubdivisionCode.GB.ENG, eng);

        SubdivisionCode.GB sct = SubdivisionCode.GB.fromCode("SCT");
        assertEquals(SubdivisionCode.GB.SCT, sct);

        SubdivisionCode.GB lnd = SubdivisionCode.GB.fromCode("LND");
        assertEquals(SubdivisionCode.GB.LND, lnd);

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.GB.fromCode("INVALID"));
    }

    @Test
    public void testFromName() {
        Subdivision england = SubdivisionCode.GB.fromName("England").orElseThrow();
        assertEquals(SubdivisionCode.GB.ENG, england);

        Subdivision scotland = SubdivisionCode.GB.fromName("scotland").orElseThrow();
        assertEquals(SubdivisionCode.GB.SCT, scotland);

        Subdivision birmingham = SubdivisionCode.GB.fromName("Birmingham").orElseThrow();
        assertEquals(SubdivisionCode.GB.BIR, birmingham);

        assertTrue(SubdivisionCode.GB.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        Subdivision lnd = SubdivisionCode.GB.find("LND").orElseThrow();
        assertEquals(SubdivisionCode.GB.LND, lnd);

        Subdivision lndFull = SubdivisionCode.GB.find("GB-LND").orElseThrow();
        assertEquals(SubdivisionCode.GB.LND, lndFull);

        Subdivision cityOfLondon = SubdivisionCode.GB.find("London, City of").orElseThrow();
        assertEquals(SubdivisionCode.GB.LND, cityOfLondon);

        Subdivision cardiff = SubdivisionCode.GB.find("Cardiff").orElseThrow();
        assertEquals(SubdivisionCode.GB.CRF, cardiff);

        assertTrue(SubdivisionCode.GB.find("Invalid").isEmpty());
    }

    @Test
    public void testHierarchy() {
        // UKM (United Kingdom) has no parent — top of the hierarchy
        Subdivision ukm = SubdivisionCode.GB.UKM;
        assertFalse(ukm.getParent().isPresent());

        // GBN (Great Britain) has parent UKM
        Subdivision gbn = SubdivisionCode.GB.GBN;
        assertTrue(gbn.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.UKM, gbn.getParent().get());

        // NIR (Northern Ireland) has parent UKM
        Subdivision nir = SubdivisionCode.GB.NIR;
        assertTrue(nir.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.UKM, nir.getParent().get());

        // EAW (England and Wales) has parent GBN
        Subdivision eaw = SubdivisionCode.GB.EAW;
        assertTrue(eaw.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.GBN, eaw.getParent().get());

        // SCT (Scotland) has parent GBN
        Subdivision sct = SubdivisionCode.GB.SCT;
        assertTrue(sct.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.GBN, sct.getParent().get());

        // ENG (England) has parent EAW — 4 levels deep from UKM
        Subdivision eng = SubdivisionCode.GB.ENG;
        assertTrue(eng.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.EAW, eng.getParent().get());

        // WLS (Wales) has parent EAW — 4 levels deep from UKM
        Subdivision wls = SubdivisionCode.GB.WLS;
        assertTrue(wls.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.EAW, wls.getParent().get());

        // Cambridgeshire (two-tier county) has parent ENG
        Subdivision cam = SubdivisionCode.GB.CAM;
        assertTrue(cam.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.ENG, cam.getParent().get());

        // City of London (city corporation) has parent ENG
        Subdivision lnd = SubdivisionCode.GB.LND;
        assertTrue(lnd.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.ENG, lnd.getParent().get());

        // Barking and Dagenham (london borough) has parent ENG
        Subdivision bdg = SubdivisionCode.GB.BDG;
        assertTrue(bdg.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.ENG, bdg.getParent().get());

        // Birmingham (metropolitan district) has parent ENG
        Subdivision bir = SubdivisionCode.GB.BIR;
        assertTrue(bir.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.ENG, bir.getParent().get());

        // Bath and North East Somerset (unitary authority) has parent ENG
        Subdivision bas = SubdivisionCode.GB.BAS;
        assertTrue(bas.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.ENG, bas.getParent().get());

        // Belfast City (NI district) has parent NIR
        Subdivision bfs = SubdivisionCode.GB.BFS;
        assertTrue(bfs.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.NIR, bfs.getParent().get());

        // Aberdeen City (council area) has parent SCT
        Subdivision abe = SubdivisionCode.GB.ABE;
        assertTrue(abe.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.SCT, abe.getParent().get());

        // Cardiff (Welsh unitary authority) has parent WLS
        Subdivision crf = SubdivisionCode.GB.CRF;
        assertTrue(crf.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.WLS, crf.getParent().get());

        // Verify the full depth chain: NMD -> NIR -> UKM (3 levels)
        Subdivision nmd = SubdivisionCode.GB.NMD;
        assertTrue(nmd.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.NIR, nmd.getParent().get());
        Subdivision nmdParent = (SubdivisionCode.GB) nmd.getParent().get();
        assertTrue(nmdParent.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.UKM, nmdParent.getParent().get());

        // Verify the full depth chain: ZET -> SCT -> GBN -> UKM (4 levels)
        Subdivision zet = SubdivisionCode.GB.ZET;
        assertTrue(zet.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.SCT, zet.getParent().get());
        Subdivision zetParent = (SubdivisionCode.GB) zet.getParent().get();
        assertTrue(zetParent.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.GBN, zetParent.getParent().get());
        Subdivision zetGrandparent = (SubdivisionCode.GB) zetParent.getParent().get();
        assertTrue(zetGrandparent.getParent().isPresent());
        assertEquals(SubdivisionCode.GB.UKM, zetGrandparent.getParent().get());
    }

    @Test
    public void testFiltering() {
        // All 10 category-specific getters for GB
        Subdivision[] groupings = SubdivisionCode.GB.getGroupings();
        assertEquals(3, groupings.length);

        Subdivision[] countries = SubdivisionCode.GB.getCountries();
        assertEquals(3, countries.length);

        Subdivision[] provinces = SubdivisionCode.GB.getProvinces();
        assertEquals(1, provinces.length);
        assertEquals("GB-NIR", provinces[0].getCode());

        Subdivision[] twoTierCounties = SubdivisionCode.GB.getTwoTierCounties();
        assertEquals(25, twoTierCounties.length);

        Subdivision[] cityCorporations = SubdivisionCode.GB.getCityCorporations();
        assertEquals(1, cityCorporations.length);
        assertEquals("GB-LND", cityCorporations[0].getCode());

        Subdivision[] londonBoroughs = SubdivisionCode.GB.getLondonBoroughs();
        assertEquals(32, londonBoroughs.length);

        Subdivision[] metropolitanDistricts = SubdivisionCode.GB.getMetropolitanDistricts();
        assertEquals(36, metropolitanDistricts.length);

        Subdivision[] unitaryAuthorities = SubdivisionCode.GB.getUnitaryAuthorities();
        assertEquals(80, unitaryAuthorities.length);

        Subdivision[] districts = SubdivisionCode.GB.getDistricts();
        assertEquals(11, districts.length);

        Subdivision[] councilAreas = SubdivisionCode.GB.getCouncilAreas();
        assertEquals(32, councilAreas.length);

        // getByParent for constituent countries
        Subdivision eng = SubdivisionCode.GB.ENG;
        Subdivision[] engChildren = SubdivisionCode.GB.getByParent(eng);
        assertEquals(152, engChildren.length);

        Subdivision nir = SubdivisionCode.GB.NIR;
        Subdivision[] nirChildren = SubdivisionCode.GB.getByParent(nir);
        assertEquals(11, nirChildren.length);

        Subdivision sct = SubdivisionCode.GB.SCT;
        Subdivision[] sctChildren = SubdivisionCode.GB.getByParent(sct);
        assertEquals(32, sctChildren.length);

        Subdivision wls = SubdivisionCode.GB.WLS;
        Subdivision[] wlsChildren = SubdivisionCode.GB.getByParent(wls);
        assertEquals(22, wlsChildren.length);

        // getByParent for grouping codes
        Subdivision gbn = SubdivisionCode.GB.GBN;
        Subdivision[] gbnChildren = SubdivisionCode.GB.getByParent(gbn);
        assertEquals(2, gbnChildren.length);

        Subdivision ukm = SubdivisionCode.GB.UKM;
        Subdivision[] ukmChildren = SubdivisionCode.GB.getByParent(ukm);
        assertEquals(2, ukmChildren.length);

        Subdivision eaw = SubdivisionCode.GB.EAW;
        Subdivision[] eawChildren = SubdivisionCode.GB.getByParent(eaw);
        assertEquals(2, eawChildren.length);

        // Verify specific children within England
        boolean hasCambridgeshire = false;
        boolean hasBirmingham = false;
        boolean hasCityOfLondon = false;
        boolean hasBarkingAndDagenham = false;
        boolean hasBathAndNorthEastSomerset = false;
        for (Subdivision s : engChildren) {
            if (s.equals(SubdivisionCode.GB.CAM)) hasCambridgeshire = true;
            if (s.equals(SubdivisionCode.GB.BIR)) hasBirmingham = true;
            if (s.equals(SubdivisionCode.GB.LND)) hasCityOfLondon = true;
            if (s.equals(SubdivisionCode.GB.BDG)) hasBarkingAndDagenham = true;
            if (s.equals(SubdivisionCode.GB.BAS)) hasBathAndNorthEastSomerset = true;
        }
        assertTrue(hasCambridgeshire);
        assertTrue(hasBirmingham);
        assertTrue(hasCityOfLondon);
        assertTrue(hasBarkingAndDagenham);
        assertTrue(hasBathAndNorthEastSomerset);

        // Verify specific children within Scotland
        Subdivision[] sctChildrenArr = SubdivisionCode.GB.getByParent(sct);
        boolean hasAberdeen = false;
        boolean hasEdinburgh = false;
        boolean hasHighland = false;
        for (Subdivision s : sctChildrenArr) {
            if (s.equals(SubdivisionCode.GB.ABE)) hasAberdeen = true;
            if (s.equals(SubdivisionCode.GB.EDH)) hasEdinburgh = true;
            if (s.equals(SubdivisionCode.GB.HLD)) hasHighland = true;
        }
        assertTrue(hasAberdeen);
        assertTrue(hasEdinburgh);
        assertTrue(hasHighland);

        // Verify specific children within Wales
        Subdivision[] wlsChildrenArr = SubdivisionCode.GB.getByParent(wls);
        boolean hasCardiff = false;
        boolean hasSwansea = false;
        boolean hasPowys = false;
        for (Subdivision s : wlsChildrenArr) {
            if (s.equals(SubdivisionCode.GB.CRF)) hasCardiff = true;
            if (s.equals(SubdivisionCode.GB.SWA)) hasSwansea = true;
            if (s.equals(SubdivisionCode.GB.POW)) hasPowys = true;
        }
        assertTrue(hasCardiff);
        assertTrue(hasSwansea);
        assertTrue(hasPowys);

        // Verify specific children within Northern Ireland
        Subdivision[] nirChildrenArr = SubdivisionCode.GB.getByParent(nir);
        boolean hasBelfast = false;
        boolean hasDerry = false;
        for (Subdivision s : nirChildrenArr) {
            if (s.equals(SubdivisionCode.GB.BFS)) hasBelfast = true;
            if (s.equals(SubdivisionCode.GB.DRS)) hasDerry = true;
        }
        assertTrue(hasBelfast);
        assertTrue(hasDerry);

        // Verify GBN children
        boolean hasEaw = false;
        boolean hasSctInGbn = false;
        for (Subdivision s : gbnChildren) {
            if (s.equals(SubdivisionCode.GB.EAW)) hasEaw = true;
            if (s.equals(SubdivisionCode.GB.SCT)) hasSctInGbn = true;
        }
        assertTrue(hasEaw);
        assertTrue(hasSctInGbn);
    }

    @Test
    public void testAuditFields() {
        assertNotNull(SubdivisionCode.GB.wikipedia());
        assertFalse(SubdivisionCode.GB.wikipedia().isBlank());
        assertTrue(SubdivisionCode.GB.wikipedia().contains("ISO_3166-2:GB"));

        assertNotNull(SubdivisionCode.GB.dateAdded());
        assertFalse(SubdivisionCode.GB.dateAdded().isBlank());

        assertNotNull(SubdivisionCode.GB.lastUpdated());
        assertFalse(SubdivisionCode.GB.lastUpdated().isBlank());
    }
}
