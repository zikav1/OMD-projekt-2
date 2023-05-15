package xl.gui;

import java.awt.Color;
import java.util.Observable;

@SuppressWarnings("deprecation")

public class CurrentSlot extends Observable {
    private SlotLabel slotLabel;

    public CurrentSlot(){

    }

    public void setColor(Color color){
        slotLabel.setBackground(color);
    }

    public String getAddress(){
        return slotLabel.getAddress();
    }

    public void setCurrent(SlotLabel newCurrent){
        this.slotLabel = newCurrent;
        setChanged();
        notifyObservers();
    }
}
