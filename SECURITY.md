# Segurança

## Como avisar sobre um problema de segurança
Não abra um "Issue" público com dados sensíveis. Fale direto com o dono do projeto.

## Regras do projeto
- Nunca subir senhas, chaves ou arquivos `.jks` para o GitHub (o `.gitignore` já bloqueia os mais comuns).
- As chaves de assinatura ficam só nos **Secrets** do GitHub: `KEYSTORE_B64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.
- Logins e listas do app ficam criptografados no aparelho (Android Keystore). Se o aparelho não permitir, o login fica só na memória.
- Atualizações só são instaladas se vierem de `github.com/<repo>/releases`, por https, e com SHA-256 conferido.

## Recomendações para produção
Veja `docs/SECURITY_AUDIT.md` (riscos que sobraram) e `docs/SECURITY_CHECKLIST.md`.
