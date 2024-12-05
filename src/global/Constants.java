package global;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public enum Constants {

    EMPTY("");

        //프로그램 명
    public static final String APP_NAME="Phonote";

    // 사이즈 관련
    public static final Dimension FRAME_SIZE=new Dimension(1000,800);

        //색상 관련
//    public static final Color FILE_OPEN_BUTTON_COLOR=new Color(17, 112, 173);
    public static final Color COLOR_ORANGE=new Color(240, 120, 42);
    public static final Color FILE_OPEN_BUTTON_COLOR=new Color(240, 120, 42);

        //이미지 경로 관련
        public static final String NEXT_ICON_PATH="src/icon/next.png";
        public static final String PREV_ICON_PATH="src/icon/prev.png";
        public static final String SAVE_ICON_PATH="src/icon/save.png";
        public static final String APP_ICON_PATH="src/icon/fire_pen.png";

        //파일 저장 경로 관련
    public static final String BASE_PATH="c:\\Phonote\\";
    public static final String DATA_PATH=BASE_PATH+"data\\";


        //폰트 경로
    public static final String FONT_REGULAR_PATH = "src/assets/fonts/SpoqaHanSansNeo_TTF_original/SpoqaHanSansNeo-Regular.ttf";
    public static final String FONT_BOLD_PATH = "src/assets/fonts/SpoqaHanSansNeo_TTF_original/SpoqaHanSansNeo-Bold.ttf";
    public static final String FONT_LIGHT_PATH = "src/assets/fonts/SpoqaHanSansNeo_TTF_original/SpoqaHanSansNeo-Light.ttf";
    public static final Font FONT_REGULAR;
    public static final Font FONT_BOLD;
    public static final Font FONT_LIGHT;

    static {
         try {
                FONT_REGULAR = Font.createFont(Font.TRUETYPE_FONT,
                        new File(FONT_REGULAR_PATH));
         } catch (FontFormatException e) {
              throw new RuntimeException(e);
         } catch (IOException e) {
               throw new RuntimeException(e);
         }
      }

    static {
        try {
            FONT_BOLD = Font.createFont(Font.TRUETYPE_FONT,
                    new File(FONT_BOLD_PATH));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            FONT_LIGHT = Font.createFont(Font.TRUETYPE_FONT,
                    new File(FONT_LIGHT_PATH));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Constants(String s) {
    }
}
