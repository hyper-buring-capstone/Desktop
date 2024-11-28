package service;

import java.util.Date;

public class IOService {

    public static void log(String msg) {
        System.out.println("[" + (new Date()) + "] " + msg);
    }
	
}
