package doutu.model;
/**
 * 图片资源对象
 * @author wangruigen
 */
public class ImageModel {
	private String imgUrl;//图片地址
	private String alt;//图片描述
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	@Override
	public String toString() {
		return "ImageModel [imgUrl=" + imgUrl + ", alt=" + alt + "]";
	}
	
}
