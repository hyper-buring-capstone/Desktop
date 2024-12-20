package StateModel;

import java.awt.Color;
import java.io.File;

import drawing.NoteFrame;
import lombok.Getter;
import lombok.Setter;
import service.Receiver;

public class StateModel {

	@Getter
	@Setter
	private boolean isNoteOpen;
	private NoteFrame noteFrame;
	private static File [] files;
	private int curPageNum;
	private Receiver receiver;
	private int imageWidth;
	private int imageHeight;
	private static String lineString;
	private String noteTitle;
	private Color penColor = new Color(255, 255, 255, 255);
	private int penWidth = 5;
	@Getter
	@Setter
	private int totalPage;

	// 페이지 이동을 한 곳에서 관리.
	public void setPageIndex(int pageIndex){
		if(pageIndex<0 || pageIndex>=totalPage){
			return;
		}
		else{
			noteFrame.setPageIndex(pageIndex);
		}
	}
	
	public void setNoteOpen(boolean isOpen) {
		isNoteOpen = isOpen;
	}
	
	public boolean getNoteOpen() {
		return isNoteOpen;
	}
	
	public void setNoteFrame(NoteFrame noteFrame) {
		this.noteFrame = noteFrame;
	}
	
	public NoteFrame getNoteFrame() {
		return noteFrame;
	}
	
	public void setFiles(File[] files) {
		StateModel.files = files;
	}
	
	public static File getFile(int num) { // 실제 num
		return files[num]; // 에러 처리 해야함(static을 없애는게 제일 좋긴함)
	}
	
	public void setCurPageNum(int num) { // 실제 num
		curPageNum = num;
	}
	
	public int getCurPageNum() {
		return curPageNum;
	}
	
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	
	public Receiver getReceiver() {
		return receiver;
	}
	
	public void setImageWidth(int width) {
		this.imageWidth = width;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public void setImageHeight(int height) {
		this.imageHeight = height;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}
	
	public void setLineString(String lineString) {
		StateModel.lineString = lineString;
	}
	
	public static String getLineString() {
		return lineString;
	}
	
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	
	public String getNoteTitle() {
		return noteTitle;
	}
	
	public void setPenWidth(int width) {
		this.penWidth = width;
	}
	
	public int getPenWidth() {
		return penWidth;
	}
	
	public void setPenColor(Color color) {
		this.penColor = color;
	}
	
	public Color getPenColor() {
		return penColor;
	}
}
