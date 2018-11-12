package hdc.AndroidBusinessCard;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AndroidBusinessCard extends Activity {
    /** Called when the activity is first created. */
	final int NOTHING = -1;
	final int INSERT = 1;
	final int MODIFY = 2;
	final int DELETE = 3;
	private Button i,m,d;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	public static TableRow newtableRow;
	public static TableLayout tblayout;
	private int index = 1,select = -1,ms = -1,mode = NOTHING;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tblayout = (TableLayout)findViewById(R.id.tableLayout1);
        i = (Button) findViewById(R.id.B_INSERT);
        i.setText("INSERT");
        m = (Button) findViewById(R.id.B_MODIFY);
        m.setText("MODIFY");
        d = (Button) findViewById(R.id.B_DELETE);
        d.setText("DELETE");
        tblayout.setStretchAllColumns(true);
        TableRow tableRow = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
		t1.setText("Name");
		t1.setBackgroundColor(Color.RED);
		t1.setTextColor(Color.YELLOW);
		t2.setText("E-Mail");
		t2.setBackgroundColor(Color.RED);
		t2.setTextColor(Color.YELLOW);
		t3.setText("Tel");
		t3.setBackgroundColor(Color.RED);
		t3.setTextColor(Color.YELLOW);
		tableRow.addView(t1);
		tableRow.addView(t2);
		tableRow.addView(t3);
		tblayout.addView(tableRow,new TableLayout.LayoutParams(FP,WC));
		i.setOnClickListener
        (
        		new View.OnClickListener()
        		{
        			public void onClick(View v)
        			{
        				if(mode != NOTHING && mode != INSERT)return;
        				if(i.getText() == "INSERT"){
        					mode = INSERT;
        					i.setText("OK!");
        					newtableRow = NETR(tblayout);
        				}else{
        					EditText ed1 = (EditText)newtableRow.getChildAt(0);
        					EditText ed2 = (EditText)newtableRow.getChildAt(1);
        					EditText ed3 = (EditText)newtableRow.getChildAt(2);
        					tblayout.removeViewAt(index++);
        					AddTR(tblayout,ed1,ed2,ed3);
        					i.setText("INSERT");
        					mode = NOTHING;
        				}
        			}
        		}
        );
		m.setOnClickListener
        (
        		new View.OnClickListener()
        		{
        			public void onClick(View v)
        			{
        				if(mode != NOTHING && mode != MODIFY)return;
        				if(m.getText() == "MODIFY"){
        					if(select > 0 && select < tblayout.getChildCount()){
        						mode = MODIFY;
        						m.setText("OK!");
        						ms = METR(tblayout,select);
        					}
        				}else{
        					MDFTR(tblayout,ms);
        					m.setText("MODIFY");
        					mode = NOTHING;
        				}
        			}
        		}
        );
		d.setOnClickListener
        (
        		new View.OnClickListener()
        		{
        			public void onClick(View v)
        			{
        				if(mode != NOTHING)return;
        				if(select > 0 && select < tblayout.getChildCount()){
        					int i,j,k;
        					for(i = 1;i < select;i++);
        					TableRow[] tl = new TableRow[index - i - 1];
        					for(j = index - 1,k = 0;j > i;j--,k++){
        						tl[k] = (TableRow)tblayout.getChildAt(j);
        						tl[k].setId(j - 1);
        						tblayout.removeViewAt(j);
        					}
        					tblayout.removeViewAt(j);
        					for(k--;k >= 0;k--){
        						tblayout.addView(tl[k]);
        					}
        					index--;
        				}
        			}
        		}
        );
    }
    public TableRow NETR(TableLayout tl){
    	TableRow newtableRow2 = new TableRow(this);
    	EditText e1 = new EditText(this);
    	EditText e2 = new EditText(this);
    	EditText e3 = new EditText(this);
    	newtableRow2.addView(e1);
    	newtableRow2.addView(e2);
    	newtableRow2.addView(e3);
    	tl.addView(newtableRow2,new TableLayout.LayoutParams(FP,WC));
    	return newtableRow2;
    }
    public void AddTR(TableLayout tl,EditText e1,EditText e2,EditText e3){
    	TableRow newtableRow2 = new TableRow(this);
    	TextView t1 = new TextView(this);
    	newtableRow2.setId(index - 1);
    	newtableRow2.setOnClickListener
        (
        		new View.OnClickListener()
        		{
        			public void onClick(View v)
        			{
        				if(select > 0 && select < tblayout.getChildCount()){
	        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(0)).setTextColor(Color.WHITE);
	        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(1)).setTextColor(Color.WHITE);
	        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(2)).setTextColor(Color.WHITE);
        				}
        				select = v.getId();
        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(0)).setTextColor(Color.BLUE);
        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(1)).setTextColor(Color.BLUE);
        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(2)).setTextColor(Color.BLUE);
        			}
        		}
        );
        TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
        t1.setText(e1.getText());
        t1.setTextColor(Color.WHITE);
        t2.setText(e2.getText());
        t2.setTextColor(Color.WHITE);
        t3.setText(e3.getText());
        t3.setTextColor(Color.WHITE);
        newtableRow2.addView(t1);
        newtableRow2.addView(t2);
        newtableRow2.addView(t3);
        tl.addView(newtableRow2,new TableLayout.LayoutParams(FP,WC));
    }
    public int METR(TableLayout tl,int ss){
    	EditText e1 = new EditText(this);
    	EditText e2 = new EditText(this);
    	EditText e3 = new EditText(this);
    	e1.setText(((TextView)((TableRow)tl.getChildAt(ss)).getChildAt(0)).getText());
    	e2.setText(((TextView)((TableRow)tl.getChildAt(ss)).getChildAt(1)).getText());
    	e3.setText(((TextView)((TableRow)tl.getChildAt(ss)).getChildAt(2)).getText());
    	((TableRow)tl.getChildAt(ss)).removeAllViews();
    	((TableRow)tl.getChildAt(ss)).addView(e1);
    	((TableRow)tl.getChildAt(ss)).addView(e2);
    	((TableRow)tl.getChildAt(ss)).addView(e3);
    	return ss;
    }
    public void MDFTR(TableLayout tl,int ss){
    	TableRow newtableRow2 = new TableRow(this);
    	newtableRow2.setId(ss);
    	newtableRow2.setOnClickListener
        (
        		new View.OnClickListener()
        		{
        			public void onClick(View v)
        			{
        				if(select > 0 && select < tblayout.getChildCount()){
	        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(0)).setTextColor(Color.WHITE);
	        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(1)).setTextColor(Color.WHITE);
	        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(2)).setTextColor(Color.WHITE);
        				}
        				select = v.getId();
        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(0)).setTextColor(Color.BLUE);
        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(1)).setTextColor(Color.BLUE);
        				((TextView)((TableRow)tblayout.getChildAt(select)).getChildAt(2)).setTextColor(Color.BLUE);
        			}
        		}
        );
    	TextView t1 = new TextView(this);
    	TextView t2 = new TextView(this);
        TextView t3 = new TextView(this);
        t1.setText(((EditText)((TableRow)tl.getChildAt(ss)).getChildAt(0)).getText());
        t1.setTextColor(Color.WHITE);
        t2.setText(((EditText)((TableRow)tl.getChildAt(ss)).getChildAt(1)).getText());
        t2.setTextColor(Color.WHITE);
        t3.setText(((EditText)((TableRow)tl.getChildAt(ss)).getChildAt(2)).getText());
        t3.setTextColor(Color.WHITE);
        ((TableRow)tl.getChildAt(ss)).removeAllViews();
        ((TableRow)tl.getChildAt(ss)).addView(t1);
        ((TableRow)tl.getChildAt(ss)).addView(t2);
        ((TableRow)tl.getChildAt(ss)).addView(t3);
    }
}