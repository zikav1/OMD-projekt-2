package xl.gui.menu;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import xl.gui.StatusLabel;
import xl.gui.XL;
import xl.util.XLBufferedReader;

import java.util.HashMap;
import java.util.Map;
import xl.model.Slot;
import xl.model.Sheet;

class LoadMenuItem extends OpenMenuItem {

    private Sheet sheet;

    public LoadMenuItem(XL xl, StatusLabel statusLabel, Sheet sheet) {
        super(xl, statusLabel, "Load");
        this.sheet = sheet;
    }

    protected void action(String path) throws FileNotFoundException {
        XLBufferedReader reader = new XLBufferedReader(path);
        Map<String, Slot> sheetMap = new HashMap<String, Slot>();

        reader.load(sheetMap);
        sheet.load(sheetMap);

        try{
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showOpenDialog(xl);
    }
}
