import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class JpgReader {


    public static void main(String args[]){


        // 임시 파일 전송 코드
        String filePath = "C:\\drawing\\data\\hw5\\images\\0.jpg";

        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("파일이 존재하지 않습니다: " + filePath);
            return;
        }


        try (FileInputStream fileInputStream = new FileInputStream(file)) {



            System.out.println("두번째 시도");
            byte[] buffer2 = new byte[fileInputStream.available()];


            System.out.println("available:" + fileInputStream.available()); //224308
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer2)) != -1) {
            }


            System.out.println(Arrays.toString(buffer2)); //224308
            System.out.println("available:" + fileInputStream.available()); //224308


            System.out.println("PDF 파일 변환 완료2: " + filePath);

            
            //저장해보기

            // 저장할 파일 경로
            String outputPath = "output.jpg";

            // byte[] 데이터를 파일로 저장
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(buffer2);
                System.out.println("JPEG 파일이 저장되었습니다: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("파일 저장 중 오류가 발생했습니다.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




        


    }
}
