package jogodacobrinha;

/**
 * Direções em que a cobrinha pode andar.
 * dx e dy indicam quanto a cabeça anda em coluna e linha a cada passo.
 */
public enum Direcao {

    CIMA( 0, -1 ),
    BAIXO( 0, 1 ),
    ESQUERDA( -1, 0 ),
    DIREITA( 1, 0 );

    private final int dx;
    private final int dy;

    private Direcao( int dx, int dy ) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    /**
     * Verifica se esta direção é o oposto exato da outra
     * (ex.: CIMA e BAIXO). A cobrinha não pode dar meia-volta.
     */
    public boolean ehOpostaA( Direcao outra ) {
        return dx == -outra.dx && dy == -outra.dy;
    }

}
