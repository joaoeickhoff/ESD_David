package montepc;
/**
 *
 * @author Pedro
 */
//estrutura usada: Deuqe, permite retirar de ambas extremidades
public class EstruturasDados {

    private int[] dados;
    private int tamanho;

    public EstruturasDados(int capacidade) {
        dados = new int[capacidade];
        tamanho = 0;
    }

    public void addLast(int valor) {
        dados[tamanho] = valor;
        tamanho++;
    }

    public void addFirst(int valor) {
        for (int i = tamanho; i > 0; i--) {
            dados[i] = dados[i - 1];
        }
        dados[0] = valor;
        tamanho++;
    }

    public int removeFirst() {
        int valor = peekFirst();

        for (int i = 0; i < tamanho - 1; i++) {
            dados[i] = dados[i + 1];
        }
        tamanho--;
        return valor;
    }

    public int removeLast() {
        int valor = peekLast();
        tamanho--;
        return valor;
    }

    public int peekFirst() {
        return dados[0];
    }

    public int peekLast() {
        return dados[tamanho - 1];
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public int getSize() {
        return tamanho;
    }

    public void clear() {
        tamanho = 0;
    }

    // Consulta uma posição para desenhar na tela.
    public int get(int indice) {
        return dados[indice];
    }
}