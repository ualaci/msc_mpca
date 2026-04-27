# Laboratório 01: Linguagens Regulares (Teoria da Computação)

Este repositório contém a solução para o Laboratório 01, implementado em Scala 3 seguindo o paradigma funcional.

## Estrutura do Projeto

* `src/main/scala/lab1/Automata.scala`: Definição funcional das estruturas NFAe, NFA e DFA.
* `src/main/scala/lab1/Converter.scala`: Algoritmos puramente funcionais de eliminação de epsilon-transições e determinização (Subset Construction).
* `src/main/scala/lab1/Regex.scala`: Árvore Sintática (AST) para Expressões Regulares e um Parser construído com `scala-parser-combinators`.
* `src/main/scala/lab1/Thompson.scala`: Conversão de Regex AST para NFAe usando a Construção de Thompson.
* `src/main/scala/lab1/YamlIO.scala`: Manipulação e I/O de YAML utilizando a biblioteca `circe`.
* `src/main/scala/lab1/Main.scala`: Ponto de entrada (CLI).

## Reprodutibilidade e Ambiente

O projeto suporta tanto o uso do `devcontainer` original fornecido na pasta `.devcontainer` (que foi mantido intacto), quanto a reprodução através do `nix` (adicionado arquivo `flake.nix`).

### Usando Nix

Se você possui o [Nix](https://nixos.org/) instalado com suporte a flakes, ative o shell com todas as dependências (OpenJDK 21, Scala 3, sbt) garantidas:

```bash
nix develop
```

## Como Executar e Testar

### 1. Conversão de Autômatos (Parte 1)

Você pode converter um autômato (ex: NFA-epsilon) para DFA utilizando o comando:

```bash
sbt "run convert <arquivo_entrada.yaml> <arquivo_saida.yaml>"
```

**Exemplo:**
Crie um arquivo `test.yaml` com a especificação (lembrando de colocar aspas no epsilon se necessário ou escrever corretamente):
```yaml
type: nfae
alphabet: ["0", "1"]
states: [q0, q1, q2]
initial_state: q0
final_states: [q2]
transitions:
  - from: q0
    symbol: "0"
    to: [q0, q1]
  - from: q0
    symbol: epsilon
    to: [q1]
  - from: q1
    symbol: "1"
    to: [q2]
```

E execute:
```bash
sbt "run convert test.yaml dfa_out.yaml"
```
O arquivo `dfa_out.yaml` conterá o DFA gerado pelo processo de subset construction.

### 2. Regex para NFA-epsilon (Parte 2)

Para testar o motor de Regex, gerar sua AST e em seguida o NFA-epsilon através da construção de Thompson:

```bash
sbt "run regex \"a(a|b)*b\" thompson_out.yaml"
```

A sintaxe suportada inclui:
* Concatenação: `ab`
* União: `a|b`
* Fecho de Kleene: `a*`
* Uma ou mais repetições: `a+`
* Opcional: `a?`
* Parênteses para agrupamento: `(a|b)`
* Caracteres de escape: `\*`

### 3. Regex Crossword (Parte 3)

Conforme as instruções, as resoluções dos "Challenges" e o quebra-cabeça criado devem ser documentados em um PDF à parte contendo as capturas de tela. Esse material deve ser entregue junto com este repositório zipado.
