package montepc;
/**
 *
 * @author Pedro
 */
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;

public class MontePCJogo extends EngineFrame {

    private EstruturasDados fila; //deque, escolher a primeira ou ultima peça, desfazer leva a peça para a extremidade de antes
    private EstruturasDados historico; //pilha LIFO, guarda a ordem das peças.
    private Desenhos desenhos;
    private int[] colocadas;
    private boolean[] retiradaFinal;
    private int fase;
    private int tela;
    private boolean selecionada;
    private boolean selecionouFinal;
    private String mensagem;
    private int[][] fases = {
        {0, 1, 2, 4, 7},
        {0, 1, 2, 4, 5, 7},
        {0, 1, 2, 3, 4, 5, 6, 7}
    };

    public MontePCJogo() {
        super(960, 640, "Monte o PC", 60, true, false, false, false, false, false);
    }

    @Override
    public void create() {
        setExitKey(KEY_NULL);

        desenhos = new Desenhos(this);
        fila = new EstruturasDados(8);
        historico = new EstruturasDados(8);
        colocadas = new int[8];
        retiradaFinal = new boolean[8];
        tela = 0;
    }

    private void iniciarFase() {
        fila.clear();
        historico.clear();

        for (int i = 0; i < colocadas.length; i++) {
            colocadas[i] = -1;
            retiradaFinal[i] = false;
        }

        for (int i = 0; i < fases[fase].length; i++) {
            int peca = fases[fase][i];
            fila.addLast(peca);
        }
        selecionada = false;
        selecionouFinal = false;
        mensagem = "Pegue a primeira ou a última peça.";
        tela = 1;
    }

    @Override
    public void update(double delta) {
        if (isKeyPressed(KEY_ESCAPE)) {
            tela = 0;
            return;
        }
        if (tela == 0) {
            if (isKeyPressed(KEY_ENTER)) {
                fase = 0;
                iniciarFase();
            }
            return;
        }
        if (tela == 2) {
            if (isKeyPressed(KEY_ENTER)) {
                fase++;
                iniciarFase();
            }
            return;
        }
        if (tela == 3) {
            if (isKeyPressed(KEY_R)) {
                fase = 0;
                iniciarFase();
            }
            return;
        }
        if (isKeyPressed(KEY_Z)) {
            if (!historico.isEmpty()) {
                int lugar = historico.removeLast();

                if (retiradaFinal[lugar]) {
                    fila.addLast(colocadas[lugar]);
                } else {
                    fila.addFirst(colocadas[lugar]);
                }
                colocadas[lugar] = -1;
                retiradaFinal[lugar] = false;
                selecionada = false;
                selecionouFinal = false;
                mensagem = "Peça removida.";
            } else {
                mensagem = "Nada para desfazer.";
            }
            return;
        }
        if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            if (desenhos.clicouNaFila() && !fila.isEmpty()) {
                selecionada = true;
                selecionouFinal = false;
                mensagem = "Coloque " + desenhos.getNome(fila.peekFirst()) + ".";
                return;
            }
            if (desenhos.clicouNaUltima(fila.getSize()) && !fila.isEmpty()) {
                selecionada = true;
                selecionouFinal = true;
                mensagem = "Coloque " + desenhos.getNome(fila.peekLast()) + ".";
                return;
            }
            if (selecionada) {
                int lugar = desenhos.getLugarClicado();
                if (lugar != -1) {
                    colocarPeca(lugar);
                }
            }
        }
    }

    private void colocarPeca(int lugar) {
        if (colocadas[lugar] != -1) {
            mensagem = "Lugar ocupado.";
            return;
        }

        int peca;
        if (selecionouFinal) {
            peca = fila.removeLast();
        } else {
            peca = fila.removeFirst();
        }

        colocadas[lugar] = peca;
        retiradaFinal[lugar] = selecionouFinal;
        historico.addLast(lugar);
        selecionada = false;
        selecionouFinal = false;

        if (peca == lugar) {
            mensagem = "Peça instalada.";
        } else {
            mensagem = "Lugar errado. Z para desfazer.";
        }
        if (fila.isEmpty()) {
            if (montagemCorreta()) {
                if (fase == 2) {
                    tela = 3;
                } else {
                    tela = 2;
                }
            } else {
                mensagem = "Corrija as peças vermelhas com Z.";
            }
        }
    }

    private boolean montagemCorreta() {
        for (int i = 0; i < colocadas.length; i++) {
            if (colocadas[i] != -1 && colocadas[i] != i) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void draw() {
        clearBackground(BLACK);

        if (tela == 0) {
            desenhos.desenharMenu();
        } else if (tela == 1) {
            desenhos.desenharJogo(fase, fila, historico, colocadas, selecionada, selecionouFinal, mensagem);
        } else {
            desenhos.desenharResultado(tela);
        }
    }

    public static void main(String[] args) {
        new MontePCJogo();
    }
}