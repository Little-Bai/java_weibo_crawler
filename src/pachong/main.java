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

public class main {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException {
		BufferedReader input_config = new BufferedReader(new InputStreamReader(
				new FileInputStream("config.txt")));
		String MYSQL_URL;
		MYSQL_URL = input_config.readLine();
		String MYSQL_ACCOUNT;
		MYSQL_ACCOUNT = input_config.readLine();
		String MYSQL_PIN;
		MYSQL_PIN = input_config.readLine();
		if (MYSQL_PIN == null)
			MYSQL_PIN = "";
		// 驱动数据库连接
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:" + MYSQL_URL
				+ "?characterEncoding=utf-8", MYSQL_ACCOUNT, MYSQL_PIN);

		// 清空表单
		// Statement state = conn.createStatement();
		// state.executeUpdate("TRUNCATE TABLE  `weibos`");

		// set sccess_token
		String access_token = "2.00fwzwOCFgIJQDa3073d37cd3d2laC";

		// Declar a TIMELINE object and send the access_token to it
		Users um = new Users();
		um.client.setToken(access_token);
		Tags tags_tm = new Tags();
		List<Tag> tags;
		String u_id = "";
		tags_tm.client.setToken(access_token);

		// Declar a TIMELINE object and send the access_token to it
		tags_tm.client.setToken(access_token);

		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		BufferedReader init = new BufferedReader(new InputStreamReader(
				new FileInputStream("last_one_name.txt"), "utf-8"));
		String last_time_end = "";
		last_time_end = init.readLine(); // 获取上次最后一个爬的人
		init.close();

		BufferedWriter last_name_writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream("last_one_name.txt"), "utf-8"));

		BufferedReader bin = new BufferedReader(new InputStreamReader(
				new FileInputStream("screen_name.txt"), "utf-8"));

		Statement stmt = conn.createStatement();
		String screen_name = "";
		try {
			System.out.println(last_time_end);
			while (!screen_name.equals(last_time_end))
			{
				screen_name = bin.readLine();
			}
			String last_time_end_id = um.showUserByScreenName(last_time_end)
					.getId();
			String del_str = "delete from weibos where u_id = '"
					+ last_time_end_id + "'";
			stmt.executeUpdate(del_str);
			del_str = "delete from labels where u_id = '" + last_time_end_id
					+ "'";
			stmt.executeUpdate(del_str);

			// Paging is an object which is used to set the number of current
			// page and the count WEIBO number of one page.
			int page_count = 50;
			Paging page = new Paging(1);
			page.setCount(page_count);
			do {
				// StatusWapper is class which is used to describe the feather
				// of user's TIMELINE.
				u_id = um.showUserByScreenName(screen_name).getId();
				StatusWapper status = tm.getUserTimelineByUid(u_id, page, 0, 0);

				// total number of this user's WEIBO.
				long num_weibo = status.getTotalNumber();

				// total page number of this user's WEIBO.
				int page_num = (int) (num_weibo / page_count);
				if (0 != (num_weibo % page_count))
					page_num++;
				if (page_num > 20)
					page_num = 20;
				Status temp = new Status();

				/*
				 * 数据库表单名称为weibos， 含有五项内容 一、用户微博昵称 二、微博内容 三、转自微博内容 四、转发数 五、评论数
				 */
				PreparedStatement Statement = conn
						.prepareStatement("INSERT INTO  `weibo_collection`.`weibos` (`u_id` ,`text` ,`source` ,`repostsCount` ,`commentsCount`) VALUES(?,?,?,?,?)");
				// 分页获取微博
				for (int i = 1; i <= page_num; i++) {
					page.setPage(i);
					status = tm.getUserTimelineByUid(u_id, page, 0, 0);

					// 抓取本页微博
					for (Status s : status.getStatuses()) {
						Statement.setString(1, u_id);
						Statement.setString(2, s.getText());

						temp = s.getRetweetedStatus();
						if (temp != null)
							Statement.setString(3, temp.getText());
						else
							Statement.setString(3, "");

						Statement.setString(4,
								String.valueOf(s.getRepostsCount()));
						Statement.setString(5,
								String.valueOf(s.getCommentsCount()));
						Statement.executeUpdate();

					}
				}
				Statement = conn
						.prepareStatement("INSERT INTO  `weibo_collection`.`labels` (`u_id` ,`label_id` ,`label_value` ,`label_weight` ) VALUES(?,?,?,?)");

				tags = null;
				tags = tags_tm.getTags(u_id);
				for (Tag tag : tags) {
					Statement.setString(1, u_id);
					Statement.setString(2, tag.getId());
					Statement.setString(3, tag.getValue());
					Statement.setString(4, tag.getWeight());
					Statement.executeUpdate();
				}
				screen_name = bin.readLine();
			} while (!screen_name.equals("end"));
			if (screen_name == "")
				screen_name = "时尚小波哥";
			last_name_writer.write(screen_name);
			System.out.println(screen_name);
		} catch (WeiboException e) {
			if (screen_name == "")
				screen_name = "时尚小波哥";
			last_name_writer.write(screen_name);
			System.out.println(screen_name);
			e.printStackTrace();
		}
		last_name_writer.close();

	}

}