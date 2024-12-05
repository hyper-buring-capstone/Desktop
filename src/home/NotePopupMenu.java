package home;

import model.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static global.Constants.DATA_PATH;

public class NotePopupMenu extends JPopupMenu {

    Note note;
    NotePanel notePanel;
    public NotePopupMenu(Note note, NotePanel notePanel){
        this.note=note;
        this.notePanel=notePanel;


        // 메뉴 아이템 추가
        JMenuItem item1 = new JMenuItem("상세 정보");
        JMenuItem item2 = new JMenuItem("이름 변경");
        JMenuItem item3 = new JMenuItem("삭제");
        item3.setForeground(Color.red);

        // 각 메뉴 아이템에 액션 추가
        item3.addActionListener(actionListener);

        // 메뉴에 아이템 추가
//        add(item1);
//        add(item2);
        add(item3);


    }

    //삭제 버튼 눌렀을 때
    ActionListener actionListener=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {

            int choice = JOptionPane.showConfirmDialog(
                    getParent(),
                    "삭제하시겠습니까?",
                    "경고",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // YES 선택 시 삭제 로직 실행
            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("삭제 동작 실행");
                // TODO: 삭제 로직 구현

                String folderPath = DATA_PATH+note.getTitle(); // 삭제하려는 파일 경로

                try {
                    Path pathToDelete = Paths.get(folderPath);
                    // 폴더 내부의 파일 및 디렉토리 삭제
                    Files.walk(pathToDelete)
                            .sorted((a, b) -> b.compareTo(a)) // 하위 경로부터 삭제 (정렬)
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                    System.out.println("삭제됨: " + path);

                                    //삭제 끝난 후 새로고침
                                    notePanel.listRefresh();
                                } catch (IOException e) {
                                    System.err.println("삭제 실패: " + path + " - " + e.getMessage());
                                }
                            });
                } catch (IOException e) {
                    System.err.println("폴더를 삭제하는 중 오류 발생: " + e.getMessage());
                }
            } else {
                System.out.println("삭제 취소됨");
            }



        }
    };
}
