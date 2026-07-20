package dev.marcosalmeida.i18n.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubdivisionCodeGeneratorTest {

    @Test
    void testLoadAndValidate_validData(@TempDir Path tempDir) throws IOException {
        Path dataDir = tempDir.resolve("data");
        Path continentDir = dataDir.resolve("north-america");
        Files.createDirectories(continentDir);
        Files.writeString(continentDir.resolve("us.json"),
                "{\"country\":\"US\",\"name\":\"United States\",\"subdivisions\":[" +
                "{\"code\":\"AL\",\"name\":\"Alabama\",\"category\":\"State\"}," +
                "{\"code\":\"AK\",\"name\":\"Alaska\",\"category\":\"State\"}" +
                "]}");

        List<CountryData> countries = SubdivisionCodeGenerator.loadAndValidate(dataDir);
        assertEquals(1, countries.size());
        assertEquals("US", countries.get(0).getCountry());
        assertEquals(2, countries.get(0).getSubdivisions().size());
    }

    @Test
    void testLoadAndValidate_missingCountry(@TempDir Path tempDir) throws IOException {
        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir.resolve("europe"));
        Files.writeString(dataDir.resolve("europe/bad.json"),
                "{\"name\":\"Nowhere\",\"subdivisions\":[" +
                "{\"code\":\"X\",\"name\":\"X Place\",\"category\":\"place\"}" +
                "]}");

        assertThrows(IllegalArgumentException.class, () ->
                SubdivisionCodeGenerator.loadAndValidate(dataDir));
    }

    @Test
    void testLoadAndValidate_duplicateCode(@TempDir Path tempDir) throws IOException {
        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir.resolve("europe"));
        Files.writeString(dataDir.resolve("europe/dup.json"),
                "{\"country\":\"XX\",\"name\":\"DupLand\",\"subdivisions\":[" +
                "{\"code\":\"A\",\"name\":\"First\",\"category\":\"thing\"}," +
                "{\"code\":\"A\",\"name\":\"Second\",\"category\":\"thing\"}" +
                "]}");

        assertThrows(IllegalArgumentException.class, () ->
                SubdivisionCodeGenerator.loadAndValidate(dataDir));
    }

    @Test
    void testLoadAndValidate_invalidParent(@TempDir Path tempDir) throws IOException {
        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir.resolve("europe"));
        Files.writeString(dataDir.resolve("europe/badparent.json"),
                "{\"country\":\"XX\",\"name\":\"BadParent\",\"subdivisions\":[" +
                "{\"code\":\"A\",\"name\":\"First\",\"category\":\"thing\",\"parent\":\"Z\"}" +
                "]}");

        assertThrows(IllegalArgumentException.class, () ->
                SubdivisionCodeGenerator.loadAndValidate(dataDir));
    }

    @Test
    void testGenerate_producesCompilableOutput(@TempDir Path tempDir) throws Exception {
        // Use the real test resource data
        Path dataDir = Paths.get("src/test/resources/test-data/valid");
        Path outputDir = tempDir.resolve("output");

        SubdivisionCodeGenerator.main(new String[]{
                dataDir.toString(), outputDir.toString()
        });

        Path generated = outputDir.resolve("dev/marcosalmeida/i18n/SubdivisionCode.java");
        assertTrue(Files.exists(generated));

        String content = Files.readString(generated);
        assertTrue(content.contains("public enum US implements Subdivision"));
        assertTrue(content.contains("US-AL"));
        assertTrue(content.contains("Alabama"));
        assertTrue(content.contains("public static Subdivision[] getSubdivisions(CountryCode code)"));
    }
}
