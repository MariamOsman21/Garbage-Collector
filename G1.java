import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class G1 extends MarkAndSweep {
    private int cellSize;
    private int[][] full = new int[16][2];

    public G1(int heapSize){
        cellSize = heapSize/16;
        for(int i = 0; i < 16; i++){
            full[i][0]= cellSize;
            full[i][1]= 0;
        }
    }
    @Override
    void HeapFile(String heapPath){
        try
        {
            String totalinfo = "";
            BufferedReader br = new BufferedReader(new FileReader(heapPath));
            while ((totalinfo = br.readLine()) != null)   //returns a Boolean value
            {
                totalinfo = acceptedline(totalinfo);
                //"," to separate id ,memory start and memory end
                String[] temp = totalinfo.split(",");
                Heap Object = new Heap(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                full[Object.memory_start/cellSize][0] -= Object.size;
                full[Object.memory_start/cellSize][1] = 1;
                Objects.add(Object);
                //put object identifier as key of hashmap and object as value
                heap.put(Object.object_identifier,Object);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //read pointers file and add linked nodes
    private void garbageFirst(){
        for(Heap object: Objects){
            if(!object.marked){
                full[object.memory_start/cellSize][0] += object.size;
                if(full[object.memory_start/cellSize][0] == cellSize)
                    full[object.memory_start/cellSize][1] = 0;
            }
        }
    }
    private void defragment() {
        Collections.sort(Objects);
        for (Heap object: Objects) {
            if(object.marked) {
                for (int j = 0; j < 16; j++) {
                    if (full[j][1] == 0 && object.size <= full[j][0]) {
                        full[object.memory_start / cellSize][0] += object.size;
                        object.memory_start = cellSize * j + (cellSize - full[j][0]);
                        object.memory_end = object.memory_start + (object.size-1);
                        full[j][0] -= object.size;
                        break;
                    }
                }
            }
        }
    }
    public void collectGarbage(String path) throws IOException {
        super.markAndsweep();
        garbageFirst();
        defragment();
        super.new_heap_write(path);
    }
    public static void main(String[] args) throws IOException {
        int heapSize = Integer.parseInt(args[0]);
        String heapPath = args[1];
        String rootPath = args[2];
        String pointersPath = args[3];
        String newHeapPath = args[4];
        G1 g1 = new G1(heapSize);
        g1.HeapFile(heapPath);
        g1.RootFile(rootPath);
        g1.PointersFile(pointersPath);
        g1.collectGarbage(newHeapPath);
    }

}
