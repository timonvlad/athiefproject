package tsysv.anti.ttt;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {

	Button mainnext;
	CheckBox check_sms, check_mail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Boolean checked_sms = getSharedPreferences("antithief", MODE_PRIVATE)
				.getBoolean("checkedsms", false);
		Boolean checked_mail = getSharedPreferences("antithief", MODE_PRIVATE)
				.getBoolean("checkedmail", false);
		
		check_mail = (CheckBox) findViewById(R.id.cbsendmail);
		check_sms = (CheckBox) findViewById(R.id.cbsendsms);
		
		if (!checked_sms) {
			check_sms.setChecked(check_sms.isChecked());
		} else {
			check_sms.setChecked(!check_sms.isChecked());
		}// ///////////////////////
		if (!checked_mail) {
			check_mail.setChecked(check_mail.isChecked());
		} else {
			check_mail.setChecked(!check_mail.isChecked());
		}// ///////////////////////
		
		
		///////////////////
			check_sms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					getSharedPreferences("antithief", MODE_PRIVATE).edit()
					.putBoolean("checkedsms", true)
					.commit();
					Log.v("CHECKED SMS", "is - " + getSharedPreferences("antithief", MODE_PRIVATE)
				.getBoolean("checkedsms", false));
				}
				else{
					getSharedPreferences("antithief", MODE_PRIVATE).edit()
					.putBoolean("checkedsms", false)
					.commit();
					Log.v("CHECKED SMS", "is - " + getSharedPreferences("antithief", MODE_PRIVATE)
				.getBoolean("checkedsms", false));
				}
				
			}
		});
		///////////////////
			check_mail.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					getSharedPreferences("antithief", MODE_PRIVATE).edit()
					.putBoolean("checkedmail", true)
					.commit();
					Log.v("CHECKED mail", "is - " + getSharedPreferences("antithief", MODE_PRIVATE)
				.getBoolean("checkedmail", false));
				}
				else{
					getSharedPreferences("antithief", MODE_PRIVATE).edit()
					.putBoolean("checkedmail", false)
					.commit();
					Log.v("CHECKED mail", "is - " + getSharedPreferences("antithief", MODE_PRIVATE)
				.getBoolean("checkedmail", false));
				}
				
			}
		});
		
		
		mainnext = (Button) findViewById(R.id.mainnext);
		mainnext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				
				
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
