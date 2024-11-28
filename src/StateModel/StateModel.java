package StateModel;

import drawing.NoteFrame;
import lombok.Getter;
import lombok.Setter;

public class StateModel {

	@Getter
	@Setter
	private boolean isNoteOpen;
	private NoteFrame noteFrame;
	
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
	
}
