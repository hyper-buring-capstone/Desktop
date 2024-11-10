package drawing.button;

import drawing.PdfPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrevPageBtn extends JButton {
    public PrevPageBtn(PdfPanel pdfPanel){
        super("이전 페이지");
        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdfPanel.goOtherPage(pdfPanel.getPageNum()-1);
            }
        };

        addActionListener(actionListener);
    }
}
