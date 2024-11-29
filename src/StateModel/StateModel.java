package StateModel;

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
}
