package xl.model;

import xl.expr.Environment;
import xl.expr.Expr;

public class ExprSlot implements Slot {

    private Expr expr;

    public ExprSlot(){
        this.expr = expr;
    }


    @Override
    public double getSlotValue(Environment environment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSlotValue'");
    }
    
}
