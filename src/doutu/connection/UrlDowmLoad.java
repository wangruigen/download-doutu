package doutu.connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import doutu.model.ImageModel;
import doutu.model.IndexModel;

/**
 * 下载网站图片到本地
 * @author wangruigen
 */
public class UrlDowmLoad {
	/**
	 * 文件下载目录
	 */
	private static final String FILE_PATH = "c://doutu";

	static {
		File file = new File(FILE_PATH);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	public void downloadSyn() {
		LinkedList<IndexModel> list = UrlParse.list;
		synchronized (list) {
			if(list.size()>0) {
				for (IndexModel indexModel : list) {
					boolean download = download(indexModel);
					if(download) {
						System.out.println("图片下载成功："+indexModel.printString());
					}else {
						System.out.println("图片下载失败："+indexModel.printString());
					}
				}
				list.removeAll(list);
			}
		}
	}
	private boolean download(IndexModel indexModel) {
		boolean success = false;
		if(indexModel!=null) {
			String firstFileDir = indexModel.getImageDate();//文件首目录=时间
			String secondFileDir = indexModel.getImageTitle();//文件子目录=标题
			LinkedList<ImageModel> imageModels = indexModel.getImageModels();
			if(imageModels!=null && imageModels.size()>0) {
				for (ImageModel imageModel : imageModels) {
					String imgUrl = imageModel.getImgUrl();
					try {
						URL url = new URL(imgUrl);  
						URLConnection con = url.openConnection();  
						con.setConnectTimeout(5*1000); 
						InputStream is = con.getInputStream();  
						byte[] bs = new byte[1024];  
						int len;  
						File sf=new File(FILE_PATH+"//"+firstFileDir+"//"+secondFileDir);  
						if(!sf.exists()){  
							sf.mkdirs();  
						}  
						String imgUrls = imageModel.getImgUrl();//文件类型判断
						if(imgUrls!=null && imgUrls.length()>0) {
							imgUrls = imgUrls.substring(imgUrls.lastIndexOf("."), imgUrls.length());
						}
						String alt = imageModel.getAlt();
						String[] reg = {"\\","/",":","*","?","<",">","|","。",".","!"};
						for (int i = 0; i < reg.length; i++) {
							if(alt.contains(reg[i])) {
								alt = alt.replaceAll("\\"+reg[i], "");
							}
						}
						OutputStream os = new FileOutputStream(sf.getPath()+"//"+alt+imgUrls);  
						// 开始读取  
						while ((len = is.read(bs)) != -1) {  
							os.write(bs, 0, len);  
						}  
						// 完毕，关闭所有链接  
						os.close();  
						is.close();  
						success = true;
					}catch (Exception e) {
						//不处理
					}
				}
			}   
		}
		return success;
	}
}
