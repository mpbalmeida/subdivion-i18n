# Data-Driven Code Generation for ISO-3166-2 Subdivisions

**Date:** 2026-07-20
**Status:** Approved

## Overview

Refactor `SubdivisionCode.java` (currently 1,266 lines for 8 countries, all hand-written) into a data-driven architecture where each country's subdivisions live in a JSON data file, and `SubdivisionCode.java` is generated at build time. This enables scaling to 200+ countries with ISO-3166-2 codes without the maintenance burden of a monolithic hand-edited file, while keeping the existing public API (`SubdivisionCode.US.AL`, `SubdivisionCode.getSubdivisions()`, etc.) fully unchanged.

## Motivation

- **Scale**: ISO-3166-2 defines 5,046 subdivision codes across ~200 countries. Hand-writing and maintaining all of them in one file is impractical.
- **Contributor experience**: Adding a country becomes creating one JSON data file (declarative, easy to review) plus a test file.
- **No breaking changes**: The public API is identical before and after migration. Consumers see no difference.

## Data Format

Each country is a JSON file under `data/`, grouped by continent subdirectory:

```
data/
├── europe/        (ie.json, it.json, de.json, ...)
├── north-america/ (us.json, ca.json, mx.json)
├── south-america/ (br.json, ar.json, ...)
├── asia/          (jp.json, cn.json, ...)
├── africa/        (...)
└── oceania/       (au.json, nz.json, ...)
```

Each file follows this schema:

```json
{
  "country": "US",
  "name": "United States",
  "subdivisions": [
    {"code": "AL", "name": "Alabama", "category": "state"},
    {"code": "AK", "name": "Alaska", "category": "state"}
  ]
}
```

Hierarchical subdivisions (e.g., Ireland where counties belong to provinces) use an optional `parent` field:

```json
{
  "country": "IE",
  "name": "Ireland",
  "subdivisions": [
    {"code": "C", "name": "Connacht", "category": "province"},
    {"code": "G", "name": "Galway", "category": "county", "parent": "C"}
  ]
}
```

### Field reference

| Field | Required | Description |
|---|---|---|
| `country` | Yes | ISO-3166-1 alpha-2 code, used as the enum constant name |
| `name` | Yes | Human-readable country name for Javadoc |
| `subdivisions[].code` | Yes | Subdivision part of the code (e.g., `"AL"`) |
| `subdivisions[].name` | Yes | Name (e.g., `"Alabama"`) |
| `subdivisions[].category` | Yes | Lowercase for generic types (`"state"`), Title Case for proper types (`"Province"`) |
| `subdivisions[].parent` | No | Code of the parent subdivision for hierarchical relationships |

### Ordering

- Countries appear in the generated file in alphabetical order by alpha-2 code.
- Subdivisions within each country appear in the order defined in the JSON file (alphabetical by code in practice).

## Code Generator

A Java class `SubdivisionCodeGenerator` in a separate `generator/` Maven module:

- Recursively reads all `*.json` files under the `data/` directory
- Validates: duplicate codes, missing required fields, invalid parent references, JSON syntax errors
- Emits `SubdivisionCode.java` — the full file including nested enums, the `SubdivisionCode` wrapper class, `getSubdivisions()` switch, `fromCode()`, `fromName()`, `find()`, `allValues()`, and all static lookup maps
- Validation errors fail the build with a clear message referencing the file and specific issue

### Validation rules

| Error | Message format |
|---|---|
| Missing required field | `"us.json: subdivision[3] missing 'name'"` |
| Duplicate code | `"ie.json: duplicate code 'C'"` |
| Invalid parent ref | `"ie.json: parent 'X' not found for subdivision 'G'"` |
| JSON parse error | Jackson exception with file path and line number |

### Dependencies

- **Jackson 3** (latest stable) for JSON parsing. Scoped to the `generator/` module only — not shipped in the library JAR.

## Build Integration

The project becomes a **multi-module Maven layout** to solve the compilation ordering: the generator must compile before it can generate code for the library.

```
i18n/
├── pom.xml                    (parent POM, modules: generator, library)
├── data/                      (JSON files grouped by continent)
│   ├── north-america/
│   │   ├── us.json
│   │   ├── ca.json
│   │   └── mx.json
│   ├── europe/
│   │   ├── ie.json
│   │   └── ...
│   └── ... (~200 countries)
├── generator/
│   ├── pom.xml                (depends on Jackson 3)
│   └── src/main/java/dev/marcosalmeida/i18n/generator/SubdivisionCodeGenerator.java
└── library/
    ├── pom.xml                (exec-maven-plugin to run generator during generate-sources)
    ├── src/main/java/dev/marcosalmeida/i18n/
    │   └── Subdivision.java   (hand-written interface, unchanged)
    └── target/generated-sources/.../
        └── SubdivisionCode.java  (generated, never committed)
```

### Build flow

1. `generator/` compiles → produces `generator.jar`
2. `library/` → `generate-sources` phase → `exec-maven-plugin` runs generator JAR → reads all `*.json` recursively under `data/` → writes `SubdivisionCode.java` to `target/generated-sources/`
3. `library/` compiles with generated code included
4. Tests run, verification passes, JAR produced

### Preserved configuration

- `groupId`, `artifactId`, and `version` remain on the `library/` module — Maven coordinates do not change
- CI workflows (`ci.yml`, `release.yml`) and JReleaser config (`jreleaser.yml`) continue to work with minor path adjustments for the new module layout
- `pom.xml` dependencies (nv-i18n, JUnit 5) stay on the `library/` module

## Testing

### Generator tests (`generator/src/test/`)

- Verify correct Java output for sample JSON data files covering: simple flat subdivisions, hierarchical subdivisions with parents, edge cases (single subdivision, special characters in names)
- Verify code output for all 8 currently-implemented countries matches the existing `SubdivisionCode.java` behavior exactly
- Verify validation errors are caught with correct messages: missing fields, duplicates, bad parent refs, malformed JSON

### Country-specific tests (`library/src/test/`)

- `SubdivisionUSTest.java`, `SubdivisionIETest.java`, etc. — unchanged from current tests
- New countries get corresponding test classes verifying: all codes resolve correctly, names match expected values, categories are correct, parent-child relationships are valid

### Cross-cutting tests (`library/src/test/`)

- `SubdivisionCodeTest.java` — expanded to cover every supported country: `getSubdivisions()` returns correct count, `fromCode()` with full and short codes, `fromName()` case-insensitive, `find()` unified lookup, `null` for unsupported countries

### Migration test gate

- The existing test suite (`mvn verify`) must pass identically on the generated code as it does on the current hand-written code before migration is considered complete.

## Migration Phases

### Phase 1 — Foundation (this milestone)

- Set up multi-module Maven layout
- Build `SubdivisionCodeGenerator` with validation
- Create continent subdirectories and JSON data files for the 8 existing countries, porting data from the current `SubdivisionCode.java`
- Generate `SubdivisionCode.java`, verify byte-for-byte equivalent output
- All existing tests pass with generated code

### Phase 2 — Europe (~50 countries)

- Add JSON data files for all European countries with ISO-3166-2 subdivisions
- Add test files for each new country
- Tag: `0.7.0` release

### Phase 3 — Asia (~50 countries)

- Japan, China, India, South Korea, Southeast Asia, Middle East
- Tag: `0.8.0` release

### Phase 4 — Africa & Oceania (~60 countries)

- Remaining African and Oceanian countries
- Tag: `0.9.0` release

### Phase 5 — Americas (~10 new countries)

- Argentina, Chile, Colombia, Peru, remaining Central/South American and Caribbean countries (US, CA, MX, BR already covered)
- Tag: `0.10.0` release

## Non-Goals

- Code generation for test files (tests are hand-written to allow per-country assertions on names, categories, and relationships)
- Auto-scraping ISO data from Wikipedia or ISO.org (data entry is manual via JSON files, ensuring accuracy and review)
- Changing the `Subdivision` interface or public API in any way
