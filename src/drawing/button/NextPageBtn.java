package drawing.button;

import StateModel.StateModel;
import drawing.PageMoveTextField;
import drawing.PdfPanel;
import global.BaseButton;
import service.FileService;
import drawing.DrawPanel;

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
                int curPage= state.getCurPageNum();
                int totalPage=pdfPanel.getTotalPageNum();
                if(curPage>=totalPage-1){ //마지막 페이지인 경우.
                    return;
                }
                drawPanel.setPageIndex(curPage+1);
                pdfPanel.setPageIndex(curPage+1);
                state.setCurPageNum(curPage+1);
                pageMoveTextField.setText(String.valueOf(curPage+2));
                state.setLineString(FileService.getSpecificBlock(state.getNoteTitle(), state.getCurPageNum(), state.getImageWidth(), state.getImageHeight()));
                if(state.getReceiver() != null) {
                    state.getReceiver().Sender("HEADER:PAGE&&" + (state.getCurPageNum()+1));
                    System.out.println(state.getCurPageNum()+1);
                }

                getParent().getParent().repaint(); //8번 트러블 문제랑 비슷하게 해결.

            }
        };

        addActionListener(actionListener);

    }


}


