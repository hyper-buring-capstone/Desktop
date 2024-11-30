package drawing.button;

import drawing.DrawPanel;
import drawing.PageMoveTextField;
import drawing.PdfPanel;
import global.BaseButton;
import service.Receiver;

import javax.swing.*;

import StateModel.StateModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static service.ImageService.recolorIcon;


public class PrevPageBtn extends BaseButton {
	StateModel state;
    public PrevPageBtn(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel, PageMoveTextField pageMoveTextField){
    	this.state = state;

        // 원본 이미지 아이콘 로드
        ImageIcon icon = new ImageIcon("src/icon/prev.png");
        // 새 색상 지정
        Color newColor = Color.black;

        Image image=icon.getImage().getScaledInstance(25,25,Image.SCALE_AREA_AVERAGING);

        // 색상 변환된 이미지 생성
       // Image coloredIconImage = recolorIcon(icon.getImage(), newColor, 25, 25);
        setIcon(new ImageIcon(image));

        // 버튼 스타일
        setBorderPainted(false);

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int curPage=pdfPanel.getPageNum();
                drawPanel.setPageNum(drawPanel.getPageNum()-1);
                pdfPanel.goOtherPage(pdfPanel.getPageNum()-1);
                state.setCurPageNum(state.getCurPageNum()-1);
                pageMoveTextField.setText(String.valueOf(Math.max(1, curPage)));
                state.getReceiver().Sender("HEADER:PAGE&&" + (state.getCurPageNum()+1));

                getParent().getParent().repaint();
            }
        };

        addActionListener(actionListener);
    }
}
