package MazeComponents;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Path extends JPanel {

    final private static int WIDTH = 8;
    final private static int HEIGHT = 8;
    final private static Color BACKGROUND_COLOR = Color.BLACK;
    final private static Color TRAVERSED_COLOR = Color.GRAY;
    final private static Color PATH_COLOR = Color.WHITE;
    final private static Color STARTING_COLOR = Color.GREEN;
    final private static Color END_POINT_COLOR = Color.RED;

    private boolean isVisited = false;
    private ArrayList<Path> availablePaths = new ArrayList<>();
    private boolean isPath = false;
    private boolean isHovered = false;
    private boolean isStartingPoint = false;
    public boolean isEndPoint = false;
    private final int rowIndex;
    private final int columnIndex;
    private int stepsToReach = Integer.MAX_VALUE;
    private Path previousPath;
    private boolean isTraversed = false;

    public Path(int XLocation, int YLocation, int rowIndex, int columnIndex) {

        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;

        this.setBounds(XLocation, YLocation, WIDTH, HEIGHT);
        this.setOpaque(true);
        this.setBackground(BACKGROUND_COLOR);
        this.setVisible(true);

    }

    public static int getPathWidth() {
        return WIDTH;
    }

    public static int getPathHeight() {
        return HEIGHT;
    }

    public void pathify(Maze maze, double brushSize) {

        if (!this.isStartingPoint && !this.isEndPoint) this.setBackground(PATH_COLOR);

        Path[][] mazePaths = maze.getMazePaths();
        HashMap<String, Integer> neighborIndex = this.neighborIndices(this.rowIndex, this.columnIndex);

        int upper = neighborIndex.get("upper");
        int lower = neighborIndex.get("lower");
        int left = neighborIndex.get("left");
        int right = neighborIndex.get("right");

        this.isPath = true;
        brushSize--;
        brushSize = Math.floor(brushSize);

        if (upper >= 0) {
            Path upperPath = mazePaths[upper][this.columnIndex];
            if (brushSize > 0) upperPath.pathify(maze, brushSize);
            if (upperPath.isPath && !availablePaths.contains(upperPath))
                this.addAvailablePath(mazePaths[upper][this.columnIndex]);
        }

        if (lower < maze.getHeight()) {
            Path lowerPath = mazePaths[lower][this.columnIndex];
            if (brushSize > 0) lowerPath.pathify(maze, brushSize);
            if (lowerPath.isPath && !availablePaths.contains(lowerPath))
                this.addAvailablePath(lowerPath);
        }

        if (left >= 0) {
            Path leftPath = mazePaths[this.rowIndex][left];
            if (brushSize > 0) leftPath.pathify(maze, brushSize);
            if (leftPath.isPath && !availablePaths.contains(leftPath))
                this.addAvailablePath(leftPath);
        }

        if (right < maze.getWidth()) {
            Path rightPath = mazePaths[this.rowIndex][right];
            if (brushSize > 0) rightPath.pathify(maze, brushSize);
            if (rightPath.isPath && !availablePaths.contains(rightPath))
                this.addAvailablePath(rightPath);
        }
    }

    public void unpathify(Maze maze, double brushSize) {
        if (!(this.isStartingPoint || this.isEndPoint)) this.setBackground(BACKGROUND_COLOR);

        Path[][] mazePaths = maze.getMazePaths();
        HashMap<String, Integer> neighborIndex = this.neighborIndices(this.rowIndex, this.columnIndex);
        int upper = neighborIndex.get("upper");
        int lower = neighborIndex.get("lower");
        int left = neighborIndex.get("left");
        int right = neighborIndex.get("right");

        if (!(this.isStartingPoint || this.isEndPoint)) this.isPath = false;
        brushSize--;
        brushSize = Math.floor(brushSize);

        this.availablePaths.clear();

        if (upper >= 0) {
            Path upperPath = mazePaths[upper][this.columnIndex];
            if (brushSize > 0) upperPath.unpathify(maze, brushSize);
            if (upperPath.isPath) upperPath.removeAvailablePath(this);
        }

        if (lower < maze.getHeight()) {
            Path lowerPath = mazePaths[lower][this.columnIndex];
            if (brushSize > 0) lowerPath.unpathify(maze, brushSize);
            if (lowerPath.isPath) lowerPath.removeAvailablePath(this);
        }

        if (left >= 0) {
            Path leftPath = mazePaths[this.rowIndex][left];
            if (brushSize > 0) leftPath.unpathify(maze, brushSize);
            if (leftPath.isPath) leftPath.removeAvailablePath(this);
        }

        if (right < maze.getWidth()) {
            Path rightPath = mazePaths[this.rowIndex][right];
            if (brushSize > 0) rightPath.unpathify(maze, brushSize);
            if (rightPath.isPath) rightPath.removeAvailablePath(this);
        }
    }

    public ArrayList<Path> hover(Maze maze, int brushSize, Color hoverColor) {

        Path[][] mazePaths = maze.getMazePaths();
        HashMap<String, Integer> neighborIndex = this.neighborIndices(this.rowIndex, this.columnIndex);
        ArrayList<Path> outlines = new ArrayList<>();
        int numOfDiagonals = (brushSize - 1) * 4;

        int upper = this.rowIndex - brushSize;
        int lower = this.rowIndex + brushSize;
        int left = this.columnIndex - brushSize;
        int right = this.columnIndex + brushSize;

        boolean hasUpper = upper >= 0;
        boolean hasLower = lower < maze.getHeight();
        boolean hasLeftPath = left >= 0;
        boolean hasRightPath = right < maze.getWidth();

        if (hasUpper) {
            Path upperPath = mazePaths[upper][this.columnIndex];
            upperPath.setBackground(hoverColor);
            outlines.add(upperPath);
        }

        if (hasLower) {
            Path lowerPath = mazePaths[lower][this.columnIndex];
            lowerPath.setBackground(hoverColor);
            outlines.add(lowerPath);
        }

        if (hasLeftPath) {
            Path leftPath = mazePaths[this.rowIndex][left];
            leftPath.setBackground(hoverColor);
            outlines.add(leftPath);
        }

        if (hasRightPath) {
            Path rightPath = mazePaths[this.rowIndex][right];
            rightPath.setBackground(hoverColor);
            outlines.add(rightPath);
        }

        return outlines;

    }

    public void unHover(ArrayList<Path> outlines) {

        for (Path path : outlines) {
            if (path.isStartingPoint)
                path.setBackground(STARTING_COLOR);
            else if (path.isEndPoint)
                path.setBackground(END_POINT_COLOR);
            else if (path.isPath)
                path.setBackground(PATH_COLOR);
            else
                path.setBackground(BACKGROUND_COLOR);
        }

    }

    public boolean isVisited() {
        return isVisited;
    }

    public void visit() {
        isVisited = true;
    }

    public void showTraversed() {
        if (!this.isStartingPoint && !this.isEndPoint) {
            this.setBackground(TRAVERSED_COLOR);
            this.isTraversed = true;
        }
    }

    public void setHovered() {
        this.isHovered = true;
    }

    public boolean isHovered() {
        return this.isHovered;
    }

    public void unVisit() {
        if (!isPath) return;
        isVisited = false;
        if (!isStartingPoint) {
            this.previousPath = null;
            this.stepsToReach = Integer.MAX_VALUE;
        }
        this.setBackground(PATH_COLOR);
        if (this.isStartingPoint) this.setBackground(STARTING_COLOR);
        if (this.isEndPoint) this.setBackground(END_POINT_COLOR);
    }

    public boolean isPath() {
        return isPath;
    }

    public void addAvailablePath(Path path) {
        this.availablePaths.add(path);
        path.availablePaths.add(this);
    }

    public void removeAvailablePath(Path path) {
        this.availablePaths.remove(path);
    }

    public HashMap<String, Integer> neighborIndices(int row, int column) {
        HashMap<String, Integer> neighborIndices = new HashMap<>();
        int upper = row - 1;
        int lower = row + 1;
        int left = column - 1;
        int right = column + 1;

        neighborIndices.put("upper", upper);
        neighborIndices.put("lower", lower);
        neighborIndices.put("left", left);
        neighborIndices.put("right", right);

        return neighborIndices;
    }

    public ArrayList<Path> getAvailablePaths() {
        return availablePaths;
    }

    public void setStartingPoint() {
        this.isStartingPoint = true;
        this.stepsToReach = 0;
        this.setBackground(STARTING_COLOR);
    }

    public void unsetStartingPoint() {
        this.isStartingPoint = false;
        this.setBackground(PATH_COLOR);
        this.stepsToReach = Integer.MAX_VALUE;
    }

    public boolean isStartingPoint() {
        return isStartingPoint;
    }

    public void setEndPoint() {
        this.isEndPoint = true;
        this.setBackground(END_POINT_COLOR);
    }

    public void unsetEndPoint() {
        this.isEndPoint = false;
        this.setBackground(PATH_COLOR);
    }

    public void setStepsToReach(int newStepsToReach) {
        stepsToReach = newStepsToReach;
    }

    public int getStepsToReach() {
        return this.stepsToReach;
    }

    public void setPreviousPath(Path path) {
        this.previousPath = path;
    }

    public Path getPreviousPath() {
        return this.previousPath;
    }

    public boolean isTraversed() {
        return isTraversed;
    }

}