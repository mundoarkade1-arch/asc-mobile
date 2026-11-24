# Relatório da auditoria de segurança — ASC Mobile

Data: 24/11/2026. Escopo: código do zip `asc-mobile-main` (app Android em Java, sem backend e sem banco no repositório).
Não foi possível compilar nem rodar o app neste ambiente (ver `SECURITY_TESTS.md`).

## Inventário resumido
- 11 telas (Activities); só `LoginActivity` é exportada. Sem WebView, deep links, Services ou Receivers.
- Rede: `HttpURLConnection` para servidores Xtream/M3U informados pelo usuário e para `github.com` (atualizações).
- Dados sensíveis: login do servidor (usuário/senha/host), URL M3U, PIN parental, histórico.
- Permissões: `INTERNET`, `REQUEST_INSTALL_PACKAGES`. `allowBackup=false`.
- Segredos no código: nenhum encontrado. Histórico do Git: não disponível.

## Vulnerabilidades
| ID | Gravidade | Situação do achado | Vulnerabilidade | Local | Status |
|---|---|---|---|---|---|
| V-01 | Alta (se o repo for público) | Confirmada no código; exposição real **não verificada** | Chave de assinatura e senhas em artifact sem criptografia | `.github/workflows/criar-chave.yml` | Corrigida no código. Pendente: ver ação manual 1 |
| V-02 | Média | Confirmada | Redirecionamento https→http levaria usuário e senha sem proteção | `XtreamApi`, `M3uApi`, `LoginChecker` (`open()`) | Corrigida (NÃO VERIFICADO em execução) |
| V-03 | Média | Confirmada | Tráfego http permitido (`usesCleartextTraffic=true`); Xtream manda senha na URL | `AndroidManifest.xml`, `LoginChecker.normalizeServer` | Parcial: aviso no login. Não desligado porque muitos servidores IPTV só têm http |
| V-04 | Baixa | Confirmada | Login e histórico gravados em Base64 (sem criptografia) se o Keystore falhar | `CredentialStore`, `ContinueStore` | Corrigida (NÃO VERIFICADO em execução) |
| V-05 | Baixa | Confirmada | PIN de 4 dígitos (PBKDF2-SHA1, 10.000 iterações) e contador de erros local | `ParentalStore` | Risco aceito: o controle é local e o app é inspecionável |
| V-06 | Informativo | Confirmada | Repositório sem `.gitignore` | raiz | Corrigida |
| V-07 | Informativo | Confirmada | Sem atualização automática de dependências | `.github` | Corrigida (`dependabot.yml`) |
| V-08 | Informativo | Confirmada | Actions fixadas por tag; `minifyEnabled false`; `targetSdk 34` | `apk.yml`, `app/build.gradle` | Não alterado (mudar sem compilar/testar pode quebrar o app) |
| V-09 | Informativo | Confirmada | O SHA-256 do APK vem do mesmo release que o APK | `Updater` | Risco residual: o Android ainda exige a mesma assinatura para atualizar |

Falsos positivos / não se aplicam: SQL/NoSQL Injection, XSS, CSRF, CORS, painel admin, IDOR/BOLA (não há backend nem banco); componentes exportados (só o launcher); WebView (não existe); logs com senha (não há chamadas `Log.*`); SSRF (o app roda no aparelho do próprio usuário).

## Ações manuais (fora do código)
1. Se o repositório for **público** e o fluxo `Criar chave` já foi executado há menos de 1 dia, trate a chave como exposta. Se foi executado há mais de 1 dia, o artifact já expirou (retenção de 1 dia). Trocar a chave exige instalar o app de novo nos aparelhos (a assinatura muda); avalie antes.
2. Conferir em Settings > Secrets que os 4 segredos existem e que não há `.jks` no repositório.
3. Ativar em Settings > Code security: Dependabot alerts e Secret scanning.
4. (Opcional) Fixar as Actions por hash de commit e ativar o CodeQL.

## Riscos residuais
Tráfego http em servidores que não têm https (V-03), PIN curto (V-05), app inspecionável por engenharia reversa (normal em Android).

## Prontidão para produção
Aceitável para distribuição direta (APK) depois de compilar, testar o login/atualização num celular e fazer a ação manual 1. Não declaro segurança absoluta.
