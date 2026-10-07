# Jogo da Cobrinha (versão 1)

Jogo feito com a JSGE para o projeto de Estrutura de Dados.

O corpo da cobrinha é uma **lista duplamente encadeada** implementada do zero
(`src/jogodacobrinha/estruturas/ListaDuplamenteEncadeada.java`). A cada passo,
uma nova cabeça é inserida no início da lista e o rabo é removido do fim, ambos em O(1).

## Como abrir
Abra a pasta do projeto no Apache NetBeans (File > Open Project) e rode com F6.
A classe principal é `jogodacobrinha.Main`.

## Controles
- WASD ou setas: movimentam a cobrinha
- ESC: pausa / continua
- V: vai para a tela de vitória
