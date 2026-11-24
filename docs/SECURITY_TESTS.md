# Testes de segurança

## Executados neste ambiente
| Teste | Resultado |
|---|---|
| Leitura de todo o manifest, build, workflows e dos arquivos de rede/armazenamento | Feito |
| Busca por segredos (api key, token, senha, chaves privadas, .jks, .env) | Nada encontrado no código |
| Busca por logs, WebView, componentes exportados, intents inseguros | Nada encontrado |
| Análise de sintaxe Java dos 6 arquivos alterados (javalang) | Aprovada |
| Validação dos arquivos YAML alterados | Aprovada |

## NÃO VERIFICADO (precisa do seu celular / do GitHub Actions)
- Compilação do app (sem Android SDK aqui). Rodar o workflow **Gerar APK** e confirmar que fica verde.
- Login Xtream e M3U, com e sem https; redirecionamento https→http deve falhar com erro de conexão.
- Reabrir o app mantém o login; "Continue assistindo" continua funcionando.
- Fluxo `Criar chave` com e sem o segredo `EXPORT_PASS` (sem ele deve falhar com mensagem clara).

Ferramentas como Semgrep, CodeQL, Gitleaks, Trivy e OWASP ZAP **não foram executadas**.
