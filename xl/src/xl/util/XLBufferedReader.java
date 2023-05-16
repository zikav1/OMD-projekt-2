package xl.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import xl.model.Slot;
import xl.model.SlotFactory;

// TODO move to another package
public class XLBufferedReader extends BufferedReader {

    public XLBufferedReader(String name) throws FileNotFoundException {
        super(new FileReader(name));
    }

    public void load(Map<String, Slot> map) {
        try {
            while (ready()) {
                String string = readLine();
                int i = string.indexOf('=');
                map.put(string.substring(0, i), SlotFactory.newSlot(string.substring(i+1)));
            }
        } catch (Exception e) {
            throw new XLException(e.getMessage());
        }
    }
}
