package doutu.run;

import java.util.Timer;
import java.util.TimerTask;

import doutu.connection.UrlDowmLoad;
import doutu.connection.UrlParse;

public class Run {
	private static int page = 1;//页码
	private static int max_page = 5;//最大页码
	private volatile static boolean isExit = false;
	
	
	public static void main(String[] args) {
		parseThread();//解析界面的线程方法
		downloadThread();//下载图片的线程方法
	}

	private static void downloadThread() {
		final UrlDowmLoad urlDowmLoad = new UrlDowmLoad();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!isExit) {
					urlDowmLoad.downloadSyn();
				}else {
					if(UrlParse.list.size()>0) {
						urlDowmLoad.downloadSyn();
					}else {
						System.exit(0);
					}
				}
			}
		}, 0,500);
	}

	private static void parseThread() {
		final UrlParse urlParse = new UrlParse();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(isExit) return;
				if(page>max_page) {//目前最大页码是561
					System.out.println("已解析到最大页码，程序即将结束运行。");
					isExit = true;
				}else {
					System.out.println("正在解析网页："+page);
					urlParse.parseUrl(page);
					System.out.println("网页解析完成："+page);
					page++;//页码自增，也可以注释掉，爬取指定页码
				}
			}
		}, 0,1500);
	}
}
