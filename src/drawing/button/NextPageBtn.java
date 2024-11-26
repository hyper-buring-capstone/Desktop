package drawing.button;

import drawing.PdfPanel;
import global.BaseButton;
import drawing.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends BaseButton {

    public NextPageBtn(PdfPanel pdfPanel, DrawPanel drawPanel){

        ImageIcon icon=new ImageIcon("src/icon/next.png");
        Image iconImage=icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        icon.setImage(iconImage);
        setIcon(icon);
        setBorderPainted(false);

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setPageNum(drawPanel.getPageNum()+1);
                pdfPanel.goOtherPage(pdfPanel.getPageNum()+1);
                getParent().getParent().repaint(); //8번 트러블 문제랑 비슷하게 해결.
            }
        };

        addActionListener(actionListener);

    }


}


