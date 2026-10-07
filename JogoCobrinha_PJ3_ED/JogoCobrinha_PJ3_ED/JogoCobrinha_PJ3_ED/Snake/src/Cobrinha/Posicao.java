package Cobrinha;

public class Posicao {
    private final int x;
    private final int y;

    public Posicao(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public boolean mesmaPosicao(Posicao outra) {
        if(outra == null){
            return false;
        }
        if(x == outra.getX() && y == outra.getY()){
            return true;
        }else{
            return false;
        }
    }
}
