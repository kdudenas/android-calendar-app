package cs407_android.com.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EventPageActivity extends AppCompatActivity {
    TextView titleView;
    TextView dateView;
    TextView timeView;
    TextView descView;
    Button backBtn;
    Button deleteBtn;
    Long eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        //instantiate widgets
        titleView = (TextView)findViewById(R.id.Title);
        dateView = (TextView)findViewById(R.id.dateView);
        timeView = (TextView)findViewById(R.id.timeView);
        descView = (TextView)findViewById(R.id.descView);

        backBtn = (Button) findViewById(R.id.backBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getStringArray("displayStrings")!= null)
        {
            String[] strs = bundle.getStringArray("displayStrings");

            titleView.setText(strs[0]);
            dateView.setText(strs[1]);
            timeView.setText(strs[2]);
            descView.setText(strs[3]);
        }
        if(bundle.getStringArray("displayStrings")!= null){
            eventId = bundle.getLong("eventId");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtnClicked();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBtnClicked();
            }
        });
    }


    public void backBtnClicked(){
        this.finish();
    }

    public void deleteBtnClicked(){
        System.out.println("!!Delete button clicked: id = " + eventId);
        Intent intent = new Intent();
        intent.putExtra("eventToBeDeletedId", eventId);
        setResult(RESULT_OK, intent);
        this.finish();
    }

}
