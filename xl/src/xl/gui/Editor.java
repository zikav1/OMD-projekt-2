package xl.gui;

import java.awt.Color;
import javax.swing.JTextField;
import java.util.Observable;
import java.util.Observer;

import xl.model.*;

@SuppressWarnings("deprecation")

public class Editor extends JTextField implements Observer{

    private Sheet sheet;
    private CurrentSlot currentSlot;

    public Editor(Sheet sheet, CurrentSlot currentSlot) {
        setBackground(Color.WHITE);
        this.sheet = sheet;
        this.currentSlot = currentSlot;
        currentSlot.addObserver(this);

        
        addActionListener(
            e -> {
                if(getText().length() > 0){
                    sheet.add(currentSlot.getAddress(), getText());
                }
            }
        );
    }

    @Override
    public void update(Observable o, Object arg) {
        Slot slot = sheet.getSlot(currentSlot.getAddress());
        if(slot != null) setText(slot.toString());
        else setText("");
    }
}
