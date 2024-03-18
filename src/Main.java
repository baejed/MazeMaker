import MazeComponents.Maze;
import MazeComponents.Path;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

/*
    Authors:
            Joseph Tristan Subong
            Zecerah Demabildo
            Kristing Cielo Garcia
            Kent Cartoneros

    CS 6/L - 5160 - Algorithms and Complexity - FINAL REQUIREMENT

    Maze Maker
    - A Maze Making game to make mazes and visualize graphs and graph traversal
 */

public class Main {

    static boolean traverse = false;
    static boolean solve = false;
    static ArrayList<Path> traversedPaths;
    static Stack<Path> bestPath;
    static int pathsChecked = 0;
    static boolean brushIncremented = false;
    static boolean brushDecremented = false;
    static boolean isSelectingEndpoint = false;
    static boolean isSelectingStartingPoint = false;
    final static int BUTTON_X = 850;
    final static int BUTTON_X2 = 1050;
    final static Font FONT_SMALLEST = new Font("ARIAL", Font.BOLD, 11);
    final static Font FONT_SMALL = new Font("ARIAL", Font.BOLD, 13);
    final static Font FONT = new Font("ARIAL", Font.BOLD, 15);
    final static Font FONT_LARGE = new Font("ARIAL", Font.BOLD, 30);
    static int speedPerPath = 10;

    public static void main(String[] args) throws InterruptedException {

        JFrame frame = new JFrame();
        Maze maze = new Maze(frame, 100, 100);
        ArrayList<JButton> buttons = new ArrayList<>();

        JButton dfsButton = new JButton();
        JButton bfsButton = new JButton();
        JButton resetButton = new JButton();
        JButton selectStartingPoint = new JButton();
        JButton selectEndPoint = new JButton();
        JButton findBomb = new JButton();
        JButton clearButton = new JButton();
        JButton speedUpButton = new JButton();
        JButton speedDownButton = new JButton();

        buttons.add(dfsButton);
        buttons.add(bfsButton);
        buttons.add(resetButton);
        buttons.add(selectStartingPoint);
        buttons.add(selectEndPoint);
        buttons.add(findBomb);
        buttons.add(clearButton);
        buttons.add(speedUpButton);
        buttons.add(speedDownButton);

        JLabel pathsCheckedLabel = new JLabel();
        JLabel brushSizeLabel = new JLabel();
        JLabel promptLabel = new JLabel();
        JLabel pathsCrossedLabel = new JLabel();
        JLabel speedLabel = new JLabel();
        JLabel controlsLabel = new JLabel();
        JLabel brushSizeControl = new JLabel();
        JLabel paintPathControl = new JLabel();
        JLabel unpaintPathControl = new JLabel();

        frame.setLayout(null);
        frame.setSize(1280, 839);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Maze Maker");

        dfsButton.setBounds(BUTTON_X, 50, 150, 50);
        dfsButton.setText("Simulate DFS");
        dfsButton.setFocusable(false);

        bfsButton.setBounds(BUTTON_X2, 50, 150, 50);
        bfsButton.setText("Simulate BFS");
        bfsButton.setFocusable(false);

        pathsCheckedLabel.setBounds(BUTTON_X, 150, 150, 50);
        pathsCheckedLabel.setText("Paths checked: " + pathsChecked);
        pathsCheckedLabel.setFont(FONT);

        pathsCrossedLabel.setBounds(BUTTON_X, 200, 150, 50);
        pathsCrossedLabel.setText("Paths crossed: 0");
        pathsCrossedLabel.setFont(FONT);

        brushSizeLabel.setBounds(BUTTON_X, 250, 150, 50);
        brushSizeLabel.setText("Brush size: " + (int) maze.getBrushSize());
        brushSizeLabel.setFont(FONT);

        speedLabel.setBounds(BUTTON_X, 300, 170, 50);
        speedLabel.setText("Speed per path: " + speedPerPath + "ms");
        speedLabel.setFont(FONT);

        promptLabel.setBounds(BUTTON_X, 430, 350, 50);
        promptLabel.setFont(FONT_LARGE);

        controlsLabel.setBounds(BUTTON_X, 345, 150, 50);
        controlsLabel.setText("Controls:");
        controlsLabel.setFont(FONT_SMALL);

        paintPathControl.setBounds(BUTTON_X + 20, 365, 150, 50);
        paintPathControl.setText("Paint path: LMB");
        paintPathControl.setFont(FONT_SMALL);

        unpaintPathControl.setBounds(BUTTON_X + 20, 380, 150, 50);
        unpaintPathControl.setText("Erase path: RMB");
        unpaintPathControl.setFont(FONT_SMALL);

        brushSizeControl.setBounds(BUTTON_X + 150, 365, 200, 50);
        brushSizeControl.setText("Set brush size: MW UP/DOWN");
        brushSizeControl.setFont(FONT_SMALL);


        speedUpButton.setBounds(BUTTON_X + 180, 300, 40, 40);
        speedUpButton.setText("+");
        speedUpButton.setFont(FONT_SMALLEST);
        speedUpButton.setFocusable(false);

        speedDownButton.setBounds(BUTTON_X + 230, 300, 40, 40);
        speedDownButton.setText("-");
        speedDownButton.setFont(FONT_SMALLEST);
        speedDownButton.setFocusable(false);

        resetButton.setBounds(BUTTON_X, 700, 150, 50);
        resetButton.setText("Reset");
        resetButton.setFocusable(false);

        selectStartingPoint.setBounds(BUTTON_X, 600, 150, 50);
        selectStartingPoint.setText("Set starting point");
        selectStartingPoint.setFocusable(false);

        selectEndPoint.setBounds(BUTTON_X2, 600, 150, 50);
        selectEndPoint.setText("Set end point");
        selectEndPoint.setFocusable(false);

        findBomb.setBounds(BUTTON_X, 500, 350, 50);
        findBomb.setText("Solve maze");
        findBomb.setFocusable(false);

        clearButton.setBounds(BUTTON_X2, 700, 150, 50);
        clearButton.setText("Clear map");
        clearButton.setFocusable(false);

        frame.add(dfsButton);
        frame.add(bfsButton);
        frame.add(resetButton);
        frame.add(selectEndPoint);
        frame.add(selectStartingPoint);
        frame.add(findBomb);
        frame.add(clearButton);
        frame.add(speedUpButton);
        frame.add(speedDownButton);

        frame.add(pathsCheckedLabel);
        frame.add(brushSizeLabel);
        frame.add(promptLabel);
        frame.add(pathsCrossedLabel);
        frame.add(speedLabel);
        frame.add(controlsLabel);
        frame.add(brushSizeControl);
        frame.add(unpaintPathControl);
        frame.add(paintPathControl);

        dfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    traversedPaths = maze.dfs();
                    traverse = true;
                } catch (Exception f) {
                    alert("Please set starting point");
                }
            }
        });

        bfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //traversedPaths = maze.bfs();
                    traversedPaths = maze.bfs();
                    traverse = true;
                } catch (Exception f) {
                    alert("Please set starting point");
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traverse = false;
                solve = false;
                if (bestPath != null) bestPath.clear();
                maze.unVisitAllPaths();
                pathsCheckedLabel.setText("Paths checked: 0");
                pathsCrossedLabel.setText("Paths crossed: 0");
                promptLabel.setText("");
                enableButtons(buttons);
                maze.enableMaze();
            }
        });

        selectStartingPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSelectingStartingPoint = true;
                isSelectingEndpoint = false;

                if (!maze.startingPointIsSet()) {
                    maze.setStartingPoint();
                    promptLabel.setText("Select starting point");
                }
                if (maze.startingPointIsSet()) {
                    maze.unsetStartingPoint();
                    selectStartingPoint.setText("Set start point");
                }
            }
        });

        selectEndPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSelectingEndpoint = true;
                isSelectingStartingPoint = false;

                if (!maze.endPointIsSet()) {
                    maze.setEndPoint();
                    promptLabel.setText("Select end point");
                }
                if (maze.endPointIsSet()) {
                    maze.unsetEndPoint();
                    selectEndPoint.setText("Set end point");
                }
            }
        });

        findBomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (maze.endPointExistsBfs() && maze.endPointIsSet()) {
                        traversedPaths = maze.bfs();
                        bestPath = maze.bfsDijkstra();
                        solve = true;
                        traverse = true;
                    } else if (maze.endPointIsSet()) alert("End point cannot be found");
                    else alert("End point does not exist");
                } catch (Exception f) {
                    alert("No starting point");
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.clearMaze();
                maze.unVisitAllPaths();
                traverse = false;
                solve = false;
            }
        });

        speedDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (speedPerPath > 5) speedPerPath -= 5;
            }
        });

        speedUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (speedPerPath < 1000) speedPerPath += 5;
            }
        });

        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == -1) {
                    maze.incrementBrushSize();
                    brushIncremented = true;
                } else {
                    maze.decrementBrushSize();
                    brushDecremented = true;
                }

                brushSizeLabel.setText("Brush size: " + (int) maze.getBrushSize());
            }
        });

        while (true) {
            if (traverse && !solve) {
                maze.disableMaze();
                disableButtons(buttons, resetButton);
                for (int i = 0; i < traversedPaths.size(); i++) {
                    if (!traverse) break;
                    traversedPaths.get(i).showTraversed();
                    pathsCheckedLabel.setText("Paths checked: " + ++pathsChecked);
                    Thread.sleep(speedPerPath);
                }
                pathsChecked = 0;
                traverse = false;
                promptLabel.setText("Simulation done");
            }

            if (solve && traverse) {
                maze.disableMaze();
                disableButtons(buttons, resetButton);
                int stepsTaken = 0;
                for (int i = 0; i < traversedPaths.size(); i++) {
                    if (!traverse) break;
                    traversedPaths.get(i).showTraversed();
                    if (traversedPaths.get(i).isEndPoint) break;
                    pathsCheckedLabel.setText("Paths checked: " + ++pathsChecked);
                    Thread.sleep(speedPerPath);
                }
                while (!bestPath.isEmpty()) {
                    Path currentPath = bestPath.pop();

                    if (currentPath.isEndPoint || currentPath.isStartingPoint())
                        currentPath.setBackground(Color.BLUE);
                    else
                        currentPath.setBackground(Color.CYAN);

                    Thread.sleep(speedPerPath);
                    pathsCrossedLabel.setText("Paths crossed: " + ++stepsTaken);
                }
                traverse = false;
                solve = false;
                promptLabel.setText("Maze Solved");
            }

            if (maze.endPointIsSet() && isSelectingEndpoint) {
                selectEndPoint.setText("Remove end point");
                promptLabel.setText("");
                isSelectingEndpoint = false;
            }

            if (maze.startingPointIsSet() && isSelectingStartingPoint) {
                selectStartingPoint.setText("Remove start point");
                promptLabel.setText("");
                isSelectingStartingPoint = false;
            }

            if (brushIncremented) {
                maze.refreshOutline();
                brushIncremented = false;
            }

            if (brushDecremented) {
                maze.refreshOutline();
                brushDecremented = false;
            }

            speedLabel.setText("Speed per path: " + speedPerPath + "ms");

        }

    }

    public static void alert(String alert) {

        JLabel label = new JLabel(alert, JLabel.CENTER);
        JOptionPane optionPane = new JOptionPane(label, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, new String[]{"OK"});
        JDialog dialog = optionPane.createDialog("Alert");
        optionPane.grabFocus();

        // Add KeyListener to the JDialog
        optionPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Close the dialog if Enter key is pressed
                    dialog.dispose();
                }
            }
        });

        dialog.setModal(true);
        dialog.setVisible(true);

    }

    public static void disableButtons(ArrayList<JButton> buttons, JButton exemptedButton) {
        for (JButton button : buttons) {
            if (button == exemptedButton) continue;
            button.setEnabled(false);
        }
    }

    public static void disableButtons(ArrayList<JButton> buttons) {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }

    public static void enableButtons(ArrayList<JButton> buttons) {
        for (JButton button : buttons) {
            button.setEnabled(true);
        }
    }

}