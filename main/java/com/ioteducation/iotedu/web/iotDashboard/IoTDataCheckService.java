package com.ioteducation.iotedu.web.iotDashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IoTDataCheckService {
    @Autowired
    private IoTDataCheckDAO ioTDataCheckDAO;

    public int insertGatheringData(Map<String, Object> vo) {
        int rtnVal = ioTDataCheckDAO.insertGatheringData(vo);

        return rtnVal;
    }

    public Map<String, Object> getLastData(String dvcId) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("dvc_id", dvcId);

        return ioTDataCheckDAO.getLastData(vo);
    }

    public int requestArduino(String ip, String port, String param) {
        int rtnVal = 0;
        String targetURL = "http://"+ip+":"+port+"/LED=" + param;
        try {
            URL url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET")
            ;
            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            // 응답 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 응답 출력
            System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rtnVal;
    }
}
