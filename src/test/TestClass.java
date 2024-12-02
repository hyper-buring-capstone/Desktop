package test;

import global.BtParser;
import org.junit.Test;

public class TestClass {

    @Test
    public void BtParserTest(){
        System.out.println(BtParser.getMsgType("HEADER:DRAWING"));
        System.out.println(BtParser.getMsgType("HEADER:PANNING"));
        System.out.println(BtParser.getMsgType("END"));
    }
}
