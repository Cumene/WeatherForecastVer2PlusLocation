package com.example.admin.weatherforecastver2;

import android.content.Context;
import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WeatherApi {

    private static final String USER_AGENT = "WeatherForecasts Sample"; //ユーザーーエージェント
    private static final String URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city="; //URL、これにMainActivityの数字をkっつける

    public static WeatherForecast getWeather(Context context, String pointId) throws IOException, JSONException {

        AndroidHttpClient client = AndroidHttpClient.newInstance(USER_AGENT,context);
        HttpGet get = new HttpGet(URL + pointId);  //HTTP通信を行う(最新版では無効なメソッド)
        StringBuilder sb = new StringBuilder(); //ただのStringBuilder

        try {
            HttpResponse response = client.execute(get); //HTTP情報をカプセル化する(最新版では無効なメソッド)
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));//出力をバッファリングする
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            client.close();
        }

        return new WeatherForecast(new JSONObject(sb.toString()));//結果を返す
    }

}
