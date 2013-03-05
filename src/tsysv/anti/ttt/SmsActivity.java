package tsysv.anti.ttt;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmsActivity extends Activity {
	
	EditText hintph;
	Button smssend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smssend);
		
		hintph = (EditText) findViewById(R.id.hintsmsphone);
		
		if (getSharedPreferences("antithief", MODE_PRIVATE).getString("phone", "").equals("")){
			hintph.setText("");
		}else{
			hintph.setText(getSharedPreferences("antithief", MODE_PRIVATE).getString("phone", ""));
		}
		
		smssend = (Button) findViewById(R.id.smsnext);
		smssend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!hintph.getText().toString().equals("")){
						getSharedPreferences("antithief", MODE_PRIVATE).edit()
						.putString("phone", hintph.getText().toString())
						.commit();
					
					Boolean checked_mail = getSharedPreferences("antithief", MODE_PRIVATE)
							.getBoolean("checkedmail", false);
					if (checked_mail){
						Intent i = new Intent(getApplicationContext(), MailActivity.class);
						startActivity(i);
						finish();
					}
				}else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.hintsmsiswr), Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
