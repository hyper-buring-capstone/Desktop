package drawing;

import drawing.button.NextPageBtn;
import drawing.button.PrevPageBtn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopLayeredPane extends JPanel {
    PdfPanel pdfPanel;
    TopLayeredPane(PdfPanel pdfPanel){
        this.pdfPanel=pdfPanel; //버튼 조작을 위해 객체 받음.

        setSize(1000, 100);
        setBackground(Color.black);
        setLayout(new BorderLayout());


        JButton nextPageBtn=new NextPageBtn(pdfPanel);
        JButton prevPageBtn=new PrevPageBtn(pdfPanel);

        add(prevPageBtn, BorderLayout.WEST,Integer.valueOf(0));
        add(nextPageBtn, BorderLayout.EAST, Integer.valueOf(0));



        setVisible(true);


    }
}
