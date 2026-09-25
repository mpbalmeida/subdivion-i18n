package dev.marcosalmeida.i18n;

import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubdivisionCodeTest {

    @Test
    public void testGetSubdivisions() {
        Subdivision[] au = SubdivisionCode.getSubdivisions(CountryCode.AU);
        assertNotNull(au);
        assertEquals(8, au.length);

        Subdivision[] us = SubdivisionCode.getSubdivisions(CountryCode.US);
        assertNotNull(us);
        assertTrue(us.length > 0);

        Subdivision[] br = SubdivisionCode.getSubdivisions(CountryCode.BR);
        assertNotNull(br);
        assertTrue(br.length > 0);

        Subdivision[] ca = SubdivisionCode.getSubdivisions(CountryCode.CA);
        assertNotNull(ca);
        assertEquals(13, ca.length);

        Subdivision[] mx = SubdivisionCode.getSubdivisions(CountryCode.MX);
        assertNotNull(mx);
        assertTrue(mx.length > 0);

        Subdivision[] ie = SubdivisionCode.getSubdivisions(CountryCode.IE);
        assertNotNull(ie);
        assertTrue(ie.length > 0);

        Subdivision[] it = SubdivisionCode.getSubdivisions(CountryCode.IT);
        assertNotNull(it);
        assertEquals(20, it.length);

        Subdivision[] nz = SubdivisionCode.getSubdivisions(CountryCode.NZ);
        assertNotNull(nz);
        assertEquals(17, nz.length);

        Subdivision[] de = SubdivisionCode.getSubdivisions(CountryCode.DE);
        assertNotNull(de);
        assertEquals(16, de.length);

        Subdivision[] at = SubdivisionCode.getSubdivisions(CountryCode.AT);
        assertNotNull(at);
        assertEquals(9, at.length);

        Subdivision[] bz = SubdivisionCode.getSubdivisions(CountryCode.BZ);
        assertNotNull(bz);
        assertEquals(6, bz.length);

        Subdivision[] cr = SubdivisionCode.getSubdivisions(CountryCode.CR);
        assertNotNull(cr);
        assertEquals(7, cr.length);

        Subdivision[] sv = SubdivisionCode.getSubdivisions(CountryCode.SV);
        assertNotNull(sv);
        assertEquals(14, sv.length);

        Subdivision[] gt = SubdivisionCode.getSubdivisions(CountryCode.GT);
        assertNotNull(gt);
        assertEquals(22, gt.length);

        Subdivision[] hn = SubdivisionCode.getSubdivisions(CountryCode.HN);
        assertNotNull(hn);
        assertEquals(18, hn.length);

        Subdivision[] ni = SubdivisionCode.getSubdivisions(CountryCode.NI);
        assertNotNull(ni);
        assertEquals(17, ni.length);

        Subdivision[] pa = SubdivisionCode.getSubdivisions(CountryCode.PA);
        assertNotNull(pa);
        assertEquals(14, pa.length);

        Subdivision[] ar = SubdivisionCode.getSubdivisions(CountryCode.AR);
        assertNotNull(ar);
        assertEquals(24, ar.length);

        Subdivision[] bo = SubdivisionCode.getSubdivisions(CountryCode.BO);
        assertNotNull(bo);
        assertEquals(9, bo.length);

        Subdivision[] cl = SubdivisionCode.getSubdivisions(CountryCode.CL);
        assertNotNull(cl);
        assertEquals(16, cl.length);

        Subdivision[] co = SubdivisionCode.getSubdivisions(CountryCode.CO);
        assertNotNull(co);
        assertEquals(33, co.length);

        Subdivision[] ec = SubdivisionCode.getSubdivisions(CountryCode.EC);
        assertNotNull(ec);
        assertEquals(24, ec.length);

        Subdivision[] gy = SubdivisionCode.getSubdivisions(CountryCode.GY);
        assertNotNull(gy);
        assertEquals(10, gy.length);

        Subdivision[] py = SubdivisionCode.getSubdivisions(CountryCode.PY);
        assertNotNull(py);
        assertEquals(18, py.length);

        Subdivision[] pe = SubdivisionCode.getSubdivisions(CountryCode.PE);
        assertNotNull(pe);
        assertEquals(26, pe.length);

        Subdivision[] sr = SubdivisionCode.getSubdivisions(CountryCode.SR);
        assertNotNull(sr);
        assertEquals(10, sr.length);

        Subdivision[] uy = SubdivisionCode.getSubdivisions(CountryCode.UY);
        assertNotNull(uy);
        assertEquals(19, uy.length);

        Subdivision[] ve = SubdivisionCode.getSubdivisions(CountryCode.VE);
        assertNotNull(ve);
        assertEquals(25, ve.length);

        assertNull(SubdivisionCode.getSubdivisions(CountryCode.AF));
    }

    @Test
    public void testFromCode() {
        Subdivision auNsw = SubdivisionCode.fromCode("AU-NSW");
        assertNotNull(auNsw);
        assertEquals("New South Wales", auNsw.getSubdivisionName());

        Subdivision usAl = SubdivisionCode.fromCode("US-AL");
        assertNotNull(usAl);
        assertEquals("Alabama", usAl.getSubdivisionName());

        Subdivision it25 = SubdivisionCode.fromCode("IT-25");
        assertNotNull(it25);
        assertEquals("Lombardia", it25.getSubdivisionName());

        Subdivision caOn = SubdivisionCode.fromCode("CA-ON");
        assertNotNull(caOn);
        assertEquals("Ontario", caOn.getSubdivisionName());

        Subdivision brSp = SubdivisionCode.fromCode("BR-SP");
        assertNotNull(brSp);
        assertEquals("São Paulo", brSp.getSubdivisionName());

        Subdivision ieD = SubdivisionCode.fromCode("IE-D");
        assertNotNull(ieD);
        assertEquals("Dublin", ieD.getSubdivisionName());

        Subdivision deBy = SubdivisionCode.fromCode("DE-BY");
        assertNotNull(deBy);
        assertEquals("Bayern", deBy.getSubdivisionName());

        Subdivision at9 = SubdivisionCode.fromCode("AT-9");
        assertNotNull(at9);
        assertEquals("Wien", at9.getSubdivisionName());

        // Central America
        assertEquals("Cayo", SubdivisionCode.fromCode("BZ-CY").getSubdivisionName());
        assertEquals("San José", SubdivisionCode.fromCode("CR-SJ").getSubdivisionName());
        assertEquals("Ahuachapán", SubdivisionCode.fromCode("SV-AH").getSubdivisionName());
        assertEquals("Guatemala", SubdivisionCode.fromCode("GT-01").getSubdivisionName());
        assertEquals("Atlántida", SubdivisionCode.fromCode("HN-AT").getSubdivisionName());
        assertEquals("Managua", SubdivisionCode.fromCode("NI-MN").getSubdivisionName());
        assertEquals("Emberá", SubdivisionCode.fromCode("PA-EM").getSubdivisionName());

        // South America
        assertEquals("Ciudad Autónoma de Buenos Aires", SubdivisionCode.fromCode("AR-C").getSubdivisionName());
        assertEquals("La Paz", SubdivisionCode.fromCode("BO-L").getSubdivisionName());
        assertEquals("Valparaíso", SubdivisionCode.fromCode("CL-VS").getSubdivisionName());
        assertEquals("Distrito Capital de Bogotá", SubdivisionCode.fromCode("CO-DC").getSubdivisionName());
        assertEquals("Pichincha", SubdivisionCode.fromCode("EC-P").getSubdivisionName());
        assertEquals("Demerara-Mahaica", SubdivisionCode.fromCode("GY-DE").getSubdivisionName());
        assertEquals("Asunción", SubdivisionCode.fromCode("PY-ASU").getSubdivisionName());
        assertEquals("Lima", SubdivisionCode.fromCode("PE-LIM").getSubdivisionName());
        assertEquals("Paramaribo", SubdivisionCode.fromCode("SR-PM").getSubdivisionName());
        assertEquals("Montevideo", SubdivisionCode.fromCode("UY-MO").getSubdivisionName());
        assertEquals("Distrito Capital", SubdivisionCode.fromCode("VE-A").getSubdivisionName());

        // Test lookup by subdivision part (returns the first match, which is BR-AL for "AL")
        Subdivision alShort = SubdivisionCode.fromCode("AL");
        assertNotNull(alShort);
        assertEquals("BR-AL", alShort.getCode());

        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.fromCode("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> SubdivisionCode.fromCode("US-XX"));
    }

    @Test
    public void testFromName() {
        Subdivision alabama = SubdivisionCode.fromName("Alabama").orElseThrow();
        assertEquals("US-AL", alabama.getCode());

        Subdivision saoPaulo = SubdivisionCode.fromName("são paulo").orElseThrow();
        assertEquals("BR-SP", saoPaulo.getCode());

        assertTrue(SubdivisionCode.fromName("Invalid").isEmpty());
    }

    @Test
    public void testFind() {
        // "AL" matches BR-AL first because BR is before US in the Stream
        Subdivision al = SubdivisionCode.find("AL").orElseThrow();
        assertEquals("BR-AL", al.getCode());

        Subdivision spFull = SubdivisionCode.find("BR-SP").orElseThrow();
        assertEquals("BR-SP", spFull.getCode());

        Subdivision mexico = SubdivisionCode.find("México").orElseThrow();
        assertEquals("MX-MEX", mexico.getCode());

        assertTrue(SubdivisionCode.find("Invalid").isEmpty());
    }

    @Test
    public void testGlobalFiltering() {
        Subdivision[] allStates = SubdivisionCode.getStates();
        // AT (9) + AU (6) + BR (26) + MX (31) + US "State" (50) + VE (23) = 145
        assertEquals(145, allStates.length);

        Subdivision[] allRegions = SubdivisionCode.getRegions();
        // Includes the CL (16), GY (10), PE (25) and IT (15) regions added with the Americas batch
        assertEquals(181, allRegions.length);

        Subdivision[] allProvinces = SubdivisionCode.getProvinces();
        // Includes AR (23), CR (7), EC (24), PA (10) added with the Americas batch
        assertEquals(232, allProvinces.length);

        Subdivision[] allCounties = SubdivisionCode.getCounties();
        assertEquals(134, allCounties.length);

        Subdivision[] allLands = SubdivisionCode.getLands();
        assertEquals(16, allLands.length);
    }
}
