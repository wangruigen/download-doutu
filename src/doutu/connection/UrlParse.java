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
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
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
