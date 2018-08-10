package doutu.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import doutu.model.ImageModel;
import doutu.model.IndexModel;

/**
 * 解析网站url
 * @author wangruigen
 */
public class UrlParse {
	private static String DOUTU_URL = "http://www.doutula.com/article/list/?page=";//斗图网基础页
	public static LinkedList<IndexModel> list = new LinkedList<IndexModel>();//下载资源的集合

	public void parseUrl(int page) {
		InputStream is = null;
		try {
			URL url = new URL(DOUTU_URL+page);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			is = connection.getInputStream();
			byte[] buf = new byte[1024]; 
			int length = 0 ;
			String html = "";
			while((length = is.read(buf))!=-1){
				html += new String(buf,0 ,length);
			}
			if(html.length()>0) {
				parse(html,page);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void parse(String html,int page) throws IOException {
		Pattern compile = Pattern.compile("<a href=\"(.*?)\" class=\"list-group-item.*\">\\s.*<div class=\"random_title\">(.*?)<div class=\"date\">(.*?)</div></div>");
		Matcher matcher = compile.matcher(html);
		while(matcher.find()) {
			//拿到链接
			IndexModel indexModel = new IndexModel();
			indexModel.setPage(String.valueOf(page));
			indexModel.setImageUrl(matcher.group(1));
			indexModel.setImageTitle(matcher.group(2));
			indexModel.setImageDate(matcher.group(3));
			//拿到所有将要访问图片资源的url
			URL url = new URL(indexModel.getImageUrl());
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("Host", "www.doutula.com");
			connection.setRequestProperty("Cookie", "__cfduid=d227b668908a38d984ae48543beed5a581517989258; UM_distinctid=1616f34948969-08d8e411d35e81-5e183017-100200-1616f34948a32c; CNZZDATA1266118427=424855970-1520305083-null%7C1520329395; yjs_id=68acb89302e7484f72dd64f897df55d0; ctrl_time=1; XSRF-TOKEN=eyJpdiI6IkpRdFNmUDBXZFl6YVhIbGxkdUJDY1E9PSIsInZhbHVlIjoiZm1GTTBWd1NTcE5YZ2k5WlVhbmF5Sk9HMjdwRmhrcG1BdjJUQlRacTZzNVduSmduY3V2V0ZxZnJxb2l4eUdUQkVleFRPQXk5VUk0K0NWUXEzdUVSXC9RPT0iLCJtYWMiOiI4MGFjMTNkMjk3N2E0OGQ5MDczYzg0YmIwOTlkM2VmNTc2ZjRhZTIwODcxYTcxNGMyODE1MDU3NzljNmFhYjM3In0%3D; doutula_session=eyJpdiI6IkRvOUNTenJIZHBnWm1LbDNIRUdHeGc9PSIsInZhbHVlIjoidEJBaTFKN0thcFVHS2cwTjFiaUVLbnZheTJNYk80T0labFwvSjVRQTRjc1hsM2czbGFEVWJPdDdYUGFIMEROSnE4bnFPeGFGYmVlbEJaYTJhU25YTXpnPT0iLCJtYWMiOiI5NjBhMjc1Y2E2ZGJlNjA4ODRlNjM1ZTQ0Y2Y1N2FlMTE3MjZmZDJhODYzZDIyMzA1ZjJmMWRjNmQzZjNjN2RhIn0%3D; _ga=GA1.2.1472771311.1517989853; _gid=GA1.2.1611081909.1531982145; CNZZDATA1256911977=1006437228-1517987099-http%253A%252F%252Flocalhost%253A8080%252F%7C1531981913");
			connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
			InputStream is = connection.getInputStream();
			byte[] buf = new byte[1024]; 
			int length = 0 ;
			String img = "";
			while((length = is.read(buf))!=-1){
				img += new String(buf,0 ,length);
			}
			Pattern imgCompile = Pattern.compile("<a href=\".*\">\\s.*<img src=\"(.*?)\" alt=\"(.*?)\" onerror=\"this.src='.*'\">\\s.*</a>");
			Matcher imgMacher = imgCompile.matcher(img);
			LinkedList<ImageModel> imageModels = new LinkedList<ImageModel>();
			while(imgMacher.find()) {
				//拿到当前页所有图片的资源地址和alt
				ImageModel imageModel = new ImageModel();
				imageModel.setImgUrl(imgMacher.group(1));
				imageModel.setAlt(imgMacher.group(2));
				imageModels.add(imageModel);
			}
			indexModel.setImageModels(imageModels);
			synchronized (list) {
				list.addLast(indexModel);
			}
			is.close();
		}
	}
}
