# Projeto cobranca-consumo spring boot microservice

Este é um projeto feito utilizando Java / Maven / Spring Boot (versão 2.0.3), esta aplicação pode ser usada para adicionar consumo de servidores que irão acessar as APIs, podendo ser adicionado seu consumo, e ver a cobrança calculada de acordo com o consumo próprio ou de todos os clientes cadastrados.

## Como executar

Esta aplicação pode ser utilizada em um servidor de aplicações, como exemplo Tomcat ou JBoss basta somente mudar o 'package' no pom para war, mas você também pode rodar utilizando a aplicação em formato jar, rodar usando o comando  ```java -jar```.

* Clone este repositório
* Tenha certeza que está utilizando uma JDK 1.8 e Maven 3.x
* Para realizar o build e rodar a aplicação utilizar o seguinte comando:
```
    mvn package && java -jar target/cobranca-consumo-1.jar
```

## Sobre o serviço

Este é um simples projeto de estudo utilizando serviços REST, ele usa a memória estática para armazenar os dados de um possivel consumo feito por um cliente, e utilizará a soma do tempo utilizado e poderá retornar a quantidade consumida e a cobrança que deve ser efetuada para um cliente em específico ou para todos que fizeram uso da API, portanto ao finalizar a aplicação perderá todos os dados contidos.

* Todas as chamadas terá como retorno um json como formato.

### Chamadas possíveis no sistema

```
  http://localhost:8080
  http://localhost:8080/iniciarConsumo
  http://localhost:8080/consumoServidor
  http://localhost:8080/consumoTodosServidores
```

### Recebendo algumas informações diretamentes do sistema (index)
```
  http://localhost:8080
  GET /
  RESPONSE: HTTP 200 (OK)
  Content-type: application/json
  
  @ Resultado
  {
      "codigoStatus": "200",
      "Para iniciar um novo consumo de servicos, adicionar na URL: ": "/iniciarConsumo",
      "Para realizar uma consulta por servidor, adicionar na URL": "/consumoServidor",
      "Para realizar uma consulta de todos servidores, adicionar na URL": "/consumoTodosServidores",
      "Ajuda": "Este é um codigo open source, para ver a documentacao siga o link: https://github.com/LuisCGS/cobranca-consumo"
  }
```

### Iniciando um novo consumo ao servidor
```
  localhost:8080/iniciarConsumo?uuid=[p1]&horasConsumidas=[p2]
  GET /iniciarConsumo
  RESPONSE: HTTP 201 (CREATED)
  Content-type: application/json
  
  *
    [p1] - informar um uuid válido. Ex.: 123e4567-e89b-12d3-a456-426655440000;
    [p2] - informar um numero. Ex.: 10
    Ex final.: localhost:8080/iniciarConsumo?uuid=123e4567-e89b-12d3-a456-426655440000&horasConsumidas=10
  *
  
  @ Resultado
  {
      "codigoStatus": "201",
      "mensagem": "Servidor com UUID 123e4567-e89b-12d3-a456-426655440000 persistido com sucesso!"
  }
```

### Consultar o consumo de um servidor em específico
```
  localhost:8080/consumoServidor?uuid=[p1]
  GET /consumoServidor
  RESPONSE: HTTP 200 (OK)
  Content-type: application/json
  
  *
   [p1] - informar um uuid válido e existente. Ex.: 123e4567-e89b-12d3-a456-426655440000 
   Ex final.: localhost:8080/consumoServidor?uuid=123e4567-e89b-12d3-a456-426655440000
  *
  
  @ Resultado
  {
    "codigoStatus": "200",
    "consumoHoras": 10,
    "valorCobranca": 20
  }
```

### Consultar o consumo de todos os servidores
```
  localhost:8080/consumoTodosServidores
  GET /consumoTodosServidores
  RESPONSE: HTTP 200 (OK)
  Content-type: application/json
  
  @Resultado
  {
    "codigoStatus": "200",
    "consumoHoras": 10,
    "valorCobranca": 20
  }
```
