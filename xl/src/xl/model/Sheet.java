package xl.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import xl.expr.ExprParser;
import xl.util.XLException;
import xl.expr.Environment;
import xl.expr.Expr;

@SuppressWarnings("deprecation")

public class Sheet extends Observable implements Environment {

    private Map<String, Slot> sheetMap;
    private Status status;
    private ExprParser parser = new ExprParser();
    
    public Sheet(){
        this.sheetMap = new HashMap<String, Slot>();
        this.status = new Status();
    }

    /**
     * 
     * @param slotName The name of the slot, e.g "D3" or "A2"
     * @param value The contents of the slot as a string
     * @return true if slotName - value pair was successfully added, false if an error was encountered
     */
    public boolean add(String slotName, String value){
        
        // Check if it is a comment
        if(value.startsWith("#")){
            sheetMap.put(slotName, new CommentSlot(value));
            setChanged();
            notifyObservers();
            printSheet();
            return true;
        }

        // Check validity of expression
        Expr expr = null;
        try{
            expr = parser.build(value);
        }
        catch(IOException e){
            System.out.println("Input does not deliver data");
            status.setStatus("Input does not deliver data");
            setChanged();
            notifyObservers();
            printSheet();
            return false;
        }
        catch(XLException e2){
            System.out.println("Invalid expression");
            status.setStatus("Invalid expression");
            setChanged();
            notifyObservers();
            printSheet();
            return false;
        }

        // Check for reference to blank slot
        try{
            expr.value(this);
        }
        catch(NullPointerException e){
            System.out.println("Blank slots resulting in null value");
            status.setStatus("Blank slots resulting in null value");
            setChanged();
            notifyObservers();
            printSheet();
            return false;
        }
        
        // Check for circular reference using bombslot
        if(circularRef(slotName, new ExprSlot(expr))){
            System.out.println("Circular reference detected");
            status.setStatus("Circular reference detected");
            setChanged();
            notifyObservers();
            printSheet();
            return false;
        }

        // Passed checks, save expression
        sheetMap.put(slotName, new ExprSlot(expr));
        setChanged();
        notifyObservers();
        printSheet();
        return true;

    }

    private boolean circularRef(String slotName, Slot slot){

        Slot temp = sheetMap.get(slotName);
        sheetMap.put(slotName, new BombSlot());

        try {
            slot.getSlotValue(this);
        } 
        catch (NullPointerException e) {
            System.out.println("Circular reference detected");
            sheetMap.put(slotName, temp);
            return true;
        } 
        catch (XLException e2){
            System.out.println("Circular reference detected");
            sheetMap.put(slotName, temp);
            return true;
        }

        sheetMap.put(slotName, temp);
        return false;
    }

    public Slot getSlot(String slotName){
        return sheetMap.get(slotName);
    }

    public void clearAll(){
        sheetMap.clear();
        status.clearStatus();
    }

    public void clearSlot(String slotName){
        if(sheetMap.containsKey(slotName)){
            sheetMap.put(slotName, null);
            status.clearStatus();
        }
    }

    public void load(Map<String, Slot> loadMap){
        sheetMap = loadMap;
        setChanged();
        notifyObservers();
    }

    public Map<String, Slot> getMap(){
        Map<String, Slot> result = sheetMap;
        return result;
    }

    @Override
    public double value(String name) {
        return sheetMap.get(name).getSlotValue(this);
    }

    public String getStatus(){
        return status.getStatus();
    }

    public void printSheet(){
        String[] addresses = {
            "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10",
            "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10",
            "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
            "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10",
            "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10",
            "G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "G10",
            "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10",
        };

        for(String address : addresses){
            if(sheetMap.containsKey(address)) System.out.println(address + ": " + sheetMap.get(address).getSlotValue(this));
            else System.out.println(address + ": blank" );
        }
    }
}