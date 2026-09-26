# Scope Freeze — PDF Toolkit v1

| Campo | Valor |
| --- | --- |
| **Status** | **FROZEN** |
| **Data do freeze** | 2026-09-26 |
| **Release alvo** | v1 (MVP) |
| **Documento pai** | [PRODUCT_BRIEF.md](./PRODUCT_BRIEF.md) |

> Qualquer feature fora desta lista **não entra na v1**, salvo revisão explícita do escopo.

---

## Regra do freeze

1. Só entram itens marcados como **IN** abaixo.
2. Itens **OUT** ficam para v1.1+ ou backlog.
3. Mudança de escopo exige: atualizar este arquivo + nova data de freeze.
4. **Definition of Done da v1:** o autor consegue, no app, fazer split/merge/extrair/remover/rotacionar e salvar o PDF em Windows e Linux (via jar no mínimo).

---

## IN — v1 (obrigatório)

| ID | Feature | Critério de aceite (resumo) |
| --- | --- | --- |
| **F1** | Abrir PDF | Usuário escolhe `.pdf`; app mostra nome do arquivo e número de páginas. Erro claro se arquivo inválido. |
| **F2** | Split | (a) dividir por intervalo de páginas; (b) gerar 1 PDF por página. Resultado(s) salvos com sucesso. |
| **F3** | Merge | Usuário adiciona N PDFs, define ordem, gera um único PDF. |
| **F4** | Extrair páginas | Usuário indica páginas/intervalos; salva só essas páginas em um PDF novo. |
| **F5** | Remover páginas | Usuário indica páginas a excluir; salva PDF sem elas. |
| **F6** | Rotacionar | Rotação 90° / 180° / 270° em páginas selecionadas ou em todas. |
| **F7** | Salvar resultado | Diálogo para escolher pasta e nome; confirmação de sucesso ou erro legível. |

### Empacotamento mínimo (IN)

| ID | Item | Critério |
| --- | --- | --- |
| **P1** | Execução local | App sobe via Maven/Gradle + JavaFX |
| **P2** | Artefato jar | É possível rodar em Windows e Linux com Java instalado (`java -jar` ou equivalente documentado) |

---

## OUT — explicitamente fora da v1

| Item | Destino sugerido |
| --- | --- |
| F8 Reordenar com drag-and-drop | v1.1 |
| F9 Preview de páginas | v1.1 |
| F10 Histórico recente | v1.1 / v2 |
| Compressão | v2 |
| JPG ↔ PDF | v2 |
| Senha (protect/unlock) | v2 |
| Watermark / numeração | v3 |
| Batch em pasta | v3 |
| OCR / Office / assinatura / AI | Backlog longo |
| Instalador nativo (`jpackage`) | Pós-v1 (nice to have) |
| Site de download / ads | Pós-v1 |
| Auto-update | Backlog |
| Conta / cloud / sync | Fora do produto (por enquanto) |

---

## Ordem de build (dentro do freeze)

```text
1. Skeleton JavaFX + PDFBox
2. F1 Abrir + F7 Salvar (esqueleto de I/O)
3. F2 Split + F3 Merge   ← primeiro valor real
4. F4 Extrair + F5 Remover
5. F6 Rotacionar
6. Smoke test Win + Linux (jar)
```

Nada de F8+ antes de F1–F7 estarem usáveis.

---

## Decisões travadas com o freeze

| Tema | Decisão |
| --- | --- |
| Plataforma | Desktop Windows + Linux |
| Processamento | 100% local |
| Stack | Java + JavaFX + Apache PDFBox |
| Build | Maven (`./mvnw`) |
| Monetização na v1 | Nenhuma |
| Escopo funcional | Apenas F1–F7 |

### Decisões fechadas depois do freeze

| Tema | Decisão |
| --- | --- |
| Build | **Maven** (+ wrapper `./mvnw`) |

### Ainda em aberto (não bloqueiam o freeze)

- Nome definitivo do produto
- Open source vs só binários
- Formato Linux além do jar (`.deb` / AppImage)

---

## Assinatura do freeze

| Papel | Nome | Data |
| --- | --- | --- |
| Product owner | Thiago | 2026-09-26 |

**Escopo v1 congelado.** Próximo passo: skeleton do projeto.
