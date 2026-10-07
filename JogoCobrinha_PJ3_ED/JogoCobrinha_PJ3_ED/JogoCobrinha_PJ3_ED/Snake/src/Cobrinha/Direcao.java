package Cobrinha;

public enum Direcao {
    CIMA(0, -1), BAIXO(0, 1), ESQUERDA(-1, 0), DIREITA(1, 0);

    private final int dx;
    private final int dy;

    Direcao(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }

    public boolean ehOposta(Direcao outra) {
        if(outra == null){
            return false;
        }
        if(dx == -outra.getDx() && dy == -outra.getDy()){
            return true;
        }else{
            return false;
        }
    }
}
