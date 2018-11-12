package hdc.ExchangeTransform;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ExchangeTransform extends Activity {
    /** Called when the activity is first created. */
	private Spinner sl,sr;
	private Button bt;
	private TextView tr,warn;
	private EditText el;
	public String ls,rs;
	public double cr,or,af;
	private static final String[] COUNTRIES = new String[] {"Taiwan dollar","US dollar","China yen"};
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,COUNTRIES);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sl = (Spinner) findViewById(R.id.S_L);
        sl.setAdapter(adapter);
        sr = (Spinner) findViewById(R.id.S_R);
        sr.setAdapter(adapter);
        bt = (Button) findViewById(R.id.B_CONVERT);
        tr = (TextView) findViewById(R.id.TV_R);
        warn = (TextView) findViewById(R.id.TV_WARN);
        el = (EditText) findViewById(R.id.ET_L);
        bt.setOnClickListener
        (
        		new View.OnClickListener()
        		{
        			public void onClick(View v)
        			{
        				if(!Check(el.getText().toString())){
        					warn.setText("WRONG!");
        					warn.setTextColor(Color.RED);
        					return;
        				}
        				warn.setText(null);
        				cr = 1.0;
        				ls = (String)sl.getSelectedItem();
        				rs = (String)sr.getSelectedItem();
        				if(ls == "Taiwan dollar"){
        		            if(rs == "US dollar")
        		                cr = 0.035002;
        		            else if(rs == "China yen")
        		                cr = 0.22727;
        		        }else if(ls == "US dollar"){
        		            if(rs == "Taiwan dollar")
        		                cr = 28.57;
        		            else if(rs == "China yen")
        		                cr = 6.4931;
        		        }else if(ls == "China yen"){
        		            if(rs == "Taiwan dollar")
        		                cr = 4.4001;
        		            else if(rs == "US dollar")
        		                cr = 0.15401;
        		        }
        				or = Double.parseDouble(el.getText().toString());
        				af = or * cr;
        				af = ((int)(af * 10000)) / 10000.0;
        				tr.setText("" + af);
        			}
        		}
        );
    }
    private boolean Check(String st){
    	if(!st.matches("[0-9]+\\.?[0-9]*")) return false;
    	return true;
    }
}