package jogodacobrinha;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import jogodacobrinha.estruturas.ListaDuplamenteEncadeada;

/**
 * A cobrinha controlada pelo jogador.
 *
 * O corpo é uma ListaDuplamenteEncadeada de posições:
 *   - o INÍCIO da lista é a CABEÇA;
 *   - o FIM da lista é a ponta do RABO.
 *
 * A cada passo a cobrinha não "arrasta" todos os pedaços: ela ganha uma
 * cabeça nova no início da lista e perde o último pedaço do fim da lista.
 * As duas operações são O(1) na lista duplamente encadeada.
 */
public class Cobrinha {

    private static final int TAMANHO_INICIAL = 4;

    private final ListaDuplamenteEncadeada<Posicao> corpo;

    private Direcao direcaoAtual;   // direção em que a cobrinha andou no último passo
    private Direcao proximaDirecao; // direção pedida pelo jogador para o próximo passo

    private final Color corCabeca;
    private final Color corCorpo;

    /**
     * Cria a cobrinha deitada na horizontal, olhando para a direita.
     *
     * @param colunaCabeca Coluna inicial da cabeça.
     * @param linha Linha inicial.
     * @param corCabeca Cor da cabeça.
     * @param corCorpo Cor do corpo.
     */
    public Cobrinha( int colunaCabeca, int linha, Color corCabeca, Color corCorpo ) {

        this.corCabeca = corCabeca;
        this.corCorpo = corCorpo;
        this.corpo = new ListaDuplamenteEncadeada<>();

        // a cabeça entra primeiro; os demais pedaços vão para o fim (rabo)
        for ( int i = 0; i < TAMANHO_INICIAL; i++ ) {
            corpo.inserirNoFim( new Posicao( colunaCabeca - i, linha ) );
        }

        direcaoAtual = Direcao.DIREITA;
        proximaDirecao = Direcao.DIREITA;

    }

    /**
     * Pede para a cobrinha virar. Meia-volta (direção oposta) é ignorada,
     * pois a cabeça entraria no próprio corpo.
     *
     * @param nova Direção desejada.
     */
    public void mudarDirecao( Direcao nova ) {
        if ( !nova.ehOpostaA( direcaoAtual ) ) {
            proximaDirecao = nova;
        }
    }

    /**
     * Anda uma célula na direção atual. Ao sair por uma borda do campo,
     * a cobrinha reaparece pela borda oposta.
     *
     * @param colunas Quantidade de colunas do campo.
     * @param linhas Quantidade de linhas do campo.
     */
    public void mover( int colunas, int linhas ) {

        direcaoAtual = proximaDirecao;

        Posicao cabeca = corpo.getPrimeiro();
        int novaColuna = ( cabeca.getColuna() + direcaoAtual.getDx() + colunas ) % colunas;
        int novaLinha = ( cabeca.getLinha() + direcaoAtual.getDy() + linhas ) % linhas;

        corpo.inserirNoInicio( new Posicao( novaColuna, novaLinha ) ); // nova cabeça
        corpo.removerDoFim();                                           // rabo sai

    }

    /**
     * Desenha a cobrinha no campo.
     *
     * A lista é percorrida do fim (rabo) para o início (cabeça), usando os
     * ponteiros "anterior", para que a cabeça seja desenhada por último,
     * por cima de todo o resto.
     *
     * @param e A engine.
     * @param xCampo X do canto superior esquerdo do campo.
     * @param yCampo Y do canto superior esquerdo do campo.
     * @param tamanhoCelula Tamanho, em pixels, de cada célula.
     */
    public void desenhar( EngineFrame e, double xCampo, double yCampo, double tamanhoCelula ) {

        Posicao cabeca = corpo.getPrimeiro();

        for ( Posicao p : corpo.doFimParaOInicio() ) {

            double x = xCampo + p.getColuna() * tamanhoCelula;
            double y = yCampo + p.getLinha() * tamanhoCelula;

            if ( p == cabeca ) {
                desenharSegmento( e, x, y, tamanhoCelula, corCabeca, true, direcaoAtual );
            } else {
                desenharSegmento( e, x, y, tamanhoCelula, corCorpo, false, direcaoAtual );
            }

        }

    }

    /**
     * Desenha um pedaço da cobrinha (cabeça ou corpo) em uma célula.
     * Também é usado na prévia da tela de personalização e no menu.
     */
    public static void desenharSegmento( EngineFrame e, double x, double y, double tamanho,
                                         Color cor, boolean ehCabeca, Direcao direcao ) {

        double margem = Math.max( 1, tamanho * 0.08 );
        double lado = tamanho - margem * 2;
        double arredondamento = ehCabeca ? tamanho * 0.6 : tamanho * 0.35;

        e.fillRoundRectangle( x + margem, y + margem, lado, lado, arredondamento, cor );
        e.setStrokeLineWidth( 2 );
        e.drawRoundRectangle( x + margem, y + margem, lado, lado, arredondamento, corDoContorno( cor ) );
        e.setStrokeLineWidth( 1 );

        if ( ehCabeca ) {

            // olhos posicionados para a frente, conforme a direção
            double cx = x + tamanho / 2;
            double cy = y + tamanho / 2;
            double dx = direcao.getDx();
            double dy = direcao.getDy();
            double frente = tamanho * 0.14;
            double lateral = tamanho * 0.20;
            double raioOlho = tamanho * 0.17;
            double raioPupila = tamanho * 0.09;

            for ( int lado2 = -1; lado2 <= 1; lado2 += 2 ) {

                double ox = cx + dx * frente + ( -dy ) * lateral * lado2;
                double oy = cy + dy * frente + dx * lateral * lado2;

                e.fillCircle( ox, oy, raioOlho, Color.WHITE );
                e.drawCircle( ox, oy, raioOlho, new Color( 40, 40, 40 ) );
                e.fillCircle( ox + dx * tamanho * 0.05, oy + dy * tamanho * 0.05, raioPupila, new Color( 20, 20, 20 ) );

            }

        }

    }

    /**
     * Cor do contorno de um pedaço: uma versão mais escura da própria cor.
     * Para cores muito escuras (preto), usa um cinza, senão o contorno some.
     */
    private static Color corDoContorno( Color c ) {

        if ( c.getRed() + c.getGreen() + c.getBlue() < 180 ) {
            return new Color( 120, 120, 120 );
        }

        return new Color(
            (int) ( c.getRed() * 0.55 ),
            (int) ( c.getGreen() * 0.55 ),
            (int) ( c.getBlue() * 0.55 )
        );

    }

}
