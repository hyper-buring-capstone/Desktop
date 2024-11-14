package drawing.button;

import drawing.PdfPanel;
import global.BaseButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends BaseButton {

    public NextPageBtn(PdfPanel pdfPanel){
        setText("다음 페이지");

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdfPanel.goOtherPage(pdfPanel.getPageNum()+1);
            }
        };

        addActionListener(actionListener);

    }


}


