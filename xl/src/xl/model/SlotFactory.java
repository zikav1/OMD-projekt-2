package xl.model;

import java.io.IOException;

import xl.expr.ExprParser;

public class SlotFactory {

    public SlotFactory(){
    }

    public static Slot newSlot(String value){

        ExprParser parser = new ExprParser();

        // Check if comment
        if(value.startsWith("#")) return new CommentSlot(value);

        // Else expression
        else{
            try{
                return new ExprSlot(parser.build(value));  
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        } 
    }
}
