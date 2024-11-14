package service;

import model.Note;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 저장/불러오기 담당 서비스
 */
public class FileService {

    //해당 경로의 디렉토리를 생성.
    public static void createDirectory(String dirPath){
        Path path= Paths.get(dirPath);

        try {
            Files.createDirectory(path); //전체 구조 생성.
            System.out.println(path + " 디렉토리가 생성되었습니다.");
        } catch (FileAlreadyExistsException ex) {
            System.out.println("디렉토리가 이미 존재합니다");
        } catch (NoSuchFileException ex) {
            System.out.println("디렉토리 경로가 존재하지 않습니다");
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    //파일 저장소 구조를 초기화함
    public static void initDirectory(){
        createDirectory("/drawing");
        createDirectory("/drawing/data");
        
    }

    public static void saveImages(File file){
        String fileName=file.getName().split("\\.")[0]; //확장자 제거이름.

       // List<Image> imageList=new ArrayList<>(); //pdf를 변환한 이미지리스트.
        try {
            PDDocument pdDocument= Loader.loadPDF(file);
            PDFRenderer pdfRenderer=new PDFRenderer(pdDocument);
            for(int i=0; i< pdDocument.getNumberOfPages(); i++){
                BufferedImage image= pdfRenderer.renderImageWithDPI(i, 300);
//              imageList.add(image); //리스트에 추가

                //파일 디렉토리 구조에 추가.
                File outputfile = new File("c:\\drawing\\data\\"+fileName+"\\images\\"+i+".jpg");
                ImageIO.write(image, "jpg", outputfile);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    public void saveLines(){


    }

    public static void saveMeta(File file){

        String fileName=file.getName().split("\\.")[0]; //확장자 제거이름.

        String txt = "SOF\n"+fileName+"\n"+ LocalDateTime.now()+"\n"+"EOF"; //메타데이터 저장


        String filePath="c:\\drawing\\data\\"+fileName+"\\meta.txt"; //경로 설정
        try{

            // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
            BufferedWriter fw = new BufferedWriter(new FileWriter(filePath, false));

            // 파일안에 문자열 쓰기
            fw.write(txt);
            fw.flush();

            // 객체 닫기
            fw.close();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //파일로부터 노트 리스트를 반환해줌. 
    //모든 노트의 이름과 메타데이터
    public static List<Note> loadNoteList(){
        List<Note> noteList=new ArrayList<>();


        File[] fileList=new File("c:\\drawing\\data").listFiles();

        for(File file:fileList){
            String fileName=file.getName().split("\\.")[0]; //확장자 제거이름.

            System.out.println(file.getPath());
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getPath()+"\\meta.txt"));
                List<String> metaData=new ArrayList<>(); //메타데이터 담는 리스트.
                String str;
                while ((str = reader.readLine()) != null) { //한 줄씩 읽는 메소드
                    metaData.add(str.split("\n")[0]); //줄바꿈 제거
                }
                reader.close();

                noteList.add(new Note(metaData.get(1),  LocalDateTime.parse(metaData.get(2))));
            }catch(IOException e){
                e.printStackTrace(); //추후 예외처리 할 것. 파일이 삭제되거나 하는 문제?
            }

        }


        return noteList;
    }


}