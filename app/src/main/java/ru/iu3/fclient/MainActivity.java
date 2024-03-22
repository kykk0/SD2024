package ru.iu3.fclient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

import ru.iu3.fclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher activityResultLauncher;

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int res = initRng();
        byte[] v = randomBytes(10);
        byte[] key = new byte[16];
        byte[] data = new byte[32];
        byte[] encrypted = encrypt(key, data);
        byte[] decrypted = decrypt(key, data);

        System.out.println("Start");
        System.out.println(Arrays.toString(encrypted));
        System.out.println(Arrays.toString(decrypted));
        System.out.println(res);
        System.out.println(Arrays.toString(v));


        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data1 = result.getData();
                        // обработка результата
                        String pin = data1.getStringExtra("pin");
                        Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onButtonClick(View v) {
//        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

//        byte[] key = stringToHex("0123456789ABCDEF0123456789ABCDE0");
//        byte[] enc = encrypt(key, stringToHex("000000000000000102"));
//        byte[] dec = decrypt(key, enc);
//        String s = new String(Hex.encodeHex(dec)).toUpperCase();
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();


        Intent it = new Intent(this, PinpadActivity.class);
//        startActivity(it);
        activityResultLauncher.launch(it);

    }

    public static byte[] stringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static native int initRng();

    public static native byte[] randomBytes(int no);

    public static native byte[] encrypt(byte[] key, byte[] data);

    public static native byte[] decrypt(byte[] key, byte[] data);
}