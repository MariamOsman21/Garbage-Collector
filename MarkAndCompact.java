import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class MarkAndCompact extends MarkAndSweep {
    @Override
    //same as mark and sweep but update addresses
    public void new_heap_write(String path)throws IOException{
        File newHeapFile = new File(path);
        newHeapFile.createNewFile();
        int new_memory_start = 0;
        FileWriter new_heap = new FileWriter(path);
        Collections.sort(Objects);
        for (int i = 0; i < Objects.size(); i++){
            Heap temp = Objects.get(i);
            if(temp.marked){
                temp.memory_start = new_memory_start;
                new_memory_start = new_memory_start+ temp.size-1;
                temp.memory_end = new_memory_start;
                new_memory_start++;
                new_heap.append(Integer.toString(temp.object_identifier));
                new_heap.append(",");
                new_heap.append(Integer.toString(temp.memory_start));
                new_heap.append(",");
                new_heap.append(Integer.toString(temp.memory_end));
                new_heap.append("\n");
            }
        }
        new_heap.flush();
        new_heap.close();
    }
    //mark reached objects
    void markAndCompact() throws IOException {
        for (int i = 0; i < rootStack.size(); i++) {
            int id = rootStack.get(i);
            Heap object = heap.get(id);
            //check if the node visited or not
            if (!object.marked) {
                //if not marked but reached mark it
                object.MarkNode();
            }
        }
    }
}