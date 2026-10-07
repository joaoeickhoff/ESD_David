package jogodacobrinha;

/**
 * Uma posição (célula) no campo do jogo, em coluna e linha.
 * Cada pedaço do corpo da cobrinha ocupa uma posição.
 */
public class Posicao {

    private final int coluna;
    private final int linha;

    public Posicao( int coluna, int linha ) {
        this.coluna = coluna;
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "(" + coluna + ", " + linha + ")";
    }

}
