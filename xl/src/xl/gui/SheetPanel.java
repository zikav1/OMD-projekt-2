package xl.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

import xl.model.*;

public class SheetPanel extends BorderPanel {

    public SheetPanel(int rows, int columns, Sheet sheet, CurrentSlot currentSlot) {
        add(WEST, new RowLabels(rows));
        add(CENTER, new SlotLabels(rows, columns, sheet, currentSlot));
    }
}
