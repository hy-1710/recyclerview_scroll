package in.gripxtech.recyclerviewscrolldemo.model;

public class ListItem {

    private int position;

    public ListItem(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "ListItem(position=" + position + ")";
    }
}
