package xl.gui;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import xl.model.*;

@SuppressWarnings("deprecation")
public class StatusLabel extends ColoredLabel implements Observer {

    private Sheet sheet;

    public StatusLabel(Sheet sheet) {
        super("", Color.WHITE);
        this.sheet = sheet;
        sheet.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object object) {
        setText(sheet.getStatus());
    }
}
