package org.jpn.majiga.androinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AndroInfoActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				InfoGatheringTask testTask = new InfoGatheringTask(
						AndroInfoActivity.this);
				testTask.execute();
			}
		});
	}

}