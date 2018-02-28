package com.project.nanfang.leweiremotectrl;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>, View.OnClickListener {

    private String nextFunction = "turnLedOn";
    private Button btn_click;
    private EditText mResultText;

    private Button mBtn_LEDswitch;
    private Button mBtn_forward;
    private Button mBtn_backward;
    private Button mBtn_left;
    private Button mBtn_right;
    private Button mBtn_pause;
    private Button mBtn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_click = (Button) findViewById(R.id.btn_click);
        mResultText = ((EditText) findViewById(R.id.result));
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "= 5a2b97f3");
        btn_click.setOnClickListener(this);


        findViews();
    }

    private void findViews() {
        mBtn_LEDswitch = (Button) findViewById(R.id.btn_LEDswitch);
            mBtn_LEDswitch.setOnClickListener(this);
        mBtn_forward = (Button) findViewById(R.id.btn_forward);
            mBtn_forward.setOnClickListener(this);
        mBtn_backward = (Button) findViewById(R.id.btn_backward);
            mBtn_backward.setOnClickListener(this);
        mBtn_left = (Button) findViewById(R.id.btn_left);
            mBtn_left.setOnClickListener(this);
        mBtn_right = (Button) findViewById(R.id.btn_right);
            mBtn_right.setOnClickListener(this);
        mBtn_pause = (Button) findViewById(R.id.btn_pause);
            mBtn_pause.setOnClickListener(this);
        mBtn_return = (Button) findViewById(R.id.btn_return);
            mBtn_return.setOnClickListener(this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new CtrlATLoader(this, nextFunction);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // No need to modify this method.
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
                btnVoice();
                break;
            case R.id.btn_LEDswitch:
                nextFunction = (nextFunction == "turnLedOn")?"turnLedOff": "turnLedOn";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);
                break;
            case R.id.btn_forward:

                nextFunction = "forward";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);

                break;
            case R.id.btn_backward:

                nextFunction = "backward";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);

                break;
            case R.id.btn_left:

                nextFunction = "left";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);

                break;
            case R.id.btn_right:

                nextFunction = "right";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);

                break;
            case R.id.btn_pause:

                nextFunction = "pause";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);

                break;
            case R.id.btn_return:

                nextFunction = "return";
                getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);

                break;
        }
    }
    //TODO 开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(this,null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
    }
    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        mResultText.append(text);

        if(text.indexOf("前") != -1){
            nextFunction = "forward";
        }
        else if(text.indexOf("后") != -1){
            nextFunction = "backward";
        }
        else if(text.indexOf("左") != -1){
            nextFunction = "left";
        }
        else  if(text.indexOf("右") != -1){
            nextFunction = "right";
        }
        else if(text.indexOf("停") != -1){
            nextFunction = "pause";
        }
        else if(text.indexOf("返回") != -1){
            nextFunction = "return";
        }
        else
            nextFunction = null;
        getSupportLoaderManager().restartLoader(R.integer.CtrlATLoader_ID, null, this);
    }
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }


}
