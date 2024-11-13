package home.button;

import home.NoteListPanel;
import home.NotePanel;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import service.FileService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileOpenBtn extends JButton {

    NoteListPanel noteListPanel;
    public FileOpenBtn(NoteListPanel noteListPanel){
        super("File Open");
        this.noteListPanel=noteListPanel;
        addActionListener(actionListener);

    }




    ActionListener actionListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //open file;
            
            //pdf 파일을 선택하고
            // 추후에 javaFx로 변경해보기.
            // 아무것도 선택 안하면 nullPointException 처리?
            JFileChooser jFileChooser=new JFileChooser();

            int ret=jFileChooser.showOpenDialog(null); //parent는 뭐임?

            if(ret!=JFileChooser.APPROVE_OPTION){
                JOptionPane.showMessageDialog(null
                        , "경로를 선택하지 않았습니다."
                        , "경고"
                        , JOptionPane.WARNING_MESSAGE);
                return;
            }

            File file=jFileChooser.getSelectedFile(); //선택한 파일 가져옴
            String filePath=file.getPath();
            //System.out.println(filePath);

            //디렉토리 구조를 내부적으로 생성

            //디렉토리 생성
            String fileName=file.getName().split("\\.")[0]; //확장자 제거한 이름.
            FileService.createDirectory("/drawing/data/"+ fileName);
            FileService.createDirectory("/drawing/data/"+ fileName+"/images");

            
            //images 폴더에 변환된 pdf이미지를 저장
            FileService.saveImages(file);


            //메타데이터 저장
            FileService.saveMeta(file);


            //패널 리스트를 재호출해서 새로고침
            noteListPanel.refresh();


        }
    };

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);

    }
}
