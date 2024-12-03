package global;

import java.awt.*;

public enum Constants {

        EMPTY("");

        //프로그램 명
        public static final String APP_NAME="Phonote";

    // 사이즈 관련
        public static final Dimension FRAME_SIZE=new Dimension(1000,800);

        //색상 관련
        public static final Color FILE_OPEN_BUTTON_COLOR=new Color(17, 112, 173);

        //이미지 경로 관련
        public static final String NEXT_ICON_PATH="src/icon/next.png";
        public static final String PREV_ICON_PATH="src/icon/prev.png";
        public static final String SAVE_ICON_PATH="src/icon/save.png";
        public static final String APP_ICON_PATH="src/icon/pen.png";

        //파일 저장 경로 관련
        public static final String BASE_PATH="c:\\Phonote\\";
        public static final String DATA_PATH=BASE_PATH+"data\\";











    Constants(String s) {
    }
}
