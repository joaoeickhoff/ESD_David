package Cobrinha;

public class No {
    private Posicao posicao;
    private No anterior;
    private No proximo;

    public No(Posicao posicao) {
        this.posicao = posicao;
    }

    public Posicao getPosicao() {
        return posicao;
    }
    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    public No getAnterior() {
        return anterior;
    }
    public void setAnterior(No anterior) {
        this.anterior = anterior;
    }
    public No getProximo() {
        return proximo;
    }
    public void setProximo(No proximo) {
        this.proximo = proximo;
    }
}
