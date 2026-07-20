# Semantic Release Pipeline

**Date:** 2026-07-20
**Issue:** [#2 — Implement semantic release](https://github.com/mpbalmeida/i18n/issues/2)
**Status:** Approved

## Overview

Automated release pipeline using JReleaser (Java-native, zero Node.js). On every push to main, conventional commits are analyzed to determine the next version, a GitHub Release is created with a changelog, the artifact is published to Maven Central, and `pom.xml` is bumped to the next SNAPSHOT for development.

## Motivation

- Currently, version bumps, releases, and publishes are manual
- Node.js-based tools (`semantic-release`) add a foreign runtime to a pure Java project
- JReleaser runs as a Maven goal, matching the existing build toolchain

## Architecture

Two workflows, one config file. No new Maven dependencies.

| File | Action | Purpose |
|---|---|---|
| `.github/workflows/ci.yml` | Replace `maven.yml` | Verify commit → `mvn verify` on push/PR to main |
| `.github/workflows/release.yml` | New | On push to main: JReleaser full-release → Central publish → bump SNAPSHOT |
| `jreleaser.yml` | New | JReleaser config: conventional commits, GitHub Release, changelog, Maven Central |
| `.releaserc` | Delete | Node.js config — no longer needed |
| `.github/workflows/maven.yml` | Delete | Replaced by `ci.yml` |

## Flow

```
PR push ──→ ci.yml: verify commit → mvn verify

Merge ──→ ci.yml: verify commit → mvn verify
     ──→ release.yml:
            mvn verify
            JReleaser full-release:
              1. Analyze conventional commits → 0.5.0
              2. Generate CHANGELOG
              3. Create GitHub Release with changelog
              4. Update pom.xml to 0.5.0
            mvn deploy -P deployment → Maven Central
            Bump pom.xml to 0.6.0-SNAPSHOT + commit
```

No snapshot deploys on PRs. Release happens only on merge to main.

## Workflows

### `ci.yml`

- **Trigger**: push to main, pull request to main
- **Steps**:
  1. `actions/checkout@v4` (fetch-depth: 0)
  2. `actions/setup-java@v4` (JDK 21, temurin, cache maven)
  3. Verify commit message via `scripts/commit-msg.sh`
  4. `mvn verify --batch-mode`

### `release.yml`

- **Trigger**: push to main
- **Permissions**: contents write, issues write, pull-requests write
- **Steps**:
  1. `actions/checkout@v4` (fetch-depth: 0, persist-credentials: false)
  2. `actions/setup-java@v4` (JDK 21, temurin, cache maven, server-id central, GPG key + passphrase)
  3. `mvn verify --batch-mode`
  4. `jreleaser full-release` (environment: JRELEASER_GITHUB_TOKEN)
  5. `mvn deploy -P deployment --batch-mode` (if release published)
  6. Bump `pom.xml` to next SNAPSHOT + commit (if release published)

## JReleaser Configuration

`jreleaser.yml` at project root:

- **Release mode**: GitHub release with generated changelog
- **Commit parser**: Conventional commits
- **Version strategy**: Increment based on feat/fix/breaking changes
- **Deploy target**: Maven Central via `central-publishing-maven-plugin`
- **Post-release**: SNAPSHOT bump handled by shell step in workflow (JReleaser does not natively bump post-release)

## Secrets Required

| Secret | Purpose |
|---|---|
| `SONATYPE_TOKEN_USERNAME` | Sonatype Central Portal token code |
| `SONATYPE_TOKEN_PASSWORD` | Sonatype Central Portal token value |
| `GPG_PRIVATE_KEY` | GPG private key for artifact signing |
| `GPG_PASSPHRASE` | GPG key passphrase |
| `JRELEASER_GITHUB_TOKEN` | GitHub token for creating releases (defaults to `secrets.GITHUB_TOKEN`) |

## Example

Given `pom.xml` at version `0.4.0` and commits since the last release:

```
feat(us): add Florida
fix(au): correct New South Wales code
```

The next push to main triggers:
1. JReleaser analyzes commits → **minor bump** (0.5.0)
2. GitHub Release "v0.5.0" created with changelog:
   - Features: add Florida (us)
   - Bug Fixes: correct New South Wales code (au)
3. `0.5.0` published to Maven Central
4. `pom.xml` bumped to `0.6.0-SNAPSHOT` and committed back to main
