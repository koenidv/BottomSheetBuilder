package com.koenidv.bottomsheetbuilder.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.koenidv.bottomsheetbuilder.BottomSheetBuilder;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomSheetBuilder b = new BottomSheetBuilder(this);

        b.setTitle("Demo - Delete");
        b.addItems(new String[] {"Delete", "Cancel"}, new int[] {R.drawable.ic_delete, R.drawable.ic_cancel});
        b.setItemColor(Color.RED, 0);
        b.setOnItemClickListener(new BottomSheetBuilder.onItemClickListener() {
            @Override
            public void onItemClicked(View view, int which, String tag) {
                if (which == 0)
                    Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        b.setOnSheetDismissedListener(new BottomSheetBuilder.onSheetDismissedListener() {
            @Override
            public void onDismissed(String tag) {
                Toast.makeText(MainActivity.this, "Dismissed!", Toast.LENGTH_SHORT).show();
            }
        });
        b.show();
    }
}
