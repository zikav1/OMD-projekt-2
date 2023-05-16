package xl.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingConstants;

import xl.model.*;

@SuppressWarnings("deprecation")

public class SlotLabels extends GridPanel implements Observer{

    private List<SlotLabel> labelList;
    private Sheet sheet;
    private CurrentSlot currentSlot;

    public SlotLabels(int rows, int cols, Sheet sheet, CurrentSlot currentSlot) {
        super(rows + 1, cols);
        this.sheet = sheet;
        this.currentSlot = currentSlot;
        sheet.addObserver(this);

        labelList = new ArrayList<SlotLabel>(rows * cols);
        for (char ch = 'A'; ch < 'A' + cols; ch++) {
            add(new ColoredLabel(Character.toString(ch), Color.LIGHT_GRAY, SwingConstants.CENTER));
        }
        for (int row = 1; row <= rows; row++) {
            for (char ch = 'A'; ch < 'A' + cols; ch++) {

                SlotLabel label = new SlotLabel(ch + String.valueOf(row));

                label.addMouseListener(
                    new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent e){
                            currentSlot.setColor(Color.WHITE);
                            currentSlot.setCurrent(label);
                            currentSlot.setColor(Color.YELLOW);
                        }
                    }
                );

                add(label);
                labelList.add(label);
            }
        }
        currentSlot.setCurrent(labelList.get(0));
        currentSlot.setColor(Color.YELLOW);
    }

    @Override
    public void update(Observable o, Object arg) {
        Map<String, Slot> sheetMap = sheet.getMap();

        for(SlotLabel label : labelList){
            if(sheetMap.containsKey(label.getAddress())){

                Slot slot = sheetMap.get(label.getAddress());

                // Check if comment
                if(slot.toString().startsWith("#")){
                    label.setText(slot.toString().substring(1));
                }

                // Not comment
                else{
                    label.setText("" + slot.getSlotValue(sheet));
                }
            }
            else label.setText("                    ");
        }
    }
}
