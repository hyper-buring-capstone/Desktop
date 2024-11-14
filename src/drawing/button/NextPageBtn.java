package drawing.button;

import drawing.PdfPanel;
import drawing.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends JButton {

    public NextPageBtn(PdfPanel pdfPanel, DrawPanel drawPanel){
        super("다음 페이지");

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setPageNum(drawPanel.getPageNum()+1);
                pdfPanel.goOtherPage(pdfPanel.getPageNum()+1);
            }
        };

        addActionListener(actionListener);

    }


}


