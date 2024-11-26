package drawing.button;

import drawing.DrawPanel;
import drawing.PdfPanel;
import global.BaseButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PrevPageBtn extends BaseButton {
    public PrevPageBtn(PdfPanel pdfPanel, DrawPanel drawPanel){

        ImageIcon icon=new ImageIcon("src/icon/prev.png");
        Image iconImage=icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        icon.setImage(iconImage);
        setIcon(icon);

        setBorderPainted(false);

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setPageNum(drawPanel.getPageNum()-1);
                pdfPanel.goOtherPage(pdfPanel.getPageNum()-1);
                getParent().getParent().repaint();
            }
        };

        addActionListener(actionListener);
    }
}
