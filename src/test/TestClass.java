package test;

import global.BtParser;
import global.Constants;
import home.LoadingFrame;
import org.junit.Test;

import static global.Constants.BASE_PATH;
import static global.Constants.DATA_PATH;
import static service.FileService.createDirectory;

public class TestClass {

    @Test
    public void BtParserTest(){
        System.out.println(BtParser.getMsgType("HEADER:DRAWING"));
        System.out.println(BtParser.getMsgType("HEADER:PANNING"));
        System.out.println(BtParser.getMsgType("END"));
    }

    @Test
    public void 전역변수테스트(){
        System.out.println(Constants.DATA_PATH.toString().equals("c:\\drawing\\data\\"));
    }

    @Test
    public void 경로생성테스트(){

        //경로 끝에 "\\" 있어도 생성 됨.
        createDirectory(BASE_PATH);
        createDirectory(DATA_PATH);
    }

    @Test
    public void 로딩창띄우기(){
        LoadingFrame loadingFrame=new LoadingFrame();
        loadingFrame.setVisible(true);
        while(true){
            ;
        }
    }
}