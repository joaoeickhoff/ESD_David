package Cobrinha;

import java.util.Random;

public class Fruta {
    private Posicao posicao;
    private final Random sorteador = new Random();

    public Posicao getPosicao() {
        return posicao;
    }

    public boolean gerar(Tabuleiro tabuleiro, ListaDuplamenteEncadeada corpo) {
        // TODO 14: escolher célula livre; atualizar posição; retornar true.
        // Se não há célula livre, definir posição como null e retornar false.
        // Evitar sorteio infinito quando o tabuleiro estiver cheio.
        throw new UnsupportedOperationException("TODO: gerar fruta");
    }
}
