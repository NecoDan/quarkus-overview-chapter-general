# Integração Itaú em Python

Porta para Python do pacote Java `itau_challenge`, mantendo as camadas de
controladores HTTP, casos de uso, domínio, portas e adaptadores. A API usa
FastAPI; o adaptador persistente usa SQLite e o adaptador em memória continua
disponível para os fluxos legados.

## Executar

Requer Python 3.11 ou superior. No PowerShell:

```powershell
py -m venv .venv
.\.venv\Scripts\Activate.ps1
py -m pip install -e ".[test]"
$env:APP_KEY_PWD_ENCRYPT = "configure-uma-chave-segura"
py -m uvicorn itau_integration.main:app --app-dir src --reload
```

`APP_KEY_PWD_ENCRYPT` é obrigatório para criptografar e descriptografar
documentos e tokens. Use a mesma chave em reinicializações para acessar os
dados persistidos. O arquivo SQLite padrão é `itau_transactions.sqlite3` no
diretório de execução; configure `ITAU_DATABASE_PATH` para alterar o caminho.
O formato criptográfico Python não é compatível com os valores Jasypt já
persistidos pela aplicação Java; não reutilize o banco Java sem uma migração.

## Rotas

- `POST /itau/transactions/v1`: cria em memória.
- `POST /itau/transactions/v2`: cria no SQLite.
- `GET /itau/transactions/v1`: lista os registros persistidos.
- `GET /itau/transactions/v1/{id}`: busca um registro persistido pelo ID numérico.
- `DELETE /itau/transactions/v1/{id}`: remove da memória pelo ID de transação.
- `DELETE /itau/transactions/v1/`: remove todos os registros em memória.
- `GET /itau/statistics/v1/?intervaloBusca={segundos}`: calcula estatísticas
  sobre os registros em memória.

O corpo de criação mantém os campos `amount`, `createdAt`, `documentNumber` e
`creditCardToken`. A documentação interativa fica disponível em `/docs`.

## Testes

```powershell
py -m pytest
```
