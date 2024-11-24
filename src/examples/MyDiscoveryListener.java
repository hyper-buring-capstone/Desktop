package examples;

import java.io.OutputStream;
import java.util.ArrayList;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;

public class MyDiscoveryListener implements DiscoveryListener{

    private static Object lock=new Object();
    public ArrayList<RemoteDevice> devices;

    public MyDiscoveryListener() {
        devices = new ArrayList<RemoteDevice>();
    }

    public static void main(String[] args) {

        MyDiscoveryListener listener =  new MyDiscoveryListener();

        try{
            // LocalDevice: 현재 로컬 시스템(즉, 이 코드를 실행하는 장치)의 Bluetooth 장치에 대한 정보를 제공
            // String localDeviceName = localDevice.getFriendlyName(); // 장치의 친근한 이름을 가져옴
            // String localDeviceAddress = localDevice.getBluetoothAddress(); // 장치의 Bluetooth 주소를 가져옴
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            // DiscoveryAgent: Bluetooth 장치, 서비스 검색을 수행하는 데 사용되는 클래스. 주변 Bluetooth 장치 검색 가능
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            // 장치 검색 시작
            // GIAC: General Inquiry Access Code. 일반적으로 검색 가능한 장치 목록 요청
            // listener: DiscoveryListener 인터페이스를 구현한 객체로, 장치가 발견될 때 호출되는 메서드를 정의
            agent.startInquiry(DiscoveryAgent.GIAC, listener);

            try {
                synchronized(lock){
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }


            System.out.println("Device Inquiry Completed. ");


            // UUID는 장치나 서비스의 고유한 식별자를 나타냄
            UUID[] uuidSet = new UUID[1];
            // 0x1105: OBEX Object Push service의 UUID
            // OBEX Object Push service: OBject EXchange의 준말. 블루투스를 통해 파일과 같은 데이터를 전송하기 위한 프로토콜
            uuidSet[0]=new UUID(0x1105);

            // attrIDs는 서비스의 속성 ID로, 서비스에 대한 추가 정보를 제공
            // 0x0100: OBEX Object Push 서비스의 이름을 조회하는 데 사용
            int[] attrIDs =  new int[] {
                    0x0100 // Service name
            };

            for (RemoteDevice device : listener.devices) {
                agent.searchServices(
                        attrIDs,uuidSet,device,listener);


                try {
                    synchronized(lock){
                        lock.wait();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }


                System.out.println("Service search finished.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
        // Bluetooth 장치 발견되면 호출
        // 이름 가져와서 출력하고(이름 없으면 주소), devices 배열에 저장
        String name;
        try {
            name = btDevice.getFriendlyName(false);
        } catch (Exception e) {
            name = btDevice.getBluetoothAddress();
        }

        devices.add(btDevice);
        System.out.println("device found: " + name);

    }

    @Override
    public void inquiryCompleted(int arg0) {
        // 장치 탐색 완료 시 호출
        // lock 해제하여 대기 중인 스레드를 깨움
        synchronized(lock){
            lock.notify();
        }
    }

    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
        // 여긴 서비스 검색. 원리는 위와 같음
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        // 발견된 서비스에 대한 정보를 처리하는 메소드
        // transID: 서비스 검색 요청의 트랜잭션 ID. 클라이언트가 서버에 서비스 검색 요청을 보낼 때 생성된 고유한 식별자
        // 그래서 여러 서비스 검색 요청을 처리할 때 이 ID를 통해 어떤 요청에 대한 응답인지를 구별 가능
        // ServiceRecord: 블루투스 장치가 제공하는 서비스에 대한 다양한 정보가 담겨 있음
        for (int i = 0; i < servRecord.length; i++) {
            // 연결 URL 가져옴. 이 URL은 클라이언트가 해당 서비스에 연결하는 데 사용
            String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (url == null) {
                continue;
            }
            DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
            if (serviceName != null) {
                System.out.println("service " + serviceName.getValue() + " found " + url);

                if(((String)serviceName.getValue()).trim().equals("OBEX Object Push")){
                    sendMessageToDevice(url);
                }
            } else {
                System.out.println("service found " + url);
            }


        }
    }

    private static void sendMessageToDevice(String serverURL){
        // 사실 핵심 기능. 연결되면 이 함수를 실행시킨다
        try{
            System.out.println("Connecting to " + serverURL);
            ClientSession clientSession = (ClientSession) Connector.open(serverURL);
            HeaderSet hsConnectReply = clientSession.connect(null);
            if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
                System.out.println("Failed to connect");
                return;
            }

            byte data[] = "Hello World".getBytes("iso-8859-1");

            HeaderSet hsOperation = clientSession.createHeaderSet();
            hsOperation.setHeader(HeaderSet.NAME, "Hello.txt");
            hsOperation.setHeader(HeaderSet.TYPE, "text/plain");
            hsOperation.setHeader(HeaderSet.LENGTH, (long) data.length);

            //Create PUT Operation
            Operation putOperation = clientSession.put(hsOperation);

            // Send some text to server
            OutputStream os = putOperation.openOutputStream();
            os.write(data);
            os.flush();
            os.close();

            putOperation.close();

            clientSession.disconnect(null);

            clientSession.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}