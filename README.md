# ASC play

App Android **nativo, só para celular** (vertical), com visual de vidro (Glass UI) azul.
Sem WebView, sem Capacitor, sem versão para TV.

- Login por servidor (Xtream) ou lista M3U; canais com guia (AGORA / A SEGUIR), filmes, séries,
  continue assistindo, busca, controle parental (PIN), atualização automática.
- Player Media3 em paisagem. Toque na tela mostra os controles; botão ⋮ abre programação
  (canal) ou áudio e legendas (filme/série); deslizar para cima/baixo troca de canal.
- applicationId `com.ascplay.app`, minSdk 26. Atualizar por cima exige a mesma chave de assinatura.

## Gerar o APK
O workflow **Gerar APK** roda a cada push em `main`. Secrets: `KEYSTORE_B64`, `KEYSTORE_PASSWORD`,
`KEY_ALIAS`, `KEY_PASSWORD` (mesma chave das versões instaladas). Sem eles sai só um APK de teste.

## Estrutura
- `app/src/main/java/com/ascplay/app/`: telas e dados (ver `docs/STATUS.md`).
- `app/src/main/res/`: tema azul de vidro (`bg_*.xml`), layouts verticais, ícones em vetor.
- `.github/workflows/apk.yml`: build.
