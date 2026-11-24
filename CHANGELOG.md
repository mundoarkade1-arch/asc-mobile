# Changelog

## Controle parental (24/11/2026)
- Corrigido: a lista de nomes adultos era curta e não pegava "OnlyFans" nem "Privacy". Agora é uma lista única e ampla (categoria, canal, filme e série), sem diferença entre categoria e nome. Também pega "sexo", "erótico", "nude", "fetiche", "18+", etc., sem bloquear "sexta".

## Auditoria de segurança (24/11/2026)
- Corrigido: fluxo `criar-chave.yml` publicava a chave de assinatura e as senhas em texto aberto num artifact; agora sai criptografado e exige o segredo `EXPORT_PASS`.
- Corrigido: redirecionamento de https para http não é mais seguido em `XtreamApi`, `M3uApi` e `LoginChecker` (a senha viaja na URL).
- Corrigido: `CredentialStore` e `ContinueStore` não gravam mais em texto aberto se o Keystore falhar.
- Novo: aviso na tela de login quando o endereço usa http.
- Novo: `.gitignore` (chaves, segredos e arquivos de build) e `.github/dependabot.yml` (atualização semanal de dependências e Actions).
- Novo: documentos `SECURITY.md`, `docs/SECURITY_AUDIT.md`, `docs/SECURITY_CHECKLIST.md`, `docs/SECURITY_TESTS.md`.
