package xl.gui;

import java.awt.Color;

public class SlotLabel extends ColoredLabel {

    private String address;

    public SlotLabel(String address) {
        super("                    ", Color.WHITE, RIGHT);
        this.address = address;
    }

    public String getAddress(){
        return address;
    }
}
