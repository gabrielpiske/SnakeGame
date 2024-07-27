package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 *
 * @author gabriel_piske
 */
public class Panel extends JPanel implements ActionListener {

    //Constantes
    static final int LARGURA_JANELA = 600;
    static final int ALTURA_JANELA = 600;
    static final int TAM_QUADRADOS = 25;
    static final int QUADRADOS = (LARGURA_JANELA * ALTURA_JANELA) / TAM_QUADRADOS;
    static final int DELAY = 75;

    //Arrays com cordenadas da cobra
    final int x[] = new int[QUADRADOS];
    final int y[] = new int[QUADRADOS];

    //Variavéis
    int bodyParts = 6;
    int macasComidas;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean rodar = false;
    Timer timer;
    Random random;

    //Construtor do Panel
    Panel() {
        random = new Random();
        this.setPreferredSize(new Dimension(LARGURA_JANELA, ALTURA_JANELA));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startJogo();
    }

    //Inicia o Jogo
    public void startJogo() {
        newApple();
        rodar = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //Desenho de Componentes
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    //Chama o paintComponents da SuperClasse
        draw(g);
    }

    // Desenhar o Jogo 
    public void draw(Graphics g) {
        if (rodar) {
            //Desenho da Grade
            for (int i = 0; i < ALTURA_JANELA / TAM_QUADRADOS; i++) {
                g.drawLine(i * TAM_QUADRADOS, 0, i * TAM_QUADRADOS, ALTURA_JANELA);
                g.drawLine(0, i * TAM_QUADRADOS, LARGURA_JANELA, i * TAM_QUADRADOS);
            }
            //Desenho da Maça
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, TAM_QUADRADOS, TAM_QUADRADOS);

            //Desenho da Cobra
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], TAM_QUADRADOS, TAM_QUADRADOS);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], TAM_QUADRADOS, TAM_QUADRADOS);
                }
            }

            //Score
            g.setColor(Color.red);
            g.setFont(new Font("JetBrains Mono", Font.PLAIN, 40));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: " + macasComidas, (LARGURA_JANELA - metrics1.stringWidth("Score: " + macasComidas)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    //Geração de Novas Maças
    public void newApple() {
        appleX = random.nextInt((int) (LARGURA_JANELA / TAM_QUADRADOS)) * TAM_QUADRADOS;
        appleY = random.nextInt((int) (ALTURA_JANELA / TAM_QUADRADOS)) * TAM_QUADRADOS;
    }

    //Mover a cobra
    public void move() {
        //Move cada parte do corpo para a posição da parte anterior
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        //Muda a posição da cabeça com base na direção
        switch (direction) {
            case 'U':
                y[0] = y[0] - TAM_QUADRADOS;
                break;
            case 'D':
                y[0] = y[0] + TAM_QUADRADOS;
                break;
            case 'L':
                x[0] = x[0] - TAM_QUADRADOS;
                break;
            case 'R':
                x[0] = x[0] + TAM_QUADRADOS;
                break;
        }
    }

    //Verifica se a maça foi comida
    public void checkMaca() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            macasComidas++;
            newApple();
        }
    }

    //Verifica as Colisões
    public void checkColisao() {
        //cabeça encostando no corpo
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                rodar = false;
            }
        }
        if (x[0] < 0 || x[0] > LARGURA_JANELA || y[0] < 0 || y[0] > ALTURA_JANELA) {
            rodar = false;
        }
//        if (x[0] < 0) {
//            rodar = false;
//        }
//        if (x[0] > LARGURA_JANELA) {
//            rodar = false;
//        }
//        if (y[0] < 0) {
//            rodar = false;
//        }
//        if (y[0] > ALTURA_JANELA) {
//            rodar = false;
//        }

        if (!rodar) {
            timer.stop();
        }
    }

    //Tela Game Over
    public void gameOver(Graphics g) {
        //Tela Game Over
        g.setColor(Color.red);
        g.setFont(new Font("JetBrains Mono", Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (LARGURA_JANELA - metrics2.stringWidth("Game Over")) / 2, ALTURA_JANELA / 2);
    }

    //Chamado a cada ação do timer
    @Override
    public void actionPerformed(ActionEvent e) {
        if (rodar) {
            move();
            checkMaca();
            checkColisao();
        }
        repaint();
    }

    //Trata eventos do teclado 
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            //muda a direção da cobra pela tecla clicada
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
