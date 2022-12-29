import java.util.*;
class Heap implements Comparable<Heap>{
    int object_identifier;
    int memory_start;
    int memory_end;
    int size;
    boolean marked;
    LinkedList<Heap> linkedNode ;
    public Heap(int id, int memory_start, int End){
        this.object_identifier = id;
        this.memory_start = memory_start;
        this.memory_end = End;
        linkedNode=new LinkedList<>();
        this.marked = false;
        this.size = memory_end - memory_start + 1;
    }
    @Override
    public int compareTo(Heap object) {
        if(memory_start==object.memory_start){
            return 0;
        }
        else if(memory_start>object.memory_start){
            return 1;
        }
        else{
            return -1;
        }
    }
    public void addLinkedNode(Heap child){//add pointer to the current object
        linkedNode.add(child);

    }
    public void MarkNode() {
        this.marked = true;
        for(int i = 0; i < this.linkedNode.size(); ++i) {
            if (!this.linkedNode.get(i).marked) {
                (this.linkedNode.get(i)).MarkNode();
            }
        }

    }

}
