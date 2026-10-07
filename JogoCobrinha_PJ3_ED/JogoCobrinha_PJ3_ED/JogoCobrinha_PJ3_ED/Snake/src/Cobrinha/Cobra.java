package Cobrinha;

public class Cobra {
    private final ListaDuplamenteEncadeada corpo;
    private Direcao direcao;
    private Direcao proximaDirecao;

    public Cobra() {
        corpo = new ListaDuplamenteEncadeada();
        direcao = Direcao.DIREITA;
        proximaDirecao = Direcao.DIREITA;
    }

    public ListaDuplamenteEncadeada getCorpo() {
        return corpo;
    }
    public Direcao getDirecao() {
        return direcao;
    }

    public void reiniciar() {
        corpo.limpar();
        
        direcao = Direcao.DIREITA;
        proximaDirecao = Direcao.DIREITA;
        
        corpo.inserirNaCabeca(new Posicao(8, 10));
        corpo.inserirNaCabeca(new Posicao(9, 10));
        corpo.inserirNaCabeca(new Posicao(10, 10));
    }

    public void mudarDirecao(Direcao novaDirecao) {
        if(novaDirecao != null){
            if(direcao.ehOposta(novaDirecao) == false){
                proximaDirecao = novaDirecao;
            }
        }
    }

    public Posicao calcularProximaCabeca() {
        Posicao atual = corpo.getCabeca().getPosicao();
        
        int x = atual.getX() + proximaDirecao.getDx();
        int y = atual.getY() + proximaDirecao.getDy();

        Posicao novaCabeca = new Posicao(x, y);
        return novaCabeca;
    }

    public boolean colideComCorpo(Posicao novaCabeca, boolean vaiCrescer) {
        No atual = corpo.getCabeca();
        
        while(atual != null){
            boolean verificarNo = true;
            if(vaiCrescer == false && atual == corpo.getCauda()){
                verificarNo = false;
            }
            if(verificarNo){
                if(atual.getPosicao().mesmaPosicao(novaCabeca)){
                    return true;
                }
            }
            atual = atual.getProximo();
        }
        return false;
    }

    public void mover(Posicao novaCabeca, boolean crescer) {
        corpo.inserirNaCabeca(novaCabeca);
        
        if(crescer == false){
            corpo.removerDaCauda();
        }
        direcao = proximaDirecao;
    }
         
    public Direcao getProximaDirecao() {
        return proximaDirecao;
    }
}
