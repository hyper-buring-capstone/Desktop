package global;


/**
 * 모바일에서 보낸 데이터 파싱하는 클래스.
 */
public class BtParser {


    /**
     * 메시지의 타입이 헤더/점/end인지 구분.
     */
    public static MsgType getMsgType(String msg){
        // 헤더인 경우
        if(msg.split(":")[0].equals("HEADER")){
            return MsgType.HEADER;
        }

        // end인 경우
        else if(msg.split("\r")[0].equals("END")){
            return MsgType.END;
        }

        //다 아니면 point라고 가정함.
        else{

            return MsgType.POINT;

        }


    }

    public static int getX(String msg){
    	System.out.println("message is " + msg);
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
