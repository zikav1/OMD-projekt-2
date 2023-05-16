package xl.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import xl.model.Sheet;
import xl.gui.CurrentSlot;

class ClearMenuItem extends JMenuItem implements ActionListener {

    private CurrentSlot currentSlot;
    private Sheet sheet;

    public ClearMenuItem(Sheet sheet, CurrentSlot currentSlot) {
        super("Clear");
        this.sheet = sheet;
        this.currentSlot = currentSlot;

        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        sheet.clearSlot(currentSlot.getAddress());
    }
}
