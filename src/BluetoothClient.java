import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.InputStream;
import java.io.IOException;

public class BluetoothClient {

    private static final String SERVER_URL = "btspp://346F245E1A56:1"; // 블루투스 장치 주소를 입력하세요

    public static void main(String[] args) {
        try {
            // Bluetooth 서버에 연결
            StreamConnection connection = (StreamConnection) Connector.open(SERVER_URL);
            System.out.println("Connected to server: " + SERVER_URL);

            // 입력 스트림을 통해 데이터 읽기
            InputStream inputStream = connection.openInputStream();
            byte[] buffer = new byte[1024]; // 수신할 데이터 버퍼
            int bytesRead;

            // 수신 상태를 나타내는 변수
            boolean receiving = true;

            while (receiving) {
                // 데이터 읽기
                bytesRead = inputStream.read(buffer);

                if (bytesRead == -1) {
                    // 연결이 끊어지면 종료
//                    System.out.println("Connection closed by the server.");
//                    receiving = false; // 루프 종료
                } else {
                    // 수신된 데이터를 문자열로 변환하여 출력
                    String receivedData = new String(buffer, 0, bytesRead);
                    System.out.println("Received: " + receivedData);
                }
            }

            // 연결 종료
            inputStream.close();
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
