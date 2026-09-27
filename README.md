# PDF Toolkit

Desktop app (Windows / Linux) for local PDF manipulation.

| Doc | Para quê |
| --- | --- |
| [`docs/CONTINUE.md`](docs/CONTINUE.md) | **Checkpoint** — onde paramos / por onde continuar |
| [`docs/PRODUCT_BRIEF.md`](docs/PRODUCT_BRIEF.md) | Visão e decisões de produto |
| [`docs/SCOPE_V1.md`](docs/SCOPE_V1.md) | Escopo congelado da v1 (F1–F7) |

**Latest release:** [v0.1.2](https://github.com/thiago2santos/pdf-toolkit/releases/tag/v0.1.2) (`pdf-toolkit-v0.1.2-linux.zip`)

Default branch: `pre-release` (fluxo Release Please → promote → backport).

## Requirements

- JDK 25+
- Maven 3.9+ (or use `./mvnw`)

## Run (JavaFX)

```shell
./mvnw javafx:run
```

## Build dist zip

```shell
./mvnw -DskipTests package
./target/pdf-toolkit-*-dist/pdf-toolkit-*/bin/pdf-toolkit
```

## Test & quality

```shell
./mvnw test
./mvnw spotless:apply
./mvnw pmd:check
./mvnw -DskipTests compile spotbugs:check
```

Optional (needs `NVD_API_KEY`):

```shell
./mvnw org.owasp:dependency-check-maven:check
```

### Pre-commit

```shell
pre-commit install --hook-type pre-commit --hook-type commit-msg --hook-type pre-push
pre-commit run --all-files
```

## Stack

| Layer | Choice |
| --- | --- |
| UI | JavaFX 25 |
| PDF | Apache PDFBox 3 |
| Build | Maven |
| Format | Spotless + Google Java Format |
| Static analysis | PMD, SpotBugs |
| Security | OWASP Dependency-Check, Trivy (pre-push), Gitleaks |
| Release | Release Please + linux dist zip on GitHub Release |

## v1 scope

Frozen in [`docs/SCOPE_V1.md`](docs/SCOPE_V1.md): open, split, merge, extract, remove, rotate, save.
**App features still TODO** — platform/release plumbing is done first.
