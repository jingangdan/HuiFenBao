package com.dq.huifenbao.openssl;

/**
 * Created by asus on 2018/4/1.
 */

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dq.huifenbao.R;

import javax.crypto.Cipher;

public class MainActivity2 extends Activity implements OnClickListener {
    private Button btn1, btn2;// 加密，解密
    private EditText et1, et2, et3;// 需加密的内容，加密后的内容，解密后的内容

    /* 密钥内容 base64 code */
    private static String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEroiJKwk3tRhhYsf4HC6mJ3Is" +
            "bce1FuyxlxBLLqAWTA3ZCAU8qJvZ9KxLEUlfS0oXauZIhesnMQhqY0weX6u2EejC" +
            "Um1amqeJTR8NRrnIR0TMJOMKiQJtQTOT4Del4WWeodHLhKzm7ls+9Pq2pcvIhZPu" +
            "a3kRfIOHu+qDa+V8DQIDAQAB";

    public static String PRIVATE_KEY = "MIICXQIBAAKBgQDEroiJKwk3tRhhYsf4HC6mJ3Isbce1FuyxlxBLLqAWTA3ZCAU8" +
            "qJvZ9KxLEUlfS0oXauZIhesnMQhqY0weX6u2EejCUm1amqeJTR8NRrnIR0TMJOMK" +
            "iQJtQTOT4Del4WWeodHLhKzm7ls+9Pq2pcvIhZPua3kRfIOHu+qDa+V8DQIDAQAB" +
            "AoGAWPYExd5UZgEgjPQMNPoyU28mIlhpa0x6NP16Hjdxq4QtA0ywabZuABC+WZnx" +
            "EQxR7/OnQ4hnS6vO/af6RwbI+F3+rWfm90b08M1XTRjH5sH2eohz9hhmsVHlRiUH" +
            "BefyE06xpM6Ix+QZhOTk5/jCCwdjS88cV94jjtYqpm8eQCECQQD9BwSkABkpHLBX" +
            "5bgy4Agflxm3DoNRvXK6Poj9STgZXXDY1iE6CKv7yW4ylj8DXbH15Qfsn9Y1sACq" +
            "fztIw+Y1AkEAxv4OJcZGaPKT6yChGbbqoxy+jgEIr2qvrn8tOJL7UZi6ThvoeFAT" +
            "jy6I7ech+dPHs4djdNaHVKYlJUlantiZeQJBAPNs7xAAcDRXP2gwvzdixS3vJnQw" +
            "aDIZeuinBQ/4gxoUBV73EtxMP3lq3rOYTGMmnvEcOLVzSJg6DUn6QcTNgwkCQGyI" +
            "quy9fS/GhxkqfdPJqaZ3ihNnyvXqSZHyOZbuy/aQR6VMoXnlqxVgOz0O4MAjW3u2" +
            "728Nh1iLGh2BKdUTrHkCQQCbLJ/mrpTCVhEk7HQYI3Y0N1puiIyNCWle3R9LqR9W" +
            "b4sNVQY25rnx1tRv7VfxPLE+S8EZ6qPWJ1hvwXcfvAFU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 加密
            case R.id.btn1:
                String source = et1.getText().toString().trim();
                try {
                    // 从字符串中得到公钥
                     PrivateKey publicKey = RSAUtils.loadPrivateKey(PRIVATE_KEY);
                    // 从文件中得到公钥
//                    InputStream inPublic = getResources().getAssets().open("rsa_public_key.pem");
//                    PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
                    // 加密
                    byte[] encryptByte = RSAUtils.encryptDataPrivate(source.getBytes(), publicKey);
                    // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
                    String afterencrypt = Base64Utils.encode(encryptByte);
                    et2.setText(afterencrypt);
                    System.out.println("111111111111111 = " + afterencrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // 解密
            case R.id.btn2:
                //String encryptContent = et2.getText().toString().trim();
                String encryptContent = "iSa8aYlb6Wl+oFB8Rwzv8RaQih6sabXEI0CApOdFodEMYpjRYy+N/UIme16MYszC1oTRy0YrIE8OFi0UPC/0R/+yl1xWqwxnNWGIeHBp6R60AZl1ilLq0ea2G9BxGjxV8CFo/wOMPYQqgwJFloI0XqntP1DiWDr6v3MyoDkcb5U=";
                try {
                    // 从字符串中得到私钥
                     PrivateKey privateKey = RSAUtils.loadPrivateKey(PRIVATE_KEY);
                     //PublicKey publicKey = RSAUtils.loadPublicKey(PUCLIC_KEY);
                    // 从文件中得到私钥
//                    InputStream inPrivate = getResources().getAssets().open("pkcs8_rsa_private_key.pem");
//                    PrivateKey privateKey = RSAUtils.loadPrivateKey(inPrivate);
                    // 因为RSA加密后的内容经Base64再加密转换了一下，所以先Base64解密回来再给RSA解密
                    byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(encryptContent), privateKey);
                    //byte[] decryptByte = RSAUtils.decryptDataPublic(Base64Utils.decode(encryptContent), publicKey);
                    String decryptStr = new String(decryptByte);
                    et3.setText(decryptStr);
                    System.out.println("111111111111111 = " + decryptStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
