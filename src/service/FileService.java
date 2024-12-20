package service;

import home.LoadingFrame;
import lombok.Synchronized;
import model.Note;
import model.PenLine;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static global.Constants.BASE_PATH;
import static global.Constants.DATA_PATH;

/**
 * 파일 저장/불러오기 담당 서비스
 */
public class FileService {

    public static int a=1;
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
//        createDirectory("/drawing");
//        createDirectory("/drawing/data");
        createDirectory(BASE_PATH);
        createDirectory(DATA_PATH);

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
                File outputfile = new File(DATA_PATH+fileName+"\\images\\"+i+".jpg");
                ImageIO.write(image, "jpg", outputfile);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    public static void saveMeta(File file){

        String fileName=file.getName().split("\\.")[0]; //확장자 제거이름.

        String txt = "SOF\n"+fileName+"\n"+ LocalDateTime.now()+"\n"+"EOF"; //메타데이터 저장


        String filePath=DATA_PATH+fileName+"\\meta.txt"; //경로 설정
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
    public static void saveMeta(Note note){

        String fileName=note.getTitle(); //확장자 제거이름.

        String txt = "SOF\n"+fileName+"\n"+ LocalDateTime.now()+"\n"+"EOF"; //메타데이터 저장


        String filePath=DATA_PATH+fileName+"\\meta.txt"; //경로 설정
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

    // 제목으로 노트 객체 반환.
    public static Note getNoteByTitle(String title){

        try {
            BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH+title+"\\meta.txt"));
            List<String> metaData=new ArrayList<>(); //메타데이터 담는 리스트.
            String str;
            while ((str = reader.readLine()) != null) { //한 줄씩 읽는 메소드
                metaData.add(str.split("\n")[0]); //줄바꿈 제거
            }
            reader.close();

            Image image=ImageIO.read(new File(DATA_PATH+title+"\\images\\0.jpg"));
            return new Note(image,title,LocalDateTime.parse(metaData.get(2))); //노트 생성
        }catch(IOException e){
            e.printStackTrace(); //추후 예외처리 할 것. 파일이 삭제되거나 하는 문제?
        }
        return null;
    }

    //파일로부터 노트 리스트를 반환해줌. 
    //모든 노트의 이름과 메타데이터
    public static List<Note> loadNoteList(){
        List<Note> noteList=new ArrayList<>();


        File[] fileList=new File(DATA_PATH).listFiles();

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

                String title=metaData.get(1);
                Image image=ImageIO.read(new File(DATA_PATH+title+"\\images\\0.jpg"));
                noteList.add(new Note(image,title,LocalDateTime.parse(metaData.get(2)))); //노트 생성
            }catch(IOException e){
                e.printStackTrace(); //추후 예외처리 할 것. 파일이 삭제되거나 하는 문제?
            }

        }


        return noteList;
    }

//    public static String getLineString(Note note) {
//        File file=new File("c:\\drawing\\data\\"+note.getTitle()+"\\lines.txt"); //선 정보가 저장된 파일 불러옴.
//        String lineString = "";
//        try {
//            lineString = new String(Files.readAllBytes(file.toPath())); // 파일의 모든 내용을 읽어서 String으로 변환
//        } catch (IOException e) {
//            System.err.println("파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage());
//        }
//        return lineString;
//    }
    public static String getSpecificBlock(String noteTitle, int targetPageNumber, int imageWidth, int imageHeight) {
        File file = new File(DATA_PATH + noteTitle + "\\lines.txt");
        String result = "";

        try {
            List<String> lines = Files.readAllLines(file.toPath()); // 파일을 줄 단위로 읽음
            boolean isHeader = false;
            boolean isLine = false;

            for (String line : lines) {
                if (line.startsWith("HEADER")) {
                	isHeader = true;
                	continue;
                }
                if (isHeader) {
                    // HEADER가 시작되면 세 번째 숫자를 파싱
                    String[] headerParts = line.split(" ");
                    if (headerParts[2].equals(Integer.toString(targetPageNumber))) {
                    	result += "{";
                    	result += "\"fontsize\" : " + "\"" + headerParts[0]+ "\"" + ",";
                    	result += "\"color\" : " + "\"" + headerParts[1] + "\"" + ",";
                    	result += "\"data\" : " + "[";
                        isLine = true;
                        isHeader = false;
                        continue;
                    }
                    else {
                    	isHeader = false;
                    }
                }
                if (isLine) {
                	if(!line.equals("END")) {
                    	result += "[" + Integer.toString((Integer.parseInt(line.split(" ")[0])*10000)/imageWidth) + "," + Integer.toString((Integer.parseInt(line.split(" ")[1])*10000)/imageHeight) + "]";	
                	}
                	else {
                		result = result.substring(0, result.length()-2);
                		result += "]}&";
                		isLine = false;
                		continue;
                	}
                	result += ", ";
                }
            }
        } catch (IOException e) {
            System.err.println("파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage());
        }
        if(result.equals("")) {
        	return "";
        }
        else {
            return result.substring(0, result.length()-1);
        }
    }
    
    //해당 파일에 대한 드로잉 정보를 penlinelists로 반환
    public static List<List<PenLine>> loadPenLineLists(Note note){

        // HEADER
        // 5 (두께) FFFFFFAA 3 (페이지) -> 정보를 한 줄로 붙여야 파싱하기 편할 듯?
        //  (new Color((int) HexFormat.fromHexDigitsToLong("ffffffaa"),true);)
        // 10 10 .. (points)
        // END

        File file=new File(DATA_PATH+note.getTitle()+"\\lines.txt"); //선 정보가 저장된 파일 불러옴.
        int totalPageNum= Objects.requireNonNull(new File(DATA_PATH + note.getTitle() + "\\images").listFiles()).length; //이미지 개수(=노트 페이지수)
        List<List<PenLine>> penLineLists= new ArrayList<List<PenLine>>(totalPageNum);

        for(int i=0; i<totalPageNum; i++){
            penLineLists.add(new ArrayList<>());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH+note.getTitle()+"\\lines.txt"));

            String str;
            PenLine penLine = null;
            int pageNum=0;
            while ((str = reader.readLine()) != null) { //한 줄씩 읽어 str에 저장.
                if(str.equals("HEADER")){ //선의 시작이므로 새 선 객체를 생성함.
                    String info= reader.readLine(); //다음 줄을 읽어 정보를 불러옴.
                    int width = 0;
    				switch(info.split(" ")[0]) {
						case "ss" -> {
							width = 3;
						}
						case "s" -> {
							width = 5;
						}
						case "m" -> {
							width = 10;
						}
						case "l" -> {
							width = 20;
						}
						case "ll" -> {
							width = 30;
						}
    				}
                    Color color=new Color((int) HexFormat.fromHexDigitsToLong(info.split(" ")[1]),true); //색상
                    pageNum=Integer.parseInt(info.split(" ")[2]); //페이지
                    penLine=new PenLine().builder()
                            .penWidth(width)
                            .penColor(color)
                            .xList(new ArrayList<>())
                            .yList(new ArrayList<>())
                            .build();


                }
                else if(str.equals("END")){ //선의 끝이므로 선을 저장.
                    //if(penLineLists.get(pageNum).isEmpty()) penLineLists.get(pageNum)=new ArrayList<>();
                    penLineLists.get(pageNum).add(penLine);
                    //penline 생성하고 점 다 했는데 이 부분에서 오류발생.
                }
                else{ //점 데이터이므로 점을 추가
                    penLine.addPoint(Integer.parseInt(str.split(" ")[0]),Integer.parseInt(str.split(" ")[1]));
                }


            }
            reader.close();



        }
        catch (IOException e){
            e.printStackTrace();
            return null; //파일 발견되지 않을 시 예외처리.
        }

        return penLineLists;


    }

    //드로잉 정보를 .txt파일로 저장.
    public static void saveLines(Note note,List<List<PenLine>> penLineLists){

        String fileName=note.getTitle(); //파일명
        String filePath=DATA_PATH+fileName+"\\lines.txt"; //경로 설정


        try {
            new File(filePath).createNewFile();
        }catch (IOException e){
            e.printStackTrace();
            createDirectory(DATA_PATH+fileName);
        }

        // HEADER
        // 5 (두께) FFFFFFAA 3 (페이지) -> 정보를 한 줄로 붙여야 파싱하기 편할 듯?
        //  (new Color((int) HexFormat.fromHexDigitsToLong("ffffffaa"),true);)
        // 10 10 .. (points)
        // END

        try{

            // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
            BufferedWriter fw = new BufferedWriter(new FileWriter(filePath, false));

            for(int page=0;page<penLineLists.size(); page++ ){

                //각 라인 객체에 대해 저장 작업 수행.
                for(PenLine penLine:penLineLists.get(page)){
                    fw.write(penLine.toData(page));
                    fw.flush();
                }
            }

            // 객체 닫기
            fw.close();


        }catch(Exception e){
            e.printStackTrace();
        }


    }

    // 노트 제목을 인자로 받아 해당 노트의 모든 이미지를 반환.
    // 새 쓰레드에서 작동.
    // 이미지 read만 하므로 굳이 lock필요 없을 듯?
    public static List<Image> getImagesByTitle(String title){
        List<Image> imageList=new ArrayList<>();


        File[] files=new File(DATA_PATH+title+"\\images").listFiles();
        // 숫자대로 정렬되도록 변경.
        Comparator<File> comparator=new Comparator<File>() {
            @Override
            public int compare(File f1, File f2)
            {
                return Integer.parseInt(f1.getName().split("\\.")[0])-Integer.parseInt(f2.getName().split("\\.")[0]);
            }
        };

        Arrays.sort(files, comparator);


        try{
            for(File file:files){

                imageList.add(ImageIO.read(file));
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }


        return imageList;
    }

    public static List<Image> getImagesByTitle(String title, LoadingFrame loadingFrame){
        List<Image> imageList=new ArrayList<>();


        File[] files=new File(DATA_PATH+title+"\\images").listFiles();
        // 숫자대로 정렬되도록 변경.
        Comparator<File> comparator=new Comparator<File>() {
            @Override
            public int compare(File f1, File f2)
            {
                return Integer.parseInt(f1.getName().split("\\.")[0])-Integer.parseInt(f2.getName().split("\\.")[0]);
            }
        };

        Arrays.sort(files, comparator);


        try{
            int size= files.length;
            for(int i=0;i< size; i++){
                loadingFrame.setLoadingValue(i,size);
                imageList.add(ImageIO.read(files[i]));

            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }


        return imageList;
    }


}
