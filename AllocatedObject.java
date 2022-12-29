import java.util.ArrayList;

public class AllocatedObject {
    String id;
    int start;
    int end;
    boolean isCopied;
    ArrayList<String> children;
    public AllocatedObject(String id, int start, int end) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.isCopied = false;
        children = new ArrayList<String> ();
    }

    public AllocatedObject(String id) {
        this.id = id;
        this.start = -1;
        this.end = -1;
        children = new ArrayList<String> ();
    }
}
