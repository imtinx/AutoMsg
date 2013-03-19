package com.sohu.wls.app.automsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;
import org.apache.http.protocol.HttpService;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button) findViewById(R.id.button_task_config);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To change body of implemented methods use File | Settings | File Templates.
                Intent intent = new Intent();
                intent.setClass(MyActivity.this, TaskConfigMainActivity.class);
                startActivity(intent);
            }
        });
    }


}
