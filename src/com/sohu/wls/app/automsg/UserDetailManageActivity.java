package com.sohu.wls.app.automsg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sohu.wls.app.automsg.common.UserDetailModel;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-29
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class UserDetailManageActivity extends Activity {
    private UserDetailOpenHelper userDetailOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.user_detail_manage);
        if (userDetailOpenHelper == null){
            userDetailOpenHelper = new UserDetailOpenHelper(this);
        }



        try {
            UserDetailModel detailModel = userDetailOpenHelper.queryUserDetailInfo();
            updateUserDetail(detailModel);
        } catch (Exception e) {
            Toast.makeText(this, R.string.user_detail_query_fail_msg + "[" + e.getMessage() + "]", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void updateUserDetail(UserDetailModel detailModel){
        if (detailModel == null){
             return;
        }
        EditText phoneField = (EditText) findViewById(R.id.user_detail_phone);
        EditText costmaxField = (EditText) findViewById(R.id.user_detail_cost_max);

        if (detailModel.getPhone_number() == null || detailModel.getPhone_number().equals("")){
//            phoneField.setText(R.string.user_detail_phone_holder);

        }   else {
            phoneField.setText(detailModel.getPhone_number());
        }

        if (detailModel.getCost_max() == 0){
//            costmaxField.setText(R.string.user_detail_cost_max_holder);
        }else{
            costmaxField.setText(detailModel.getCost_max()+"");
        }
    }

    public void onInfoFieldClick(View view){
        TextView _view = (TextView)view;
        Log.v(TaskConfigMainActivity.TAG,_view.getText().toString());
        if (_view.getId() == R.id.user_detail_phone &&
                _view.getText().toString().equals(getString(R.string.user_detail_phone_holder))){
             _view.setText("");
        }else if(_view.getId() == R.id.user_detail_cost_max &&
                _view.getText().toString().equals(getString(R.string.user_detail_cost_max_holder))){
            _view.setText("");
        }
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
