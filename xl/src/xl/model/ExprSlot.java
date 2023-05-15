package xl.model;

import xl.expr.Environment;
import xl.expr.Expr;

public class ExprSlot implements Slot {

    private Expr expr;

    public ExprSlot(Expr expr){
        this.expr = expr;
    }


    @Override
    public double getSlotValue(Environment environment) {
        // Return value of expression
        return expr.value(environment);
    }
    
}
