package com.sohu.wls.app.automsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sohu.wls.app.automsg.common.UserDetailModel;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;
import org.apache.http.protocol.HttpService;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private UserDetailOpenHelper userDetailOpenHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (userDetailOpenHelper == null){
            userDetailOpenHelper = new UserDetailOpenHelper(this);
        }
        EditText phoneField = (EditText) findViewById(R.id.user_detail_phone);
        EditText costmaxField = (EditText) findViewById(R.id.user_detail_cost_max);

        try {
            UserDetailModel detailModel = userDetailOpenHelper.queryUserDetailInfo();
            phoneField.setText(detailModel.getPhone_number());
            costmaxField.setText(detailModel.getCost_max()+"");
        } catch (Exception e) {
            Toast.makeText(this, R.string.user_detail_query_fail_msg+"["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

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

    public void onUpdateUserDetailButtonClick(View view){
        EditText phoneField = (EditText) findViewById(R.id.user_detail_phone);
        EditText costmaxField = (EditText) findViewById(R.id.user_detail_cost_max);

        String phone = phoneField.getText().toString();
        int costmax = 0;
        if(costmaxField.getText().toString() == null || costmaxField.getText().toString().equals("")){
            Toast.makeText(this, R.string.user_detail_cost_max_empty_msg, Toast.LENGTH_LONG).show();
            return;
        }else{
            costmax = Integer.parseInt(costmaxField.getText().toString());
        }
        if (phone.length()!=11 || !phone.startsWith("1")){
            Toast.makeText(this, R.string.user_detail_phone_invalid_msg, Toast.LENGTH_LONG).show();
            return;
        }

        UserDetailModel detailModel = new UserDetailModel(phone,costmax);

        try {
            userDetailOpenHelper.updateUserDetail(detailModel);
            Toast.makeText(this, R.string.user_detail_save_success_msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.user_detail_save_fail_msg+"["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
    }
}
