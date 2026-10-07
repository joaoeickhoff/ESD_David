package jogodacobrinha;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

/**
 * Campo de texto simples para digitar o nome do jogador.
 * Aceita letras sem acento, números e espaço, até um limite de caracteres.
 */
public class CampoDeTexto {

    private final double x;
    private final double y;
    private final double largura;
    private final double altura;
    private final int limite;

    private String texto;
    private double tempoCursor;

    public CampoDeTexto( double x, double y, double largura, double altura, int limite ) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.limite = limite;
        this.texto = "";
    }

    /**
     * Lê as teclas pressionadas neste quadro e atualiza o texto.
     */
    public void atualizar( EngineFrame e, double delta ) {

        tempoCursor += delta;

        if ( e.isKeyPressed( EngineFrame.KEY_BACKSPACE ) && !texto.isEmpty() ) {
            texto = texto.substring( 0, texto.length() - 1 );
            tempoCursor = 0;
        }

        // letras A a Z
        for ( int tecla = EngineFrame.KEY_A; tecla <= EngineFrame.KEY_Z; tecla++ ) {
            if ( e.isKeyPressed( tecla ) ) {
                boolean maiuscula = e.isKeyDown( EngineFrame.KEY_SHIFT ) != capsLockLigado();
                char c = (char) tecla; // os códigos de A a Z coincidem com os caracteres maiúsculos
                adicionar( maiuscula ? c : Character.toLowerCase( c ) );
            }
        }

        // números da linha principal e do teclado numérico
        for ( int i = 0; i <= 9; i++ ) {
            if ( e.isKeyPressed( EngineFrame.KEY_ZERO + i ) || e.isKeyPressed( EngineFrame.KEY_KP_0 + i ) ) {
                adicionar( (char) ( '0' + i ) );
            }
        }

        // espaço (não deixa começar o nome com espaço)
        if ( e.isKeyPressed( EngineFrame.KEY_SPACE ) && !texto.isEmpty() ) {
            adicionar( ' ' );
        }

    }

    private void adicionar( char c ) {
        if ( texto.length() < limite ) {
            texto += c;
            tempoCursor = 0;
        }
    }

    private boolean capsLockLigado() {
        try {
            return Toolkit.getDefaultToolkit().getLockingKeyState( KeyEvent.VK_CAPS_LOCK );
        } catch ( UnsupportedOperationException exc ) {
            return false;
        }
    }

    public void desenhar( EngineFrame e ) {

        int tamanhoFonte = 24;
        double cy = y + altura / 2;

        e.fillRoundRectangle( x, y, largura, altura, 14, new Color( 14, 20, 17 ) );
        e.setStrokeLineWidth( 2 );
        e.drawRoundRectangle( x, y, largura, altura, 14, Tema.DESTAQUE );
        e.setStrokeLineWidth( 1 );

        if ( texto.isEmpty() ) {
            Tema.textoAEsquerda( e, "Digite seu nome", x + 16, cy, 20, Tema.TEXTO_DESABILITADO );
        } else {
            Tema.textoAEsquerda( e, texto, x + 16, cy, tamanhoFonte, Tema.TEXTO );
        }

        // cursor piscando depois do texto
        if ( ( (int) ( tempoCursor / 0.5 ) ) % 2 == 0 ) {
            double cursorX = x + 16 + ( texto.isEmpty() ? 0 : e.measureText( texto, tamanhoFonte ) + 3 );
            e.fillRectangle( cursorX, cy - 13, 2, 26, Tema.DESTAQUE );
        }

        // contador de caracteres
        String contador = texto.length() + "/" + limite;
        Tema.textoAEsquerda( e, contador, x + largura - 14 - e.measureText( contador, 14 ), cy, 14,
                texto.length() == limite ? Tema.OURO : Tema.TEXTO_SUAVE );

    }

    public String getTexto() {
        return texto;
    }

}
