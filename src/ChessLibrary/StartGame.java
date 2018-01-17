package ChessLibrary;

import ChessLibrary.Interface.ChessGUI;

public class StartGame {
    /**
     * Starting point of GUI.
     */
    public static void main(String[] args) {
        new ChessGUI(8, 8);
    }
}
