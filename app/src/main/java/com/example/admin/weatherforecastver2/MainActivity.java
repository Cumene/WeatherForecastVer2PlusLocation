package com.example.admin.weatherforecastver2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

    private TextView textView;
    private double latitude = 0, longitude = 0;
    String urlNumber;

    @Override
    public void onLocationChanged(Location location) {
        // 緯度
        latitude = location.getLatitude();

        // 経度
        longitude = location.getLongitude();
        //ここから緯度・経度→都市

        urlNumber = Place.minDistance(latitude, longitude);

        //ここまで緯度・経度→都市

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    class GetWeatherForecastTask extends GetWeatherForecastApiTask  {

        public GetWeatherForecastTask(Context context) {

            super(context);

        } //コンストラクタGetWeatherForecast

        @Override
        protected void onPostExecute(WeatherForecast data) {
            super.onPostExecute(data); //メインスレッドに反映させたい処理をここに書く
            if (data != null) {
                textView.setText(data.location.area + " " + data.location.prefecture + " " + data.location.city); //地方県都市
                for (WeatherForecast.Forecast forecast : data.forecastList) {  // 予報を一覧表示(2日分)
                    textView.append("\n");
                    textView.append(forecast.dateLabel + " " + forecast.telop);
                }
            } else if (exception != null) {//データが空であったら(エラーが出たら)
                Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();//トーストで注意
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //アプリが起動したときに呼び出される
        super.onCreate(savedInstanceState);

        //ここから位置情報取得
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = mLocationManager.getBestProvider(criteria, true);

        // LocationListenerを登録//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);

        //ここまで位置情報取得
        //
        latitude = 35.689633;
        longitude = 139.692100;
        urlNumber = Place.minDistance(latitude, longitude);


        setContentView(R.layout.activity_main); //アクティビティを配置
        textView = (TextView) findViewById(R.id.tv_main); //別のviewの定義を呼び出す(定義自体は先にcontent_main.xmlにする)
        new GetWeatherForecastTask(this).execute(urlNumber); //URLの最後の部分例えば140020なら小田原を指す詳しくはlivedoorのお天気サービス

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //アクションバーを定義するメソッド
        getMenuInflater().inflate(R.menu.menu_main, menu); //xmlから定義を呼び出し
        return true; //成功
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //　メニューアイテムの追加
        int id = item.getItemId(); //メニューアイテムに(上記の)設定を追加
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item); //定義したメニューアイテムを返す
    }
}
