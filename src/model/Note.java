package model;


import lombok.Getter;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.oer.its.ieee1609dot2.basetypes.HashAlgorithm;

import java.awt.*;
import java.io.IOException;
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


    public Note(Image thumbNail, String title, LocalDateTime localDateTime) {
        this.thumbNail=thumbNail;
        this.title=title;
        modified_at= localDateTime; //현재 시간으로 자동 설정.

    }


}
