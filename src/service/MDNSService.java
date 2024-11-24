package service;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.Request;  // 추가: Request 클래스 임포트
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MDNSService {

    public static void main(String[] args) throws Exception {
        // 1. mDNS를 통해 서비스 등록
        InetAddress localHost = InetAddress.getLocalHost(); // 로컬 호스트의 IP를 가져옴
        JmDNS jmdns = JmDNS.create(localHost); // mDNS 인스턴스를 생성

        ServiceInfo serviceInfo = ServiceInfo.create(
            "_http._tcp.local.", // 서비스 타입 (HTTP를 TCP 프로토콜로 로컬 네트워크에 등록)
            "ImageServer",       // 서비스 이름
            8080,                // 서비스 포트 번호
            "path=/image"        // 서비스 설명 (이미지 요청 경로)
        );
        jmdns.registerService(serviceInfo); // mDNS 서비스 등록
        System.out.println("mDNS 서비스가 등록되었습니다: ImageServer.local:8080");

        // 2. HTTP 서버 설정
        Server server = new Server(8080); // 8080번 포트로 HTTP 서버 생성
        server.setHandler(new AbstractHandler() {

            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
                if ("/image".equals(target)) { // 요청 경로가 "/image"일 때만 처리
                    File imageFile = new File("C:\\Users\\PC\\Desktop\\example.jpg"); // 이미지 파일 경로 지정
                    response.setContentType("image/jpeg"); // 응답 타입 설정 (JPEG 이미지)
                    response.setStatus(HttpServletResponse.SC_OK); // 상태 코드 200 반환
                    Files.copy(imageFile.toPath(), response.getOutputStream()); // 이미지 파일을 HTTP 응답으로 전송
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 요청 경로가 틀릴 경우 404 반환
                }
                baseRequest.setHandled(true); // 요청이 처리되었음을 명시
            }

        });

        server.start(); // 서버 시작
        server.join();  // 서버 종료 대기
    }
}
