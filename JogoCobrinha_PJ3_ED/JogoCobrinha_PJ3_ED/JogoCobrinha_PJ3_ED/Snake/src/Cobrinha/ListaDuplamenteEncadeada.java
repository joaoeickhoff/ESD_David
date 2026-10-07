package Cobrinha;

public class ListaDuplamenteEncadeada {
    private No cabeca;
    private No cauda;
    private int tamanho;

    public ListaDuplamenteEncadeada() {
        cabeca = null;
        cauda = null;
        tamanho = 0;
    }

    public No getCabeca() {
        return cabeca; 
    }
    
    public No getCauda() {
        return cauda; 
    }
    
    public int getTamanho() {
        return tamanho; 
    }

    public boolean estaVazia() {
        if(cabeca == null){
            return true;
        }else{
            return false;
        }
    }

    public void inserirNaCabeca(Posicao posicao) {
        No novo = new No(posicao);
        
        if(estaVazia()){
            cabeca = novo;
            cauda = novo;
        }else{
            novo.setProximo(cabeca);
            cabeca.setAnterior(novo);
            
            cabeca = novo;
        }
        tamanho ++;
    }

    public Posicao removerDaCauda() {
        if(estaVazia()){
            return null;
        }
        
        No removido = cauda;
        Posicao posicaoRemovida = removido.getPosicao();
        
        if(tamanho == 1){
            cabeca = null;
            cauda = null;
        }else{
            cauda = removido.getAnterior();
            cauda.setProximo(null);
        }
        
        removido.setAnterior(null);
        removido.setProximo(null);
        
        tamanho--;
        
        return posicaoRemovida;
    }

    public boolean contem(Posicao posicao) {
        No atual = cabeca;
        
        while(atual != null){
            if(atual.getPosicao().mesmaPosicao(posicao)){
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    public void limpar() {
        cabeca = null;
        cauda = null;
        tamanho = 0;
    }
}
