# Add Market

Add one or more ISO-3166-2 countries to the i18n library. Supports single country, continent batch, region batch, or explicit array of country codes.

## Usage

```
/add-market DE                          # Single country
/add-market Europe                      # Continent batch
/add-market "Southeast Asia"            # Region batch (use quotes for multi-word regions)
/add-market DE,FR,ES,PL                 # Explicit array
```

## Workflow

### Mode A: Single Country

1. **Fetch** — WebFetch `https://en.wikipedia.org/wiki/ISO_3166-2:<CC>`
2. **Analyze** — Extract from the page:
   - Country name
   - Continent (from infobox or page context — determines `data/<continent>/` subdirectory)
   - Subdivision table: code, name, category, parent (if hierarchical)
3. **Create JSON** — Write `data/<continent>/<cc>.json`:
   ```json
   {
     "country": "<CC>",
     "name": "<Country Name>",
     "wikipedia": "https://en.wikipedia.org/wiki/ISO_3166-2:<CC>",
     "dateAdded": "<YYYY-MM-DD>",
     "lastUpdated": "<YYYY-MM-DD>",
     "subdivisions": [
       {"code": "...", "name": "...", "category": "..."}
     ]
   }
   ```
   - Subdivisions ordered alphabetically by code
   - Lowercase for generic categories (`"state"`, `"province"`, `"county"`), Title Case for proper types (`"Province"`, `"Land"`). Match existing conventions in the repo.
   - Use `parent` field for hierarchical subdivisions
   - `dateAdded` and `lastUpdated` both set to today's date
4. **Create Test** — Write `library/src/test/java/dev/marcosalmeida/i18n/Subdivision<CC>Test.java` following existing patterns. Match the structure of `SubdivisionIETest.java`:
   - Test: all codes resolve, names match, categories correct
   - Test: `fromCode()` with full code and subdivision part
   - Test: `fromName()` case-insensitive
   - Test: `find()` unified lookup
   - Test: parent-child relationships (if applicable)
   - Test: category filter methods (if applicable)
   - Test: `wikipedia()`, `dateAdded()`, `lastUpdated()` return non-null, non-empty strings
   - Test: `getSubdivisions(CountryCode.<CC>)` returns correct count
5. **Update SubdivisionCodeTest** — Add assertions for the new country in `library/src/test/java/dev/marcosalmeida/i18n/SubdivisionCodeTest.java`:
   - `testGetSubdivisions()` — add assertion for the new country code and count
   - `testFromCode()` — add a fromCode assertion with a representative code
   - `testGlobalFiltering()` — update counts for any global category methods affected
6. **Verify** — Run `mvn verify --batch-mode`. If it fails:
   - Analyze the error output
   - Fix the JSON data or test assertions
   - Re-run `mvn verify`
   - Loop until green
7. **Commit & PR** — Create branch, commit, push, open PR:
   ```bash
   git checkout -b feat/add-<cc>-subdivisions
   git add data/<continent>/<cc>.json library/src/test/java/dev/marcosalmeida/i18n/Subdivision<CC>Test.java library/src/test/java/dev/marcosalmeida/i18n/SubdivisionCodeTest.java
   git commit -m "feat(data): add <Country Name> subdivisions"
   git push -u origin feat/add-<cc>-subdivisions
   gh pr create --title "feat(data): add <Country Name> subdivisions" --body "Adds ISO-3166-2 subdivisions for <Country Name>.

   🤖 Generated with [Claude Code](https://claude.com/claude-code)"
   ```

### Mode B: Batch (continent, region, or array)

1. **Discover** — Map the input to a concrete list of country codes:
   - Continent name: WebFetch `https://en.wikipedia.org/wiki/ISO_3166-2`, parse the continent's country table, extract all two-letter codes
   - Region name: same approach, filter by region column or section
   - Explicit array: parse the comma-separated list directly
   - Filter out: countries already in the library (check `data/<continent>/*.json`), countries with no ISO-3166-2 subdivisions
2. **Plan** — Create a TaskCreate entry per country. Name each task with the country code and name.
3. **Dispatch** — Spin sub-agents in parallel batches of 5:
   - Each sub-agent runs Mode A (single country workflow)
   - Default model: `haiku` for simple flat subdivisions, `sonnet` for hierarchical/complex countries
   - After each batch of 5 completes, run `mvn verify --batch-mode` as a checkpoint
   - Fix any failures within the batch before moving to the next
   - Update task status (pending → in_progress → completed) via TaskUpdate
4. **Converge** — After all batches:
   - Run full `mvn verify --batch-mode`
   - Fix any remaining failures
   - Loop until green
5. **Commit & PR** — Single branch, single commit:
   ```bash
   git checkout -b feat/add-<batch-name>-subdivisions
   git add data/ library/src/test/
   git commit -m "feat(data): add <N> <batch-name> subdivisions"
   git push -u origin feat/add-<batch-name>-subdivisions
   gh pr create --title "feat(data): add <N> <batch-name> subdivisions" --body "Adds ISO-3166-2 subdivisions for <N> countries in <batch-name>.

   🤖 Generated with [Claude Code](https://claude.com/claude-code)"
   ```

## Conventions

### Category casing
- Generic types → lowercase: `"state"`, `"province"`, `"county"`, `"region"`, `"district"`, `"municipality"`, `"parish"`, `"canton"`, `"commune"`
- Proper-noun types → Title Case: `"Province"`, `"State"`, `"Land"`, `"Autonomous Region"`, `"Federal District"`
- When unsure, look at existing countries in the same continent and follow their convention

### Parent relationships
- Only add `parent` when the Wikipedia table explicitly shows hierarchical grouping
- Parent codes must reference another subdivision code within the same file
- The generator validates parent references — a bad reference fails the build

### Numeric codes
- Some countries have numeric subdivision codes (e.g., Italy `"25"` for Lombardia)
- The generator automatically prefixes these with the country code (e.g., `IT_25`)
- No special handling needed in the JSON — just use the numeric code as-is

### Wikipedia link format
- Always: `https://en.wikipedia.org/wiki/ISO_3166-2:<CC>`
- The `<CC>` must match the `country` field in the JSON

### Transcontinental countries
- Place in the continent where the majority of subdivisions lie
- Russia → `data/europe/`, Turkey → `data/europe/`

## Model Selection

| Country complexity | Model | When |
|---|---|---|
| Simple flat, ≤2 categories | `haiku` | Default for most countries |
| Hierarchical with parents | `sonnet` | Parent-child relationships present |
| 3+ categories, unusual structure | `sonnet` | More edges to verify |
| Edge cases (numeric codes, disputed territories) | `sonnet`/`opus` | Needs deeper reasoning |

The orchestrator in batch mode uses `sonnet` to coordinate.
