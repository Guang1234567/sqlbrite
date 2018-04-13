package com.example.sqlbrite.todo.controler;

import android.app.Application;
import android.net.ParseException;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.Toast;

import com.gg.rxbase.net.retrofit.ApiCode;
import com.gg.rxbase.net.retrofit.ApiException;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * @author Guang1234567
 * @date 2018/4/13 9:24
 */

@MainThread
@UiThread
public class TodoApiThrowableHandler implements Consumer<Throwable> {
    private Application mApplication;

    public TodoApiThrowableHandler(Application application) {
        mApplication = application;
    }

    @Override
    public void accept(Throwable e) throws Exception {
        Toast.makeText(mApplication, Log.getStackTraceString(e), Toast.LENGTH_LONG).show();

        if (e instanceof HttpException) {             //HTTP错误, 如配置了 Https 但证书不匹配?
            // handle
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) { //均视为协议解析错误, 此时应该检查协议与代码是否一致
            // handle
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof ConnectTimeoutException) { // "连接失败"
            // handle
        } else if (e instanceof ApiException) { // 内部协议错误
            ApiException apiException = (ApiException) e;
            ApiCode code = apiException.getErrorCode();
            switch (code) {
                case ERROR_CLIENT_AUTHORIZED:
                    // handle
                    break;
                case ERROR_USER_AUTHORIZED:
                    // handle
                    break;
                case ERROR_REQUEST_PARAM:
                    // handle
                    break;
                case ERROR_PARAM_CHECK:
                    // handle
                    break;
                case ERROR_OTHER:
                    // handle
                    break;
                case ERROR_NO_INTERNET:
                    // handle
                    break;
            }
        } else {
        }
    }
}
