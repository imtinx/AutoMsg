package com.sohu.wls.app.automsg.common;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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

    private final String server_url="http://app.sms.sohu.com/wls/savecode/sync.php";

    private final String server_url_version="http://app.sms.sohu.com/wls/savecode/new.php";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Version getVersion() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(server_url_version);
        HttpResponse httpResponse = httpClient.execute(get);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            Version version = new Version();
            JSONObject resultJson = new JSONObject(result);
            version.setVersion(resultJson.getInt("version"));
            version.setUrl(resultJson.getString("url"));
            return version;
        } else {
            return null;
        }

    }

    public List<SMSCommand> getCommonds(){

        List<SMSCommand> commands;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(server_url);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                commands = this.jsonToSMSCommand(result);
            } else{
                return null;
            }
        } catch (Exception e) {
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
            return null;
        }
        return  commands;
    }



}
