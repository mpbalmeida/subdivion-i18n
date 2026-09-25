# ISO-3166-2 Subdivision Library

A Java library that implements the [ISO-3166-2](https://en.wikipedia.org/wiki/ISO_3166-2) subdivision codes ([ISO official page](https://www.iso.org/iso-3166-country-codes.html#2012_iso3166-2)), inspired by and integrated with the `nv-i18n` library.

## Usage

### Direct access to subdivisions

You can access subdivision constants directly from the `SubdivisionCode` class.

```java
import dev.marcosalmeida.i18n.SubdivisionCode;

// Access a specific US subdivision
Subdivision alabama = SubdivisionCode.US.AL;
System.out.println(alabama.getCode());     // "US-AL"

// Access a specific Australia subdivision
Subdivision nsw = SubdivisionCode.AU.NSW;
System.out.println(nsw.getCode());         // "AU-NSW"
System.out.println(nsw.getName());         // "New South Wales"

// Access a specific Brazil subdivision
Subdivision saoPaulo = SubdivisionCode.BR.SP;
System.out.println(saoPaulo.getCode());       // "BR-SP"
System.out.println(saoPaulo.getName());       // "São Paulo"

// Access a specific Mexico subdivision
Subdivision mexicoCity = SubdivisionCode.MX.CMX;
System.out.println(mexicoCity.getCode());     // "MX-CMX"
System.out.println(mexicoCity.getName());     // "Ciudad de México"

// Access a specific New Zealand subdivision
Subdivision auckland = SubdivisionCode.NZ.AUK;
System.out.println(auckland.getCode());       // "NZ-AUK"
System.out.println(auckland.getName());       // "Auckland"

// Access a specific Canada subdivision
Subdivision ontario = SubdivisionCode.CA.ON;
System.out.println(ontario.getCode());        // "CA-ON"
System.out.println(ontario.getName());        // "Ontario"

// Access a specific Ireland subdivision
Subdivision dublin = SubdivisionCode.IE.D;
System.out.println(dublin.getCode());         // "IE-D"
System.out.println(dublin.getName());         // "Dublin"

// Access a specific Italy subdivision
Subdivision lombardy = SubdivisionCode.IT.IT_25;
System.out.println(lombardy.getCode());       // "IT-25"
System.out.println(lombardy.getName());       // "Lombardia"
```

### Accessing via CountryCode

The library integrates with `com.neovisionaries.i18n.CountryCode`.

```java
import com.neovisionaries.i18n.CountryCode;
import dev.marcosalmeida.i18n.SubdivisionCode;
import dev.marcosalmeida.i18n.Subdivision;

// Get all subdivisions for a country
Subdivision[] subdivisions = SubdivisionCode.getSubdivisions(CountryCode.US);
if (subdivisions != null) {
    for (Subdivision s : subdivisions) {
        System.out.println(s.getCode() + ": " + s.getName());
    }
}
```

### Looking up subdivisions

You can look up subdivisions by their full ISO-3166-2 code, their subdivision part, or their name.

```java
import dev.marcosalmeida.i18n.SubdivisionCode;
import dev.marcosalmeida.i18n.Subdivision;
import java.util.Optional;

// Look up across all countries by full code
Subdivision al = SubdivisionCode.fromCode("US-AL");

// Look up within a specific country
Subdivision sp = SubdivisionCode.BR.fromCode("BR-SP");

// Look up by subdivision part (e.g., "AL")
Subdivision alShort = SubdivisionCode.US.fromCode("AL");

// Look up by name
Optional<Subdivision> california = SubdivisionCode.US.fromName("California");

// Unified lookup (tries code then name)
Optional<Subdivision> ny = SubdivisionCode.US.find("New York");
Optional<Subdivision> caPart = SubdivisionCode.US.find("CA");
```

### Getting the subdivision code part

You can extract the subdivision part of the ISO-3166-2 code (e.g., "AL" from "US-AL").

```java
Subdivision alabama = SubdivisionCode.US.AL;
System.out.println(alabama.getSubdivisionCode()); // "AL"
```

### Parent-Child Relationships

The library supports hierarchical relationships between subdivisions (e.g., Irish counties belonging to provinces).

```java
import dev.marcosalmeida.i18n.SubdivisionCode;
import dev.marcosalmeida.i18n.Subdivision;
import java.util.Optional;

Subdivision dublin = SubdivisionCode.IE.D;
Optional<Subdivision> parent = dublin.getParent();
if (parent.isPresent()) {
    System.out.println("Parent: " + parent.get().getName()); // "Leinster"
}
```

### Filtering subdivisions

You can filter subdivisions by category or parent.

```java
// Get all provinces in Ireland
Subdivision[] provinces = SubdivisionCode.IE.getProvinces();

// Get all counties in a specific province
Subdivision leinster = SubdivisionCode.IE.L;
Subdivision[] countiesInLeinster = SubdivisionCode.IE.getByParent(leinster);

// Global filtering across all countries
Subdivision[] allStates = SubdivisionCode.getStates();
```

### Core Interface

All subdivision enums implement the `Subdivision` interface:

```java
public interface Subdivision {
    String getCode();            // Returns the full ISO-3166-2 code (e.g., "US-AL")
    String getSubdivisionCode(); // Returns the subdivision part (e.g., "AL")
    String getName();            // Returns the local/English name
    String getCategory();        // Returns the category (e.g., "State", "District")
}
```

## Features

- **Public Access**: Uses nested enums to provide direct access to subdivision constants while keeping them grouped by country.
- **`nv-i18n` Integration**: Easily find subdivisions using established `CountryCode` constants.
- **Shortened Keys**: Enum constants use the subdivision part of the code (e.g., `PR`) for a cleaner API.

## Code Generation

`SubdivisionCode` is not hand-written. It is generated at build time from the JSON data files
under [`data/`](data) by the `i18n-generator` module, which is also published to Maven Central
as [`dev.marcosalmeida:i18n-generator`](https://central.sonatype.com/artifact/dev.marcosalmeida/i18n-generator).

The `library` module runs the generator automatically during `generate-sources`, so building
the project regenerates `SubdivisionCode.java` into `target/generated-sources/java`. The
generated file is never committed.

### Data file format

Each country is one JSON file at `data/<continent>/<cc>.json`:

```json
{
  "country": "US",
  "name": "United States",
  "wikipedia": "https://en.wikipedia.org/wiki/ISO_3166-2:US",
  "dateAdded": "2026-01-01",
  "lastUpdated": "2026-01-01",
  "subdivisions": [
    {"code": "AL", "name": "Alabama", "category": "state"},
    {"code": "AK", "name": "Alaska", "category": "state"}
  ]
}
```

| Field | Required | Description |
|---|---|---|
| `country` | yes | ISO 3166-1 alpha-2 code; becomes the generated enum's name |
| `name` | yes | Human-readable country name, used in the generated Javadoc |
| `wikipedia` | no | URL to the country's ISO 3166-2 page |
| `dateAdded` | no | ISO-8601 date the country's data was first added |
| `lastUpdated` | no | ISO-8601 date the data was last revised |
| `subdivisions[].code` | yes | Subdivision part of the code, without the country prefix |
| `subdivisions[].name` | yes | Subdivision name as published in ISO 3166-2 |
| `subdivisions[].category` | yes | Lowercase for generic types (`state`), Title Case for proper nouns (`Land`) |
| `subdivisions[].parent` | no | Code of another subdivision in the same file, for hierarchical countries |

Countries are emitted alphabetically by code, and subdivisions keep the order they appear in the
file — so list them alphabetically by code. Every category in the data produces a matching
filter method on the generated enum; adding a new category therefore adds a new public method.

The generator validates each file before writing any output and fails the build on a missing
required field, a lowercase country code, a duplicate country or subdivision code, or a
`parent` that does not resolve within the same file.

### Running the generator yourself

To generate subdivisions for your own data — for example a fork covering countries this library
does not ship — depend on the generator and run it over a data directory:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.6.3</version>
    <executions>
        <execution>
            <id>generate-subdivision-code</id>
            <phase>generate-sources</phase>
            <goals><goal>java</goal></goals>
            <configuration>
                <mainClass>dev.marcosalmeida.i18n.generator.SubdivisionCodeGenerator</mainClass>
                <arguments>
                    <argument>${project.basedir}/data</argument>
                    <argument>${project.build.directory}/generated-sources/java</argument>
                </arguments>
                <includeProjectDependencies>true</includeProjectDependencies>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Add the output directory as a source root with `build-helper-maven-plugin` (as `library/pom.xml`
does), or run it directly from the command line:

```bash
java -cp i18n-generator.jar \
  dev.marcosalmeida.i18n.generator.SubdivisionCodeGenerator \
  ./data ./target/generated-sources/java
```

Either way the result is written to
`<output-dir>/dev/marcosalmeida/i18n/SubdivisionCode.java`.

## Deployment

Both `i18n` and `i18n-generator` are published to Maven Central and are always released at the
same version, because `i18n` declares `i18n-generator` as a dependency at `${project.version}`.
Releasing one without the other leaves that dependency unresolvable.

Artifacts are signed with GPG as required by Sonatype Central. To deploy, use:

```bash
mvn deploy -P deployment -pl library,generator
```

Ensure your GPG key is configured correctly in your environment.

Publishing plugins are confined to the `deployment` profile so that an ordinary `mvn verify`
never has to resolve them.
