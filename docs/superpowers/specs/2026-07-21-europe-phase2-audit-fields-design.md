# Phase 2 Europe — Audit Fields & Jackson 3 Migration

**Date:** 2026-07-21
**Status:** Draft

## Overview

Three changes bundled into one milestone:

1. **Audit metadata fields** — add `wikipedia`, `dateAdded`, and `lastUpdated` at the country level in JSON data files, surfaced in generated Javadoc and as static methods on each country enum
2. **Jackson 3 migration** — upgrade the generator module from Jackson 2 (`com.fasterxml.jackson.core:jackson-databind:2.18.3`) to Jackson 3 (`tools.jackson.core:jackson-databind:3.2.1`)
3. **Phase 2 Europe** — add JSON data files and tests for all European countries with ISO-3166-2 subdivisions (~50 countries)

The Jackson 3 migration is a prerequisite — it must be done before any new country data is added so all files use the newer JSON parsing stack.

## Motivation

- **Audit fields**: When a country changes its subdivisions (common in ISO-3166-2 maintenance), `dateAdded` and `lastUpdated` make it obvious which version of the data a codebase is using and when it needs updating. `wikipedia` provides a direct link to the authoritative ISO-3166-2 data source for verification.
- **Jackson 3**: Currently on 2.18.3. Jackson 3 is the active line with ongoing development (latest: 3.2.1 as of July 2026). Migrating now avoids accumulating technical debt.
- **Europe**: First major expansion from the existing 8 countries, establishing the pattern for all future phases.

## Audit Fields: Data Format

Each JSON file gains three new **optional** fields at the country level:

```json
{
  "country": "IE",
  "name": "Ireland",
  "wikipedia": "https://en.wikipedia.org/wiki/ISO_3166-2:IE",
  "dateAdded": "2026-07-21",
  "lastUpdated": "2026-07-21",
  "subdivisions": [
    {"code": "C", "name": "Connaught", "category": "province"}
  ]
}
```

### Field reference

| Field | Required | Format | Description |
|---|---|---|---|
| `wikipedia` | No | URL string | Link to the ISO-3166-2 Wikipedia page for this country |
| `dateAdded` | No | ISO-8601 date (`YYYY-MM-DD`) | Date the country was first added to the library |
| `lastUpdated` | No | ISO-8601 date (`YYYY-MM-DD`) | Date the subdivision data was last modified |

All three are optional. When omitted, the corresponding Javadoc line and static method are not generated for that country. The existing 8 countries may be backfilled with audit fields later.

### Validation

No format validation is applied to `wikipedia` or the date strings — they are treated as plain strings. The generator simply passes them through. This keeps the generator simple and avoids brittle URL/date parsing.

## Audit Fields: Generated Code

For each country enum, when audit fields are present, the generator emits:

```java
/**
 * ISO-3166-2 subdivisions for Ireland.
 *
 * <p>Wikipedia: <a href="https://en.wikipedia.org/wiki/ISO_3166-2:IE">ISO 3166-2:IE</a></p>
 * <p>Date added: 2026-07-21</p>
 * <p>Last updated: 2026-07-21</p>
 */
public enum IE implements Subdivision {
    // ... constants unchanged ...

    /** Returns the Wikipedia page for this country's ISO-3166-2 subdivisions. */
    public static String wikipedia() { return "https://en.wikipedia.org/wiki/ISO_3166-2:IE"; }

    /** Returns the date this country was added to the library (ISO-8601). */
    public static String dateAdded() { return "2026-07-21"; }

    /** Returns the date this country's subdivision data was last updated (ISO-8601). */
    public static String lastUpdated() { return "2026-07-21"; }
}
```

Key design decisions:

- **Static methods, not instance methods** — these are country-level metadata, not per-subdivision. Instance methods on the `Subdivision` interface would be misleading (each subdivision constant would redundantly return the same country-level values).
- **No `Subdivision` interface changes** — zero breaking changes to the public API.
- **Return type is `String`** — dates are returned as strings, not `LocalDate`, keeping the library free of Java time dependencies. Consumers can parse if they choose.
- **When fields are absent, methods are absent** — no `Optional<String>` or `null` returns. Callers either have the method or they don't, which is self-documenting.

## Jackson 3 Migration

### What changes

| Item | Jackson 2 | Jackson 3 |
|---|---|---|
| Maven groupId | `com.fasterxml.jackson.core` | `tools.jackson.core` |
| Maven artifactId | `jackson-databind` | `jackson-databind` (unchanged) |
| Version | `2.18.3` | `3.2.1` |
| `@JsonProperty` annotation | `com.fasterxml.jackson.annotation.JsonProperty` | **unchanged** (`jackson-annotations` stays at `com.fasterxml.jackson.core`) |
| `ObjectMapper` import | `com.fasterxml.jackson.databind.ObjectMapper` | `tools.jackson.databind.ObjectMapper` |
| Java baseline | Java 8 | Java 17 (project already uses Java 21) |

### Files affected

| File | Change |
|---|---|
| `generator/pom.xml` | Replace `com.fasterxml.jackson.core:jackson-databind:2.18.3` with `tools.jackson.core:jackson-databind:3.2.1` |
| `SubdivisionCodeGenerator.java` | Update `import com.fasterxml.jackson.databind.ObjectMapper` → `import tools.jackson.databind.ObjectMapper` |
| `SubdivisionEntry.java` | No change — `@JsonProperty` stays at `com.fasterxml.jackson.annotation` |

### Migration note

`jackson-annotations` (which provides `@JsonProperty`) remains at the old `com.fasterxml.jackson.core` groupId even in Jackson 3. The generator's `SubdivisionEntry.java` uses `@JsonProperty(required = false)` — this import does not change. Only the `ObjectMapper` import in `SubdivisionCodeGenerator.java` needs updating.

## Phase 2 Europe: Data Files

~50 new JSON files under `data/europe/`, one per ISO-3166-2 country:

```
data/europe/
├── ie.json  (existing)
├── it.json  (existing)
├── de.json  (new — Germany, 16 Länder)
├── fr.json  (new — France, 18 régions + dependencies)
├── es.json  (new — Spain, 17 comunidades + 2 ciudades autónomas)
├── gb.json  (new — United Kingdom, 4 constituent countries + subdivisions)
├── nl.json  (new — Netherlands, 12 provinces + 3 special municipalities)
├── be.json  (new — Belgium, 3 regions + 10 provinces)
├── ch.json  (new — Switzerland, 26 cantons)
├── at.json  (new — Austria, 9 Länder)
├── pl.json  (new — Poland, 16 voivodeships)
├── se.json  (new — Sweden, 21 counties)
├── pt.json  (new — Portugal, 18 districts + 2 autonomous regions)
├── dk.json  (new — Denmark, 5 regions)
├── fi.json  (new — Finland, 19 regions)
├── no.json  (new — Norway, 11 counties + 2 integral overseas areas)
├── gr.json  (new — Greece, 13 administrative regions + 1 autonomous entity)
├── cz.json  (new — Czech Republic, 14 regions + capital city)
├── hu.json  (new — Hungary, 19 counties + capital city)
├── ro.json  (new — Romania, 41 counties + municipality)
├── bg.json  (new — Bulgaria, 28 regions)
├── sk.json  (new — Slovakia, 8 regions)
├── hr.json  (new — Croatia, 21 counties + city)
├── si.json  (new — Slovenia, 12 statistical regions)
├── lt.json  (new — Lithuania, 10 counties)
├── lv.json  (new — Latvia, 36 municipalities + 7 state cities)
├── ee.json  (new — Estonia, 15 counties)
├── ua.json  (new — Ukraine, 24 regions + 2 cities + republic)
├── rs.json  (new — Serbia, 29 districts + 2 autonomous provinces + city)
├── is.json  (new — Iceland, 8 regions)
├── lu.json  (new — Luxembourg, 12 cantons)
├── mt.json  (new — Malta, 6 regions)
├── cy.json  (new — Cyprus, 6 districts)
├── al.json  (new — Albania, 12 counties)
├── mk.json  (new — North Macedonia, 8 statistical regions)
├── md.json  (new — Moldova, 32 districts + 3 municipalities + 2 territorial units)
├── me.json  (new — Montenegro, 25 municipalities)
├── ba.json  (new — Bosnia and Herzegovina, 2 entities + 1 district)
├── ad.json  (new — Andorra, 7 parishes)
├── mc.json  (new — Monaco, 1 unincorporated area)
├── li.json  (new — Liechtenstein, 11 communes)
└── sm.json  (new — San Marino, 9 municipalities)
```

Countries that have no ISO-3166-2 subdivisions (e.g., Vatican City) are excluded. The exact set will be confirmed from the ISO-3166-2 standard during implementation.

Each file includes the `wikipedia` and `dateAdded` fields from day one. `lastUpdated` starts equal to `dateAdded`.

## Testing

### Generator tests

- `SubdivisionCodeGeneratorTest` — new test cases:
  - Country with all three audit fields present → verify Javadoc and static methods in output
  - Country with no audit fields → verify neither Javadoc lines nor static methods appear
  - Country with partial audit fields (e.g., `wikipedia` only) → verify only that field appears

### Country-specific tests

Each new country gets a test class following the existing pattern (e.g., `SubdivisionDETest.java`):

- Verify all codes resolve correctly
- Verify names match expected values
- Verify categories are correct
- Verify parent-child relationships where applicable
- Verify `wikipedia()`, `dateAdded()`, and `lastUpdated()` static methods return non-null, non-empty strings
- Verify `getSubdivisions(CountryCode.DE)` returns correct count

### Cross-cutting tests

- `SubdivisionCodeTest` — expanded to cover all new European countries in `getSubdivisions()`, `fromCode()`, `find()`

## Non-Goals

- Modifying the `Subdivision` interface
- Per-subdivision audit fields (country-level only)
- Backfilling audit fields for the 8 existing countries (can be done later)
- Format validation for wikipedia URLs or date strings

## `add-market` Skill

A project-level Claude Code slash command (`/add-market`) that automates adding countries to the library. Supports two modes:

- **Single country**: `/add-market DE` — adds one country
- **Continent batch**: `/add-market Europe` — adds all countries in a continent (useful for bulk phases like Phase 2)

The skill lives at `.claude/commands/add-market.md`.

### Why a skill instead of purely manual authoring

ISO-3166-2 subdivision data lives on Wikipedia in a standardized tabular format. Having Claude fetch, parse, and author the JSON file is faster than manual transcription and less error-prone. The PR review gate ensures human oversight — the skill does the mechanical work, a human verifies the result.

### Mode A: Single country (`/add-market DE`)

```
/add-market DE
    │
    ▼
1. FETCH — WebFetch: https://en.wikipedia.org/wiki/ISO_3166-2:DE
    │
    ▼
2. ANALYZE — Extract from the page:
   - Country name
   - Continent (to determine data/ subdirectory)
   - Subdivision table: code, name, category, parent (if hierarchical)
    │
    ▼
3. CREATE — Write JSON file:
   - data/<continent>/<cc>.json with wikipedia, dateAdded, lastUpdated
   - Subdivisions ordered alphabetically by code
   - Use lowercase categories for generic types, Title Case for proper types
    │
    ▼
4. TEST — Create test class + update SubdivisionCodeTest
   - library/src/test/java/.../Subdivision<CC>Test.java
   - Follow existing test class patterns
    │
    ▼
5. VERIFY — Run: mvn verify --batch-mode
    │
    ├─ PASS → proceed to step 6
    └─ FAIL → analyze errors, fix JSON or test, repeat step 5
    │
    ▼
6. COMMIT — Create branch, commit, push, open PR
   - Branch: feat/add-<cc>-subdivisions
   - Commit: feat(data): add <Country> subdivisions
```

### Mode B: Continent batch (`/add-market Europe`)

The batch mode uses multi-agent orchestration: discover all countries, create a task plan, fan out sub-agents in parallel, then converge and verify.

```
/add-market Europe
    │
    ▼
1. DISCOVER — Map the batch to a concrete list of country codes:
   - Continent name ("Europe") → fetch Wikipedia ISO-3166-2 page, extract all
     country codes in the continent's table
   - Region name ("Southeast Asia") → same approach, filter by region
   - Explicit array ("DE,FR,ES") → parse directly, no fetch needed
   - Filter out: countries already in the library, countries with no subdivisions
    │
    ▼
2. PLAN — Create a task per country using TaskCreate:
   ┌─────────────────────────────────────────────┐
   │ Task 1: Add DE (Germany)        [pending]   │
   │ Task 2: Add FR (France)         [pending]   │
   │ Task 3: Add ES (Spain)          [pending]   │
   │ ...                                         │
   │ Task N: Add PL (Poland)         [pending]   │
   └─────────────────────────────────────────────┘
    │
    ▼
3. DISPATCH — Spin sub-agents in parallel batches:
   ┌──────────────────────────────────────────────┐
   │ Batch 1 (5 agents, parallel):                │
   │   agent-1: /add-market DE  (model: haiku)    │
   │   agent-2: /add-market FR  (model: haiku)    │
   │   agent-3: /add-market ES  (model: haiku)    │
   │   agent-4: /add-market NL  (model: haiku)    │
   │   agent-5: /add-market BE  (model: haiku)    │
   │                                              │
   │ Wait for all 5 → mvn verify → fix if needed  │
   │                                              │
   │ Batch 2 (5 agents, parallel):                │
   │   agent-6: /add-market PL  (model: haiku)    │
   │   ...                                        │
   └──────────────────────────────────────────────┘
    │
    ▼
4. CONVERGE — After all batches complete:
   - Run full mvn verify --batch-mode
   - Fix any failures (re-dispatch individual country agents as needed)
   - Loop until green
    │
    ▼
5. COMMIT — One branch, one commit, push, open PR
   - Branch: feat/add-<batch-name>-subdivisions
   - Commit: feat(data): add <N> <batch-name> subdivisions
```

**Model selection per sub-agent**:

| Task complexity | Model | Rationale |
|---|---|---|
| Simple flat subdivisions, single category | `haiku` | Cheap, fast — most countries fall here |
| Hierarchical subdivisions with parents | `sonnet` | Needs careful parent-child mapping |
| Complex multi-category, unusual structure | `sonnet` | More categories = more edges to get right |
| Unusual edge cases (numeric codes, special territories) | `opus` | Needs deeper reasoning |

The orchestrator (main agent) uses `sonnet` or `opus` to coordinate. Sub-agents default to `haiku` unless the country's Wikipedia page reveals complexity that warrants a bump.

**Batching strategy**:
- Batch size of 5 keeps the feedback loop tight — if one country has a validation error, it's caught within its batch
- Each batch is a checkpoint: `mvn verify` runs after every batch completes
- Batch size can be tuned: use 3 for complex countries (many subdivisions, hierarchy), 8 for simple ones
- The orchestrator tracks task status (pending → in_progress → completed) via TaskUpdate

### Key instructions within the skill

- **Continent mapping for single mode**: Determine which subdirectory the country belongs to (e.g., DE → `data/europe/`). Wikipedia infobox often includes the continent. For transcontinental countries (Russia, Turkey), place in the continent where the majority of subdivisions lie.
- **Country list discovery for batch mode**: Wikipedia's ISO-3166-2 main page has tables grouping countries by continent. Parse the country codes from the relevant continent's section.
- **Category conventions**: Match existing conventions — lowercase for generic categories (`"state"`, `"province"`, `"county"`), Title Case for proper-noun categories (`"Province"`, `"Land"`). Use judgment based on existing files.
- **Parent relationships**: If the Wikipedia table shows hierarchical subdivisions (e.g., Irish counties belong to provinces), use the `parent` field with the parent's code.
- **Numeric codes**: Some countries (e.g., Italy's regions) have numeric subdivision codes. The generator already handles this with the `needsPrefix` detection — no special handling needed.
- **Date fields**: `dateAdded` and `lastUpdated` both set to today's date.
- **Wikipedia link**: Format as `https://en.wikipedia.org/wiki/ISO_3166-2:<CC>`.
- **Retry loop**: If `mvn verify` fails, analyze the test output and generator validation errors, fix the JSON data or test assertions, and re-run. Do not proceed to PR until the build is green.
- **Test class pattern**: Match existing tests — verify all codes resolve, names match, categories are correct, parent relationships work, audit static methods return non-null strings.
- **Update SubdivisionCodeTest**: Each new country must be added to the cross-cutting `SubdivisionCodeTest` assertions (verify `getSubdivisions()` count, `fromCode()` resolution, etc.).

### What the skill does NOT do

- It does NOT auto-merge the PR — human review is the gate
- It does NOT modify the `Subdivision` interface
- It does NOT handle countries without Wikipedia ISO-3166-2 pages (those are done manually)
- It does NOT auto-detect continent for single-country mode — uses Wikipedia infobox data

### Future continent batch targets

Once Europe is shipped, the same skill supports:
- `/add-market Asia` — Phase 3
- `/add-market Africa` — Phase 4
- `/add-market South-America` — remaining Americas (Phase 5)
- `/add-market Oceania` — remaining Oceania (Phase 4)

## Implementation Order

1. **Jackson 3 migration** — update `generator/pom.xml` and imports; verify `mvn verify` passes
2. **Audit fields in the generator** — add fields to `CountryData.java`, update `SubdivisionCodeGenerator.java`, add generator tests
3. **`add-market` skill** — create `.claude/commands/add-market.md` with the workflow instructions
4. **Europe data files** — create ~50 JSON files with audit fields, add country test classes, expand `SubdivisionCodeTest` (each country added via the `add-market` skill)
5. **Verification** — full `mvn verify` with all ~58 countries passing
