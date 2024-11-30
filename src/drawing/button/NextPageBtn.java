package drawing.button;

import StateModel.StateModel;
import drawing.PageMoveTextField;
import drawing.PdfPanel;
import global.BaseButton;
import service.Receiver;
import drawing.DrawPanel;
import drawing.PageMoveTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextPageBtn extends BaseButton {
	StateModel state;
	
    public NextPageBtn(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel, PageMoveTextField pageMoveTextField){
    	this.state = state;
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
                pdfPanel.goOtherPage(pdfPanel.getPageNum()+1);
                state.setCurPageNum(state.getCurPageNum()+1);
                pageMoveTextField.setText(String.valueOf(Math.min(pdfPanel.getImageListSize(), curPage+2)));
                state.getReceiver().Sender("HEADER:PAGE&&" + (state.getCurPageNum()+1));

                getParent().getParent().repaint(); //8번 트러블 문제랑 비슷하게 해결.

            }
        };

        addActionListener(actionListener);

    }


}


