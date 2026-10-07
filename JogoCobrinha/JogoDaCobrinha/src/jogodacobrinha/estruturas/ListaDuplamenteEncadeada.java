package jogodacobrinha.estruturas;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista duplamente encadeada genérica, implementada do zero.
 *
 * Cada nó guarda um valor e dois ponteiros: um para o nó anterior e outro
 * para o próximo. A lista mantém referências para o primeiro nó (início) e
 * para o último nó (fim), o que permite inserir e remover nas duas pontas
 * em tempo constante, O(1).
 *
 *   inicio                         fim
 *     |                             |
 *     v                             v
 *   [ A ] <-> [ B ] <-> [ C ] <-> [ D ]
 *
 * No jogo, essa lista representa o corpo da cobrinha: o início é a cabeça
 * e o fim é a ponta do rabo.
 *
 * @param <T> Tipo dos valores armazenados na lista.
 */
public class ListaDuplamenteEncadeada<T> implements Iterable<T> {

    /**
     * Nó da lista: um valor e os ponteiros para os vizinhos.
     */
    private static class No<T> {

        T valor;
        No<T> anterior;
        No<T> proximo;

        No( T valor ) {
            this.valor = valor;
        }

    }

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    /**
     * Insere um valor no início da lista. O(1)
     *
     * @param valor Valor a ser inserido.
     */
    public void inserirNoInicio( T valor ) {

        No<T> novo = new No<>( valor );

        if ( estaVazia() ) {
            inicio = novo;
            fim = novo;
        } else {
            novo.proximo = inicio;
            inicio.anterior = novo;
            inicio = novo;
        }

        tamanho++;

    }

    /**
     * Insere um valor no fim da lista. O(1)
     *
     * @param valor Valor a ser inserido.
     */
    public void inserirNoFim( T valor ) {

        No<T> novo = new No<>( valor );

        if ( estaVazia() ) {
            inicio = novo;
            fim = novo;
        } else {
            novo.anterior = fim;
            fim.proximo = novo;
            fim = novo;
        }

        tamanho++;

    }

    /**
     * Remove e retorna o valor do início da lista. O(1)
     *
     * @return O valor que estava no início.
     * @throws NoSuchElementException Se a lista estiver vazia.
     */
    public T removerDoInicio() {

        if ( estaVazia() ) {
            throw new NoSuchElementException( "A lista está vazia!" );
        }

        T valor = inicio.valor;

        if ( inicio == fim ) {
            inicio = null;
            fim = null;
        } else {
            inicio = inicio.proximo;
            inicio.anterior = null;
        }

        tamanho--;
        return valor;

    }

    /**
     * Remove e retorna o valor do fim da lista. O(1)
     *
     * Graças ao ponteiro "anterior", não é preciso percorrer a lista para
     * descobrir quem é o penúltimo nó.
     *
     * @return O valor que estava no fim.
     * @throws NoSuchElementException Se a lista estiver vazia.
     */
    public T removerDoFim() {

        if ( estaVazia() ) {
            throw new NoSuchElementException( "A lista está vazia!" );
        }

        T valor = fim.valor;

        if ( inicio == fim ) {
            inicio = null;
            fim = null;
        } else {
            fim = fim.anterior;
            fim.proximo = null;
        }

        tamanho--;
        return valor;

    }

    /**
     * Retorna, sem remover, o valor do início da lista.
     *
     * @return O primeiro valor.
     * @throws NoSuchElementException Se a lista estiver vazia.
     */
    public T getPrimeiro() {
        if ( estaVazia() ) {
            throw new NoSuchElementException( "A lista está vazia!" );
        }
        return inicio.valor;
    }

    /**
     * Retorna, sem remover, o valor do fim da lista.
     *
     * @return O último valor.
     * @throws NoSuchElementException Se a lista estiver vazia.
     */
    public T getUltimo() {
        if ( estaVazia() ) {
            throw new NoSuchElementException( "A lista está vazia!" );
        }
        return fim.valor;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Remove todos os elementos da lista.
     */
    public void limpar() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }

    /**
     * Percorre a lista do início para o fim, seguindo os ponteiros "proximo".
     * Permite usar a lista em um for-each: for ( T v : lista ) { ... }
     */
    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            private No<T> atual = inicio;

            @Override
            public boolean hasNext() {
                return atual != null;
            }

            @Override
            public T next() {
                if ( atual == null ) {
                    throw new NoSuchElementException();
                }
                T valor = atual.valor;
                atual = atual.proximo;
                return valor;
            }

        };

    }

    /**
     * Percorre a lista do fim para o início, seguindo os ponteiros "anterior".
     * Uso: for ( T v : lista.doFimParaOInicio() ) { ... }
     *
     * @return Um Iterable que caminha de trás para frente.
     */
    public Iterable<T> doFimParaOInicio() {

        return () -> new Iterator<T>() {

            private No<T> atual = fim;

            @Override
            public boolean hasNext() {
                return atual != null;
            }

            @Override
            public T next() {
                if ( atual == null ) {
                    throw new NoSuchElementException();
                }
                T valor = atual.valor;
                atual = atual.anterior;
                return valor;
            }

        };

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder( "[" );

        for ( No<T> no = inicio; no != null; no = no.proximo ) {
            sb.append( no.valor );
            if ( no.proximo != null ) {
                sb.append( " <-> " );
            }
        }

        return sb.append( "]" ).toString();

    }

}
