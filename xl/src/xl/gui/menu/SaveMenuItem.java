package xl.gui.menu;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import xl.gui.StatusLabel;
import xl.gui.XL;
import xl.model.Sheet;
import xl.model.Slot;
import xl.util.XLPrintStream;
import java.util.Map;

class SaveMenuItem extends OpenMenuItem {

    private Sheet sheet;

    public SaveMenuItem(XL xl, StatusLabel statusLabel, Sheet sheet) {
        super(xl, statusLabel, "Save");
        this.sheet = sheet;
    }

    protected void action(String path) throws FileNotFoundException {
        Map<String, Slot> sheetMap = sheet.getMap();

        XLPrintStream writer = new XLPrintStream(path);
        
        writer.save(sheetMap.entrySet(), sheet);
        writer.close();
    }

    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showSaveDialog(xl);
    }
}
