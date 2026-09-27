# PDF Toolkit — continuar daqui

Última atualização: **2026-09-27**
Repo: https://github.com/thiago2santos/pdf-toolkit
Docs de produto: [PRODUCT_BRIEF.md](./PRODUCT_BRIEF.md) · [SCOPE_V1.md](./SCOPE_V1.md) · [ARCHITECTURE.md](./ARCHITECTURE.md)

> Leia este arquivo ao retomar o trabalho. É o “checkpoint” operacional (como o `HOTEL-STACK-CONTINUE` do availability-api).

---

## Onde paramos

**v0.2.0 em produção**: abrir, visualizar páginas e salvar cópia. Split/Merge ainda não.

| Item | Estado |
| --- | --- |
| Produto (escopo v1) | F1 Abrir ✅ · F9-lite Visualizar ✅ · F7 Salvar ✅ · F2–F6 pendentes |
| Arquitetura | MVVM — [ARCHITECTURE.md](./ARCHITECTURE.md) |
| Versão atual (`pre-release`) | **0.2.0** |
| Última release | **[v0.2.0](https://github.com/thiago2santos/pdf-toolkit/releases/tag/v0.2.0)** (`pdf-toolkit-v0.2.0-linux.zip` ✅) |
| Default branch | `pre-release` |
| `main` | Promovida para **v0.2.0** |
| Escopo v1 | [SCOPE_V1.md](./SCOPE_V1.md) (revisado 2026-09-27: F9-lite entrou) |

### Já feito (plataforma / engenharia)

- [x] Product Brief + Scope Freeze (F1–F7)
- [x] Skeleton Maven + JavaFX 25 + PDFBox 3 + JUnit
- [x] Quality: Spotless, PMD, SpotBugs, OWASP, pre-commit, Trivy/Gitleaks
- [x] Repo GitHub público + conventional commits
- [x] CI (PR → `pre-release` / `main`)
- [x] Rulesets (ver "Regras de PR" abaixo)
- [x] Dist zip local (`maven-assembly` + launcher `bin/pdf-toolkit [arquivo.pdf]`)
- [x] Release Please (Maven, `skip-snapshot`, target `pre-release`)
- [x] Promote manual `pre-release` → `main` + backport via PR
- [x] Zip anexado no mesmo job do Release Please (`release_created`)
- [x] Workflow manual "Attach dist to Release" (backfill de tags antigas)

### Regras de PR

| Branch alvo | PR obrigatória | Aprovações | Bypass |
| --- | --- | --- | --- |
| `pre-release` | Sim | **1** | Admin, só ao mergear PR (`gh pr merge --admin`) |
| `main` | Sim | 0 | — |

- PR de **feature** → `pre-release`: revisar e aprovar. Sozinho no repo, o autor não pode aprovar a própria PR → mergear com bypass de admin depois de revisar o diff.
- PRs de **bot** (Release PR, backport) → `pre-release`: você pode aprovar (autor é o bot) ou usar o bypass.
- PR de **promote** → `main`: sem aprovação.
- **CI em PR aberta por workflow** fica `action_required` (o bot conta como "first-time contributor"; não há política mais permissiva). Aprovar a execução antes do merge:
  ```bash
  gh run list --limit 5            # pegar o id com action_required
  gh api -X POST repos/thiago2santos/pdf-toolkit/actions/runs/<id>/approve
  ```
  Para eliminar isso: workflows abrirem PRs com token de GitHub App/PAT (não feito).

### Fluxo de release (vigente)

```text
feature branch → PR → pre-release (1 aprovação ou bypass admin)
        ↓
Release Please abre/atualiza Release PR (versão + CHANGELOG)
        ↓
merge Release PR → tag vX.Y.Z + GitHub Release + zip linux
        ↓
(opcional) Actions → Promote pre-release to main -f tag=vX.Y.Z
        ↓
workflow abre PR de backport main → pre-release → merge (merge commit)
```

Commits que movem versão: `feat:` (minor pré-1.0), `fix:` (patch), `docs:` (patch).
`ci:` / `chore:` / `build:` em geral **não** abrem Release PR.

---

## Continuar a partir daqui

### Próximo valor de produto (prioridade)

```text
1. ✅ Arquitetura MVVM (ARCHITECTURE.md)
2. ✅ F1 Abrir + F9-lite Visualizar páginas + F7 Salvar  (v0.2.0)
3. F2 Split + F3 Merge   ← próximo; primeiro uso real no dia a dia
   (começar por PageRange.parse("1-3,5", pageCount) no core — reusado por F4/F5/F6)
4. F4 Extrair + F5 Remover
5. F6 Rotacionar
6. Smoke test do zip Linux (+ Windows depois)
```

Opcional: backfill zip da `v0.1.1` → `gh workflow run "Attach dist to Release" --ref pre-release -f tag=v0.1.1`

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

Rodar o app:

```bash
./mvnw javafx:run
# ou, já abrindo um PDF:
./mvnw -DskipTests package
./target/pdf-toolkit-*-dist/pdf-toolkit-*/bin/pdf-toolkit ~/algum.pdf
```
