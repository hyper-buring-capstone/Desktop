package drawing.button;

import drawing.DrawPanel;
import drawing.PdfPanel;
import global.BaseButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PrevPageBtn extends BaseButton {
    public PrevPageBtn(PdfPanel pdfPanel, DrawPanel drawPanel){
        setText("이전 페이지");
        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setPageNum(drawPanel.getPageNum()-1);
                pdfPanel.goOtherPage(pdfPanel.getPageNum()-1);
            }
        };

        addActionListener(actionListener);
    }
}
