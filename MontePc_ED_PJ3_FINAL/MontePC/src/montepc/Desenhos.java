package montepc;
/**
 *
 * @author Pedro
 */
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.*;

public class Desenhos {
    private EngineFrame jogo;
    private String[] nomes = {"PLACA-MAE", "CPU", "RAM 1", "RAM 2", "SSD", "GPU", "COOLER", "FONTE"};
    private int[][] lugares = {
        {275, 115, 370, 40},
        {275, 185, 100, 65},
        {400, 185, 95, 65},
        {520, 185, 95, 65},
        {445, 280, 170, 55},
        {275, 365, 340, 55},
        {275, 280, 140, 55},
        {275, 455, 180, 55}
    };

    public Desenhos(EngineFrame jogo) {
        this.jogo = jogo;
    }

    public String getNome(int peca) {
        return nomes[peca];
    }

    private boolean clicouDentro(int x, int y, int largura, int altura) {
        return jogo.getMouseX() >= x
                && jogo.getMouseX() < x + largura
                && jogo.getMouseY() >= y
                && jogo.getMouseY() < y + altura;
    }

    public boolean clicouNaFila() {
        return clicouDentro(20, 100, 200, 35);
    }

    public boolean clicouNaUltima(int tamanho) {
        if (tamanho == 0) {
            return false;
        }
        int y = 100 + (tamanho - 1) * 45;
        return clicouDentro(20, y, 200, 35);
    }

    public int getLugarClicado() {
        for (int i = 0; i < lugares.length; i++) {
            int[] lugar = lugares[i];
            if (clicouDentro(lugar[0], lugar[1], lugar[2], lugar[3])) {
                return i;
            }
        }
        return -1;
    }

    private void textoCentralizado(String texto, int y, int tamanho) {
        int x = (jogo.getScreenWidth() - jogo.measureText(texto, tamanho)) / 2;
        jogo.drawText(texto, x, y, tamanho, WHITE);
    }

    public void desenharMenu() {
        textoCentralizado("MONTE O PC", 160, 40);
        textoCentralizado("ENTER: comecar", 280, 24);
        textoCentralizado("Clique na primeira ou na ultima peca e depois no lugar certo.", 355, 20);
        textoCentralizado("Z: desfazer | ESC: menu", 410, 18);
        textoCentralizado("Joao Guilherme e Pedro Mussulini", 570, 18);
    }

    public void desenharResultado(int tela) {
        if (tela == 2) {
            textoCentralizado("PC MONTADO!", 220, 36);
            textoCentralizado("ENTER: proxima fase", 320, 24);
        } else {
            textoCentralizado("VOCE VENCEU!", 220, 36);
            textoCentralizado("Os tres PCs estao prontos.", 280, 24);
            textoCentralizado("R: jogar novamente", 350, 24);
        }
        textoCentralizado("ESC: menu", 430, 20);
    }

    public void desenharJogo(int fase, EstruturasDados fila, EstruturasDados historico, int[] colocadas, boolean selecionada, boolean selecionouFinal, String mensagem) {
        jogo.drawText("Fase " + (fase + 1) + "/3", 20, 20, 24, WHITE);
        jogo.drawText("DEQUE DE PECAS", 20, 70, 20, WHITE);
        jogo.drawText("COMPUTADOR", 275, 70, 20, WHITE);
        jogo.drawText("PECAS INSTALADAS", 700, 70, 20, WHITE);
        jogo.fillRectangle(255, 100, 410, 430, GRAY);
        jogo.drawRectangle(255, 100, 410, 430, WHITE);

        int y = 100;
        int indiceSelecionado = 0;
        if (selecionouFinal) {
            indiceSelecionado = fila.getSize() - 1;
        }

        for (int i = 0; i < fila.getSize(); i++) {
            int peca = fila.get(i);
            Color corTexto = WHITE;

            if (i == indiceSelecionado && selecionada) {
                jogo.fillRectangle(20, y, 200, 35, GREEN);
                corTexto = BLACK;
            }

            jogo.drawRectangle(20, y, 200, 35, WHITE);
            jogo.drawText(nomes[peca], 30, y + 8, 18, corTexto);

            y += 45;
        }

        for (int i = 0; i < lugares.length; i++) {
            int[] lugar = lugares[i];
            int peca = colocadas[i];

            Color cor = LIGHTGRAY;
            Color corTexto = WHITE;

            if (peca != -1) {
                cor = DARKBLUE;
                if (peca != i) {
                    cor = RED;
                    corTexto = BLACK;
                }
            }

            jogo.fillRectangle(lugar[0], lugar[1], lugar[2], lugar[3], cor);
            jogo.drawRectangle(lugar[0], lugar[1], lugar[2], lugar[3], BLACK);

            if (peca != -1 && peca != i) {
                // Nome do lugar em cima e da peça errada embaixo.
                jogo.drawText(nomes[i], lugar[0] + 5, lugar[1] + 4, 12, BLACK);
                int x = lugar[0] + (lugar[2] - jogo.measureText(nomes[peca], 14)) / 2;
                jogo.drawText(nomes[peca], x, lugar[1] + lugar[3] - 20, 14, BLACK);
            } else {
                int x = lugar[0] + (lugar[2] - jogo.measureText(nomes[i], 16)) / 2;
                jogo.drawText(nomes[i], x, lugar[1] + lugar[3] / 2 - 8, 16, corTexto);
            }
        }

        y = 100;

        for (int i = historico.getSize() - 1; i >= 0; i--) {
            int lugar = historico.get(i);
            int peca = colocadas[lugar];

            jogo.drawRectangle(700, y, 235, 35, WHITE);
            jogo.drawText(nomes[peca], 710, y + 8, 18, WHITE);

            y += 45;
        }
        jogo.drawText("Escolha o primeiro ou ultimo", 20, 495, 14, WHITE);
        jogo.drawText("Ultima peca em cima", 700, 495, 15, WHITE);
        jogo.drawText(mensagem, 20, 550, 18, WHITE);
        jogo.drawText("Z: desfazer | ESC: menu", 20, 600, 18, WHITE);
    }
}