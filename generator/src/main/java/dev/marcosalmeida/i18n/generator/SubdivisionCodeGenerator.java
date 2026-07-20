package dev.marcosalmeida.i18n.generator;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SubdivisionCodeGenerator {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: SubdivisionCodeGenerator <data-dir> <output-dir>");
            System.exit(1);
        }
        Path dataDir = Paths.get(args[0]);
        Path outputDir = Paths.get(args[1]);

        List<CountryData> countries = loadAndValidate(dataDir);
        countries.sort(Comparator.comparing(CountryData::getCountry));

        Files.createDirectories(outputDir);
        Path outputFile = outputDir.resolve("dev/marcosalmeida/i18n/SubdivisionCode.java");
        Files.createDirectories(outputFile.getParent());

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(outputFile))) {
            generate(countries, out);
        }

        System.out.println("Generated " + outputFile + " with " + countries.size() + " countries.");
    }

    static List<CountryData> loadAndValidate(Path dataDir) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<CountryData> result = new ArrayList<>();
        Set<String> seenCountryCodes = new HashSet<>();

        try (Stream<Path> files = Files.walk(dataDir)) {
            List<Path> jsonFiles = files
                    .filter(p -> p.toString().endsWith(".json"))
                    .sorted()
                    .collect(Collectors.toList());

            for (Path file : jsonFiles) {
                String fileName = dataDir.relativize(file).toString(); // e.g., "europe/ie.json"
                CountryData country;
                try {
                    country = mapper.readValue(file.toFile(), CountryData.class);
                } catch (IOException e) {
                    throw new IOException(fileName + ": JSON parse error: " + e.getMessage(), e);
                }

                // Validate required fields
                if (country.getCountry() == null || country.getCountry().isBlank()) {
                    throw new IllegalArgumentException(fileName + ": missing 'country' field");
                }
                if (country.getName() == null || country.getName().isBlank()) {
                    throw new IllegalArgumentException(fileName + ": missing 'name' field");
                }
                if (country.getSubdivisions() == null || country.getSubdivisions().isEmpty()) {
                    throw new IllegalArgumentException(fileName + ": 'subdivisions' array is empty or missing");
                }
                if (!country.getCountry().equals(country.getCountry().toUpperCase())) {
                    throw new IllegalArgumentException(fileName + ": 'country' must be uppercase, got '" + country.getCountry() + "'");
                }
                if (country.getCountry().length() != 2) {
                    throw new IllegalArgumentException(fileName + ": 'country' must be exactly 2 characters, got '" + country.getCountry() + "'");
                }

                // Validate no duplicate country codes (across files)
                if (!seenCountryCodes.add(country.getCountry())) {
                    throw new IllegalArgumentException(fileName + ": duplicate country code '" + country.getCountry() + "' already defined in another file");
                }

                // Validate subdivisions
                Set<String> seenCodes = new HashSet<>();
                Map<String, SubdivisionEntry> allSubs = country.getSubdivisions().stream()
                        .collect(Collectors.toMap(SubdivisionEntry::getCode, s -> s));

                for (int i = 0; i < country.getSubdivisions().size(); i++) {
                    SubdivisionEntry s = country.getSubdivisions().get(i);
                    String prefix = fileName + ": subdivision[" + i + "]";

                    if (s.getCode() == null || s.getCode().isBlank()) {
                        throw new IllegalArgumentException(prefix + " missing 'code'");
                    }
                    if (s.getName() == null || s.getName().isBlank()) {
                        throw new IllegalArgumentException(prefix + " missing 'name'");
                    }
                    if (s.getCategory() == null || s.getCategory().isBlank()) {
                        throw new IllegalArgumentException(prefix + " missing 'category'");
                    }
                    if (!seenCodes.add(s.getCode())) {
                        throw new IllegalArgumentException(fileName + ": duplicate code '" + s.getCode() + "'");
                    }
                    if (s.getParent() != null && !allSubs.containsKey(s.getParent())) {
                        throw new IllegalArgumentException(fileName + ": parent '" + s.getParent() + "' not found for subdivision '" + s.getCode() + "'");
                    }
                }

                result.add(country);
            }
        }

        if (result.isEmpty()) {
            throw new IllegalArgumentException("No JSON files found under " + dataDir);
        }

        return result;
    }

    // --- Code generation ---

    static void generate(List<CountryData> countries, PrintWriter out) {
        out.println("package dev.marcosalmeida.i18n;");
        out.println();
        out.println("import com.neovisionaries.i18n.CountryCode;");
        out.println("import java.util.Arrays;");
        out.println("import java.util.Optional;");
        out.println("import java.util.stream.Stream;");
        out.println();
        out.println("/**");
        out.println(" * Entry point for accessing ISO-3166-2 subdivisions.");
        out.println(" *");
        out.println(" * <p>");
        out.println(" * This class provides access to subdivisions for various countries using the syntax");
        out.println(" * {@code SubdivisionCode.COUNTRY.SUBDIVISION} (e.g., {@code SubdivisionCode.US.AL}).");
        out.println(" * </p>");
        out.println(" *");
        out.println(" * <p>This file is auto-generated. Do not edit manually.</p>");
        out.println(" */");
        out.println("public final class SubdivisionCode {");
        out.println();
        out.println("    private SubdivisionCode() {}");
        out.println();

        // Private helper methods (identical to current hand-written versions)
        generateHelperMethods(out);

        // One nested enum per country
        for (CountryData country : countries) {
            generateCountryEnum(country, out);
        }

        // allValues()
        generateAllValues(countries, out);

        // getSubdivisions(CountryCode) switch
        generateGetSubdivisions(countries, out);

        // Global cross-country lookups
        generateGlobalMethods(out);

        // Global category filters
        generateGlobalCategoryFilters(countries, out);

        // getByParent
        generateGlobalGetByParent(out);

        out.println("}");
    }

    static void generateHelperMethods(PrintWriter out) {
        out.println("    private static <T extends Subdivision> T fromCode(T[] values, String code) {");
        out.println("        if (code == null || code.isEmpty()) {");
        out.println("            throw new IllegalArgumentException(\"Invalid subdivision code: \" + code);");
        out.println("        }");
        out.println("        return Arrays.stream(values)");
        out.println("                .filter(s -> s.getCode().equalsIgnoreCase(code) || s.getSubdivisionCode().equalsIgnoreCase(code))");
        out.println("                .findFirst()");
        out.println("                .orElseThrow(() -> new IllegalArgumentException(\"No subdivision found for code: \" + code));");
        out.println("    }");
        out.println();
        out.println("    private static <T extends Subdivision> Optional<Subdivision> fromName(T[] values, String name) {");
        out.println("        if (name == null || name.isBlank()) {");
        out.println("            return Optional.empty();");
        out.println("        }");
        out.println("        String normalized = name.trim();");
        out.println("        return Arrays.stream(values)");
        out.println("                .filter(s -> s.getSubdivisionName().equalsIgnoreCase(normalized))");
        out.println("                .map(Subdivision.class::cast)");
        out.println("                .findFirst();");
        out.println("    }");
        out.println();
        out.println("    private static <T extends Subdivision> Optional<Subdivision> find(T[] values, String value) {");
        out.println("        if (value == null || value.isBlank()) {");
        out.println("            return Optional.empty();");
        out.println("        }");
        out.println("        try {");
        out.println("            return Optional.of(fromCode(values, value));");
        out.println("        } catch (IllegalArgumentException e) {");
        out.println("            return fromName(values, value);");
        out.println("        }");
        out.println("    }");
        out.println();
        out.println("    private static <T extends Subdivision> Subdivision[] getByCategory(T[] values, String category) {");
        out.println("        if (category == null || category.isBlank()) {");
        out.println("            return new Subdivision[0];");
        out.println("        }");
        out.println("        return Arrays.stream(values)");
        out.println("                .filter(s -> s.getCategory().equalsIgnoreCase(category))");
        out.println("                .toArray(Subdivision[]::new);");
        out.println("    }");
        out.println();
        out.println("    private static <T extends Subdivision> Subdivision[] getByParent(T[] values, Subdivision parent) {");
        out.println("        if (parent == null) {");
        out.println("            return new Subdivision[0];");
        out.println("        }");
        out.println("        return Arrays.stream(values)");
        out.println("                .filter(s -> s.getParent().map(p -> p.equals(parent)).orElse(false))");
        out.println("                .toArray(Subdivision[]::new);");
        out.println("    }");
        out.println();
    }

    static void generateCountryEnum(CountryData country, PrintWriter out) {
        String code = country.getCountry();
        String name = country.getName();

        boolean needsPrefix = country.getSubdivisions().stream()
                .anyMatch(s -> Character.isDigit(s.getCode().charAt(0)));
        String prefix = needsPrefix ? code + "_" : "";

        boolean hasParent = country.getSubdivisions().stream()
                .anyMatch(s -> s.getParent() != null);

        Map<String, List<SubdivisionEntry>> byCategory = country.getSubdivisions().stream()
                .collect(Collectors.groupingBy(SubdivisionEntry::getCategory, LinkedHashMap::new, Collectors.toList()));

        out.println("    /**");
        out.println("     * ISO-3166-2 subdivisions for " + name + ".");
        out.println("     */");
        out.println("    public enum " + code + " implements Subdivision {");

        // Constants
        List<SubdivisionEntry> subs = country.getSubdivisions();
        for (int i = 0; i < subs.size(); i++) {
            SubdivisionEntry s = subs.get(i);
            String constName = prefix + s.getCode();
            String fullCode = code + "-" + s.getCode();
            out.print("        /** " + s.getName() + " (" + s.getCategory() + ") */");
            out.println();
            if (hasParent && s.getParent() != null) {
                String parentConst = prefix + s.getParent();
                out.println("        " + constName + "(\"" + fullCode + "\", \"" + s.getName() + "\", \"" + s.getCategory() + "\", " + parentConst + "),");
            } else if (hasParent) {
                out.println("        " + constName + "(\"" + fullCode + "\", \"" + s.getName() + "\", \"" + s.getCategory() + "\", null),");
            } else {
                out.println("        " + constName + "(\"" + fullCode + "\", \"" + s.getName() + "\", \"" + s.getCategory() + "\"),");
            }
        }
        out.println("        ;");
        out.println();

        // Fields
        out.println("        private final String code;");
        out.println("        private final String name;");
        out.println("        private final String category;");
        if (hasParent) {
            out.println("        private final " + code + " parent;");
        }
        out.println();

        // Constructor
        if (hasParent) {
            out.println("        " + code + "(String code, String name, String category, " + code + " parent) {");
        } else {
            out.println("        " + code + "(String code, String name, String category) {");
        }
        out.println("            this.code = code;");
        out.println("            this.name = name;");
        out.println("            this.category = category;");
        if (hasParent) {
            out.println("            this.parent = parent;");
        }
        out.println("        }");
        out.println();

        // Getters
        out.println("        @Override");
        out.println("        public String getCode() { return code; }");
        out.println();
        out.println("        @Override");
        out.println("        public String getSubdivisionName() { return name; }");
        out.println();
        out.println("        @Override");
        out.println("        public String getCategory() { return category; }");
        out.println();

        // getSubdivisionCode() override for numeric codes
        if (needsPrefix) {
            out.println("        @Override");
            out.println("        public String getSubdivisionCode() {");
            out.println("            String n = name();");
            out.println("            return n.substring(n.lastIndexOf(\"_\") + 1);");
            out.println("        }");
            out.println();
        }

        // getParent() override for parent relationships
        if (hasParent) {
            out.println("        @Override");
            out.println("        public Optional<Subdivision> getParent() {");
            out.println("            return Optional.ofNullable(parent);");
            out.println("        }");
            out.println();
        }

        // fromCode(), fromName(), find()
        out.println("        /**");
        out.println("         * Returns the subdivision for the given code.");
        out.println("         * @param code the ISO-3166-2 code or subdivision code part.");
        out.println("         * @return the subdivision.");
        out.println("         * @throws IllegalArgumentException if no subdivision is found for the given code.");
        out.println("         */");
        out.println("        public static " + code + " fromCode(String code) { return SubdivisionCode.fromCode(values(), code); }");
        out.println();
        out.println("        /**");
        out.println("         * Returns the subdivision for the given name.");
        out.println("         * @param name the subdivision name.");
        out.println("         * @return an Optional containing the subdivision if found, or empty otherwise.");
        out.println("         */");
        out.println("        public static Optional<Subdivision> fromName(String name) { return SubdivisionCode.fromName(values(), name); }");
        out.println();
        out.println("        /**");
        out.println("         * Returns the subdivision for the given value, searching by code or name.");
        out.println("         * @param value the code or name.");
        out.println("         * @return an Optional containing the subdivision if found, or empty otherwise.");
        out.println("         */");
        out.println("        public static Optional<Subdivision> find(String value) { return SubdivisionCode.find(values(), value); }");
        out.println();

        // Category-specific methods
        for (Map.Entry<String, List<SubdivisionEntry>> entry : byCategory.entrySet()) {
            String cat = entry.getKey();
            String methodName = categoryToMethodName(cat);
            String displayPlural = categoryToDisplayPlural(cat);
            out.println("        /**");
            out.println("         * Returns the " + name + " " + displayPlural + ".");
            out.println("         * @return an array of " + displayPlural + ".");
            out.println("         */");
            out.println("        public static Subdivision[] " + methodName + "() { return SubdivisionCode.getByCategory(values(), \"" + cat + "\"); }");
            out.println();
        }

        // getByParent() for parent relationships
        if (hasParent) {
            out.println("        /**");
            out.println("         * Returns the subdivisions for the given parent.");
            out.println("         * @param parent the parent subdivision.");
            out.println("         * @return an array of subdivisions belonging to the parent.");
            out.println("         */");
            out.println("        public static Subdivision[] getByParent(Subdivision parent) { return SubdivisionCode.getByParent(values(), parent); }");
            out.println();
        }

        out.println("    }");
        out.println();
    }

    static void generateAllValues(List<CountryData> countries, PrintWriter out) {
        out.println("    private static Subdivision[] allValues() {");
        String values = countries.stream()
                .map(c -> c.getCountry() + ".values()")
                .collect(Collectors.joining(", "));
        out.println("        return Stream.of(" + values + ")");
        out.println("                .flatMap(Arrays::stream)");
        out.println("                .toArray(Subdivision[]::new);");
        out.println("    }");
        out.println();
    }

    static void generateGetSubdivisions(List<CountryData> countries, PrintWriter out) {
        out.println("    /**");
        out.println("     * Gets subdivisions by country code.");
        out.println("     * @param code the country code.");
        out.println("     * @return an array of subdivisions for the country, or null if not supported.");
        out.println("     */");
        out.println("    public static Subdivision[] getSubdivisions(CountryCode code) {");
        out.println("        if (code == null) return null;");
        out.println("        return switch (code) {");
        for (CountryData c : countries) {
            out.println("            case " + c.getCountry() + " -> " + c.getCountry() + ".values();");
        }
        out.println("            default -> null;");
        out.println("        };");
        out.println("    }");
        out.println();
    }

    static void generateGlobalMethods(PrintWriter out) {
        out.println("    /**");
        out.println("     * Gets a subdivision by its ISO-3166-2 code.");
        out.println("     * @param code the ISO-3166-2 code (e.g., \"US-AL\").");
        out.println("     * @return the subdivision.");
        out.println("     * @throws IllegalArgumentException if the code is invalid or not supported.");
        out.println("     */");
        out.println("    public static Subdivision fromCode(String code) { return fromCode(allValues(), code); }");
        out.println();
        out.println("    /**");
        out.println("     * Returns the subdivision for the given name.");
        out.println("     * @param name the subdivision name.");
        out.println("     * @return an Optional containing the subdivision if found, or empty otherwise.");
        out.println("     */");
        out.println("    public static Optional<Subdivision> fromName(String name) { return fromName(allValues(), name); }");
        out.println();
        out.println("    /**");
        out.println("     * Returns the subdivision for the given value, searching by code or name across all countries.");
        out.println("     * @param value the code or name.");
        out.println("     * @return an Optional containing the subdivision if found, or empty otherwise.");
        out.println("     */");
        out.println("    public static Optional<Subdivision> find(String value) { return find(allValues(), value); }");
        out.println();
    }

    static void generateGlobalCategoryFilters(List<CountryData> countries, PrintWriter out) {
        // Normalize to lowercase for deduplication (getByCategory uses equalsIgnoreCase)
        Set<String> categories = countries.stream()
                .flatMap(c -> c.getSubdivisions().stream())
                .map(s -> s.getCategory().toLowerCase())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (String cat : categories) {
            String methodName = categoryToMethodName(cat);
            String displayPlural = categoryToDisplayPlural(cat);
            out.println("    /**");
            out.println("     * Returns all " + displayPlural + " across supported countries.");
            out.println("     * @return an array of " + displayPlural + ".");
            out.println("     */");
            out.println("    public static Subdivision[] " + methodName + "() { return getByCategory(allValues(), \"" + cat + "\"); }");
            out.println();
        }
    }

    static void generateGlobalGetByParent(PrintWriter out) {
        out.println("    /**");
        out.println("     * Returns the subdivisions for the given parent across all countries.");
        out.println("     * @param parent the parent subdivision.");
        out.println("     * @return an array of subdivisions belonging to the parent.");
        out.println("     */");
        out.println("    public static Subdivision[] getByParent(Subdivision parent) { return getByParent(allValues(), parent); }");
    }

    // --- Category name transforms ---

    // Map category singular → method name for irregular plurals
    private static final Map<String, String> CATEGORY_METHOD_NAMES = Map.ofEntries(
            Map.entry("county", "getCounties"),
            Map.entry("territory", "getTerritories"),
            Map.entry("federal entity", "getFederalEntities"),
            Map.entry("federal district", "getFederalDistricts"),
            Map.entry("autonomous region", "getAutonomousRegions"),
            Map.entry("outlying area", "getOutlyingAreas"),
            Map.entry("special island authority", "getSpecialIslandAuthorities")
    );

    static String categoryToMethodName(String category) {
        String lower = category.toLowerCase();
        if (CATEGORY_METHOD_NAMES.containsKey(lower)) {
            return CATEGORY_METHOD_NAMES.get(lower);
        }
        // Default: "state" → "getStates", "region" → "getRegions", "province" → "getProvinces"
        String[] words = lower.split(" ");
        StringBuilder sb = new StringBuilder("get");
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)));
            sb.append(word.substring(1));
        }
        sb.append("s");
        return sb.toString();
    }

    static String categoryToDisplayPlural(String category) {
        String lower = category.toLowerCase();
        String methodName = categoryToMethodName(category);
        // Strip the "get" prefix and lowercase the first char
        return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }
}
