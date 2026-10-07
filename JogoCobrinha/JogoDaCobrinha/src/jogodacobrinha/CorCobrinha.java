package jogodacobrinha;

import java.awt.Color;

/**
 * Cores disponíveis para a cabeça e para o corpo da cobrinha.
 */
public enum CorCobrinha {

    VERMELHO( "Vermelho", new Color( 229, 57, 53 ) ),
    LARANJA( "Laranja", new Color( 251, 140, 0 ) ),
    AMARELO( "Amarelo", new Color( 253, 216, 53 ) ),
    VERDE( "Verde", new Color( 67, 160, 71 ) ),
    AZUL( "Azul", new Color( 30, 136, 229 ) ),
    ROXO( "Roxo", new Color( 142, 36, 170 ) ),
    PRETO( "Preto", new Color( 33, 33, 33 ) ),
    BRANCO( "Branco", new Color( 250, 250, 250 ) );

    private final String nome;
    private final Color cor;

    private CorCobrinha( String nome, Color cor ) {
        this.nome = nome;
        this.cor = cor;
    }

    public String getNome() {
        return nome;
    }

    public Color getCor() {
        return cor;
    }

}
