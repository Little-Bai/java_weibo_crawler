package pachong;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import weibo4j.Tags;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.Tag;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
//Access token key: 62e95f355bb7f420d54b78f4bd11d608
//Access token secret: 1d8f8f4ccb8c899fc995c8ba68ba84a3

/*将要爬取用户的screen_name写在screen_name。txt
 * 
 * 
 * */

public class get_label {

    public static void main(String[] args) throws IOException,
    ClassNotFoundException,
    SQLException, WeiboException {
        BufferedReader input_config = new BufferedReader(new InputStreamReader(new FileInputStream("config.txt")));
        String MYSQL_URL;
        MYSQL_URL = input_config.readLine();
        String MYSQL_ACCOUNT;
        MYSQL_ACCOUNT = input_config.readLine();
        String MYSQL_PIN;
        MYSQL_PIN = input_config.readLine();
        if (MYSQL_PIN == null) MYSQL_PIN = "";
        //驱动数据库连接
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:" + MYSQL_URL + "?characterEncoding=utf-16", MYSQL_ACCOUNT, MYSQL_PIN);

        String access_token = "2.00fwzwOCFgIJQDa3073d37cd3d2laC";

        BufferedReader init = new BufferedReader(new InputStreamReader(new FileInputStream("last_one_name.txt"), "utf-16"));
        String last_time_end = "";
        last_time_end = init.readLine(); //获取上次最后一个爬的人
        init.close();

        BufferedWriter last_name_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("last_one_name.txt"), "utf-16"));

        BufferedReader bin = new BufferedReader(new InputStreamReader(new FileInputStream("screen_name.txt"), "utf-16"));

        Statement stmt = conn.createStatement();
        String del_str = "delete from labels where screen_name = '" + last_time_end + "'";
        stmt.executeUpdate(del_str);

        String screen_name = "";
        while (!screen_name.equals(last_time_end)) screen_name = bin.readLine();
        try {
            do {
                // StatusWapper is class which is used to describe the feather of user's TIMELINE.
                PreparedStatement 
                
                screen_name = bin.readLine();
            } while (!screen_name.equals("end"));
            if (screen_name == "") screen_name = "时尚小波哥";
            last_name_writer.write(screen_name);
            System.out.println(screen_name);
        } catch(WeiboException e) {
            if (screen_name == "") screen_name = "时尚小波哥";
            last_name_writer.write(screen_name);
            System.out.println(screen_name);
            e.printStackTrace();
        }
        last_name_writer.close();

    }

}