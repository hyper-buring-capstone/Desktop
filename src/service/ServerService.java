package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import StateModel.StateModel;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;


public class ServerService {

    public static void startHttpServer() throws Exception {
    	// Jetty 로깅 레벨을 코드에서 설정
    	
        // IP 주소 가져오기
        String hostAddress = getLocalHostAddress();
        if (hostAddress == null) {
            System.err.println("로컬 IP 주소를 가져오지 못했습니다.");
            return;
        }

        // HTTP 서버 설정
        Server server = new Server(8080); // 8080번 포트로 HTTP 서버 생성
        server.setHandler(new AbstractHandler() {

            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
                // "/images/{imageName}" 형식으로 요청 처리
                if (target.startsWith("/images/")) {
                    String imageName = target.substring("/images/".length()); // 이미지 이름 추출. 가상 num
                    File imageFile = StateModel.getFile(Integer.parseInt(imageName)-1);
                    
                    if (imageFile.exists()) {
                        // 이미지 파일 존재하면 반환
                        response.setContentType("image/jpeg"); // 응답 타입 설정 (JPEG 이미지)
                        response.setStatus(HttpServletResponse.SC_OK); // 상태 코드 200 반환
                        Files.copy(imageFile.toPath(), response.getOutputStream()); // 이미지 파일을 HTTP 응답으로 전송
                    } else {
                        // 이미지 파일이 없으면 404 반환
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 파일이 없을 경우 404 반환
                        response.getWriter().write("Image not found: " + imageName);
                    }
                } else if (target.startsWith("/lines/")) {
                    // "/status" API 요청 처리
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(StateModel.getLineString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 요청 경로가 틀릴 경우 404 반환
                    response.getWriter().write("Not Found");
                }
                baseRequest.setHandled(true); // 요청이 처리되었음을 명시
            }

        });

        // 서버 시작
        try {
            server.start();
        }
        catch(Exception e) {
        	IOService.log("서버 연결에 오류가 있습니다. 한 포트에 여러 서버가 있는지 확인 바랍니다.");
        	System.exit(1);
        }
        System.out.printf("HTTP 서버가 시작되었습니다: http://%s:8080/images/{imageName}\n", hostAddress);

        // JVM 종료 시 서버를 멈추기 위한 shutdown hook 추가
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("서버를 종료합니다...");
                server.stop();  // 서버 종료
                System.out.println("서버 종료 완료.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        server.join(); // 서버 종료 대기
    }

    // 로컬 IP 주소 가져오기 메서드
    public static String getLocalHostAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost(); // 로컬 호스트 주소 가져오기
            return localHost.getHostAddress(); // IP 주소 반환
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
