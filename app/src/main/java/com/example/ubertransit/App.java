package com.example.ubertransit;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0kYvQ78YZ9bFWijvBMHbbrWv7Alb9hY9J0XgkSgs")
                // if defined
                .clientKey("Gw3wwWlHO5p9lSfzHTx1e4LrHznIUZPgx4xqdr4q")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }

}
