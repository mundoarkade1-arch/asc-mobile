# Checklist de segurança

Antes de cada versão:
- [ ] O Actions compilou sem erro e o APK instala por cima da versão antiga.
- [ ] Login por servidor (https e http) e por lista M3U funcionam; aparece o aviso quando é http.
- [ ] Sair e entrar de novo mantém o login (e "Continue assistindo" aparece).
- [ ] Controle parental pede a senha nas categorias adultas.
- [ ] Atualização automática baixa, confere e abre o instalador.
- [ ] Nenhum `.jks`, senha ou lista M3U no repositório (`git log` também).
- [ ] Os 4 segredos existem em Settings > Secrets > Actions.
- [ ] Dependabot alerts e Secret scanning ativados.
- [ ] Pull requests do Dependabot revisados (e o app testado depois de atualizar).
