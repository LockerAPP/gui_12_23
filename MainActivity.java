package com.ntou.locker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity  extends Activity {
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main); 
Button button = (Button)findViewById(R.id.startButton); 
button.setOnClickListener(new Button.OnClickListener(){
@Override
public void onClick(View v) {
// TODO Auto-generated method stub 
Intent intent = new Intent();
intent.setClass(MainActivity.this, SubPageActivity.class);
startActivity(intent); 
MainActivity.this.finish(); 
}
});
}
}
