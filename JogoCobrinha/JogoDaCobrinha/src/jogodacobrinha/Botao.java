package jogodacobrinha;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;

/**
 * Botão clicável com o mouse, desenhado com as primitivas da engine.
 */
public class Botao {

    private final double x;
    private final double y;
    private final double largura;
    private final double altura;
    private final String texto;
    private final int tamanhoFonte;
    private boolean habilitado;

    public Botao( double x, double y, double largura, double altura, String texto, int tamanhoFonte ) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.texto = texto;
        this.tamanhoFonte = tamanhoFonte;
        this.habilitado = true;
    }

    public boolean mouseEmCima( EngineFrame e ) {
        int mx = e.getMouseX();
        int my = e.getMouseY();
        return mx >= x && mx <= x + largura && my >= y && my <= y + altura;
    }

    public boolean foiClicado( EngineFrame e ) {
        return habilitado && mouseEmCima( e ) && e.isMouseButtonPressed( EngineFrame.MOUSE_BUTTON_LEFT );
    }

    public void desenhar( EngineFrame e ) {

        Color fundo;
        Color borda;
        Color corTexto;

        if ( !habilitado ) {
            fundo = Tema.BOTAO_DESABILITADO;
            borda = Tema.BOTAO_DESABILITADO;
            corTexto = Tema.TEXTO_DESABILITADO;
        } else if ( mouseEmCima( e ) ) {
            fundo = Tema.DESTAQUE;
            borda = Tema.DESTAQUE;
            corTexto = Tema.FUNDO;
        } else {
            fundo = Tema.BOTAO;
            borda = Tema.BORDA;
            corTexto = Tema.TEXTO;
        }

        e.fillRoundRectangle( x, y, largura, altura, 18, fundo );
        e.setStrokeLineWidth( 2 );
        e.drawRoundRectangle( x, y, largura, altura, 18, borda );
        e.setStrokeLineWidth( 1 );

        Tema.textoNoCentro( e, texto, x + largura / 2, y + altura / 2, tamanhoFonte, corTexto );

    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado( boolean habilitado ) {
        this.habilitado = habilitado;
    }

}
