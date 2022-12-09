package listener;

import java.awt.*;
import java.awt.event.*;

import gui.ChoosingFrame;

public class ChoiceListener implements ActionListener{
    ChoosingFrame choosingFrame;
    String ipChoosed;

    public ChoiceListener(ChoosingFrame cf,String ipc){
        this.choosingFrame=cf;
        this.ipChoosed=ipc;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(this.ipChoosed);
    }
}
