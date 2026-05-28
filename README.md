# Minimizador Lógico Quine-McCluskey Concorrente (EDA Tool)

Este repositório contém uma ferramenta EDA (Electronic Design Automation) desenvolvida em **Java** para a minimização exata de funções booleanas utilizando o algoritmo clássico de **Quine-McCluskey**. O projeto foi projetado especificamente para suportar o processamento de circuitos de larga escala (testado com benchmarks de até 32 variáveis) através do uso de programação concorrente nativa (*Multithreading*).

Projeto desenvolvido como parte prática das atividades do laboratório de Sistemas Digitais do Departamento de Computação (DCOMP) da Universidade Federal de Sergipe (UFS).

---

## 🚀 Diferenciais e Características Técnicas

- **Arquitetura Baseada em Strings:** Processamento flexível que elimina problemas de transbordo de memória (*Arithmetic Overflow*) ao ler termos binários longos (suporta circuitos com mais de 15 variáveis, estendendo-se até 32 bits de entrada).
- **Concorrência Manual (Multithreading):** Divisão cartesiana da tabela de agrupamentos por níveis de contagem de bits `1`, onde cada par de blocos adjacentes é processado de forma assíncrona por uma thread dedicada (`Agrupamentos`).
- **Sincronização por Barreira (`.join()`):** Controle de fluxo iterativo estrito, garantindo que a Thread principal aguarde a convergência de todos os agrupamentos paralelos antes de avançar para a próxima fase da tabela.
- **Leitor PLA Avançado:** Interpretador compatível com o formato industrial `.pla` (utilizado pelo Espresso de Berkeley), com suporte à captura dinâmica de rótulos de entrada através da diretiva `.ilb`.
- **Mapeamento Literal Inteligente:** Dedução automática de variáveis independentes e ocultação imediata de termos redundantes (*don't cares* / `-`), gerando expressões booleanas em Soma de Produtos de forma limpa.

---

## 🛠️ Estrutura do Projeto

O código está organizado dentro do pacote `quineMcCluskey` e subdividido nas seguintes classes:

1. **`ItemLogico.java`**: Modela a linha binária da tabela verdade e computa as reduções bit a bit combinando termos adjacentes com distância de Hamming igual a 1.
2. **`Agrupamentos.java`**: Linha de execução concorrente (`extends Thread`) que isola o laço de cruzamento combinatório da CPU.
3. **`LeitorPLA.java`**: Gerencia o parse do arquivo de texto, filtrando apenas mintermos ativos (saída `1`) e mapeando os rótulos `.ilb`.
4. **`AlgoritmoQM.java`**: O motor central do sistema. Organiza o mapeamento inicial por grupos de peso (quantidade de uns) e sincroniza as iterações.
5. **`ExecutarTeste.java`**: Classe de ponto de entrada (`main`) que gerencia os tempos de execução do *benchmarking* através do `System.currentTimeMillis()`.

---

## 📊 Como Utilizar

### Pré-requisitos
- Java JDK 17 ou superior.
- Uma IDE de sua preferência (Eclipse, IntelliJ) ou terminal configurado.

### Configuração do Caminho
Dentro da classe `ExecutarTeste.java`, defina o caminho absoluto ou relativo do seu arquivo de teste `.pla`:

```java
String caminhoArquivo = "src/arquivosTeste/meu_circuito.pla";
