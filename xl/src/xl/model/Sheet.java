package xl.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import xl.expr.ExprParser;
import xl.util.XLException;
import xl.util.XLBufferedReader;
import xl.util.XLPrintStream;
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
            return false;
        }
        catch(XLException e2){
            System.out.println("Invalid expression");
            status.setStatus("Invalid expression");
            setChanged();
            notifyObservers();
            return false;
        }

        // Check for reference to blank slot or division by zero
        try{
            expr.value(this);
        }
        catch(NullPointerException e){
            System.out.println("Blank slots resulting in null value");
            status.setStatus("Blank slots resulting in null value");
            setChanged();
            notifyObservers();
            return false;
        }
        catch(XLException e2){
            System.out.println("Division by zero");
            status.setStatus("Division by zero");
            setChanged();
            notifyObservers();
            return false;
        }
        
        // Check for circular reference using bombslot
        if(circularRef(slotName, new ExprSlot(expr))){
            System.out.println("Circular reference detected");
            status.setStatus("Circular reference detected");
            setChanged();
            notifyObservers();
            return false;
        }

        // Save new expression and iterate through map, effectively just checking for new divisions by zer0   
        Slot temp;
        if(sheetMap.containsKey(slotName)) temp = sheetMap.get(slotName);
        else temp = null;
        sheetMap.put(slotName, new ExprSlot(expr));

        for(Map.Entry<String, Slot> slot : sheetMap.entrySet()){
            try{
                slot.getValue().getSlotValue(this);
            }
            catch(XLException e){
                status.setStatus("Division by zero in cell " + slot.getKey());
                if(temp != null) sheetMap.put(slotName, temp);
                else sheetMap.remove(slotName);
                setChanged();
                notifyObservers();
                return false;
            }
        }

        // All ok
        setChanged();
        notifyObservers();
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

    public String getFormula(String slotName){
        if(sheetMap.containsKey(slotName)) return sheetMap.get(slotName).toString();
        else return "";
    }

    public String getDisplayString(String slotName){
        if(sheetMap.containsKey(slotName)) {
            Slot slot = sheetMap.get(slotName);
            // Check if comment
            if(slot.toString().startsWith("#")) return slot.toString();
            else return "" + sheetMap.get(slotName).getSlotValue(this);
        }
        else return "                    ";
    }

    public void clearAll(){
        sheetMap.clear();
        status.clearStatus();
        setChanged();
        notifyObservers();
    }

    public void clearSlot(String slotName){

        if(sheetMap.containsKey(slotName)){
            Slot temp = sheetMap.get(slotName);
            sheetMap.remove(slotName);

            for(Map.Entry<String, Slot> slot : sheetMap.entrySet()){
                try{
                    slot.getValue().getSlotValue(this);
                }
                catch(NullPointerException e){
                    status.setStatus("Blank slot reference in cell " + slot.getKey());
                    sheetMap.put(slotName,temp);
                    setChanged();
                    notifyObservers();
                    return;
                }
            }

            status.clearStatus();
        }
        setChanged();
        notifyObservers();

        }

    public void load(XLBufferedReader reader){
        
        reader.load(sheetMap);
        setChanged();
        notifyObservers();
        
        try{
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void save(XLPrintStream writer){
        writer.save(sheetMap.entrySet(), this);
        writer.close();
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
}