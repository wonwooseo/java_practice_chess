package ChessLibrary.Interface;

import ChessLibrary.ChessBoard;
import ChessLibrary.Game;
import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Util.IntPair;
import ChessLibrary.Util.TurnData;

import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Chess GUI Class.
 * @author Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class ChessGUI {
    // Static ints for default dimension of JFrame and JPanel.
    private static int WINDOW_WIDTH = 800;
    private static int WINDOW_HEIGHT = 800;
    private static int BOARD_WIDTH = 640;
    private static int BOARD_HEIGHT = 640;
    // private variables related to GUI components.
    private int BOARD_ROWS;
    private int BOARD_COLUMNS;
    private Game currentGame;
    private JButton[][] buttonArray;
    private JFrame gameWindow;
    private JPanel chessboardPanel;
    private InterfaceController control;
    // private variables for move related ActionListener.
    private JButton previousButton = null;
    private List<IntPair> previousMovesList = null;
    private String previousId = null;

    /**
     * Class constructor. JFrame and JPanel is created and set up here.
     */
    public ChessGUI(int rows, int columns) {
        BOARD_ROWS = rows;
        BOARD_COLUMNS = columns;
        setupFrame();
    }

    /**
     * Class constructor from existing chessboard.
     * @param turn Which turn to start from.
     * @param source Existing ChessBoard object.
     */
    public ChessGUI(int turn, ChessBoard source) {
        currentGame = new Game(turn, source);
        setupFrame();
    }

    /**
     * Sets up JFrame with specified dimension and sets up JPanel to represent chessboard.
     */
    public void setupFrame() {
        control = new InterfaceController();
        gameWindow = new JFrame("WonChess");
        Dimension expectedDimension = new Dimension(BOARD_WIDTH + 17, BOARD_HEIGHT + 64);
        gameWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameWindow.setMinimumSize(expectedDimension);
        gameWindow.setLayout(new GridBagLayout());
        gameWindow.getContentPane().setBackground(Color.gray);
        JMenuBar menuBar = setupMenuBar();
        gameWindow.setJMenuBar(menuBar);
        gameWindow.setVisible(true);
        // Set Player names
        setPlayerName();
        // Select game mode (use custom piece)
        currentGame = control.startNewGame(BOARD_ROWS, BOARD_COLUMNS, checkUseCustomPiece(gameWindow));
        // JPanel setup
        chessboardPanel = new ChessPanel(BOARD_WIDTH, BOARD_HEIGHT);
        buttonArray = addButtons(chessboardPanel, BOARD_ROWS, BOARD_COLUMNS);
        setupIcons(buttonArray, BOARD_ROWS, BOARD_COLUMNS);
        gameWindow.add(chessboardPanel, new GridBagConstraints());
        gameWindow.setTitle("Turn of " + control.getWhiteName() + " - WonChess");
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets up the menu bar of JFrame.
     * @return JMenuBar object.
     */
    public JMenuBar setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem reset = new JMenuItem("New Game");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        fileMenu.add(reset);
        JMenuItem exit = new JMenuItem("Exit..");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show dialog box and exit game if user selects yes
                int exitOption = JOptionPane.showConfirmDialog(gameWindow, "Do you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
                if(exitOption == 0) {
                    System.exit(0);
                }
            }
        });
        fileMenu.add(exit);
        JMenu gameMenu = new JMenu("Game");
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentGame.getTurn() == 0) {
                    if(control.isBlackStackEmpty()) {
                        JOptionPane.showMessageDialog(gameWindow, "There is no move to undo.", "Warning", JOptionPane.OK_OPTION);
                        return;
                    }
                } else {
                    if(control.isWhiteStackEmpty()) {
                        JOptionPane.showMessageDialog(gameWindow, "There is no move to undo.", "Warning", JOptionPane.OK_OPTION);
                        return;
                    }
                }
                control.undo(currentGame.getTurn(), currentGame.getBoard());
                chessboardPanel.removeAll();
                buttonArray = addButtons(chessboardPanel, BOARD_ROWS, BOARD_COLUMNS);
                setupIcons(buttonArray, BOARD_ROWS, BOARD_COLUMNS);
                previousButton = null;
                previousId = null;
                previousMovesList = null;
            }
        });
        gameMenu.add(undoItem);
        JMenuItem forfeit = new JMenuItem("Surrender..");
        forfeit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int forfeitOption = JOptionPane.showConfirmDialog(gameWindow, "Do you want to surrender?", "Surrender", JOptionPane.YES_NO_OPTION);
                if(forfeitOption == 0) {
                    // Remove current JPanel, make new game and update JPanel
                    int currentTurn = currentGame.getTurn();
                    chessboardPanel.setVisible(false);
                    chessboardPanel.removeAll();
                    resetPrivates();
                    currentGame = null;
                    currentGame = control.forfeit(BOARD_ROWS, BOARD_COLUMNS, currentTurn, checkUseCustomPiece(gameWindow));
                    buttonArray = addButtons(chessboardPanel, BOARD_ROWS, BOARD_COLUMNS);
                    setupIcons(buttonArray, BOARD_ROWS, BOARD_COLUMNS);
                    chessboardPanel.setVisible(true);
                    gameWindow.setTitle("Turn of " + control.getWhiteName() + " - WonChess");
                }
            }
        });
        gameMenu.add(forfeit);
        JMenuItem showScore = new JMenuItem("Show Score..");
        showScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image messageIcon = getImageIcon("/chessboard.png");
                JOptionPane.showMessageDialog(gameWindow, control.getBlackName() + ": " + control.getBlackScore() + "\n" +
                                control.getWhiteName() + ": " + control.getWhiteScore(),
                        "Current Score", JOptionPane.OK_OPTION, new ImageIcon(messageIcon));
            }
        });
        gameMenu.add(showScore);
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        return menuBar;
    }

    /**
     * Adds clickable buttons with set background colors to JPanel. Layout and icon is not defined here.
     * @param panel JPanel to contain created buttons.
     * @param rows Number of rows of buttons.
     * @param columns Number of columns of buttons.
     * @return 2D-array of JButton objects for reference.
     */
    public JButton[][] addButtons(JPanel panel, int rows, int columns) {
        JButton[][] buttons = new JButton[rows][columns];
        panel.setLayout(new GridBagLayout());
        for(int count_y = 0; count_y < 8; count_y ++) {
            if(count_y % 2 == 0) {
                for (int count_x = 0; count_x < 8; count_x += 2) {
                    addButtonToGrid(panel, buttons, count_y, count_x, new Color(251, 201, 159));
                }
            } else {
                for(int count_x = 1; count_x < 8; count_x += 2) {
                    addButtonToGrid(panel, buttons, count_y, count_x, new Color(251, 201, 159));
                }
            }
        }

        for(int count_y = 0; count_y < 8; count_y ++) {
            if(count_y % 2 == 0) {
                for (int count_x = 1; count_x < 8; count_x += 2) {
                    addButtonToGrid(panel, buttons, count_y, count_x, new Color(209, 139, 71));
                }
            } else {
                for (int count_x = 0; count_x < 8; count_x += 2) {
                    addButtonToGrid(panel, buttons, count_y, count_x, new Color(209, 139, 71));
                }
            }
        }
        return buttons;
    }

    /**
     * Sets up icons of the JButton objects to default setup of a chessboard.
     * ActionListener for each JButton is also set in this method.
     * Size of the resized image is defined in private helper function getImageIcon.
     * @param buttons 2D-array of JButton object.
     * @param rows Number of rows of the chessboard.
     * @param columns Number of columns of the chessboard.
     */
    public void setupIcons(JButton[][] buttons, int rows, int columns) {
        ChessBoard board = currentGame.getBoard();
        for(int index_y = 0; index_y < rows; index_y++) {
            for(int index_x = 0; index_x < columns; index_x++) {
                ChessPiece piece = board.getChessPiece(index_x, index_y);
                if(piece == null) {
                    Image imgNull = getImageIconByType(null);
                    buttons[index_y][index_x].setIcon(new ImageIcon(imgNull));
                    setActionListener(buttons[index_y][index_x], null);
                } else {
                    Image imgIcon = getImageIconByType(piece.getIdentifier());
                    buttons[index_y][index_x].setIcon(new ImageIcon(imgIcon));
                    setActionListener(buttons[index_y][index_x], piece.getIdentifier());
                }
            }
        }
    }

    /**
     * Helper function to set layout of buttons.
     */
    private void addButtonToGrid(JPanel panel, JButton[][] buttons, int count_y, int count_x, Color color) {
        JButton button = new JButton();
        button.setBackground(color);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = count_x * (BOARD_WIDTH / BOARD_COLUMNS);
        gbc.gridy = count_y * (BOARD_HEIGHT / BOARD_ROWS);
        gbc.gridwidth = (BOARD_WIDTH / BOARD_COLUMNS);
        gbc.gridheight = (BOARD_HEIGHT / BOARD_ROWS);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setModel(new CustomButtonModel());
        buttons[count_y][count_x] = button;
        panel.add(button, gbc);
    }

    /**
     * Helper function to add action listener which handles click event to JButton.
     * @param button JButton object to add ActionListener.
     * @param initialId Identifier of Pieces.ChessPiece on position.
     */
    private void setActionListener(JButton button, String initialId) {
        button.addActionListener(new ActionListener() {
            boolean pressed = true;
            String currentId = initialId;
            @Override
            public void actionPerformed(ActionEvent e) {
                // Undo highlight of previous clicked button
                if(pressed) {
                    if(previousButton != null) {
                        previousButton.doClick();
                    }
                    previousButton = button;
                } else {
                    previousButton = null;
                }
                // Highlight moves on first click, undo on second click
                List<IntPair> movesList = currentGame.getMovesInterface(currentId);
                if(movesList == null) { // currently clicked button is empty cell
                    if(previousMovesList == null || previousMovesList.size() == 0) { // previous click was meaningless
                        pressed = !pressed;
                        previousMovesList = null;
                        previousId = null;
                        return;
                    } else { // previous click was on movable ally piece
                        IntPair buttonIndex = getButtonIndex(button);
                        int moveIndex;
                        for(moveIndex = 0; moveIndex < previousMovesList.size(); moveIndex++) {
                            if(buttonIndex.equals(previousMovesList.get(moveIndex))) {
                                // Push chessboard to stack before moving
                                ChessPiece movingPiece = currentGame.getBoard().getChessPieceById(previousId);
                                ChessPiece capturedPiece = currentGame.getBoard().getChessPiece(buttonIndex.left(), buttonIndex.right());
                                IntPair origPosition = movingPiece.getPosition();
                                TurnData turnItem = new TurnData(origPosition, buttonIndex, movingPiece, capturedPiece);
                                if(currentGame.getTurn() == 0) { // black's turn
                                    control.pushBlackStack(turnItem);
                                } else {
                                    control.pushWhiteStack(turnItem);
                                }
                                // Move piece on chessBoard and refresh JPanel
                                currentGame.movePieceInterface(moveIndex, previousMovesList, previousId);
                                chessboardPanel.removeAll();
                                buttonArray = addButtons(chessboardPanel, BOARD_ROWS, BOARD_COLUMNS);
                                setupIcons(buttonArray, BOARD_ROWS, BOARD_COLUMNS);
                                previousMovesList = null;
                                previousId = null;
                                previousButton = null;
                                nextTurnHandler();
                                endGameHandler();
                                return;
                            }
                        }
                        pressed = !pressed;
                        previousMovesList = null;
                        previousId = null;
                        return;
                    }
                } else if(movesList.size() == 0) {
                    pressed = !pressed;
                    previousMovesList = movesList;
                    previousId = currentId;
                    return;
                }
                for(int count = 0; count < movesList.size(); count++) {
                    IntPair move = movesList.get(count);
                    Color currentColor = buttonArray[move.right()][move.left()].getBackground();
                    if(pressed) {
                        Color highlightColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue() + 80);
                        buttonArray[move.right()][move.left()].setBackground(highlightColor);
                    } else {
                        Color revertColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue() - 80);
                        buttonArray[move.right()][move.left()].setBackground(revertColor);
                    }
                }
                pressed = !pressed;
                previousMovesList = movesList;
                previousId = currentId;
            }
        });
    }

    /**
     * Helper function to handle changing turn and displaying visual alerts for check / checkmate.
     */
    private void nextTurnHandler() {
        int TURN_BLACK = 0;
        int TURN_WHITE = 1;
        int CHECK = 1;
        int CHECKMATE = 2;

        currentGame.nextTurn();
        currentGame.updateCheckStatus();
        int checkStatus = currentGame.getCheckStatus();
        if(checkStatus == CHECK || checkStatus == CHECKMATE) { // Visual alert for check / checkmate
            if(currentGame.getTurn() == TURN_BLACK) {
                IntPair kingPosition = currentGame.getBoard().getChessPieceById(TURN_BLACK + "K0").getPosition();
                paintThreatColor(kingPosition);
            } else {
                IntPair kingPosition = currentGame.getBoard().getChessPieceById(TURN_WHITE + "K0").getPosition();
                paintThreatColor(kingPosition);
            }
            List<ChessPiece> attackerList = currentGame.getBoard().checkIfCheck(currentGame.getTurn());
            for(int count = 0; count < attackerList.size(); count++) {
                paintThreatColor(attackerList.get(count).getPosition());
            }
        }
        if(currentGame.getTurn() == TURN_BLACK) {
            gameWindow.setTitle("Turn of " + control.getBlackName() + " - WonChess");
        } else {
            gameWindow.setTitle("Turn of " + control.getWhiteName() + " - WonChess");
        }
    }

    /**
     * Helper function to handle game-end status and start a new game.
     */
    private void endGameHandler() {
        int BLACK_WIN = 0;
        int WHITE_WIN = 1;
        int DRAW = 2;

        int gameEndStatus = currentGame.checkGameEnd();
        int newGameCheck;
        if(gameEndStatus == BLACK_WIN) {
            control.incrementBlackScore();
            Image blackKing = getImageIcon("/Black_King.png");
            newGameCheck = JOptionPane.showConfirmDialog(gameWindow,
                    control.getBlackName() + " Wins!\n" + "Start a new game?",
                    control.getBlackName() + " Wins", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon(blackKing));
        } else if(gameEndStatus == WHITE_WIN) {
            control.incrementWhiteScore();
            Image whiteKing = getImageIcon("/White_King.png");
            newGameCheck = JOptionPane.showConfirmDialog(gameWindow,
                    control.getWhiteName() + " Wins!\n" + "Start a new game?",
                    control.getWhiteName() + " Wins", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon(whiteKing));
        } else if(gameEndStatus == DRAW) {
            newGameCheck = JOptionPane.showConfirmDialog(gameWindow,
                    "Draw!\n" + "Start a new game?",
                    "Draw", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            return;
        }
        if(newGameCheck == 0) {
            startNewGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * Helper function to get ImageIcon by type specified in identifier.
     * @param id Identifier of the piece.
     * @return Image object to make an ImageIcon.
     */
    private Image getImageIconByType(String id) {
        if(id == null) {
            return getImageIcon("/null.png");
        }
        char pieceType = id.charAt(1);
        char pieceSide = id.charAt(0);
        Image img;
        switch (pieceType) {
            case 'p': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Pawn.png");
                } else {
                    img = getImageIcon("/White_Pawn.png");
                }
                return img;
            }
            case 'r': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Rook.png");
                } else {
                    img = getImageIcon("/White_Rook.png");
                }
                return img;
            }
            case 'k': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Knight.png");
                } else {
                    img = getImageIcon("/White_Knight.png");
                }
                return img;
            }
            case 'l': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Leaper.png");
                } else {
                    img = getImageIcon("/White_Leaper.png");
                }
                return img;
            }
            case 'b': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Bishop.png");
                } else {
                    img = getImageIcon("/White_Bishop.png");
                }
                return img;
            }
            case 'Q': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Queen.png");
                                  } else {
                    img = getImageIcon("/White_Queen.png");
                 }
                return img;
            }
            case 'c': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_Charger.png");
                } else {
                    img = getImageIcon("/White_Charger.png");
                }
                return img;
            }
            case 'K': {
                if (pieceSide == '0') {
                    img = getImageIcon("/Black_King.png");
                } else {
                    img = getImageIcon("/White_King.png");
                }
                return img;
            }
        }
        return null;
    }

    /**
     * Helper function to read image from resource and resize it. Size of resized image is defined here.
     */
    private Image getImageIcon(String name) {
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource(name));
            img = img.getScaledInstance(BOARD_WIDTH / BOARD_COLUMNS, BOARD_HEIGHT / BOARD_ROWS, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.out.println(e);
        }
        return img;
    }

    /**
     * Helper function to get and set names of players.
     */
    private void setPlayerName() {
        String whitePlayer = JOptionPane.showInputDialog(gameWindow, "Enter player name of white side.", control.getWhiteName());
        if(whitePlayer != null) {
            control.setWhiteName(whitePlayer);
        }
        String blackPlayer = JOptionPane.showInputDialog(gameWindow, "Enter player name of black side.", control.getBlackName());
        if(blackPlayer != null) {
            control.setBlackName(blackPlayer);
        }
    }

    /**
     * Helper function to prompt user to choose whether game should use custom pieces or not and setup the Game object accordingly.
     * @param frame Parent JFrame to determine position of dialog box.
     */
    private boolean checkUseCustomPiece(JFrame frame) {
        int gameMode = JOptionPane.showConfirmDialog(frame, "Use Custom Pieces?", "Custom Piece Confirmation", JOptionPane.YES_NO_OPTION);
        if(gameMode == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper function to get index of given button in the button array.
     * @param button JButton to find index of.
     * @return Index of given JButton in IntPair.
     */
    private IntPair getButtonIndex(JButton button) {
        for(int index_y = 0; index_y < BOARD_ROWS; index_y++) {
            for (int index_x = 0; index_x < BOARD_COLUMNS; index_x++) {
                if(buttonArray[index_y][index_x] == button) {
                    return new IntPair(index_x, index_y);
                }
            }
        }
        return null;
    }

    /**
     * Helper function to start a new game.
     */
    private void startNewGame() {
        // Remove current JPanel, make new game and update JPanel
        chessboardPanel.setVisible(false);
        chessboardPanel.removeAll();
        resetPrivates();
        currentGame = null;
        currentGame = control.startNewGame(BOARD_ROWS, BOARD_COLUMNS, checkUseCustomPiece(gameWindow));
        buttonArray = addButtons(chessboardPanel, BOARD_ROWS, BOARD_COLUMNS);
        setupIcons(buttonArray, BOARD_ROWS, BOARD_COLUMNS);
        chessboardPanel.setVisible(true);
        gameWindow.setTitle("Turn of " + control.getWhiteName() + " - WonChess");
    }

    /**
     * Helper function to reset private variables.
     */
    private void resetPrivates() {
        previousButton = null;
        previousMovesList = null;
        previousId = null;
        control.resetStacks();
    }

    /**
     * Helper function for nextTurnHandler to paint cells with threatened King and attacker red.
     * @param position Position of the piece as IntPair.
     */
    private void paintThreatColor(IntPair position) {
        Color currentColor = buttonArray[position.right()][position.left()].getBackground();
        Color threatColor = new Color(currentColor.getRed(), currentColor.getGreen() - 100, currentColor.getBlue() - 60);
        buttonArray[position.right()][position.left()].setBackground(threatColor);
    }
}

/**
 * ChessPanel class; JPanel for representing a chess board.
 */
class ChessPanel extends JPanel {
    /**
     * ChessPanel constructor. Creates JPanel with given width and height with a border line.
     * @param width Width of the chess board in pixels.
     * @param height Height of the chess board in pixels.
     */
    public ChessPanel(int width, int height) {
        Dimension panelSize = new Dimension(width, height);
        setPreferredSize(panelSize);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
}

/**
 * Custom model of JButton to disable onClick highlight effect.
 */
class CustomButtonModel extends DefaultButtonModel {
    @Override
    public boolean isPressed() {
        return false;
    }
}