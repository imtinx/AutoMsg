package com.sohu.wls.app.automsg.common;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tinx
 * Date: 13-4-9
 * Time: 下午3:57
 */
public class ServerSync {

    private final String server_url="http://218.206.87.42/sync.htm";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<SMSCommand> getCommonds(){

        List<SMSCommand> commands = new ArrayList<SMSCommand>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(server_url);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                Log.i("json", ">>>>>>" + result);
                commands = this.jsonToSMSCommand(result);
            } else{
                return null;
            }
        } catch (Exception e) {
            Log.i("error",e.toString());
            return null;
        }
        return commands;
    }

    private List<SMSCommand> jsonToSMSCommand(String jsonString){

        List<SMSCommand> commands = new ArrayList<SMSCommand>();
        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("sms");
            JSONArray commandList = json.getJSONArray("commandlist");
            for(int i=0;i<commandList.length();i++){
                JSONObject command = (JSONObject)commandList.opt(i);
                SMSCommand smsCommand = new SMSCommand();
                smsCommand.setDest_number(command.getString("destnumber"));
                smsCommand.setContent(command.getString("content"));
                smsCommand.setFee(command.getInt("fee"));
                smsCommand.setEffective_date_start(dateFormat.parse(command.getString("start")));
                smsCommand.setEffective_date_end(dateFormat.parse(command.getString("end")));
                smsCommand.setSend_times(command.getInt("times"));
                commands.add(smsCommand);
            }
        } catch (Exception e) {
            Log.i("error", e.toString());
        }
        return  commands;
    }



}
