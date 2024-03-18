package MazeComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Maze {

    private static final Color OUTLINE_COLOR = Color.DARK_GRAY;

    // Private fields
    private Path[][] mazePaths;
    private boolean pathifyEnabled = false;
    private boolean unpathifyEnabled = false;
    private Path startingPoint;
    private Path endPoint;
    private Path selectedPath;
    private ArrayList<Path> outlines;

    // Final fields
    private final Maze maze;
    private final int width;
    private final int height;

    // Other fields
    private int brushSize = 2;
    private boolean selectStartingPoint = false;
    private boolean selectEndPoint = false;
    private boolean hovering = false;
    private boolean mazeDisabled = false;

    public Maze(JFrame frame, int x, int y) {

        int gap = 0;
        width = x;
        height = y;
        this.maze = this;

        mazePaths = new Path[y][x];

        frame.setVisible(true);

        for (int i = 0, yPos = 0; i < mazePaths.length; i++, yPos += (Path.getPathHeight() + gap)) {
            for (int j = 0, xPos = 0; j < mazePaths[i].length; j++, xPos += (Path.getPathWidth() + gap)) {
                mazePaths[i][j] = new Path(xPos, yPos, i, j);
            }
        }

        for (int i = 0; i < mazePaths.length; i++) {
            for (int j = 0; j < mazePaths[i].length; j++) {
                Path currentPath = mazePaths[i][j];

                frame.add(currentPath);

                currentPath.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(mazeDisabled) return;
                        int mouseButtonPressed = e.getButton();

                        if (mouseButtonPressed == 2) {
                            System.out.println("Num of neighbors: " + currentPath.getAvailablePaths().size());
                            System.out.println("Steps to reach: " + currentPath.getStepsToReach());
                            System.out.println("Visited: " + currentPath.isVisited());
                            if (currentPath.isPath()) System.out.println("is path");
                            else System.out.println("not path");
                            System.out.println();
                        }

                        if (mouseButtonPressed == 1 && !unpathifyEnabled && (!selectStartingPoint && !selectEndPoint)) {
                            pathifyEnabled = true;
                            currentPath.pathify(maze, brushSize);
                        }

                        if (mouseButtonPressed == 3 && !pathifyEnabled && (!selectStartingPoint && !selectEndPoint)) {
                            unpathifyEnabled = true;
                            currentPath.unpathify(maze, brushSize);
                        }

                        if (mouseButtonPressed == 1 && selectStartingPoint) {
                            if (currentPath != endPoint) {
                                if (startingPoint != null) {
                                    startingPoint.unsetStartingPoint();
                                    startingPoint.pathify(maze, 1);
                                }
                                startingPoint = currentPath;
                                selectStartingPoint = false;
                                currentPath.pathify(maze, 1);
                                currentPath.setStartingPoint();
                            }
                        }

                        if (mouseButtonPressed == 1 && selectEndPoint) {
                            if (currentPath != startingPoint) {
                                if (endPoint != null) {
                                    endPoint.unsetEndPoint();
                                    endPoint.pathify(maze, 1);
                                }
                                endPoint = currentPath;
                                selectEndPoint = false;
                                currentPath.pathify(maze, 1);
                                currentPath.setEndPoint();
                            }
                        }

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if(mazeDisabled) return;
                        int mouseButtonReleased = e.getButton();
                        if (mouseButtonReleased == 1) pathifyEnabled = false;
                        if (mouseButtonReleased == 3) unpathifyEnabled = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if(mazeDisabled) return;
                        if (pathifyEnabled) currentPath.pathify(maze, brushSize);
                        if (unpathifyEnabled) currentPath.unpathify(maze, brushSize);
                        outlines = currentPath.hover(maze, brushSize, OUTLINE_COLOR);
                        selectedPath = currentPath;
                        hovering = true;
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(mazeDisabled) return;
                        currentPath.unHover(outlines);
                        hovering = false;
                        selectedPath = null;
                    }
                });

                currentPath.addMouseWheelListener(new MouseWheelListener() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        if (hovering && !mazeDisabled) {
                            currentPath.unHover(outlines);
                            outlines = currentPath.hover(maze, brushSize, OUTLINE_COLOR);
                        }
                    }
                });
            }

        }

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Path[][] getMazePaths() {
        return mazePaths;
    }

    public void displayMaze() {
        for (int i = 0; i < mazePaths.length; i++) {
            for (int j = 0; j < mazePaths[i].length; j++) {
                if (mazePaths[i][j].isPath())
                    System.out.print(1 + " ");
                else
                    System.out.print(0 + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public ArrayList<Path> dfs() {
        ArrayList<Path> visitedNodes = new ArrayList<>();
        Stack<Path> stack = new Stack<>();

        stack.push(this.startingPoint);

        do {
            Path currentPath = stack.pop();
            ArrayList<Path> currentPathAvailablePaths = currentPath.getAvailablePaths();

            currentPath.visit();
            visitedNodes.add(currentPath);

            for (Path path : currentPathAvailablePaths) {
                if (!path.isVisited() && !stack.contains(path)) stack.push(path);
            }
        } while (!stack.isEmpty());

        return visitedNodes;
    }

    public ArrayList<Path> bfs() {
        ArrayList<Path> visitedNodes = new ArrayList<>();
        Queue<Path> queue = new LinkedList<>();

        queue.add(this.startingPoint);

        do {
            Path currentPath = queue.remove();
            ArrayList<Path> availablePaths = currentPath.getAvailablePaths();

            currentPath.visit();
            visitedNodes.add(currentPath);

            for (Path path : availablePaths) {
                if (!path.isVisited() && !queue.contains(path)) queue.add(path);
            }
        } while (!queue.isEmpty());

        this.unVisitAllPaths();
        return visitedNodes;
    }

    public boolean endPointExistsBfs() {
        Queue<Path> queue = new LinkedList<>();

        queue.add(this.startingPoint);

        do {
            Path currentPath = queue.remove();
            ArrayList<Path> availablePaths = currentPath.getAvailablePaths();

            currentPath.visit();
            if (currentPath.isEndPoint) {
                unVisitAllPaths();
                return true;
            }

            for (Path path : availablePaths) {
                if (!path.isVisited() && !queue.contains(path)) queue.add(path);
            }
        } while (!queue.isEmpty());

        unVisitAllPaths();
        return false;
    }

    public ArrayList<Path> dfsDijkstra() {
        ArrayList<Path> visitedNodes = new ArrayList<>();
        Stack<Path> stack = new Stack<>();

        stack.push(this.startingPoint);

        do {
            Path currentPath = stack.pop();
            ArrayList<Path> currentPathAvailablePaths = currentPath.getAvailablePaths();

            currentPath.visit();
            visitedNodes.add(currentPath);

            for (Path path : currentPathAvailablePaths) {
                if (!path.isVisited() && !stack.contains(path)) {
                    stack.push(path);
                    updateStepsToReach(currentPath, path);
                }
            }
        } while (!stack.isEmpty());

        return visitedNodes;
    }

    public Stack<Path> bfsDijkstra() {
        Queue<Path> queue = new LinkedList<>();

        queue.add(this.startingPoint);

        do {
            Path currentPath = queue.remove();
            ArrayList<Path> availablePaths = currentPath.getAvailablePaths();

            currentPath.visit();

            for (Path path : availablePaths) {
                if (!path.isVisited() && !queue.contains(path)) {
                    queue.add(path);
                    updateStepsToReach(currentPath, path);
                }
            }
        } while (!queue.isEmpty());

        return getBestPath(endPoint);
    }

    public void unVisitAllPaths() {
        for (Path[] paths : mazePaths) {
            for (Path path : paths) {
                path.unVisit();
            }
        }
    }

    public void clearMaze() {
        if (maze.endPointIsSet()) this.unsetEndPoint();
        if (maze.startingPointIsSet()) this.unsetStartingPoint();

        for (Path[] paths : mazePaths) {
            for (Path path : paths) {
                path.unVisit();
                path.unpathify(maze, 0);
            }
        }
    }

    public void incrementBrushSize() {
        if (brushSize >= 5) return;
        this.brushSize++;
    }

    public void decrementBrushSize() {
        if (brushSize <= 1) return;
        this.brushSize--;
    }

    public double getBrushSize() {
        return this.brushSize;
    }

    public void refreshOutline() {
        try {
            selectedPath.unHover(outlines);
            outlines = selectedPath.hover(maze, brushSize, OUTLINE_COLOR);
        } catch (Exception ignored) {
        }
    }

    public void setStartingPoint() {
        selectStartingPoint = true;
        selectEndPoint = false;
    }

    public void setEndPoint() {
        selectEndPoint = true;
        selectStartingPoint = false;
    }

    public boolean startingPointIsSet() {
        return (startingPoint != null);
    }

    public boolean endPointIsSet() {
        return (endPoint != null);
    }


    public void updateStepsToReach(Path currentPath, Path adjacentPath) {
        int stepsToReachCurrentPath = currentPath.getStepsToReach();
        int stepsToReachAdjPath = adjacentPath.getStepsToReach();
        int newStepsToReachAdjPath = stepsToReachCurrentPath + 1;
        if (newStepsToReachAdjPath < stepsToReachAdjPath) {
            adjacentPath.setStepsToReach(newStepsToReachAdjPath);
            adjacentPath.setPreviousPath(currentPath);
        }
    }

    public Stack<Path> getBestPath(Path path) {
        Stack<Path> bestPath = new Stack<>();
        Path currentPath = path;

        bestPath.push(currentPath);

        while (currentPath.getPreviousPath() != null) {
            currentPath = currentPath.getPreviousPath();
            bestPath.push(currentPath);
        }
        return bestPath;
    }

    public boolean isHovering() {
        return hovering;
    }

    public void unsetEndPoint() {
        endPoint.unsetEndPoint();
        endPoint = null;
    }

    public void unsetStartingPoint() {
        startingPoint.unsetStartingPoint();
        startingPoint = null;
    }

    public void disableMaze() {
        mazeDisabled = true;
    }

    public void enableMaze() {
        mazeDisabled = false;
    }

}