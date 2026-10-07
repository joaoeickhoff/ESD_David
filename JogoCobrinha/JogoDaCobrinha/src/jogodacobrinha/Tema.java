package jogodacobrinha;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import java.awt.Color;

/**
 * Cores e pequenos ajudantes de desenho compartilhados pelas telas.
 */
public final class Tema {

    // fundo das telas de menu
    public static final Color FUNDO = new Color( 20, 29, 24 );
    public static final Color FUNDO_XADREZ = new Color( 24, 35, 29 );

    // painéis e botões
    public static final Color PAINEL = new Color( 30, 43, 36 );
    public static final Color BORDA = new Color( 62, 86, 72 );
    public static final Color BOTAO = new Color( 38, 54, 45 );
    public static final Color BOTAO_DESABILITADO = new Color( 30, 38, 34 );

    // textos e destaques
    public static final Color TEXTO = new Color( 232, 240, 234 );
    public static final Color TEXTO_SUAVE = new Color( 150, 170, 158 );
    public static final Color TEXTO_DESABILITADO = new Color( 90, 104, 96 );
    public static final Color DESTAQUE = new Color( 166, 227, 92 );
    public static final Color OURO = new Color( 255, 200, 40 );

    // campo de jogo (xadrez claro, para todas as cores da cobrinha aparecerem)
    public static final Color CAMPO_CLARO = new Color( 236, 230, 214 );
    public static final Color CAMPO_ESCURO = new Color( 225, 218, 199 );
    public static final Color BARRA_SUPERIOR = new Color( 16, 24, 19 );

    private Tema() {
    }

    /**
     * Pinta o fundo quadriculado das telas de menu.
     */
    public static void desenharFundo( EngineFrame e ) {

        e.clearBackground( FUNDO );
        int tam = 40;

        for ( int lin = 0; lin * tam < e.getScreenHeight(); lin++ ) {
            for ( int col = 0; col * tam < e.getScreenWidth(); col++ ) {
                if ( ( lin + col ) % 2 == 0 ) {
                    e.fillRectangle( col * tam, lin * tam, tam, tam, FUNDO_XADREZ );
                }
            }
        }

    }

    /**
     * Desenha um painel arredondado com borda.
     */
    public static void desenharPainel( EngineFrame e, double x, double y, double largura, double altura ) {
        e.fillRoundRectangle( x, y, largura, altura, 24, PAINEL );
        e.setStrokeLineWidth( 2 );
        e.drawRoundRectangle( x, y, largura, altura, 24, BORDA );
        e.setStrokeLineWidth( 1 );
    }

    /**
     * Desenha um texto centralizado horizontalmente em cx, com o topo em y.
     */
    public static void textoCentralizado( EngineFrame e, String texto, double cx, double y, int tamanho, Color cor ) {
        e.drawText( texto, cx - e.measureText( texto, tamanho ) / 2.0, y, tamanho, cor );
    }

    /**
     * Desenha um texto com o centro (horizontal e vertical) no ponto (cx, cy).
     */
    public static void textoNoCentro( EngineFrame e, String texto, double cx, double cy, int tamanho, Color cor ) {
        e.drawText( texto, cx - e.measureText( texto, tamanho ) / 2.0, yParaCentralizar( e, texto, cy, tamanho ), tamanho, cor );
    }

    /**
     * Desenha um texto alinhado à esquerda em x, centralizado verticalmente em cy.
     */
    public static void textoAEsquerda( EngineFrame e, String texto, double x, double cy, int tamanho, Color cor ) {
        e.drawText( texto, x, yParaCentralizar( e, texto, cy, tamanho ), tamanho, cor );
    }

    /**
     * A engine posiciona a linha de base do texto em (y + altura / 2).
     * Este cálculo devolve o y que deixa as letras centralizadas em cy.
     */
    private static double yParaCentralizar( EngineFrame e, String texto, double cy, int tamanho ) {
        Rectangle r = e.measureTextBounds( texto, tamanho );
        return cy + tamanho * 0.36 - r.height / 2;
    }

    /**
     * Desenha uma tecla do teclado com um rótulo (ex.: "W", "ESC").
     */
    public static void desenharTecla( EngineFrame e, double x, double y, double largura, double altura, String rotulo ) {
        e.fillRoundRectangle( x, y + 4, largura, altura, 12, new Color( 10, 14, 12 ) );
        e.fillRoundRectangle( x, y, largura, altura, 12, new Color( 58, 78, 66 ) );
        e.drawRoundRectangle( x, y, largura, altura, 12, new Color( 96, 124, 108 ) );
        textoNoCentro( e, rotulo, x + largura / 2, y + altura / 2, rotulo.length() > 1 ? 15 : 20, TEXTO );
    }

    /**
     * Desenha uma tecla de seta, com um triângulo apontando para a direção.
     */
    public static void desenharTeclaSeta( EngineFrame e, double x, double y, double tamanho, Direcao direcao ) {

        desenharTecla( e, x, y, tamanho, tamanho, "" );

        double cx = x + tamanho / 2;
        double cy = y + tamanho / 2;
        double r = tamanho * 0.22;
        double dx = direcao.getDx();
        double dy = direcao.getDy();

        // ponta do triângulo na direção da seta, base perpendicular
        e.fillTriangle(
            cx + dx * r, cy + dy * r,
            cx - dx * r * 0.7 + ( -dy ) * r, cy - dy * r * 0.7 + dx * r,
            cx - dx * r * 0.7 - ( -dy ) * r, cy - dy * r * 0.7 - dx * r,
            TEXTO
        );

    }

}
