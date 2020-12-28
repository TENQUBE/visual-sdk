package com.tenqube.visualsample.catcher.rcs;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.tenqube.visualsample.visual.VisualManager;


//1) “content://im/chat”로 contentObserver 를 등록
//2) 등록한 ContentObserver 의 onChange()가 호출됨
//3) onChange()로 넘겨받은 Uri 파라미터의 message id 를 이용하여
//“content://im/rcs_read_im_msgid/#(message_id)”를 query
//4) 이때 projection 은 위 테이블을 참고하여 사용
//4-1) type 이 1 인 inbox 경우만 처리
//4-2) 위에서 언급한 표의 content_type 에 해당되는 A2P 인 경우만 처리
//5) 위 4 의 경우가 존재하면 body 를 위 설명한 Open RichCard 의 구성을 보고 파싱하여 처리
public class RcsCatcher {

    private static RcsCatcher mInstance;
    private final Context context;
    private Handler handler;
    private HandlerThread handlerThread;
    private VisualManager visualManager;
    private static final Uri CONTENT_URI = Uri.parse("content://im/chat");

    public static RcsCatcher getInstance(Context context){
        synchronized (RcsCatcher.class) {
            if(mInstance == null){
                mInstance = new RcsCatcher(context);
            }
        }
        return mInstance;
    }

    private RcsCatcher(Context context) {
        this.context = context;
        visualManager = VisualManager.getInstance(context);
    }

    public void unregister() {

        try {
            if(handler != null) {
                final ContentResolver contentResolver = context.getContentResolver();
                RcsContentObserver observer = new RcsContentObserver(visualManager, handler);
                contentResolver.unregisterContentObserver(observer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void register() {
        try {
            unregister();
            Log.i("RCS", "register");

            handlerThread = new HandlerThread("Rcs worker");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
            final ContentResolver contentResolver = context.getContentResolver();
            RcsContentObserver observer = new RcsContentObserver(visualManager, handler);
            contentResolver.registerContentObserver(CONTENT_URI, true, observer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


