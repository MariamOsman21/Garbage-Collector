import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CopyGC {
    String heapPath, rootsPath, pointersPath, newHeapPath;
    ArrayList<AllocatedObject> toSpace;
    int lastTraversedIndex;
    int firstFreeByte;
    HashMap<String, AllocatedObject> fromSpace;
    ArrayList<String> stack;

    public CopyGC(String heapPath, String rootsPath, String pointersPath, String newHeapPath) {
        this.heapPath = heapPath;
        this.rootsPath = rootsPath;
        this.pointersPath = pointersPath;
        this.newHeapPath = newHeapPath;
        fromSpace = new HashMap<> ();
        toSpace = new ArrayList<> ();
        stack = new ArrayList<> ();
        lastTraversedIndex = -1;
        firstFreeByte = 0;
    }

    public void collectGarbage() {
        readFiles();
        copyActive();
        writeFile();
    }

    private void copyActive() {
        for(String stackObjId : stack) {
            AllocatedObject stackObject = fromSpace.get(stackObjId);
            if(!stackObject.isCopied) {
                toSpace.add(stackObject);
                stackObject.isCopied = true;
                int size = stackObject.end - stackObject.start;
                stackObject.start = firstFreeByte;
                stackObject.end = firstFreeByte + size;
                firstFreeByte = stackObject.end + 1;
            }
        }
        while(lastTraversedIndex != toSpace.size()-1) {
            lastTraversedIndex++;
            for(String id : toSpace.get(lastTraversedIndex).children) {
                if(!fromSpace.get(id).isCopied) {
                    AllocatedObject heapObject = fromSpace.get(id);
                    toSpace.add(heapObject);
                    heapObject.isCopied = true;
                    int size = heapObject.end - heapObject.start;
                    heapObject.start = firstFreeByte;
                    heapObject.end = firstFreeByte + size;
                    firstFreeByte = heapObject.end + 1;
                }
            }
        }
    }

    private void writeFile() {
        try {
            File newHeapFile = new File(newHeapPath);
            newHeapFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newHeapPath);
            for(AllocatedObject active : toSpace) {
                fileWriter.write(active.id + "," + active.start + "," + active.end + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR!");
        }
    }

    private void readFiles() {
        readHeapFile();
        readRootsFile();
        readPointersFile();
    }

    private void readHeapFile() {
        try {
            File heapFile = new File(heapPath);
            Scanner scanner = new Scanner(heapFile);
            while(scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] strArr = str.split(",");
                String id = strArr[0];
                int start = Integer.parseInt(strArr[1]), end = Integer.parseInt(strArr[2]);
                AllocatedObject obj = new AllocatedObject(id, start, end);
                fromSpace.put(id, obj);
            }
            scanner.close();
        } catch(FileNotFoundException exception) {
            System.out.println("File Not Found!");
        }
    }

    private void readRootsFile() {
        try {
            File rootsFile = new File(rootsPath);
            Scanner scanner = new Scanner(rootsFile);
            while(scanner.hasNextLine()) {
                String id;
                id = scanner.nextLine();
                if(fromSpace.get(id) != null) stack.add(id);
            }
            scanner.close();
        } catch(FileNotFoundException exception) {
            System.out.println("File Not Found!");
        }
    }

    private void readPointersFile() {
        try {
            File pointersFile = new File(pointersPath);
            Scanner scanner = new Scanner(pointersFile);
            while(scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] pair = str.split(",");
                if(fromSpace.get(pair[0]) == null) break;
                fromSpace.get(pair[0]).children.add(pair[1]);
            }
            scanner.close();
        } catch(FileNotFoundException exception) {
            System.out.println("File Not Found!");
        }
    }

    public static void main(String[] args) {
        CopyGC collector = new CopyGC(args[0], args[1], args[2], args[3]);
        collector.collectGarbage();
    }
}