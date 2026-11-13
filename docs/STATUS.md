# Status

Última atualização: 13/11/2026. Conversão de híbrido (Capacitor) para nativo só celular com Glass UI.
**Ainda não compilado nem testado**: rodar o Actions e testar num celular.

## Tela por tela
- Home: cabeçalho (ícone, busca, atualizar), destaque (1º filme), Continue assistindo, Ao vivo agora,
  Filmes e Séries em alta, "Ver todos". Navegação inferior em cápsula de vidro (`layout_nav.xml`).
- Canais / Filmes / Séries (`BrowseActivity`, `M3uBrowseActivity`): chips de categoria
  (`CategoryChips`, adultos por último com cadeado/PIN). Canais = cards (`item_channel.xml`,
  `ChannelRow`) com selo HD/FHD/SD, AGORA com barra, A SEGUIR; 24h = barra esmaecida.
  Filmes/séries = grade de 3 colunas.
- Busca: `SearchKeyboard` abre janela com o teclado do celular.
- Player (`MainActivity`): paisagem, ⋮ Opções, deslizar troca canal.

## Decisões
- Vidro feito com formas translúcidas + borda (sem blur real): leve em listas.
- Sem "+ Minha lista" (favoritos não existem no app).
- Sem ícone/fundo de marca enviados: ícone e fundo são vetores/gradientes (trocáveis em
  `ic_launcher_fg.xml`, `ic_launcher_bg.xml`, `bg_gradient.xml`).
- Dados e regras (XtreamApi, M3uApi, stores, EPG, player) não foram alterados.
