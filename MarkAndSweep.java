import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MarkAndSweep {
    public HashMap<Integer,Heap> heap = new HashMap<>();
    public ArrayList<Integer> rootStack = new ArrayList<>();
    public ArrayList<Heap> Objects = new ArrayList<>();
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
    void PointersFile(String pointersPath) {
        try {
            String parent_child = "";
            BufferedReader br = new BufferedReader(new FileReader(pointersPath));
            while ((parent_child = br.readLine()) != null) {
                parent_child = acceptedline(parent_child);
                String[] temp = parent_child.split(",");
                //temp[0]<--parent_identifier
                //temp[1]<--child_identifier
                if(parent_child==""){
                    break;
                }
                Heap parent = this.heap.get(Integer.parseInt(temp[0]));
                Heap child = this.heap.get(Integer.parseInt(temp[1]));
                parent.addLinkedNode(child);
            }
            //   }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void RootFile(String rootPath){
        try
        {
            String root = "";
            BufferedReader br = new BufferedReader(new FileReader(rootPath));
            while ((root = br.readLine()) != null)
            {
                root = acceptedline(root);
                this.rootStack.add(Integer.parseInt(root));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    void markAndsweep()throws IOException {
        //to mark all reached nodes
        for (int i = 0; i < rootStack.size(); i++) {//loop to mark all used objects
            int id = this.rootStack.get(i);
            Heap object = heap.get(id);
            //check if the node visited or not
            if (!object.marked) {
                //if not marked but reached mark it
                object.MarkNode();
            }
        }

    }
    public void new_heap_write(String path)throws IOException{
        File newHeapFile = new File(path);
        newHeapFile.createNewFile();
        FileWriter new_heap_File = new FileWriter(path);
        Collections.sort(this.Objects);
        //put reached objects in csv file
        for (int i = 0; i < Objects.size(); i++) {
            Heap temp =Objects.get(i);
            if (temp.marked) {
                new_heap_File.append(Integer.toString(temp.object_identifier));
                new_heap_File.append(",");
                new_heap_File.append(Integer.toString(temp.memory_start));
                new_heap_File.append(",");
                new_heap_File.append(Integer.toString(temp.memory_end));
                new_heap_File.append("\n");
            }
        }
        new_heap_File.flush();
        new_heap_File.close();
    }

    public String acceptedline(String str)
    {
        String newStr = "";
        for(int i=0; i<str.length(); i++)
        {
            char ch = str.charAt(i);
            if(Character.isDigit(ch) || ch == ',')
            {
                newStr = newStr + ch;
            }
        }
        return newStr;
    }
}