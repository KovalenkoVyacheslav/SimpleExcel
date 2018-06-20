package MODELS;

public class InfoField {
    private int countRow;
    private int countColumn;

    public InfoField(int r, int c) {
        countColumn  = c;
        countRow = r;
    }

    public int getCountRow() {
        return countRow;
    }

    public int getCountColumn() {
        return countColumn;
    }
}
