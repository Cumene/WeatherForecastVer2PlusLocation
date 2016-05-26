package com.example.admin.weatherforecastver2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {

    public final Location location;//地理的な場所を扱うため
    public final List<Forecast> forecastList = new ArrayList<Forecast>(); //複数日の予報を入れる

    public WeatherForecast(JSONObject jsonObject) throws JSONException {

        JSONObject locationObject = jsonObject.getJSONObject("location"); //取得したJSON形式のデータから"location"で定義されたオブジェクトを取り出す
        //なぜ"location"で場所が取得できるかはお天気Webサービスのページを参考 http://weather.livedoor.com/weather_hacks/webservice
        location = new Location(locationObject);
        JSONArray forecastArray = jsonObject.getJSONArray("forecasts"); //上に同じく

        int len = forecastArray.length(); //何日分の予報を取得したか(ふつうは2~3日分)
        for (int i = 0; i < len; i++) {
            JSONObject forecastJson = forecastArray.getJSONObject(i);
            Forecast forecast = new Forecast(forecastJson); //更に細かく取り出す
            forecastList.add(forecast);
        }
    }

    public class Location {
        public final String area; //地域
        public final String prefecture; //剣
        public final String city; //市名

        public Location(JSONObject jsonObject) throws JSONException {
            area = jsonObject.getString("area"); //上と同じく取得していく 地方名
            prefecture = jsonObject.getString("prefecture");//県名
            city = jsonObject.getString("city"); //一次細区分名
        }
    }

    public class Forecast {
        public final String date;
        public final String dateLabel;
        public final String telop;
        public final Image image;
        public final Temperature temperature;

        public Forecast(JSONObject jsonObject) throws JSONException {

            date = jsonObject.getString("date"); //予報日
            dateLabel = jsonObject.getString("dateLabel"); //予報日
            telop = jsonObject.getString("telop"); //天気
            image = new Image(jsonObject.getJSONObject("image"));
            temperature = new Temperature(jsonObject.getJSONObject("temperature"));
        }

        public class Image {
            public final String title;
            public final String link;
            public final String url;
            public final int width;
            public final int height;

            public Image(JSONObject jsonObject) throws JSONException {
                title = jsonObject.getString("title");
                if (jsonObject.has("link")) {
                    link = jsonObject.getString("link");
                } else {
                    link = null;
                }
                url = jsonObject.getString("url");
                width = jsonObject.getInt("width");
                height = jsonObject.getInt("height");
            }
        }

        public class Temperature {
            public final Temp min;
            public final Temp max;

            public Temperature(JSONObject jsonObject) throws JSONException {
                if (!jsonObject.isNull("min")) {
                    min = new Temp(jsonObject.getJSONObject("min"));
                } else {
                    min = new Temp(null);
                }
                if (!jsonObject.isNull("max")) {
                    max = new Temp(jsonObject.getJSONObject("max"));
                } else {
                    max = new Temp(null);
                }
            }

            public class Temp {
                public final String celsius;
                public final String fahrenheit;

                public Temp(JSONObject jsonObject) throws JSONException {
                    if (jsonObject == null) {
                        celsius = null;
                        fahrenheit = null;
                        return;
                    }
                    celsius = jsonObject.getString("celsius");
                    fahrenheit = jsonObject.getString("fahrenheit");
                }

            }
        }
    }

}

