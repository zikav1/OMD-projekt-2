package xl.model;

import xl.expr.Environment;

public class CommentSlot implements Slot{
    
    private String comment;

    public CommentSlot(String comment){
        this.comment = comment;
    }

    @Override
    public double getSlotValue(Environment environment) {
        // Return 0
        return 0.0;
    }

    @Override
    public String toString(){
        return comment;
    }
}
