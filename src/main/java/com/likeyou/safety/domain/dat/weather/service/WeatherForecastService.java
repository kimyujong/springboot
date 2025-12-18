package com.likeyou.safety.domain.dat.weather.service;

import com.likeyou.safety.domain.dat.weather.dto.WeatherForecastDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherForecastService {

    @Value("${forecast.url}")
    private String forecastUrl;

    @Value("${forecast.service-key}")
    private String serviceKey;

    @Value("${forecast.nx}")
    private int nx;

    @Value("${forecast.ny}")
    private int ny;

    // base_time 자동 계산
    private String getBaseTime() {
        LocalTime now = LocalTime.now();

        int hour = now.getHour();
        int minute = now.getMinute();

        // 발표는 매 정시 이후 30분에 조회 가능
        if (minute < 30) {
            hour -= 1;
        }
        if (hour < 0) hour = 23;

        return String.format("%02d00", hour);
    }

    public List<WeatherForecastDto> getShortForecast() {
        try {
            String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = getBaseTime();

            // Service Key를 URL 인코딩 (UTF-8)
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8.toString());

            StringBuilder urlBuilder = new StringBuilder(forecastUrl);
            urlBuilder.append("?serviceKey=").append(encodedServiceKey);
            urlBuilder.append("&pageNo=1");
            urlBuilder.append("&numOfRows=1000");
            urlBuilder.append("&dataType=XML");
            urlBuilder.append("&base_date=").append(baseDate);
            urlBuilder.append("&base_time=").append(baseTime);
            urlBuilder.append("&nx=").append(nx);
            urlBuilder.append("&ny=").append(ny);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(conn.getInputStream());

            XPath xPath = XPathFactory.newInstance().newXPath();

            // 모든 item 노드에서 데이터 추출
            NodeList items = (NodeList) xPath.evaluate("//item", document, XPathConstants.NODESET);
            
            // 시간별로 그룹화하기 위한 Map
            Map<String, ForecastData> timeMap = new HashMap<>();

            for (int i = 0; i < items.getLength(); i++) {
                String category = xPath.evaluate("category", items.item(i));
                String fcstTime = xPath.evaluate("fcstTime", items.item(i));
                String fcstValue = xPath.evaluate("fcstValue", items.item(i));

                // SKY와 T1H만 필터링
                if ("SKY".equals(category) || "T1H".equals(category)) {
                    timeMap.putIfAbsent(fcstTime, new ForecastData());
                    ForecastData data = timeMap.get(fcstTime);
                    
                    if ("SKY".equals(category)) {
                        data.setSky(fcstValue);
                    } else if ("T1H".equals(category)) {
                        data.setTemp(fcstValue);
                    }
                }
            }

            // 시간순으로 정렬하고 결과 리스트 생성
            List<WeatherForecastDto> results = new ArrayList<>();
            timeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(5) // 처음 5개만
                .forEach(entry -> {
                    String time = entry.getKey();
                    ForecastData data = entry.getValue();
                    
                    // 시간 포맷팅 (1400 -> 14:00)
                    String formattedTime = time.substring(0, 2) + ":" + time.substring(2);
                    
                    // 하늘상태 변환
                    String sky = convertSky(data.getSky());
                    
                    // 기온을 String에서 int로 변환
                    int temp = data.getTemp() != null && !data.getTemp().isEmpty() 
                        ? Integer.parseInt(data.getTemp()) 
                        : 0;
                    
                    // WeatherForecastDto 생성
                    results.add(WeatherForecastDto.builder()
                        .time(formattedTime)
                        .sky(sky)
                        .temp(temp)
                        .build());
                });

            return results;

        } catch (Exception e) {
            throw new RuntimeException("기상청 초단기예보 XML 파싱 실패: " + e.getMessage(), e);
        }
    }

    private String convertSky(String skyValue) {
        if (skyValue == null) return "";
        switch (skyValue) {
            case "1": return "맑음";
            case "3": return "구름많음";
            case "4": return "흐림";
            default: return skyValue;
        }
    }

    // 내부 클래스: 시간별 데이터 임시 저장
    static class ForecastData {
        private String sky;
        private String temp;

        public String getSky() {
            return sky;
        }

        public void setSky(String sky) {
            this.sky = sky;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }
    }
}