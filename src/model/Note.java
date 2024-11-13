package model;


import lombok.Getter;

import java.awt.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
/**
 * 노트 정보를 가져오는 객체(entity)
 */
public class Note {

    Image thumbNail;
    String title;
    LocalDateTime modified_at;

    public Note(Image thumbNail, String title){
        this.thumbNail=thumbNail;
        this.title=title;
        modified_at= LocalDateTime.now(); //현재 시간으로 자동 설정.
    }

    public Note(String title){
        this.thumbNail=null;
        this.title=title;
        modified_at= LocalDateTime.now(); //현재 시간으로 자동 설정.
    }

    public Note(String title, LocalDateTime localDateTime){
        this.thumbNail=null;
        this.title=title;
        modified_at= localDateTime; //현재 시간으로 자동 설정.
    }
}
