# PDF Toolkit

Desktop app (Windows / Linux) for local PDF manipulation.
Product docs: [`docs/PRODUCT_BRIEF.md`](docs/PRODUCT_BRIEF.md), [`docs/SCOPE_V1.md`](docs/SCOPE_V1.md).

## Requirements

- JDK 25+
- Maven 3.9+ (or use `./mvnw`)

## Run (JavaFX)

```shell
./mvnw javafx:run
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

## v1 scope

Frozen in [`docs/SCOPE_V1.md`](docs/SCOPE_V1.md): open, split, merge, extract, remove, rotate, save.
