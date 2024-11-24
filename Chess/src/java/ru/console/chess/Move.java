package ru.console.chess;

public class Move {
    private final Point from;
    private final Point to;

    public Move(int x, int y, int x1, int y1) {
        from = new Point(x,y);
        to = new Point(x1, y1);
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }
}
