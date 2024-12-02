package global;


/**
 * 모바일에서 보낸 데이터 파싱하는 클래스.
 */
public class BtParser {


    /**
     * 메시지의 타입이 헤더/점/end인지 구분.
     * 끝에 \r 들어가 있음.
     */
    public static MsgType getMsgType(String msg){

        String header=msg.split("&&")[0];
        // 펜의 헤더인 경우
        if(header.equals("HEADER:DRAWING\r")){
            return MsgType.HEADER_PEN;
        }
        // 지우개 헤더
        else if(header.equals("HEADER:ERASER\r")){
            return MsgType.HEADER_ERASER;
        }
        // 드로잉 박스 조작하는 경우.
        else if(header.equals("HEADER:PANNING")){
            return MsgType.HEADER_PANNING;
        }

        // end인 경우
//        else if(msg.split("\r")[0].equals("END")){
        else if(header.equals("END\r")){
            return MsgType.END;
        }


        //다 아니면 point라고 가정함.
        else{

            return MsgType.POINT;

        }


    }

    public static int getX(String msg){
        if( !getMsgType(msg).equals(MsgType.POINT)){ //점 데이터가 아니면 예외처리 해야 함.
            return -1;
        }
        return Math.round(Float.parseFloat(msg.split(" ")[0])); // 이거 계산시간 길면 걍 반올림 말고 점 떼는것도 생각해야함

    }


    public static int getY(String msg){
        if( !getMsgType(msg).equals(MsgType.POINT)){ //점 데이터가 아니면 예외처리 해야 함.
            return -1;
        }
        return Math.round(Float.parseFloat(msg.split(" ")[1]));

    }
}
