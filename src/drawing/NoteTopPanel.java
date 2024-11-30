package drawing;

import drawing.button.NextPageBtn;
import drawing.button.PrevPageBtn;
import drawing.button.SaveBtn;

import javax.swing.*;

import StateModel.StateModel;

import java.awt.*;

public class NoteTopPanel extends JPanel {
    PdfPanel pdfPanel;
    DrawPanel drawPanel;
    StateModel state;

    NoteTopPanel(StateModel state, PdfPanel pdfPanel, DrawPanel drawPanel){
    	this.state = state;
        this.pdfPanel=pdfPanel; //버튼 조작을 위해 객체 받음.
        this.drawPanel=drawPanel; // 이하동문


        //setBackground(new Color(120,1,22));
        setBackground(Color.white);
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createEmptyBorder(10 , 50 , 10 , 50));//내부 패딩


        PageMoveTextField pageMoveTextField = new PageMoveTextField(state, pdfPanel, drawPanel);
        JButton nextPageBtn=new NextPageBtn(state, pdfPanel, drawPanel, pageMoveTextField);
        nextPageBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton prevPageBtn=new PrevPageBtn(state, pdfPanel, drawPanel, pageMoveTextField);

        JButton saveBtn=new SaveBtn(drawPanel);

        add(prevPageBtn); //이전 버튼
        add(nextPageBtn); //다음 버튼
        add(pageMoveTextField); //페이지 무빙
        add(new JLabel("/ "+ pdfPanel.getTotalPageNum() + " "));
        add(saveBtn); // 저장 버튼



        setVisible(true);


    }
}
