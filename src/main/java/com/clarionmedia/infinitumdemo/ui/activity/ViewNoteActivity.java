package com.clarionmedia.infinitumdemo.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.clarionmedia.infinitum.activity.InfinitumActivity;
import com.clarionmedia.infinitum.activity.annotation.Bind;
import com.clarionmedia.infinitum.activity.annotation.InjectLayout;
import com.clarionmedia.infinitum.activity.annotation.InjectView;
import com.clarionmedia.infinitum.di.annotation.Autowired;
import com.clarionmedia.infinitumdemo.R;
import com.clarionmedia.infinitumdemo.domain.Note;
import com.clarionmedia.infinitumdemo.service.NoteService;

@InjectLayout(R.layout.activity_view_note)
public class ViewNoteActivity extends InfinitumActivity {

	public static final String EXTRA_NOTE_ID = "com.clarionmedia.infinitumdemo.NoteId";
	
	private static final int REQUEST_EDIT_NOTE = 1;

	@InjectView(R.id.note_name)
	private TextView mNoteName;
	
	@InjectView(R.id.category_name)
	private TextView mCategoryName;

	@InjectView(R.id.note_contents)
	private TextView mNoteContents;
	
	@InjectView(R.id.delete_note)
	@Bind("createConfirmDeleteDialog")
	private Button mDeleteNote;
	
	@InjectView(R.id.edit_note)
	@Bind("editNote")
	private Button mEditNote;

    @Autowired
    private NoteService mNoteService;

	private Note mNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
		bindNote(mNoteService.getById(noteId));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_EDIT_NOTE) {
			if (resultCode == RESULT_OK) {
                bindNote(mNoteService.getById(mNote.getId()));
			}
		}
	}

	protected void createConfirmDeleteDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this note?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mNoteService.deleteNote(mNote);
						dialog.dismiss();
						finish();
					}
				}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
	
	protected void editNote(View view) {
		Intent intent = new Intent(this, EditNoteActivity.class);
		intent.putExtra(ViewNoteActivity.EXTRA_NOTE_ID, mNote.getId());
		startActivityForResult(intent, REQUEST_EDIT_NOTE);
	}
	
	private void bindNote(Note note) {
		mNote = note;
		mNoteName.setText(mNote.getName());
		mCategoryName.setText(mNote.getCategory().getName());
		mNoteContents.setText(mNote.getContents());
	}

}
