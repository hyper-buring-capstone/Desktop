package drawing.button;

import drawing.PageMoveTextField;
import drawing.PdfPanel;
import global.BaseButton;
import drawing.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends BaseButton {

    public NextPageBtn(PdfPanel pdfPanel, DrawPanel drawPanel, PageMoveTextField pageMoveTextField){

        // 원본 이미지 아이콘 로드
        ImageIcon icon = new ImageIcon("src/icon/next.png");
        // 새 색상 지정
        Color newColor = Color.black;

        Image image=icon.getImage().getScaledInstance(25,25,Image.SCALE_AREA_AVERAGING);

        // 색상 변환된 이미지 생성
       // Image coloredIconImage = recolorIcon(icon.getImage(), newColor, 25, 25);
        setIcon(new ImageIcon(image));

        setBorderPainted(false);

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int curPage=pdfPanel.getPageNum();
                drawPanel.setPageNum(drawPanel.getPageNum()+1);
                pdfPanel.goOtherPage(curPage+1);
                pageMoveTextField.setText(String.valueOf(Math.min(pdfPanel.getImageListSize(),curPage+2)));
                getParent().getParent().repaint(); //8번 트러블 문제랑 비슷하게 해결.
            }
        };

        addActionListener(actionListener);

    }


}


