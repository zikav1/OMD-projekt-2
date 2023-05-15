package xl.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import xl.expr.ExprParser;
import xl.util.XLException;
import xl.expr.Environment;
import xl.expr.Expr;

public class Sheet implements Environment{

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
            sheetMap.put(slotName, new CommentSlot(value.substring(1)));
            return true;
        }

        // Check validity of expression
        Expr expr = null;
        try{
            expr = parser.build(value);
        }
        catch(IOException e){
            System.out.println("Input does not deliver data");
            return false;
        }
        catch(XLException e2){
            System.out.println("Invalid expression");
            return false;
        }

        // Check for reference to blank slot
        try{
            expr.value(this);
        }
        catch(NullPointerException e){
            System.out.println("Blank slots resulting in null value");
            return false;
        }
        
        // Check for circular reference using bombslot
        if(circularRef(slotName, new ExprSlot(expr))){
            System.out.println("Circular reference detected");
            return false;
        }

        // Passed checks, save expression
        sheetMap.put(slotName, new ExprSlot(expr));
        return true;

    }

    private boolean emptySlot(String slotName){
        if(getSlot(slotName) == null) return true;
        return false;
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

    @Override
    public double value(String name) {
        return sheetMap.get(name).getSlotValue(this);
    }
}