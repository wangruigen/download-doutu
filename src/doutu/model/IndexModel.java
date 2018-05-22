package doutu.model;

import java.util.LinkedList;

/**
 * 首页数据实体模型
 * @author wangruigen
 */
public class IndexModel {
	private String page;//图片所在页码
	private String imageTitle;//图片小标题
	private String imageUrl;//访问图片的url
	private String imageDate;//日期
	private LinkedList<ImageModel> imageModels = new LinkedList<ImageModel>();//图片数据模型
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getImageTitle() {
		return imageTitle;
	}
	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageDate() {
		return imageDate;
	}
	public void setImageDate(String imageDate) {
		this.imageDate = imageDate;
	}
	public LinkedList<ImageModel> getImageModels() {
		return imageModels;
	}
	public void setImageModels(LinkedList<ImageModel> imageModels) {
		this.imageModels = imageModels;
	}
	@Override
	public String toString() {
		return "IndexModel [page=" + page + ", imageTitle=" + imageTitle + ", imageUrl=" + imageUrl + ", imageDate="
				+ imageDate + ", imageModels=" + imageModels + "]";
	}
	public String printString(){
		return "imageTitle=" + imageTitle +", imageDate="
				+ imageDate + ", imageModels=" + imageModels + "";
	}
}
