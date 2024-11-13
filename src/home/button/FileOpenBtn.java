package home.button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileOpenBtn extends JButton {

    public FileOpenBtn(){
        super("File Open");

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open file;
            }
        };

        addActionListener(actionListener);
    }
}
