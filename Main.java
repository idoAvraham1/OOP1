import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /**
         * The Main function to start the game.
         * Don't make any changes here
         * @param args
         */
        PlayableLogic gameLogic = new GameLogic();
        GUI_for_chess_like_games gui = new GUI_for_chess_like_games(gameLogic, "Vikings Chess Game");
        SwingUtilities.invokeLater(() -> {
            // Set the UIManager property to make the focus color transparent
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
            UIManager.put("Button.select", new ColorUIResource(new Color(0, 0, 0, 0)));
            gui.start();
        });
    }


}
