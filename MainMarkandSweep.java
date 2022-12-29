import java.io.IOException;

public class MainMarkandSweep {
    public static void main(String[] args) {
        MarkAndSweep markandsweep = new MarkAndSweep();
        String heapPath=null, pointersPath=null,rootsPath =null, newheapPath=null;
        try {
            heapPath = args[0];//put the heap path here
            pointersPath= args[1];// put ur pointers file path
            rootsPath = args[2];
            newheapPath = args[3];

        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ArrayIndexOutOfBoundsException caught");
        }
        finally {
            markandsweep.HeapFile(heapPath);
            markandsweep.PointersFile(pointersPath);
            markandsweep.RootFile(rootsPath);
            try {
                markandsweep.markAndsweep();
                markandsweep.new_heap_write(newheapPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}