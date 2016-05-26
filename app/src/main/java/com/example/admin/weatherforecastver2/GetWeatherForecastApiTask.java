package com.example.admin.weatherforecastver2;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

public class GetWeatherForecastApiTask extends AsyncTask<String, Void, WeatherForecast> { //WeatherForecastとUI非同期処理を行う

    private final Context context; //画面のインターフェースのようなもの
    Exception exception; //例外処理を出す

    public GetWeatherForecastApiTask(Context context) { //コンストラクタ
        this.context = context;
    }

    @Override
    protected WeatherForecast doInBackground(String... params) { //非同期処理したい内容を書く、このクラスは必ず作らなくてはならない
        try {
            return WeatherApi.getWeather(context, params[0]);//取得したWebAPIの情報を渡す
        } catch (IOException e) { //読み込みエラー
            exception = e;
        } catch (JSONException e) { //JSONに関するエラー
            exception = e;
        }
        return null; //適当に
    }
}
