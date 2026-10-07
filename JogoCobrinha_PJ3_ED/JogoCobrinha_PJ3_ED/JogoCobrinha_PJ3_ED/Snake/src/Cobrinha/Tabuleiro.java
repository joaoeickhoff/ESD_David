package Cobrinha;

public class Tabuleiro {
    public static final int COLUNAS = 24;
    public static final int LINHAS = 20;
    public static final int TAMANHO_CELULA = 24;
    public static final int ORIGEM_X = 24;
    public static final int ORIGEM_Y = 80;

    public boolean estaDentro(Posicao posicao) {
        int x = posicao.getX();
        int y = posicao.getY();

        if(x < 0 || x >= COLUNAS){
            return false;
        }
        if(y < 0 || y >= LINHAS){
            return false;
        }
        return true;
    }
}
