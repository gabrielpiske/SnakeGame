package snakegame;

import javax.swing.JFrame;

/**
 *
 * @author gabriel_piske
 */
public class Frame extends JFrame {

    Frame() {
        
        this.add(new Panel());
        this.setTitle("Jogo da Cobra");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
