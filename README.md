# ilegra-agibank-prova

Sistema de Análise de Dados

## Requerimentos

Para construir e rodar a aplicação você vai precisar:

- [JDK 14](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html)
- [Gradle](https://gradle.org/install/)

## Rodando a aplicação localmente

Basta importar o projeto numa IDE como projeto Gradle e executar a classe IlegraAgibankApplication.java

Ou alternativamente executar:
```shell
 ./gradlew bootRun
```
## Arquivos .dat para teste

- [files.zip ](https://drive.google.com/file/d/1PnZe_-9wDKTtr1mFZQZGJvRb-7M5cC_w/view?usp=sharing) extrair para pasta %HOMEPATH%/data/in


## Acesso ao console h2 (banco de dados em memória)


```shell
http://localhost:8181/h2-console  
```
- JDBC URL: jdbc:h2:mem:ilegra-agibank
- User Name: sa
- Password:


## Docker**

- [Docker](https://www.docker.com/products/docker-desktop)

Para rodar a aplicação via docker executar os seguintes comandos na raiz do projeto

```shell
 ./gradlew bootJar
 docker build -t ilegra-agibank:0.0.1-SNAPSHOT .
 docker run ilegra-agibank:0.0.1-SNAPSHOT
```
**Experimental

## Outras Informações

- A job é executada a cada 30 segundos
- Caso os diretórios não existam o sistema irá criá-los
- O sistema produzirá um resumo de todos os erros e linhas não processadas no arquivo final de saída.
- A aplicação contém um Dockerfile para geração de imagem. Caso rode a aplicação via Docker importante prestar atenção que os diretórios ficarão dentro do container.
 O que não recomendo para fazer a avaliação e os testes da aplicação.  Infelizmente iria demandar mais um tempo para ajustar a aplicação para apontar os diretórios para fora do container.

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
