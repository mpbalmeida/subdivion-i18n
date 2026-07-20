# i18n — ISO-3166-2 Subdivision Library

Java library providing ISO-3166-2 subdivisions for various countries.

## Build

```bash
mvn verify --batch-mode    # compile + test
mvn deploy -P deployment   # publish to Maven Central (requires GPG + Sonatype token)
```

- Java 21, Maven wrapper not used (use `mvn` directly)
- Dependencies: `nv-i18n` (CountryCode)

## Conventions

- **Commit messages**: Conventional Commits — `feat(scope): description`, `fix(scope): description`
  - Types: feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert
  - Enforced by `scripts/commit-msg.sh` hook and CI
- **SubdivisionCode.java**: Countries in alphabetical order, subdivisions within each country in alphabetical order by code
- **Enum naming**: Use shortened keys for enum constants (e.g., `US.AL`, not `US.ALABAMA`)
- **Categories**: Use lowercase (e.g., `"state"`, `"province"`) for simple types, Title Case for proper types (e.g., `"State"`, `"Province"`)
- **Javadoc**: Every enum constant needs a Javadoc comment

## Adding a new country

1. Add the enum to `SubdivisionCode.java` in alphabetical order
2. Implement the `Subdivision` interface pattern matching existing country enums
3. Add test in `src/test/java/dev/marcosalmeida/i18n/`
4. Update `getSubdivisions()` switch, `allValues()`, and `SubdivisionCodeTest`

## CI/CD

| Workflow | Trigger | What it does |
|---|---|---|
| `ci.yml` | Push/PR to main | Verify commit message → `mvn verify` |
| `release.yml` | Push to main | JReleaser creates GitHub Release with changelog → publish to Maven Central → bump SNAPSHOT |

- Semantic release via JReleaser (`jreleaser.yml`), triggered on merge to `main`
- No Node.js in the pipeline — pure Java
