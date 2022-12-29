import java.io.IOException;

public class MainMarkAndCompact {
    public static void main(String[] args) {
        MarkAndCompact MarkandCompact = new MarkAndCompact();
        String heapPath=null, pointersPath=null,rootsPath =null, newheapPath=null;
        System.out.println(args[0]);
        try {
            heapPath = args[0];
            pointersPath= args[1];
            rootsPath = args[2];
            newheapPath = args[3];

        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ArrayIndexOutOfBoundsException caught");
        }
        finally {
            MarkandCompact.HeapFile(heapPath);
            MarkandCompact.PointersFile(pointersPath);
            MarkandCompact.RootFile(rootsPath);
            try {
                MarkandCompact.markAndCompact();
                MarkandCompact.new_heap_write(newheapPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
