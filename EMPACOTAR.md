# Empacotamento

O projeto gera um `.jar` executavel e possui perfis Maven para empacotar o app com `jpackage`.

## Requisitos

- Java 17 ou superior
- Maven instalado
- `jpackage` disponivel no ambiente

## Gerar o jar executavel

```bash
mvn clean package
```

O arquivo sera gerado em:

```bash
target/ficha-em-cmd-1.0-SNAPSHOT.jar
```

Para executar:

```bash
java -jar target/ficha-em-cmd-1.0-SNAPSHOT.jar
```

## Gerar pacote para macOS

Execute no macOS:

```bash
mvn clean package -Ppackage-mac
```

Saida esperada:

```bash
target/mac/FichaEmCmd.app
```

Ao abrir pelo Finder, o app agora abre um Terminal automaticamente para executar o programa interativo.

## Gerar executavel para Windows

Execute no Windows:

```bash
mvn clean package -Ppackage-windows
```

Saida esperada:

```bash
target/windows/FichaEmCmd.exe
```

## Observacao importante

O `jpackage` nao gera executaveis de outro sistema operacional. Isso significa:

- para gerar `.app` ou `.dmg`, rode no macOS
- para gerar `.exe`, rode no Windows
