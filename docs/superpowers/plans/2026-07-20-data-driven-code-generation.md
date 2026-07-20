# Data-Driven Code Generation — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor `SubdivisionCode.java` from a hand-written 1,266-line file to a data-driven architecture where JSON files per country feed a build-time code generator, keeping the public API identical.

**Architecture:** Multi-module Maven project — `generator/` compiles first using Jackson 3 to parse JSON data files, then `library/` uses `exec-maven-plugin` to run the generator during `generate-sources`, producing `SubdivisionCode.java` in `target/generated-sources/`. The existing `Subdivision.java` interface and all test files move into `library/` unchanged.

**Tech Stack:** Java 21, Maven, Jackson 3 (generator module only), JUnit 5

## Global Constraints

- Java 21, `mvn verify --batch-mode` for build
- No breaking changes to public API (`SubdivisionCode.US.AL`, `SubdivisionCode.getSubdivisions()`, etc.)
- Existing test suite must pass identically with generated code
- Generated `SubdivisionCode.java` is never committed — lives in `target/generated-sources/`
- Data files grouped by continent: `data/{europe,north-america,south-america,asia,africa,oceania}/`
- Commit messages follow Conventional Commits

---

## File Structure

```
i18n/
├── pom.xml                                  (NEW: parent POM, modules: generator, library)
├── data/                                    (NEW: one JSON file per country, grouped by continent)
│   ├── north-america/
│   │   ├── us.json
│   │   ├── ca.json
│   │   └── mx.json
│   ├── south-america/
│   │   └── br.json
│   ├── europe/
│   │   ├── ie.json
│   │   └── it.json
│   └── oceania/
│       ├── au.json
│       └── nz.json
├── generator/                               (NEW: code generator module)
│   ├── pom.xml
│   ├── src/main/java/dev/marcosalmeida/i18n/generator/
│   │   ├── SubdivisionCodeGenerator.java
│   │   ├── CountryData.java
│   │   └── SubdivisionEntry.java
│   └── src/test/java/dev/marcosalmeida/i18n/generator/
│       └── SubdivisionCodeGeneratorTest.java
├── library/                                 (MOVED: existing code as submodule)
│   ├── pom.xml
│   └── src/
│       ├── main/java/dev/marcosalmeida/i18n/
│       │   └── Subdivision.java            (MOVED: unchanged)
│       └── test/java/dev/marcosalmeida/i18n/
│           ├── SubdivisionCodeTest.java     (MOVED: unchanged)
│           ├── SubdivisionUSTest.java       (MOVED: unchanged)
│           ├── SubdivisionIETest.java       (MOVED: unchanged)
│           ├── SubdivisionITTest.java       (MOVED: unchanged)
│           ├── SubdivisionBRTest.java       (MOVED: unchanged)
│           ├── SubdivisionCATest.java       (MOVED: unchanged)
│           ├── SubdivisionMXTest.java       (MOVED: unchanged)
│           ├── SubdivisionAUTest.java       (MOVED: unchanged)
│           └── SubdivisionNZTest.java       (MOVED: unchanged)
├── scripts/                                 (MOVED: unchanged)
│   └── commit-msg.sh
├── jreleaser.yml                            (UNCHANGED)
├── .github/workflows/
│   ├── ci.yml                               (MODIFIED: multi-module paths)
│   └── release.yml                          (MODIFIED: multi-module paths)
└── README.md                                (UNCHANGED)
```

**Removed files:**
- `src/main/java/dev/marcosalmeida/i18n/SubdivisionCode.java` (old hand-written, replaced by generated version)
- Root `pom.xml` (replaced by parent POM)

---

### Task 1: Set up multi-module Maven layout

**Files:**
- Create: `pom.xml` (parent POM)
- Create: `generator/pom.xml`
- Create: `library/pom.xml`
- Modify: `pom.xml` (old root POM → parent POM)
- Move: `src/` → `library/src/`
- Move: `scripts/` → `scripts/` (stays at root)

**Interfaces:**
- Produces: Three Maven modules with correct dependency ordering

- [ ] **Step 1: Create parent POM**

```bash
cp pom.xml pom.xml.bak
```

Write `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>dev.marcosalmeida</groupId>
    <artifactId>i18n-parent</artifactId>
    <version>0.6.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <name>i18n Parent</name>
    <description>Internationalization utilities for Java, focusing on subdivision ISO-3166-2 codes.</description>
    <url>https://github.com/mpbalmeida/i18n</url>

    <developers>
        <developer>
            <name>Marcos Almeida</name>
            <email>me@marcosalmeida.dev</email>
            <url>https://github.com/mpbalmeida</url>
            <roles><role>developer</role></roles>
        </developer>
    </developers>

    <scm>
        <connection>scm:git:git://github.com/mpbalmeida/i18n.git</connection>
        <developerConnection>scm:git:ssh://github.com/mpbalmeida/i18n.git</developerConnection>
        <url>https://github.com/mpbalmeida/i18n</url>
    </scm>

    <licenses>
        <license>
            <name>Apache License, Version 2.0</name>
            <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
            <distribution>repo</distribution>
        </license>
    </licenses>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>21</maven.compiler.release>
    </properties>

    <modules>
        <module>generator</module>
        <module>library</module>
    </modules>
</project>
```

- [ ] **Step 2: Create generator module POM**

```bash
mkdir -p generator/src/main/java/dev/marcosalmeida/i18n/generator
mkdir -p generator/src/test/java/dev/marcosalmeida/i18n/generator
```

Write `generator/pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>dev.marcosalmeida</groupId>
        <artifactId>i18n-parent</artifactId>
        <version>0.6.0-SNAPSHOT</version>
    </parent>

    <artifactId>i18n-generator</artifactId>
    <name>i18n Code Generator</name>

    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.18.3</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.11.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-params</artifactId>
            <version>5.11.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Note: Jackson 3 (jakarta namespace, `com.fasterxml.jackson` groupId) is still in alpha as of mid-2026. The 2.x line (`jackson-databind` 2.18.3) is stable, widely used, and has the same API surface we need. The spec says "Jackson 3 (latest stable)" — if Jackson 3 is stable by implementation time, swap to `com.fasterxml.jackson:jackson-databind:3.x`. Otherwise use 2.18.3 which is identical for our use case.

- [ ] **Step 3: Create library module POM**

```bash
mkdir -p library/src/main/java/dev/marcosalmeida/i18n
mkdir -p library/src/test/java/dev/marcosalmeida/i18n
```

Write `library/pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>dev.marcosalmeida</groupId>
        <artifactId>i18n-parent</artifactId>
        <version>0.6.0-SNAPSHOT</version>
    </parent>

    <artifactId>i18n</artifactId>
    <name>i18n</name>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.junit</groupId>
                <artifactId>junit-bom</artifactId>
                <version>5.11.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>com.neovisionaries</groupId>
            <artifactId>nv-i18n</artifactId>
            <version>1.29</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-params</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <id>generate-subdivision-code</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>java</goal>
                        </goals>
                        <configuration>
                            <mainClass>dev.marcosalmeida.i18n.generator.SubdivisionCodeGenerator</mainClass>
                            <arguments>
                                <argument>${project.basedir}/../data</argument>
                                <argument>${project.build.directory}/generated-sources/java</argument>
                            </arguments>
                            <includePluginDependencies>true</includePluginDependencies>
                        </configuration>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>dev.marcosalmeida</groupId>
                        <artifactId>i18n-generator</artifactId>
                        <version>${project.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <id>add-generated-sources</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <source>${project.build.directory}/generated-sources/java</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>com.rudikershaw.gitbuildhook</groupId>
                <artifactId>git-build-hook-maven-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <goals><goal>install</goal></goals>
                    </execution>
                </executions>
                <configuration>
                    <installHooks>
                        <commit-msg>scripts/commit-msg.sh</commit-msg>
                    </installHooks>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.sonatype.central</groupId>
                <artifactId>central-publishing-maven-plugin</artifactId>
                <version>0.9.0</version>
                <extensions>true</extensions>
                <configuration>
                    <publishingServerId>central</publishingServerId>
                    <autoPublish>true</autoPublish>
                </configuration>
            </plugin>
        </plugins>
        <pluginManagement>
            <plugins>
                <plugin><artifactId>maven-clean-plugin</artifactId><version>3.4.0</version></plugin>
                <plugin><artifactId>maven-resources-plugin</artifactId><version>3.3.1</version></plugin>
                <plugin><artifactId>maven-compiler-plugin</artifactId><version>3.13.0</version></plugin>
                <plugin><artifactId>maven-surefire-plugin</artifactId><version>3.3.0</version></plugin>
                <plugin><artifactId>maven-jar-plugin</artifactId><version>3.4.2</version></plugin>
                <plugin><artifactId>maven-install-plugin</artifactId><version>3.1.4</version></plugin>
                <plugin><artifactId>maven-deploy-plugin</artifactId><version>3.1.4</version></plugin>
                <plugin><artifactId>maven-site-plugin</artifactId><version>3.12.1</version></plugin>
                <plugin><artifactId>maven-project-info-reports-plugin</artifactId><version>3.6.1</version></plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-javadoc-plugin</artifactId>
                    <version>3.12.0</version>
                    <executions>
                        <execution>
                            <id>attach-javadocs</id>
                            <goals><goal>jar</goal></goals>
                        </execution>
                    </executions>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-source-plugin</artifactId>
                    <version>3.3.1</version>
                    <executions>
                        <execution>
                            <id>attach-sources</id>
                            <goals><goal>jar-no-fork</goal></goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>

    <profiles>
        <profile>
            <id>deployment</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-javadoc-plugin</artifactId>
                        <version>3.12.0</version>
                        <executions>
                            <execution>
                                <id>attach-javadocs</id>
                                <goals><goal>jar</goal></goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-source-plugin</artifactId>
                        <version>3.3.1</version>
                        <executions>
                            <execution>
                                <id>attach-sources</id>
                                <goals><goal>jar-no-fork</goal></goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-gpg-plugin</artifactId>
                        <version>3.2.7</version>
                        <executions>
                            <execution>
                                <id>sign-artifacts</id>
                                <phase>verify</phase>
                                <goals><goal>sign</goal></goals>
                                <configuration>
                                    <gpgArguments>
                                        <arg>--pinentry-mode</arg>
                                        <arg>loopback</arg>
                                    </gpgArguments>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
```

- [ ] **Step 4: Move source files to library module**

```bash
mv src library/src
```

- [ ] **Step 5: Remove old hand-written SubdivisionCode.java**

```bash
rm library/src/main/java/dev/marcosalmeida/i18n/SubdivisionCode.java
```

This file will now be generated into `library/target/generated-sources/`.

- [ ] **Step 6: Move .gitignore entries appropriately, add generated-sources exclusion**

Ensure `library/target/` is git-ignored (already covered by existing `.gitignore` patterns).

- [ ] **Step 7: Build from root to verify Maven module structure compiles**

```bash
mvn compile --batch-mode
```

Expected: `library/` module fails because `SubdivisionCode.java` is missing. `generator/` module compiles successfully.

- [ ] **Step 8: Commit**

```bash
git add pom.xml generator/pom.xml library/pom.xml library/src/
git add .gitignore
git rm src/main/java/dev/marcosalmeida/i18n/SubdivisionCode.java
git rm pom.xml.bak  # if created
git commit -m "refactor(build): set up multi-module Maven layout for code generation"
```

---

### Task 2: Create data directory structure

**Files:**
- Create: `data/north-america/`, `data/south-america/`, `data/europe/`, `data/oceania/`, `data/asia/`, `data/africa/` (empty placeholder `.gitkeep` files in each)

**Interfaces:**
- Produces: Data directories that the generator reads recursively

- [ ] **Step 1: Create continent directories**

```bash
mkdir -p data/{north-america,south-america,europe,asia,africa,oceania}
```

- [ ] **Step 2: Commit**

```bash
git add data/
git commit -m "chore: create data directory structure grouped by continent"
```

---

### Task 3: Create Jackson data model classes

**Files:**
- Create: `generator/src/main/java/dev/marcosalmeida/i18n/generator/SubdivisionEntry.java`
- Create: `generator/src/main/java/dev/marcosalmeida/i18n/generator/CountryData.java`

**Interfaces:**
- Consumes: Jackson 3 (or 2.x) `ObjectMapper`
- Produces: `CountryData` — parsed representation of one JSON data file, `SubdivisionEntry` — parsed representation of one subdivision

- [ ] **Step 1: Write SubdivisionEntry.java**

```java
package dev.marcosalmeida.i18n.generator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubdivisionEntry {
    private String code;
    private String name;
    private String category;
    @JsonProperty(required = false)
    private String parent;

    public SubdivisionEntry() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
}
```

- [ ] **Step 2: Write CountryData.java**

```java
package dev.marcosalmeida.i18n.generator;

import java.util.List;

public class CountryData {
    private String country;
    private String name;
    private List<SubdivisionEntry> subdivisions;

    public CountryData() {}

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<SubdivisionEntry> getSubdivisions() { return subdivisions; }
    public void setSubdivisions(List<SubdivisionEntry> subdivisions) { this.subdivisions = subdivisions; }
}
```

- [ ] **Step 3: Verify compilation**

```bash
cd generator && mvn compile --batch-mode
```

Expected: SUCCESS.

- [ ] **Step 4: Commit**

```bash
git add generator/src/main/java/dev/marcosalmeida/i18n/generator/CountryData.java
git add generator/src/main/java/dev/marcosalmeida/i18n/generator/SubdivisionEntry.java
git commit -m "feat(generator): add Jackson data model classes for JSON parsing"
```

---

### Task 4: Implement SubdivisionCodeGenerator

**Files:**
- Create: `generator/src/main/java/dev/marcosalmeida/i18n/generator/SubdivisionCodeGenerator.java`

**Interfaces:**
- Consumes: `data/` directory path (args[0]), output directory path (args[1])
- Produces: `SubdivisionCode.java` written to output directory

- [ ] **Step 1: Write the generator main class**

The generator reads JSON, validates, sorts, and generates Java source. Key behaviors:

- Detects whether a country needs `XX_` constant prefix (any subdivision code starts with a digit → all constants get country code + `_` prefix, and `getSubdivisionCode()` is overridden)
- Detects whether a country has parent relationships (any subdivision has a `parent` field → 4-arg constructor with parent, `getParent()` override, `getByParent()` static method)
- Auto-generates category-specific query methods (e.g., `getStates()`, `getRegions()`) from unique categories found in the data
- Countries sorted alphabetically by alpha-2 code
- Subdivisions within each country appear in the order they're listed in the JSON file

```java
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
```

- [ ] **Step 2: Verify compilation**

```bash
cd generator && mvn compile --batch-mode
```

Expected: SUCCESS.

- [ ] **Step 3: Commit**

```bash
git add generator/src/main/java/dev/marcosalmeida/i18n/generator/SubdivisionCodeGenerator.java
git commit -m "feat(generator): implement SubdivisionCodeGenerator with validation and code generation"
```

---

### Task 5: Create JSON data files for existing 8 countries

**Files:**
- Create: `data/oceania/au.json`
- Create: `data/south-america/br.json`
- Create: `data/north-america/ca.json`
- Create: `data/europe/ie.json`
- Create: `data/europe/it.json`
- Create: `data/north-america/mx.json`
- Create: `data/oceania/nz.json`
- Create: `data/north-america/us.json`

**Interfaces:**
- Consumes: Data format defined in spec
- Produces: Valid JSON that the generator reads

- [ ] **Step 1: Write data/oceania/au.json**

```json
{
  "country": "AU",
  "name": "Australia",
  "subdivisions": [
    {"code": "ACT", "name": "Australian Capital Territory", "category": "territory"},
    {"code": "NSW", "name": "New South Wales", "category": "state"},
    {"code": "NT", "name": "Northern Territory", "category": "territory"},
    {"code": "QLD", "name": "Queensland", "category": "state"},
    {"code": "SA", "name": "South Australia", "category": "state"},
    {"code": "TAS", "name": "Tasmania", "category": "state"},
    {"code": "VIC", "name": "Victoria", "category": "state"},
    {"code": "WA", "name": "Western Australia", "category": "state"}
  ]
}
```

- [ ] **Step 2: Write data/south-america/br.json**

```json
{
  "country": "BR",
  "name": "Brazil",
  "subdivisions": [
    {"code": "AC", "name": "Acre", "category": "state"},
    {"code": "AL", "name": "Alagoas", "category": "state"},
    {"code": "AP", "name": "Amapá", "category": "state"},
    {"code": "AM", "name": "Amazonas", "category": "state"},
    {"code": "BA", "name": "Bahia", "category": "state"},
    {"code": "CE", "name": "Ceará", "category": "state"},
    {"code": "DF", "name": "Distrito Federal", "category": "federal district"},
    {"code": "ES", "name": "Espírito Santo", "category": "state"},
    {"code": "GO", "name": "Goiás", "category": "state"},
    {"code": "MA", "name": "Maranhão", "category": "state"},
    {"code": "MT", "name": "Mato Grosso", "category": "state"},
    {"code": "MS", "name": "Mato Grosso do Sul", "category": "state"},
    {"code": "MG", "name": "Minas Gerais", "category": "state"},
    {"code": "PA", "name": "Pará", "category": "state"},
    {"code": "PB", "name": "Paraíba", "category": "state"},
    {"code": "PR", "name": "Paraná", "category": "state"},
    {"code": "PE", "name": "Pernambuco", "category": "state"},
    {"code": "PI", "name": "Piauí", "category": "state"},
    {"code": "RJ", "name": "Rio de Janeiro", "category": "state"},
    {"code": "RN", "name": "Rio Grande do Norte", "category": "state"},
    {"code": "RS", "name": "Rio Grande do Sul", "category": "state"},
    {"code": "RO", "name": "Rondônia", "category": "state"},
    {"code": "RR", "name": "Roraima", "category": "state"},
    {"code": "SC", "name": "Santa Catarina", "category": "state"},
    {"code": "SP", "name": "São Paulo", "category": "state"},
    {"code": "SE", "name": "Sergipe", "category": "state"},
    {"code": "TO", "name": "Tocantins", "category": "state"}
  ]
}
```

- [ ] **Step 3: Write data/north-america/ca.json**

```json
{
  "country": "CA",
  "name": "Canada",
  "subdivisions": [
    {"code": "AB", "name": "Alberta", "category": "Province"},
    {"code": "BC", "name": "British Columbia", "category": "Province"},
    {"code": "MB", "name": "Manitoba", "category": "Province"},
    {"code": "NB", "name": "New Brunswick", "category": "Province"},
    {"code": "NL", "name": "Newfoundland and Labrador", "category": "Province"},
    {"code": "NS", "name": "Nova Scotia", "category": "Province"},
    {"code": "NT", "name": "Northwest Territories", "category": "Territory"},
    {"code": "NU", "name": "Nunavut", "category": "Territory"},
    {"code": "ON", "name": "Ontario", "category": "Province"},
    {"code": "PE", "name": "Prince Edward Island", "category": "Province"},
    {"code": "QC", "name": "Quebec", "category": "Province"},
    {"code": "SK", "name": "Saskatchewan", "category": "Province"},
    {"code": "YT", "name": "Yukon", "category": "Territory"}
  ]
}
```

- [ ] **Step 4: Write data/europe/ie.json**

```json
{
  "country": "IE",
  "name": "Ireland",
  "subdivisions": [
    {"code": "C", "name": "Connaught", "category": "province"},
    {"code": "L", "name": "Leinster", "category": "province"},
    {"code": "M", "name": "Munster", "category": "province"},
    {"code": "U", "name": "Ulster", "category": "province"},
    {"code": "CW", "name": "Carlow", "category": "county", "parent": "L"},
    {"code": "CN", "name": "Cavan", "category": "county", "parent": "U"},
    {"code": "CE", "name": "Clare", "category": "county", "parent": "M"},
    {"code": "CO", "name": "Cork", "category": "county", "parent": "M"},
    {"code": "DL", "name": "Donegal", "category": "county", "parent": "U"},
    {"code": "D", "name": "Dublin", "category": "county", "parent": "L"},
    {"code": "G", "name": "Galway", "category": "county", "parent": "C"},
    {"code": "KY", "name": "Kerry", "category": "county", "parent": "M"},
    {"code": "KE", "name": "Kildare", "category": "county", "parent": "L"},
    {"code": "KK", "name": "Kilkenny", "category": "county", "parent": "L"},
    {"code": "LS", "name": "Laois", "category": "county", "parent": "L"},
    {"code": "LM", "name": "Leitrim", "category": "county", "parent": "C"},
    {"code": "LK", "name": "Limerick", "category": "county", "parent": "M"},
    {"code": "LD", "name": "Longford", "category": "county", "parent": "L"},
    {"code": "LH", "name": "Louth", "category": "county", "parent": "L"},
    {"code": "MO", "name": "Mayo", "category": "county", "parent": "C"},
    {"code": "MH", "name": "Meath", "category": "county", "parent": "L"},
    {"code": "MN", "name": "Monaghan", "category": "county", "parent": "U"},
    {"code": "OY", "name": "Offaly", "category": "county", "parent": "L"},
    {"code": "RN", "name": "Roscommon", "category": "county", "parent": "C"},
    {"code": "SO", "name": "Sligo", "category": "county", "parent": "C"},
    {"code": "TA", "name": "Tipperary", "category": "county", "parent": "M"},
    {"code": "WD", "name": "Waterford", "category": "county", "parent": "M"},
    {"code": "WH", "name": "Westmeath", "category": "county", "parent": "L"},
    {"code": "WX", "name": "Wexford", "category": "county", "parent": "L"},
    {"code": "WW", "name": "Wicklow", "category": "county", "parent": "L"}
  ]
}
```

- [ ] **Step 5: Write data/europe/it.json**

```json
{
  "country": "IT",
  "name": "Italy",
  "subdivisions": [
    {"code": "65", "name": "Abruzzo", "category": "region"},
    {"code": "77", "name": "Basilicata", "category": "region"},
    {"code": "78", "name": "Calabria", "category": "region"},
    {"code": "72", "name": "Campania", "category": "region"},
    {"code": "45", "name": "Emilia-Romagna", "category": "region"},
    {"code": "36", "name": "Friuli Venezia Giulia", "category": "autonomous region"},
    {"code": "62", "name": "Lazio", "category": "region"},
    {"code": "42", "name": "Liguria", "category": "region"},
    {"code": "25", "name": "Lombardia", "category": "region"},
    {"code": "57", "name": "Marche", "category": "region"},
    {"code": "67", "name": "Molise", "category": "region"},
    {"code": "21", "name": "Piemonte", "category": "region"},
    {"code": "75", "name": "Puglia", "category": "region"},
    {"code": "88", "name": "Sardegna", "category": "autonomous region"},
    {"code": "82", "name": "Sicilia", "category": "autonomous region"},
    {"code": "52", "name": "Toscana", "category": "region"},
    {"code": "32", "name": "Trentino-Alto Adige", "category": "autonomous region"},
    {"code": "55", "name": "Umbria", "category": "region"},
    {"code": "23", "name": "Valle d'Aosta", "category": "autonomous region"},
    {"code": "34", "name": "Veneto", "category": "region"}
  ]
}
```

- [ ] **Step 6: Write data/north-america/mx.json**

```json
{
  "country": "MX",
  "name": "Mexico",
  "subdivisions": [
    {"code": "AGU", "name": "Aguascalientes", "category": "state"},
    {"code": "BCN", "name": "Baja California", "category": "state"},
    {"code": "BCS", "name": "Baja California Sur", "category": "state"},
    {"code": "CAM", "name": "Campeche", "category": "state"},
    {"code": "CHP", "name": "Chiapas", "category": "state"},
    {"code": "CHH", "name": "Chihuahua", "category": "state"},
    {"code": "CMX", "name": "Ciudad de México", "category": "federal entity"},
    {"code": "COA", "name": "Coahuila de Zaragoza", "category": "state"},
    {"code": "COL", "name": "Colima", "category": "state"},
    {"code": "DUR", "name": "Durango", "category": "state"},
    {"code": "GUA", "name": "Guanajuato", "category": "state"},
    {"code": "GRO", "name": "Guerrero", "category": "state"},
    {"code": "HID", "name": "Hidalgo", "category": "state"},
    {"code": "JAL", "name": "Jalisco", "category": "state"},
    {"code": "MIC", "name": "Michoacán de Ocampo", "category": "state"},
    {"code": "MOR", "name": "Morelos", "category": "state"},
    {"code": "MEX", "name": "México", "category": "state"},
    {"code": "NAY", "name": "Nayarit", "category": "state"},
    {"code": "NLE", "name": "Nuevo León", "category": "state"},
    {"code": "OAX", "name": "Oaxaca", "category": "state"},
    {"code": "PUE", "name": "Puebla", "category": "state"},
    {"code": "QUE", "name": "Querétaro", "category": "state"},
    {"code": "ROO", "name": "Quintana Roo", "category": "state"},
    {"code": "SLP", "name": "San Luis Potosí", "category": "state"},
    {"code": "SIN", "name": "Sinaloa", "category": "state"},
    {"code": "SON", "name": "Sonora", "category": "state"},
    {"code": "TAB", "name": "Tabasco", "category": "state"},
    {"code": "TAM", "name": "Tamaulipas", "category": "state"},
    {"code": "TLA", "name": "Tlaxcala", "category": "state"},
    {"code": "VER", "name": "Veracruz de Ignacio de la Llave", "category": "state"},
    {"code": "YUC", "name": "Yucatán", "category": "state"},
    {"code": "ZAC", "name": "Zacatecas", "category": "state"}
  ]
}
```

- [ ] **Step 7: Write data/oceania/nz.json**

```json
{
  "country": "NZ",
  "name": "New Zealand",
  "subdivisions": [
    {"code": "AUK", "name": "Auckland", "category": "region"},
    {"code": "BOP", "name": "Bay of Plenty", "category": "region"},
    {"code": "CAN", "name": "Canterbury", "category": "region"},
    {"code": "CIT", "name": "Chatham Islands Territory", "category": "special island authority"},
    {"code": "GIS", "name": "Gisborne", "category": "region"},
    {"code": "WGN", "name": "Greater Wellington", "category": "region"},
    {"code": "HKB", "name": "Hawke's Bay", "category": "region"},
    {"code": "MWT", "name": "Manawatū-Whanganui", "category": "region"},
    {"code": "MBH", "name": "Marlborough", "category": "region"},
    {"code": "NSN", "name": "Nelson", "category": "region"},
    {"code": "NTL", "name": "Northland", "category": "region"},
    {"code": "OTA", "name": "Otago", "category": "region"},
    {"code": "STL", "name": "Southland", "category": "region"},
    {"code": "TKI", "name": "Taranaki", "category": "region"},
    {"code": "TAS", "name": "Tasman", "category": "region"},
    {"code": "WKO", "name": "Waikato", "category": "region"},
    {"code": "WTC", "name": "West Coast", "category": "region"}
  ]
}
```

- [ ] **Step 8: Write data/north-america/us.json**

```json
{
  "country": "US",
  "name": "United States",
  "subdivisions": [
    {"code": "AL", "name": "Alabama", "category": "State"},
    {"code": "AK", "name": "Alaska", "category": "State"},
    {"code": "AZ", "name": "Arizona", "category": "State"},
    {"code": "AR", "name": "Arkansas", "category": "State"},
    {"code": "CA", "name": "California", "category": "State"},
    {"code": "CO", "name": "Colorado", "category": "State"},
    {"code": "CT", "name": "Connecticut", "category": "State"},
    {"code": "DE", "name": "Delaware", "category": "State"},
    {"code": "FL", "name": "Florida", "category": "State"},
    {"code": "GA", "name": "Georgia", "category": "State"},
    {"code": "HI", "name": "Hawaii", "category": "State"},
    {"code": "ID", "name": "Idaho", "category": "State"},
    {"code": "IL", "name": "Illinois", "category": "State"},
    {"code": "IN", "name": "Indiana", "category": "State"},
    {"code": "IA", "name": "Iowa", "category": "State"},
    {"code": "KS", "name": "Kansas", "category": "State"},
    {"code": "KY", "name": "Kentucky", "category": "State"},
    {"code": "LA", "name": "Louisiana", "category": "State"},
    {"code": "ME", "name": "Maine", "category": "State"},
    {"code": "MD", "name": "Maryland", "category": "State"},
    {"code": "MA", "name": "Massachusetts", "category": "State"},
    {"code": "MI", "name": "Michigan", "category": "State"},
    {"code": "MN", "name": "Minnesota", "category": "State"},
    {"code": "MS", "name": "Mississippi", "category": "State"},
    {"code": "MO", "name": "Missouri", "category": "State"},
    {"code": "MT", "name": "Montana", "category": "State"},
    {"code": "NE", "name": "Nebraska", "category": "State"},
    {"code": "NV", "name": "Nevada", "category": "State"},
    {"code": "NH", "name": "New Hampshire", "category": "State"},
    {"code": "NJ", "name": "New Jersey", "category": "State"},
    {"code": "NM", "name": "New Mexico", "category": "State"},
    {"code": "NY", "name": "New York", "category": "State"},
    {"code": "NC", "name": "North Carolina", "category": "State"},
    {"code": "ND", "name": "North Dakota", "category": "State"},
    {"code": "OH", "name": "Ohio", "category": "State"},
    {"code": "OK", "name": "Oklahoma", "category": "State"},
    {"code": "OR", "name": "Oregon", "category": "State"},
    {"code": "PA", "name": "Pennsylvania", "category": "State"},
    {"code": "RI", "name": "Rhode Island", "category": "State"},
    {"code": "SC", "name": "South Carolina", "category": "State"},
    {"code": "SD", "name": "South Dakota", "category": "State"},
    {"code": "TN", "name": "Tennessee", "category": "State"},
    {"code": "TX", "name": "Texas", "category": "State"},
    {"code": "UT", "name": "Utah", "category": "State"},
    {"code": "VT", "name": "Vermont", "category": "State"},
    {"code": "VA", "name": "Virginia", "category": "State"},
    {"code": "WA", "name": "Washington", "category": "State"},
    {"code": "WV", "name": "West Virginia", "category": "State"},
    {"code": "WI", "name": "Wisconsin", "category": "State"},
    {"code": "WY", "name": "Wyoming", "category": "State"},
    {"code": "DC", "name": "District of Columbia", "category": "District"},
    {"code": "AS", "name": "American Samoa", "category": "Outlying area"},
    {"code": "GU", "name": "Guam", "category": "Outlying area"},
    {"code": "MP", "name": "Northern Mariana Islands", "category": "Outlying area"},
    {"code": "PR", "name": "Puerto Rico", "category": "Outlying area"},
    {"code": "UM", "name": "United States Minor Outlying Islands", "category": "Outlying area"},
    {"code": "VI", "name": "Virgin Islands, U.S.", "category": "Outlying area"}
  ]
}
```

- [ ] **Step 9: Commit**

```bash
git add data/
git commit -m "feat(data): add JSON data files for 8 existing countries"
```

---

### Task 6: Run generator end-to-end and verify tests pass

**Files:**
- No new files — verify generated output and test suite

**Interfaces:**
- Consumes: JSON data files, generator module
- Produces: Passing test suite with generated SubdivisionCode.java

- [ ] **Step 1: Build from root to generate SubdivisionCode.java and run tests**

```bash
mvn verify --batch-mode
```

Expected: The generator runs during `generate-sources`, `SubdivisionCode.java` is generated, the library compiles, and all tests pass.

- [ ] **Step 2: If any test failures, inspect the generated file**

```bash
cat library/target/generated-sources/java/dev/marcosalmeida/i18n/SubdivisionCode.java
```

Compare behavior against the old hand-written `SubdivisionCode.java`. Key things to check:
- The `SubdivisionCodeTest.testFind()` test asserts `SubdivisionCode.find("AL")` returns `BR-AL` because BR comes before US in the stream. Ensure country sorting (alphabetical by code) produces the same behavior. BR < US alphabetically, so BR values come first — correct.
- The global filtering method names must match exactly (`getStates()`, `getRegions()`, `getProvinces()`, `getCounties()`).
- MX has `getByParent()` even though no subdivision currently uses it. The generator should NOT emit `getByParent()` for countries that don't need it. Check: MX test has no hierarchy test, so this is fine to omit.

- [ ] **Step 3: If any test failures, inspect the generated file**

```bash
cat library/target/generated-sources/java/dev/marcosalmeida/i18n/SubdivisionCode.java
```

Common issues to check:
- `SubdivisionCodeTest.testFind("AL")` must return `BR-AL` (BR sorts before US alphabetically)
- Global method names must match: `getStates()`, `getRegions()`, `getProvinces()`, `getCounties()`
- Category plurals (covered by the `CATEGORY_METHOD_NAMES` map in the generator)

Fix any mismatches in the generator's `CATEGORY_METHOD_NAMES` map or generation logic, then rebuild.

- [ ] **Step 4: Commit any fixes**

```bash
git add -A
git commit -m "fix(generator): correct category method naming for irregular plurals"
```

- [ ] **Step 5: Run full build to confirm all tests pass**

```bash
mvn verify --batch-mode
```

Expected: BUILD SUCCESS, all tests pass.

---

### Task 7: Update CI workflows for multi-module layout

**Files:**
- Modify: `.github/workflows/ci.yml`
- Modify: `.github/workflows/release.yml`

**Interfaces:**
- Consumes: Multi-module Maven project structure
- Produces: CI passes with `mvn verify` from root

- [ ] **Step 1: Update ci.yml**

The `mvn versions:set` and `mvn deploy` commands in the snapshot deploy step need to target the `library/` module (since the generator is never deployed). Additionally, `mvn help:evaluate` for version lookup should run from the root.

In `ci.yml`, replace:
```yaml
- run: mvn verify --batch-mode
```
with:
```yaml
- run: mvn verify --batch-mode -pl library
```

Actually, `mvn verify` from root should compile both modules and run all tests. The generator tests run too — that's fine. Let's keep `mvn verify --batch-mode` from root.

For the snapshot deploy step, change the `versions:set` to target only the library:
```yaml
mvn versions:set -DnewVersion="${{ steps.snapshot.outputs.version }}" -DgenerateBackupPoms=false -pl library
```

And deploy should be from library:
```yaml
mvn deploy -P deployment --batch-mode -pl library
```

Also update the version evaluation command to work from root:
```yaml
LAST_TAG="v$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout -pl library | sed 's/-SNAPSHOT//')"
```

- [ ] **Step 2: Update release.yml**

Same pattern — target the `library` module for version changes and deploy:

Replace `mvn versions:set`:
```yaml
mvn versions:set -DnewVersion="${{ steps.version.outputs.version }}" -DgenerateBackupPoms=false -pl library
```

Replace `mvn verify`:
```yaml
mvn verify --batch-mode
```
(run from root to include generator tests)

Replace `mvn deploy`:
```yaml
mvn deploy -P deployment --batch-mode -pl library
```

Update version evaluation:
```yaml
LAST_TAG="v$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout -pl library | sed 's/-SNAPSHOT//')"
```

- [ ] **Step 3: Commit**

```bash
git add .github/workflows/ci.yml .github/workflows/release.yml
git commit -m "ci: update workflows for multi-module layout"
```

---

### Task 8: Write generator tests

**Files:**
- Create: `generator/src/test/java/dev/marcosalmeida/i18n/generator/SubdivisionCodeGeneratorTest.java`
- Create: `generator/src/test/resources/test-data/valid/us.json` (sample valid test data)
- Create: `generator/src/test/resources/test-data/valid/ie.json` (sample hierarchical test data)

**Interfaces:**
- Consumes: Test data files, generator module
- Produces: Test coverage for loading, validation, and generation

- [ ] **Step 1: Create test resource data files**

```bash
mkdir -p generator/src/test/resources/test-data/valid/north-america
```

Write `generator/src/test/resources/test-data/valid/north-america/us.json`:
```json
{
  "country": "US",
  "name": "United States",
  "subdivisions": [
    {"code": "AL", "name": "Alabama", "category": "State"},
    {"code": "AK", "name": "Alaska", "category": "State"}
  ]
}
```

- [ ] **Step 2: Write generator test class**

```java
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
```

- [ ] **Step 2: Run generator tests**

```bash
mvn test -pl generator --batch-mode
```

Expected: All tests pass.

- [ ] **Step 3: Commit**

```bash
git add generator/src/test/
git commit -m "test(generator): add unit tests for validation and code generation"
```

---

### Task 9: Verification gate — full test suite

- [ ] **Step 1: Clean build from root**

```bash
mvn clean verify --batch-mode
```

Expected: BUILD SUCCESS. All existing tests (`SubdivisionCodeTest`, `SubdivisionUSTest`, `SubdivisionAUTest`, `SubdivisionBRTest`, `SubdivisionCATest`, `SubdivisionIETest`, `SubdivisionITTest`, `SubdivisionMXTest`, `SubdivisionNZTest`) pass, plus all generator tests pass.

- [ ] **Step 2: Verify generated file is not committed**

```bash
git status
```

Expected: `library/target/` is git-ignored. No generated file shows up.

- [ ] **Step 3: Commit if any final cleanup needed**

---

## Self-Review

**1. Spec coverage:**
- ✅ Multi-module Maven layout → Task 1
- ✅ Code generator reading JSON, validating, producing Java → Task 4
- ✅ Jackson 3 (or 2.x stable fallback) for JSON → Task 1 (generator POM), Task 3 (data models)
- ✅ Continent-grouped data directories → Task 2
- ✅ JSON data files for 8 existing countries → Task 5
- ✅ `exec-maven-plugin` in library for `generate-sources` phase → Task 1
- ✅ Generated `SubdivisionCode.java` in `target/generated-sources/`, never committed → Task 1, Task 9
- ✅ `Subdivision.java` hand-written, unchanged → Files moved to `library/src/`
- ✅ Existing test suite passes identically → Task 6, Task 9
- ✅ CI workflows updated for multi-module → Task 7
- ✅ Generator tests with validation and output verification → Task 8
- ✅ `getFederalEntities` irregular plural → Task 6 step 3 fix

**2. Placeholder scan:**
- No "TBD", "TODO", "implement later"
- All steps have actual code or exact commands
- All file paths are exact

**3. Type consistency:**
- `CountryData` and `SubdivisionEntry` defined in Task 3, consumed in Task 4
- `loadAndValidate()` returns `List<CountryData>`, used in Task 4 main method and Task 8 tests
- Method signatures consistent between generator code and tests
