# Arquitetura — PDF Toolkit

| Campo | Valor |
| --- | --- |
| **Status** | Decidido |
| **Data** | 2026-09-27 |
| **Decisão** | MVVM com JavaFX properties sobre um `core` sem JavaFX |

## Contexto

O escopo da v1 (F1–F7, ver [SCOPE_V1.md](./SCOPE_V1.md)) é pequeno, mas a v1.1 traz preview e reordenar (F8–F9), com telas mais ricas. Requisitos que pesam:

- Lógica de PDF testável sem abrir janela.
- Não travar a UI em PDFs grandes (processar fora da UI thread).
- Não empilhar tudo na `Application`.

Opções consideradas: 2 camadas (`core` + `ui`), **MVVM**, hexagonal. Hexagonal foi descartado (abstrai PDFBox/JavaFX sem necessidade real). MVVM foi escolhido em vez de 2 camadas para manter a lógica de tela testável e escalar para a v1.1.

## Pacotes

```text
com.pdftoolkit
├── PdfToolkitApp        bootstrap: cria serviços, ViewModels e Views
├── core                 PDFBox puro — sem JavaFX
│   ├── PdfFileService       abrir (F1) / salvar (F7)
│   ├── PdfPreview           documento aberto p/ renderizar páginas (F9-lite), AutoCloseable
│   ├── PdfInfo, PageInfo    metadados imutáveis
│   └── PdfOperationException  erro com mensagem legível ao usuário
└── ui                   infraestrutura compartilhada das telas
    ├── BackgroundRunner     contrato: trabalho fora da UI thread
    ├── FxBackgroundRunner   implementação com Task + fila de 1 thread daemon
    ├── UserNotifier         contrato: mensagens de sucesso/erro
    ├── FxImages             BufferedImage → Image (sem javafx.swing)
    └── main                 uma tela = um pacote
        ├── MainViewModel    estado (properties) + ações
        ├── MainView         layout, bindings, FileChooser, Alert
        └── PageCell         célula da lista de páginas (render sob demanda)
```

## Regras

1. **`core` não importa `javafx.*`.** Serviços recebem `Path` + parâmetros, retornam resultado ou lançam `PdfOperationException` (mensagem pronta para o usuário).
2. **Parsing/validação de entrada vive no `core`** como função pura e testada (ex.: intervalos de páginas `"1-3,5"`, usados por F2/F4/F5/F6).
3. **ViewModel** expõe estado via properties/bindings somente leitura e ações como métodos. Não usa `Stage`, `Alert`, `FileChooser` nem `Task` diretamente — recebe `BackgroundRunner` e `UserNotifier` por construtor.
4. **View** só monta layout, faz bindings e abre diálogos nativos (escolha de arquivo). Nenhuma regra de negócio.
5. **Uma tela = um pacote** em `ui/` (`ui.split`, `ui.merge`, …) com seu par View/ViewModel.
6. **Testes:** `core` com PDFs reais gerados em `@TempDir`; ViewModels com `BackgroundRunner` síncrono e `UserNotifier` fake. Views não têm teste automatizado na v1 (smoke manual).
7. Injeção de dependências manual em `PdfToolkitApp` — sem framework de DI.
8. **Duas filas de background:** `pdf-io` (abrir/salvar) e `pdf-render` (páginas), para que uma rolagem longa não atrase um salvamento.
9. **Visualização virtualizada:** `ListView` só cria células das páginas visíveis; cada célula pede a renderização e descarta o pedido se já rolou para outra página. Cache LRU pequeno de imagens na View.
