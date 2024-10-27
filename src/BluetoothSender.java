import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.Vector;

public class BluetoothSender {

    private static final Object lock = new Object();
    private static Vector<String> serviceURLs = new Vector<>();

    public static void main(String[] args) throws BluetoothStateException, InterruptedException {
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();

        // 페어링된 장치 검색
        RemoteDevice[] pairedDevices = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);

        if (pairedDevices != null && pairedDevices.length > 0) {
            System.out.println("페어링된 장치 목록:");
            for (int i = 0; i < pairedDevices.length; i++) {
                try {
                    System.out.println((i + 1) + ": " + pairedDevices[i].getFriendlyName(false) + " (" + pairedDevices[i].getBluetoothAddress() + ")");
                } catch (IOException e) {
                    System.out.println((i + 1) + ": " + pairedDevices[i].getBluetoothAddress());
                }
            }

            // 사용자로부터 장치 선택 입력 받기
            Scanner scanner = new Scanner(System.in);
            System.out.print("연결할 장치 번호를 선택하세요: ");
            int deviceIndex = scanner.nextInt() - 1;  // 인덱스는 0부터 시작하므로 1을 뺍니다

            if (deviceIndex >= 0 && deviceIndex < pairedDevices.length) {
                // 선택된 장치
                RemoteDevice selectedDevice = pairedDevices[deviceIndex];

                // 서비스 검색 시작
                UUID[] uuidSet = new UUID[1];
                uuidSet[0] = new UUID(0x1101);  // RFCOMM 서비스 UUID
                agent.searchServices(null, uuidSet, selectedDevice, new MyDiscoveryListener());

                // 검색 완료 대기
                synchronized (lock) {
                    lock.wait();
                }

                // 첫 번째 서비스 URL 사용
                if (!serviceURLs.isEmpty()) {
                    String serviceURL = serviceURLs.firstElement();
                    System.out.println("찾은 서비스 URL: " + serviceURL);

                    // 여기서 블루투스 연결 및 메시지 전송 수행
                    sendMessage(serviceURL, "Hello Smartphone!");
                } else {
                    System.out.println("서비스를 찾지 못했습니다.");
                }
            } else {
                System.out.println("유효하지 않은 선택입니다.");
            }

        } else {
            System.out.println("페어링된 장치가 없습니다.");
        }
    }

    private static void sendMessage(String serviceURL, String message) {
        try {
            // 서비스 URL로 연결 생성
            StreamConnection streamConnection = (StreamConnection) Connector.open(serviceURL);

            // 메시지 전송
            OutputStream outputStream = streamConnection.openOutputStream();
            outputStream.write(message.getBytes());

            // 스트림 및 연결 닫기
            outputStream.close();
            streamConnection.close();

            System.out.println("메시지가 성공적으로 전송되었습니다!");
        } catch (IOException e) {
            System.err.println("메시지 전송 실패: " + e.getMessage());
        }
    }

    private static class MyDiscoveryListener implements DiscoveryListener {

        @Override
        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            // 장치 발견 시 아무 작업도 하지 않음
        }

        @Override
        public void inquiryCompleted(int discType) {
            // 장치 탐색 완료
        }

        @Override
        public void serviceSearchCompleted(int transID, int respCode) {
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            for (ServiceRecord record : servRecord) {
                String url = record.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                if (url != null) {
                    serviceURLs.add(url);
                }
            }
        }
    }
}
