package Cobrinha;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;

public class CobrinhaJogo extends EngineFrame {
    public static final int META_FRUTAS = 25;
    private Cobra cobra;
    private Fruta fruta;
    private Tabuleiro tabuleiro;
    private EstadoJogo estado;
    private int pontuacao;
    private double tempoAcumulado;
    private double intervaloMovimento;

    public CobrinhaJogo() {
        super(960, 640, "Snake - Estruturas de Dados", 60,
                true, false, false, false, false, false);
    }

    @Override
    public void create() {
        setExitKey(KEY_NULL);
        cobra = new Cobra();
        fruta = new Fruta();
        tabuleiro = new Tabuleiro();
        estado = EstadoJogo.MENU;
        pontuacao = 0;
        tempoAcumulado = 0;
        intervaloMovimento = 0.18;
    }

    @Override
    public void update(double delta) {
        lerTeclado();
        
        if(estado == EstadoJogo.JOGANDO){
            tempoAcumulado += delta;
            
            while(tempoAcumulado >= intervaloMovimento && estado == EstadoJogo.JOGANDO){
                tempoAcumulado -= intervaloMovimento;
                executarPasso();
            }
        }
    }

    private void lerTeclado() {
        if(estado == EstadoJogo.MENU){
            if(isKeyPressed(KEY_ENTER)){
                iniciarPartida();
            }
            return;
        }
        if(isKeyPressed(KEY_ESCAPE)){
            estado = EstadoJogo.MENU;
            return;
        }
        if(isKeyPressed(KEY_P)){
            if(estado == EstadoJogo.JOGANDO){
                estado = EstadoJogo.PAUSADO;
            }else if(estado == EstadoJogo.PAUSADO){
                estado = EstadoJogo.JOGANDO;
            }
            return;
        }
        if(estado == EstadoJogo.DERROTA || estado == EstadoJogo.VITORIA){
            if(isKeyPressed(KEY_R)){
                iniciarPartida();
            }
            return;
        }
        if(isKeyPressed(KEY_V)){
            estado = EstadoJogo.VITORIA;
            return;
        }
        if(estado != EstadoJogo.JOGANDO){
            return;
        }
        if(isKeyPressed(KEY_W)){
            cobra.mudarDirecao(Direcao.CIMA);
        }else if(isKeyPressed(KEY_S)){
            cobra.mudarDirecao(Direcao.BAIXO);
        }else if(isKeyPressed(KEY_A)){
            cobra.mudarDirecao(Direcao.ESQUERDA);
        }
        else if(isKeyPressed(KEY_D)){
            cobra.mudarDirecao(Direcao.DIREITA);
        }
    }

    private void iniciarPartida() {
        cobra = new Cobra();
        cobra.reiniciar();
        fruta = new Fruta();
        
        pontuacao = 0;
        tempoAcumulado = 0;
        intervaloMovimento = 0.15;
        
        estado = EstadoJogo.JOGANDO;
    }

    private void executarPasso() {
        Posicao novaCabeca = cobra.calcularProximaCabeca();
        boolean crescer = false;
        
        if(tabuleiro.estaDentro(novaCabeca) == false){
            estado = EstadoJogo.DERROTA;
            return;
        }
        if(cobra.colideComCorpo(novaCabeca, crescer)){
            estado = EstadoJogo.DERROTA;
            return;
        }
        cobra.mover(novaCabeca, crescer);
    }

    private void verificarVitoria() {
        // TODO 17: vencer ao comer META_FRUTAS frutas ou preencher o tabuleiro.
    }

    @Override
    public void draw() {
        clearBackground(BLACK);
        
        if(estado == EstadoJogo.MENU){
            desenharMenu();
        }else{
            desenharTabuleiro();
            desenharCobra();
            drawText("WASD: mover | P: pausar | ESC: menu", 24, 25, 18, WHITE);
            
            if(estado == EstadoJogo.PAUSADO){
                drawText("PAUSADO", 640, 160, 24, YELLOW);
            }else if(estado == EstadoJogo.DERROTA || estado == EstadoJogo.VITORIA){
                desenharResultado();
            }
        }   
    }

    private void desenharTabuleiro() {
        for(int linha = 0; linha < Tabuleiro.LINHAS; linha++){
            for(int coluna = 0; coluna < Tabuleiro.COLUNAS; coluna++){
                int x = Tabuleiro.ORIGEM_X + coluna * Tabuleiro.TAMANHO_CELULA;
                int y = Tabuleiro.ORIGEM_Y + linha * Tabuleiro.TAMANHO_CELULA;
                
                Color cor;
                if ((linha + coluna) % 2 == 0) {
                    cor = LIME;
                } else {
                    cor = GREEN;
                }
                
                fillRectangle(x, y, Tabuleiro.TAMANHO_CELULA, Tabuleiro.TAMANHO_CELULA, cor);
            }
        }
        drawRectangle(Tabuleiro.ORIGEM_X, Tabuleiro.ORIGEM_Y, Tabuleiro.COLUNAS * Tabuleiro.TAMANHO_CELULA,
                Tabuleiro.LINHAS * Tabuleiro.TAMANHO_CELULA, WHITE);
    }

    private void desenharCobra() {
        No atual = cobra.getCorpo().getCabeca();
        
        while(atual != null){
            Posicao posicao = atual.getPosicao();
            
            int x = Tabuleiro.ORIGEM_X + posicao.getX()* Tabuleiro.TAMANHO_CELULA;
            int y = Tabuleiro.ORIGEM_Y + posicao.getY()* Tabuleiro.TAMANHO_CELULA;
            
            Color cor;
            if (atual == cobra.getCorpo().getCabeca()) {
                cor = PINK;
            } else {
                cor = DARKPURPLE;
            }
            
            fillRectangle(x, y, Tabuleiro.TAMANHO_CELULA - 2, Tabuleiro.TAMANHO_CELULA - 2, cor);
            
            atual = atual.getProximo();
        }    
    }

    private void desenharFruta() {
        // TODO: converter posição em célula para pixels e desenhar fruta.
    }

    private void desenharPainelEstrutura() {
        // TODO: mostrar tamanho, cabeça, cauda e nós ligados por setas.
        // Se o corpo não couber no painel, mostrar trecho + quantidade restante.
    }

    private void desenharMenu() {
        String titulo = " C O B R I N H A";
        String iniciar = "PRESSIONE ENTER PARA INICIAR";
        String comandos = "W: CIMA   |   S: BAIXO   |   A: ESQUERDA   |   D: DIREITA";
        String creditos = "Joao Guilherme e Pedro Mussulini | Estruturas de Dados";
        
        int xTitulo = (getScreenWidth() - measureText(titulo, 64)) / 2;
        drawText(titulo, xTitulo, 85, 64, GREEN);
        int xIniciar = (getScreenWidth() - measureText(iniciar, 24)) / 2;
        drawText(iniciar, xIniciar, 340, 24, GREEN);
        int xComandos = (getScreenWidth() - measureText(comandos, 18)) / 2;
        drawText(comandos, xComandos, 405, 18, GREEN);
        int xCreditos = (getScreenWidth() - measureText(creditos, 16)) / 2;
        drawText(creditos, xCreditos, getScreenHeight() - 65, 16, GRAY);
    }

    private void desenharResultado() {
        if(estado == EstadoJogo.VITORIA){
            drawText("VITORIA!", 640, 160, 24, BLUE);
        }else{
            drawText("FIM DE JOGO", 640, 160, 24, RED);
        }
        drawText("R: tentar novamente", 640, 205, 18, WHITE);
        drawText("ESC: voltar ao menu", 640, 235, 18, LIGHTGRAY);
    }

    public static void main(String[] args) {
        new CobrinhaJogo();
    }
    
    // Tela pra teste, ta aqui caso precise conferir os controles.
    private void desenharTesteControles() {
        String titulo = "TESTE DOS CONTROLES";
        String direcaoTexto = "Proxima direcao: " + cobra.getProximaDirecao();
        String comandos = "W: cima | S: baixo | A: esquerda | D: direita";
        String atalhos = "P: pausar/continuar | ESC: voltar ao menu";
        String tamanhoTexto = "Segmentos na lista: " + cobra.getCorpo().getTamanho();

        int xTitulo = (getScreenWidth() - measureText(titulo, 30)) / 2;
        int xDirecao = (getScreenWidth() - measureText(direcaoTexto, 24)) / 2;
        int xComandos = (getScreenWidth() - measureText(comandos, 18)) / 2;
        int xAtalhos = (getScreenWidth() - measureText(atalhos, 18)) / 2;
        int xTamanho = (getScreenWidth() - measureText(tamanhoTexto, 18)) / 2;

        drawText(titulo, xTitulo, 100, 30, GREEN);
        drawText(direcaoTexto, xDirecao, 230, 24, WHITE);
        drawText(comandos, xComandos, 340, 18, LIGHTGRAY);
        drawText(atalhos, xAtalhos, 380, 18, LIGHTGRAY);

        if(estado == EstadoJogo.PAUSADO){
            String pausa = "PAUSADO";
            int xPausa = (getScreenWidth() - measureText(pausa, 28)) / 2;
            drawText(pausa, xPausa, 460, 28, YELLOW);
        }
        drawText(tamanhoTexto, xTamanho, 550, 18, WHITE);
    }
}
