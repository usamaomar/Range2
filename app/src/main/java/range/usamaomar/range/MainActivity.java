package range.usamaomar.range;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RangeView rangeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        rangeView = (RangeView) findViewById(R.id.rangeView);
        rangeView.setbackGroundSrc(R.drawable.sers);
    }
}
