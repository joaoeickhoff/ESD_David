package jogodacobrinha;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import java.awt.Font;

/**
 * Jogo da Cobrinha - versão 1.
 *
 * Telas: menu, como jogar, ranking (em breve), personalização, jogo e vitória.
 * O corpo da cobrinha é uma lista duplamente encadeada (veja a classe Cobrinha).
 *
 * Controles no jogo:
 *   WASD ou setas - movimentam a cobrinha
 *   ESC           - pausa / continua
 *   V             - vai para a tela de vitória
 *
 * @author Charles
 */
public class Main extends EngineFrame {

    /**
     * As telas do jogo.
     */
    private enum Tela {
        MENU,
        COMO_JOGAR,
        RANKING,
        PERSONALIZACAO,
        JOGO,
        VITORIA
    }

    // campo de jogo: 32 x 22 células de 25 px, abaixo de uma barra de 50 px
    private static final int TAMANHO_CELULA = 25;
    private static final int COLUNAS = 32;
    private static final int LINHAS = 22;
    private static final int ALTURA_BARRA = 50;

    // tempo, em segundos, entre um passo e outro da cobrinha
    private static final double INTERVALO_MOVIMENTO = 0.11;

    private static final int LIMITE_NOME = 10;

    private Tela telaAtual;

    // menu
    private Botao botaoJogar;
    private Botao botaoComoJogar;
    private Botao botaoRanking;

    // como jogar e ranking
    private Botao botaoVoltarComoJogar;
    private Botao botaoVoltarRanking;

    // personalização
    private CampoDeTexto campoNome;
    private CorCobrinha corCabeca;
    private CorCobrinha corCorpo;
    private Botao botaoVoltarPersonalizacao;
    private Botao botaoComecar;

    private static final double AMOSTRA_TAMANHO = 56;
    private static final double AMOSTRA_ESPACO = 74;
    private static final double AMOSTRA_X = 113;
    private static final double AMOSTRA_Y_CABECA = 122;
    private static final double AMOSTRA_Y_CORPO = 252;

    // jogo
    private Cobrinha cobrinha;
    private String nomeJogador;
    private boolean pausado;
    private double tempoAcumulado;

    // vitória
    private Botao botaoJogarNovamente;
    private Botao botaoVoltarMenu;

    public Main() {

        super(
            800,                 // largura                      / width
            600,                 // altura                       / height
            "Jogo da Cobrinha",  // título                       / title
            60,                  // quadros por segundo desejado / target FPS
            true,                // suavização                   / antialiasing
            false,               // redimensionável              / resizable
            false,               // tela cheia                   / full screen
            false,               // sem decoração                / undecorated
            false,               // sempre no topo               / always on top
            false                // fundo invisível              / invisible background
        );

    }

    /**
     * Cria o mundo do jogo. Executa apenas uma vez.
     */
    @Override
    public void create() {

        // por padrão a engine fecha a janela no ESC; aqui o ESC pausa o jogo
        setExitKey( -1 );

        setDefaultFont( new Font( Font.SANS_SERIF, Font.BOLD, 16 ) );

        botaoJogar = new Botao( 250, 250, 300, 58, "Jogar", 24 );
        botaoComoJogar = new Botao( 250, 326, 300, 58, "Como jogar", 24 );
        botaoRanking = new Botao( 250, 402, 300, 58, "Ranking", 24 );

        botaoVoltarComoJogar = new Botao( 290, 522, 220, 52, "Voltar", 22 );
        botaoVoltarRanking = new Botao( 290, 490, 220, 52, "Voltar", 22 );

        campoNome = new CampoDeTexto( 113, 380, 330, 52, LIMITE_NOME );
        corCabeca = CorCobrinha.VERDE;
        corCorpo = CorCobrinha.AMARELO;
        botaoVoltarPersonalizacao = new Botao( 113, 512, 220, 56, "Voltar", 22 );
        botaoComecar = new Botao( 467, 512, 220, 56, "Jogar", 22 );

        botaoJogarNovamente = new Botao( 250, 345, 300, 54, "Jogar novamente", 22 );
        botaoVoltarMenu = new Botao( 250, 412, 300, 54, "Voltar ao menu", 22 );

        telaAtual = Tela.MENU;

    }

    //**************************************************************************
    // Atualização (entrada do usuário e lógica)
    //**************************************************************************

    @Override
    public void update( double delta ) {

        switch ( telaAtual ) {
            case MENU:
                atualizarMenu();
                break;
            case COMO_JOGAR:
                atualizarComoJogar();
                break;
            case RANKING:
                atualizarRanking();
                break;
            case PERSONALIZACAO:
                atualizarPersonalizacao( delta );
                break;
            case JOGO:
                atualizarJogo( delta );
                break;
            case VITORIA:
                atualizarVitoria();
                break;
        }


    }

    private void atualizarMenu() {
        if ( botaoJogar.foiClicado( this ) ) {
            telaAtual = Tela.PERSONALIZACAO;
        } else if ( botaoComoJogar.foiClicado( this ) ) {
            telaAtual = Tela.COMO_JOGAR;
        } else if ( botaoRanking.foiClicado( this ) ) {
            telaAtual = Tela.RANKING;
        }
    }

    private void atualizarComoJogar() {
        if ( botaoVoltarComoJogar.foiClicado( this ) ) {
            telaAtual = Tela.MENU;
        }
    }

    private void atualizarRanking() {
        if ( botaoVoltarRanking.foiClicado( this ) ) {
            telaAtual = Tela.MENU;
        }
    }

    private void atualizarPersonalizacao( double delta ) {

        campoNome.atualizar( this, delta );

        if ( isMouseButtonPressed( MOUSE_BUTTON_LEFT ) ) {
            CorCobrinha[] cores = CorCobrinha.values();
            for ( int i = 0; i < cores.length; i++ ) {
                if ( mouseNaAmostra( i, AMOSTRA_Y_CABECA ) ) {
                    corCabeca = cores[i];
                }
                if ( mouseNaAmostra( i, AMOSTRA_Y_CORPO ) ) {
                    corCorpo = cores[i];
                }
            }
        }

        // só dá para jogar depois de digitar um nome
        botaoComecar.setHabilitado( !campoNome.getTexto().trim().isEmpty() );

        if ( botaoComecar.foiClicado( this )
                || ( botaoComecar.isHabilitado() && isKeyPressed( KEY_ENTER ) ) ) {
            iniciarJogo();
        } else if ( botaoVoltarPersonalizacao.foiClicado( this ) ) {
            telaAtual = Tela.MENU;
        }

    }

    private void atualizarJogo( double delta ) {

        if ( isKeyPressed( KEY_ESCAPE ) ) {
            pausado = !pausado;
        }

        if ( pausado ) {
            return;
        }

        if ( isKeyPressed( KEY_V ) ) {
            telaAtual = Tela.VITORIA;
            return;
        }

        if ( isKeyPressed( KEY_W ) || isKeyPressed( KEY_UP ) ) {
            cobrinha.mudarDirecao( Direcao.CIMA );
        } else if ( isKeyPressed( KEY_S ) || isKeyPressed( KEY_DOWN ) ) {
            cobrinha.mudarDirecao( Direcao.BAIXO );
        } else if ( isKeyPressed( KEY_A ) || isKeyPressed( KEY_LEFT ) ) {
            cobrinha.mudarDirecao( Direcao.ESQUERDA );
        } else if ( isKeyPressed( KEY_D ) || isKeyPressed( KEY_RIGHT ) ) {
            cobrinha.mudarDirecao( Direcao.DIREITA );
        }

        tempoAcumulado += delta;

        if ( tempoAcumulado >= INTERVALO_MOVIMENTO ) {
            tempoAcumulado -= INTERVALO_MOVIMENTO;
            cobrinha.mover( COLUNAS, LINHAS );
        }

    }

    private void atualizarVitoria() {
        if ( botaoJogarNovamente.foiClicado( this ) ) {
            iniciarJogo();
        } else if ( botaoVoltarMenu.foiClicado( this ) ) {
            telaAtual = Tela.MENU;
        }
    }

    /**
     * Começa uma partida nova com o nome e as cores escolhidos.
     */
    private void iniciarJogo() {
        nomeJogador = campoNome.getTexto().trim();
        cobrinha = new Cobrinha( 8, LINHAS / 2, corCabeca.getCor(), corCorpo.getCor() );
        pausado = false;
        tempoAcumulado = 0;
        telaAtual = Tela.JOGO;
    }

    private boolean mouseNaAmostra( int indice, double yLinha ) {
        double x = AMOSTRA_X + indice * AMOSTRA_ESPACO;
        int mx = getMouseX();
        int my = getMouseY();
        return mx >= x && mx <= x + AMOSTRA_TAMANHO && my >= yLinha && my <= yLinha + AMOSTRA_TAMANHO;
    }

    //**************************************************************************
    // Desenho
    //**************************************************************************

    @Override
    public void draw() {

        switch ( telaAtual ) {
            case MENU:
                desenharMenu();
                break;
            case COMO_JOGAR:
                desenharComoJogar();
                break;
            case RANKING:
                desenharRanking();
                break;
            case PERSONALIZACAO:
                desenharPersonalizacao();
                break;
            case JOGO:
                desenharJogo();
                if ( pausado ) {
                    desenharPausa();
                }
                break;
            case VITORIA:
                desenharJogo();
                desenharVitoria();
                break;
        }

    }

    private void desenharMenu() {

        Tema.desenharFundo( this );

        Tema.textoNoCentro( this, "JOGO DA COBRINHA", 400, 100, 54, Tema.DESTAQUE );

        // cobrinha decorativa com as cores escolhidas
        double tam = 34;
        int pedacos = 7;
        double inicioX = 400 - pedacos * tam / 2;
        for ( int i = 0; i < pedacos; i++ ) {
            boolean ehCabeca = i == pedacos - 1;
            Cobrinha.desenharSegmento( this, inicioX + i * tam, 160, tam,
                    ehCabeca ? corCabeca.getCor() : corCorpo.getCor(), ehCabeca, Direcao.DIREITA );
        }

        botaoJogar.desenhar( this );
        botaoComoJogar.desenhar( this );
        botaoRanking.desenhar( this );

        Tema.textoNoCentro( this, "Use o mouse para escolher uma opção", 400, 545, 15, Tema.TEXTO_SUAVE );
        drawText( "versão 1.0", 800 - measureText( "versão 1.0", 13 ) - 14, 576, 13, Tema.TEXTO_DESABILITADO );

    }

    private void desenharComoJogar() {

        Tema.desenharFundo( this );
        Tema.textoNoCentro( this, "Como jogar", 400, 50, 40, Tema.DESTAQUE );

        // objetivo
        Tema.desenharPainel( this, 50, 92, 700, 140 );
        drawText( "Objetivo", 76, 108, 22, Tema.OURO );
        drawText( "Guie a cobrinha pelo campo e coma as maçãs para crescer e fazer", 76, 146, 17, Tema.TEXTO );
        drawText( "pontos. Cada maçã deixa a cobrinha maior e o desafio mais difícil.", 76, 172, 17, Tema.TEXTO );
        drawText( "Não bata no próprio corpo e tente chegar ao topo do ranking!", 76, 198, 17, Tema.TEXTO );

        // controles
        Tema.desenharPainel( this, 50, 248, 700, 256 );
        drawText( "Controles", 76, 264, 22, Tema.OURO );

        // WASD
        double t = 46;
        double g = 6;
        double wx = 82;
        double wy = 310;
        Tema.desenharTecla( this, wx + t + g, wy, t, t, "W" );
        Tema.desenharTecla( this, wx, wy + t + g, t, t, "A" );
        Tema.desenharTecla( this, wx + t + g, wy + t + g, t, t, "S" );
        Tema.desenharTecla( this, wx + 2 * ( t + g ), wy + t + g, t, t, "D" );

        Tema.textoNoCentro( this, "ou", 266, wy + t + g / 2, 18, Tema.TEXTO_SUAVE );

        // setas
        double sx = 296;
        Tema.desenharTeclaSeta( this, sx + t + g, wy, t, Direcao.CIMA );
        Tema.desenharTeclaSeta( this, sx, wy + t + g, t, Direcao.ESQUERDA );
        Tema.desenharTeclaSeta( this, sx + t + g, wy + t + g, t, Direcao.BAIXO );
        Tema.desenharTeclaSeta( this, sx + 2 * ( t + g ), wy + t + g, t, Direcao.DIREITA );

        Tema.textoNoCentro( this, "Movimentam a cobrinha", 266, 446, 17, Tema.TEXTO );
        Tema.textoNoCentro( this, "(cima, esquerda, baixo e direita)", 266, 470, 14, Tema.TEXTO_SUAVE );

        // divisória
        fillRectangle( 488, 300, 2, 180, Tema.BORDA );

        // ESC e V
        Tema.desenharTecla( this, 514, 322, 64, 46, "ESC" );
        Tema.textoAEsquerda( this, "Pausa / continua", 594, 345, 17, Tema.TEXTO );

        Tema.desenharTecla( this, 523, 400, 46, 46, "V" );
        Tema.textoAEsquerda( this, "Tela de vitória", 594, 423, 17, Tema.TEXTO );

        botaoVoltarComoJogar.desenhar( this );

    }

    private void desenharRanking() {

        Tema.desenharFundo( this );
        Tema.textoNoCentro( this, "Ranking", 400, 70, 44, Tema.DESTAQUE );

        Tema.desenharPainel( this, 150, 140, 500, 300 );
        fillStar( 400, 225, 5, 46, -90, Tema.OURO );
        Tema.textoNoCentro( this, "Ranking em construção", 400, 310, 28, Tema.TEXTO );
        Tema.textoNoCentro( this, "A pontuação e o ranking serão", 400, 356, 18, Tema.TEXTO_SUAVE );
        Tema.textoNoCentro( this, "implementados na próxima versão do jogo.", 400, 382, 18, Tema.TEXTO_SUAVE );

        botaoVoltarRanking.desenhar( this );

    }

    private void desenharPersonalizacao() {

        Tema.desenharFundo( this );
        Tema.textoNoCentro( this, "Personalize sua cobrinha", 400, 46, 34, Tema.DESTAQUE );

        desenharLinhaDeCores( "Cor da cabeça", AMOSTRA_Y_CABECA, corCabeca );
        desenharLinhaDeCores( "Cor do corpo", AMOSTRA_Y_CORPO, corCorpo );

        // nome
        Tema.textoAEsquerda( this, "Seu nome", AMOSTRA_X, 360, 20, Tema.TEXTO );
        campoNome.desenhar( this );
        Tema.textoAEsquerda( this, "Até 10 caracteres: letras sem acento, números e espaço",
                AMOSTRA_X, 452, 13, Tema.TEXTO_SUAVE );

        // prévia da cobrinha com as cores escolhidas
        Tema.textoAEsquerda( this, "Prévia", 479, 360, 20, Tema.TEXTO );
        fillRoundRectangle( 479, 378, 216, 56, 14, Tema.CAMPO_CLARO );
        double tam = 40;
        for ( int i = 0; i < 5; i++ ) {
            boolean ehCabeca = i == 4;
            Cobrinha.desenharSegmento( this, 487 + i * tam, 386, tam,
                    ehCabeca ? corCabeca.getCor() : corCorpo.getCor(), ehCabeca, Direcao.DIREITA );
        }

        botaoVoltarPersonalizacao.desenhar( this );
        botaoComecar.desenhar( this );

    }

    /**
     * Desenha o título e as 8 amostras de cor de uma linha (cabeça ou corpo).
     */
    private void desenharLinhaDeCores( String titulo, double y, CorCobrinha selecionada ) {

        Tema.textoAEsquerda( this, titulo, AMOSTRA_X, y - 26, 20, Tema.TEXTO );
        Tema.textoAEsquerda( this, selecionada.getNome(),
                AMOSTRA_X + measureText( titulo, 20 ) + 14, y - 26, 20, Tema.DESTAQUE );

        CorCobrinha[] cores = CorCobrinha.values();

        for ( int i = 0; i < cores.length; i++ ) {

            double x = AMOSTRA_X + i * AMOSTRA_ESPACO;
            boolean escolhida = cores[i] == selecionada;

            if ( escolhida ) {
                fillRoundRectangle( x - 6, y - 6, AMOSTRA_TAMANHO + 12, AMOSTRA_TAMANHO + 12, 22, Tema.DESTAQUE );
            } else if ( mouseNaAmostra( i, y ) ) {
                fillRoundRectangle( x - 4, y - 4, AMOSTRA_TAMANHO + 8, AMOSTRA_TAMANHO + 8, 20, Tema.TEXTO_SUAVE );
            }

            fillRoundRectangle( x, y, AMOSTRA_TAMANHO, AMOSTRA_TAMANHO, 16, cores[i].getCor() );
            drawRoundRectangle( x, y, AMOSTRA_TAMANHO, AMOSTRA_TAMANHO, 16, Tema.TEXTO_SUAVE );

            Tema.textoNoCentro( this, cores[i].getNome(), x + AMOSTRA_TAMANHO / 2, y + AMOSTRA_TAMANHO + 18, 13,
                    escolhida ? Tema.DESTAQUE : Tema.TEXTO_SUAVE );

        }

    }

    private void desenharJogo() {

        clearBackground( Tema.BARRA_SUPERIOR );

        // barra superior
        Tema.textoAEsquerda( this, "Jogador:", 20, ALTURA_BARRA / 2, 18, Tema.TEXTO_SUAVE );
        Tema.textoAEsquerda( this, nomeJogador, 20 + measureText( "Jogador: ", 18 ), ALTURA_BARRA / 2, 20, Tema.TEXTO );
        Tema.desenharTecla( this, 648, 10, 52, 28, "ESC" );
        Tema.textoAEsquerda( this, "pausar", 712, ALTURA_BARRA / 2, 16, Tema.TEXTO_SUAVE );

        // campo quadriculado
        for ( int lin = 0; lin < LINHAS; lin++ ) {
            for ( int col = 0; col < COLUNAS; col++ ) {
                Color cor = ( lin + col ) % 2 == 0 ? Tema.CAMPO_CLARO : Tema.CAMPO_ESCURO;
                fillRectangle( col * TAMANHO_CELULA, ALTURA_BARRA + lin * TAMANHO_CELULA,
                        TAMANHO_CELULA, TAMANHO_CELULA, cor );
            }
        }

        cobrinha.desenhar( this, 0, ALTURA_BARRA, TAMANHO_CELULA );

    }

    private void desenharPausa() {

        fillRectangle( 0, ALTURA_BARRA, getScreenWidth(), getScreenHeight() - ALTURA_BARRA, new Color( 0, 0, 0, 150 ) );

        Tema.desenharPainel( this, 220, 230, 360, 160 );
        Tema.textoNoCentro( this, "PAUSADO", 400, 288, 44, Tema.DESTAQUE );
        Tema.textoNoCentro( this, "Pressione ESC para continuar", 400, 345, 18, Tema.TEXTO );

    }

    private void desenharVitoria() {

        fillRectangle( 0, 0, getScreenWidth(), getScreenHeight(), new Color( 0, 0, 0, 170 ) );

        fillRoundRectangle( 170, 110, 460, 380, 28, Tema.PAINEL );
        setStrokeLineWidth( 3 );
        drawRoundRectangle( 170, 110, 460, 380, 28, Tema.OURO );
        setStrokeLineWidth( 1 );

        fillStar( 228, 178, 5, 22, -90, Tema.OURO );
        fillStar( 572, 178, 5, 22, -90, Tema.OURO );
        Tema.textoNoCentro( this, "VITÓRIA!", 400, 178, 56, Tema.OURO );

        Tema.textoNoCentro( this, "Parabéns, " + nomeJogador + "!", 400, 252, 30, Tema.TEXTO );
        Tema.textoNoCentro( this, "Você venceu o jogo!", 400, 292, 20, Tema.TEXTO_SUAVE );

        botaoJogarNovamente.desenhar( this );
        botaoVoltarMenu.desenhar( this );

    }

    /**
     * Instancia a engine e inicia o jogo.
     */
    public static void main( String[] args ) {
        new Main();
    }

}
