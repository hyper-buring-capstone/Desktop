package drawing.button;

import drawing.PdfPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends JButton {

    public NextPageBtn(PdfPanel pdfPanel){
        super("다음 페이지");

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdfPanel.goOtherPage(pdfPanel.getPageNum()+1);
            }
        };

        addActionListener(actionListener);

    }


}


