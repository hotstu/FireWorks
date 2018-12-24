package github.hotstu.fireworks;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "ZCOOLKuaiLe-Regular.ttf");
        tv.setTypeface(typeface);
    }
}
