# Product Brief — PDF Toolkit (Desktop)

| Campo | Valor |
| --- | --- |
| **Status** | Scope frozen (v1) |
| **Autor** | Thiago |
| **Data** | 2026-09-26 |
| **Freeze** | 2026-09-26 — ver [SCOPE_V1.md](./SCOPE_V1.md) |
| **Tipo de artefato** | Product Brief / PRD leve |
| **Público** | Founder / builder (side project) |

> Documento de alinhamento de produto. Define *por quê*, *para quem*, *o quê* e *em que ordem* — sem detalhe de implementação.
> **Contrato de escopo da v1:** [SCOPE_V1.md](./SCOPE_V1.md) (congelado).

---

## 1. Resumo executivo

Construir um **aplicativo desktop** (Windows e Linux) para manipulação básica de PDFs, processando arquivos **100% na máquina do usuário**.

No curto prazo: ferramenta útil para uso pessoal e aprendizado de Java além do desenvolvimento web.
No médio/longo prazo: base para distribuição gratuita e eventual monetização (ex.: ads no site de download), sem depender disso no MVP.

**Nome de trabalho:** PDF Toolkit *(provisório)*

---

## 2. Problema

Pessoas (incluindo o próprio autor) precisam com frequência:

- dividir PDFs grandes;
- juntar vários arquivos em um;
- extrair ou remover páginas;
- fazer isso **rápido**, sem upload para a nuvem e sem pagar assinatura.

Soluções existentes (Smallpdf, iLovePDF, etc.) resolvem o problema, mas:

- muitas exigem upload (privacidade / internet);
- planos gratuitos têm limites;
- o usuário já chegou a **pagar** por urgência — sinal de dor real.

## 3. Insights de mercado (pesquisa)

Ferramentas líderes oferecem tipicamente **25–30 tools**. O uso diário se concentra em:

1. Merge / Split / organizar páginas
2. Comprimir
3. Converter (imagem ↔ PDF, Office ↔ PDF)
4. Proteger / marca d’água / numeração
5. Editar, OCR, assinar (mais avançado)

Referências: Smallpdf, iLovePDF, PDFgear, PDFsam.

**Implicação:** um MVP competitivo *para uso pessoal* não precisa de 30 tools — precisa fazer bem o bloco de **organização de páginas**.

## 4. Visão do produto

> Um app desktop leve, em Java, que resolve as tarefas PDF do dia a dia offline, com privacidade total e sem custo de infraestrutura.

### Princípios de produto

1. **Local-first** — arquivos não saem do computador
2. **Uma tarefa por vez, bem feita** — no MVP, profundidade > amplitude
3. **Custo zero de operação** — sem servidor de processamento
4. **Aprendizado como valor** — JavaFX + domínio de arquivos/desktop
5. **Renda é etapa posterior** — não bloqueia o ship do MVP

## 5. Objetivos e não-objetivos

### Objetivos (agora)

- [ ] Ter um app útil no dia a dia do autor
- [ ] Praticar Java fora do web (UI desktop, arquivos, empacotamento)
- [ ] Entregar MVP com split/merge/organização de páginas
- [ ] Rodar em Windows e Linux

### Não-objetivos (agora)

- SaaS com processamento na nuvem
- Monetização obrigatória no lançamento
- OCR, conversão Office de qualidade Adobe, editor visual rico
- App mobile
- Conta de usuário / sync na nuvem

## 6. Usuários e jobs-to-be-done

### Persona primária (MVP)

**Usuário-poder (o próprio autor)**
Precisa manipular PDFs com frequência, valoriza velocidade e privacidade, aceita UI simples.

### Persona secundária (pós-MVP)

**Usuário ocasional**
Baixa o app quando precisa split/merge uma vez; não quer cadastrar nem pagar.

### Jobs-to-be-done

| Quando… | Quero… | Para… |
| --- | --- | --- |
| Tenho um PDF grande | dividir por páginas/intervalos | enviar só o trecho necessário |
| Tenho vários PDFs | juntar em um arquivo | entregar um pacote único |
| Tenho páginas erradas/extra | remover ou reordenar | corrigir sem recriar o doc |
| Não quero upload | fazer tudo offline | privacidade e velocidade |

## 7. Proposta de valor

**Para** quem precisa manipular PDFs com frequência,
**PDF Toolkit** é um app desktop offline
**que** faz split, merge e organização de páginas sem enviar arquivos para a internet
**diferente de** ferramentas online freemium,
**porque** o processamento é local, ilimitado no uso pessoal e sem custo de servidor.

## 8. Escopo do MVP (v1) — CONGELADO

> Freeze em 2026-09-26. Detalhe e critérios de aceite: **[SCOPE_V1.md](./SCOPE_V1.md)**.
> Features fora de F1–F7 não entram na v1 sem novo freeze.

### Must have (IN)

| ID | Feature | Descrição | Prioridade |
| --- | --- | --- | --- |
| F1 | Abrir PDF | Selecionar arquivo e ver info básica (nome, páginas) | P0 |
| F2 | Split | Dividir por intervalo ou 1 PDF por página | P0 |
| F3 | Merge | Juntar N PDFs na ordem definida | P0 |
| F4 | Extrair páginas | Salvar só as páginas escolhidas | P0 |
| F5 | Remover páginas | Gerar PDF sem as páginas indicadas | P0 |
| F6 | Rotacionar | Girar páginas 90/180/270° | P0 |
| F7 | Salvar resultado | Escolher pasta/nome de saída | P0 |

### Should have (OUT da v1 → v1.1+)

| ID | Feature | Prioridade |
| --- | --- | --- |
| F8 | Reordenar páginas (lista / drag) | P1 |
| F9 | Preview simples de páginas | P1 |
| F10 | Histórico recente de arquivos | P2 |

### Out of scope (v1)

- Compressão avançada
- JPG ↔ PDF
- Senha / watermark / numeração
- OCR / Word / Excel
- Assinatura digital
- Batch em pasta inteira
- Auto-update
- Reordenar / preview / histórico (F8–F10)

## 9. Roadmap de produto

```text
v1 (MVP)          → Split, Merge, Extrair, Remover, Rotacionar
v1.1              → Reordenar + preview básico
v2                → Compressão básica, imagem ↔ PDF, senha
v3                → Watermark, numeração, batch
Distribuição      → Site estático de download + GitHub Releases
Monetização (*)   → Avaliar ads no site / doação / “Pro” (só após uso real)
```

`(*)` Monetização é hipótese futura, não compromisso do MVP.

## 10. Métricas de sucesso

### MVP (sucesso = uso pessoal)

| Métrica | Meta |
| --- | --- |
| App instalado e abrindo em Win + Linux | Sim |
| Autor usa em tarefas reais | ≥ 1x/semana por 1 mês |
| Fluxos P0 funcionando sem crash | 100% dos casos de uso pessoais |
| Tempo para split/merge típico | Segundos (arquivos comuns) |

### Pós-distribuição (se publicar)

| Métrica | Meta inicial |
| --- | --- |
| Downloads | Qualitativa no começo |
| Feedback / issues | Pelo menos entender fricções |
| Retorno ao app | Indício de retenção |

## 11. Requisitos não funcionais

| Área | Requisito |
| --- | --- |
| Privacidade | Nenhum upload; processamento local |
| Plataformas | Windows e Linux |
| Performance | Não travar a UI em PDFs grandes (processar fora da UI thread) |
| UX | Fluxo óbvio: abrir → ação → salvar |
| Custo | Infra ~R$ 0 (exceto domínio opcional) |
| Empacotamento | `.jar` no início; instalador/`jpackage` depois |

## 12. Stack sugerida (orientação, não contrato técnico)

| Camada | Escolha |
| --- | --- |
| Linguagem | Java |
| UI | JavaFX |
| PDF | Apache PDFBox |
| Build | **Maven** |
| Distribuição | GitHub Releases (+ site estático opcional) |

## 13. Riscos e mitigação

| Risco | Impacto | Mitigação |
| --- | --- | --- |
| Escopo inchado (querer rivalizar Smallpdf) | Alto | Freeze do MVP nas features P0 |
| PDF corrompido / edge cases | Médio | Validar erros com mensagem clara; não prometer “repair” no v1 |
| Antivírus no Windows | Médio | Assinatura de código só se distribuir amplamente |
| Desânimo por perfeccionismo | Alto | Definition of Done = “eu uso no meu fluxo real” |
| Mercado saturado p/ monetizar | Alto | Aceitar: v1 é aprendizado + utilidade; renda é etapa 2+ |

## 14. Decisão de monetização (posição atual)

| Opção | Status |
| --- | --- |
| Assinatura SaaS | Descartada no curto prazo (custo + saturação) |
| Ads no app desktop | Desencorajada (UX ruim, poucas redes) |
| Ads no site de download | Possível depois, se houver tráfego |
| Gratuito forever (uso pessoal) | **Default do MVP** |

**Decisão:** shippar valor primeiro; só revisitamos monetização após o app existir e ser usado.

## 15. Open questions

- [ ] Nome definitivo do produto
- [ ] Ícone / identidade visual mínima
- [x] ~~Maven vs Gradle~~ → **Maven**
- [ ] Publicar open source ou só binários?
- [x] ~~Linux: só jar no começo~~ → **dist zip + launcher** (Release anexa `*-linux.zip`); `.deb` / AppImage / `jpackage` depois
- [ ] Artefato Windows no CI (matrix)

## 16. Próximos passos

Checkpoint operacional: **[CONTINUE.md](./CONTINUE.md)** (atualizado 2026-09-26).

1. ~~Congelar escopo v1 (F1–F7)~~ ✅
2. ~~Skeleton JavaFX + PDFBox + quality tools~~ ✅
3. ~~CI + rulesets + Release Please + zip em prod (`v0.1.2`)~~ ✅
4. (Opcional) Promote `v0.1.2` → `main` + backport
5. **Implementar F1–F7** (começar por Split + Merge) ← próximo valor de produto
6. Smoke test Win + Linux; uso real; só então v1.1

---

## Apêndice A — Mapa de features do mercado (referência)

| Categoria | Exemplos |
| --- | --- |
| Organizar | Merge, Split, Extract, Remove, Organize, Rotate, Mix, Crop |
| Otimizar | Compress, Repair |
| Converter | JPG↔PDF, Office↔PDF, HTML/Markdown→PDF, PDF/A |
| Proteger | Protect, Unlock, Watermark, Page numbers, Redact, Sign |
| Editar | Annotate, Edit text, Forms, OCR, Compare, AI summarize/translate |

## Apêndice B — Tipos de documento PM (referência)

Este arquivo combina elementos de:

| Documento | Para que serve |
| --- | --- |
| **Product Brief / One-pager** | Alinhar visão e problema em 1 leitura |
| **PRD** | Escopo, requisitos, não-objetivos |
| **Roadmap** | Ordem de entrega |
| **Opportunity assessment** | Por que vale (ou não) monetizar agora |

Quando o produto crescer, dá para separar em: Brief (estratégia) + PRD por release + changelog.
