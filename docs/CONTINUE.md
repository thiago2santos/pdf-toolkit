# PDF Toolkit — continuar daqui

Última atualização: **2026-09-26**
Repo: https://github.com/thiago2santos/pdf-toolkit
Docs de produto: [PRODUCT_BRIEF.md](./PRODUCT_BRIEF.md) · [SCOPE_V1.md](./SCOPE_V1.md) · [ARCHITECTURE.md](./ARCHITECTURE.md)

> Leia este arquivo ao retomar o trabalho. É o “checkpoint” operacional (como o `HOTEL-STACK-CONTINUE` do availability-api).

---

## Onde paramos

Hello-world **em produção** (GitHub Release + zip Linux), com fluxo de branch espelhando o `availability-api`.

| Item | Estado |
| --- | --- |
| Produto funcional (F1–F7) | **Ainda não** — app é skeleton JavaFX (menu Hello World) |
| Versão atual (`pre-release`) | **0.1.2** |
| Última release | **[v0.1.2](https://github.com/thiago2santos/pdf-toolkit/releases/tag/v0.1.2)** |
| Artefato Linux | `pdf-toolkit-v0.1.2-linux.zip` anexado ✅ |
| Default branch | `pre-release` |
| `main` | Parada no promote de **v0.1.1** (ainda **não** promoveu 0.1.2) |
| Escopo v1 | Congelado em [SCOPE_V1.md](./SCOPE_V1.md) |

### Já feito (plataforma / engenharia)

- [x] Product Brief + Scope Freeze (F1–F7)
- [x] Skeleton Maven + JavaFX 25 + PDFBox 3 + JUnit
- [x] Quality: Spotless, PMD, SpotBugs, OWASP, pre-commit, Trivy/Gitleaks
- [x] Repo GitHub público + conventional commits
- [x] CI (PR → `pre-release` / `main`)
- [x] Rulesets: Protect `main` + Protect `pre-release` (PR obrigatório)
- [x] Dist zip local (`maven-assembly` + launcher `bin/pdf-toolkit`)
- [x] Release Please (Maven, `skip-snapshot`, target `pre-release`)
- [x] Promote manual `pre-release` → `main`
- [x] Backport via **PR** (não push direto) após promote
- [x] Zip anexado no mesmo job do Release Please (`release_created`)
- [x] Workflow manual “Attach dist to Release” (backfill de tags antigas)

### Fluxo de release (vigente)

```text
feature branch → PR → pre-release
        ↓
Release Please abre/atualiza Release PR (versão + CHANGELOG)
        ↓
merge Release PR → tag vX.Y.Z + GitHub Release + zip linux
        ↓
(opcional) Actions → Promote pre-release to main
        ↓
workflow abre PR de backport main → pre-release → merge
```

Commits que movem versão: `feat:` (minor pré-1.0), `fix:` (patch).
`ci:` / `chore:` / `build:` em geral **não** abrem Release PR.

---

## Continuar a partir daqui

### Imediato (higiene do fluxo) — opcional mas recomendado

1. **Promote `v0.1.2` → `main`**
   ```bash
   gh workflow run "Promote pre-release to main" --ref pre-release -f tag=v0.1.2
   gh pr list --base main
   # merge do promote PR
   ```
2. Mergear o **backport PR** que o workflow abrir (`main` → `pre-release`).
3. (Opcional) Backfill zip da `v0.1.1`:
   ```bash
   gh workflow run "Attach dist to Release" --ref pre-release -f tag=v0.1.1
   ```

### Próximo valor de produto (prioridade)

Retomar o roadmap do escopo congelado — **features de PDF**:

```text
1. Arquitetura UI × domínio × I/O → decidido: MVVM, ver [ARCHITECTURE.md](./ARCHITECTURE.md)
2. F1 Abrir + F9-lite Visualizar páginas + F7 Salvar
3. F2 Split + F3 Merge   ← primeiro uso real no dia a dia
4. F4 Extrair + F5 Remover
5. F6 Rotacionar
6. Smoke test do zip Linux (+ Windows depois)
```

Definition of Done da v1: ver [SCOPE_V1.md](./SCOPE_V1.md).

### Depois da v1 funcional

- Empacotamento Windows (natives JavaFX no CI matrix / zip win)
- `jpackage` / instaladores
- Preview / reordenar (F8–F9, v1.1)
- Landing de download / ads (só com tráfego)

---

## Como retomar no Cursor

Abrir este chat ou pedir:

> continua o pdf-toolkit a partir de `docs/CONTINUE.md`

Branch de trabalho local:

```bash
cd ~/pdf-toolkit
git checkout pre-release
git pull
git checkout -b feat/...   # ou fix/...
```

Rodar o skeleton:

```bash
./mvnw javafx:run
# ou
./mvnw -DskipTests package
./target/pdf-toolkit-*-dist/pdf-toolkit-*/bin/pdf-toolkit
```
