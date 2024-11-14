package drawing.button;

import drawing.PdfPanel;
import global.BaseButton;
import drawing.DrawPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends BaseButton {

    public NextPageBtn(PdfPanel pdfPanel, DrawPanel drawPanel){
        setText("다음 페이지");

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


