package cs407_android.com.calendarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddEventActivity extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText month;
    EditText day;
    EditText time;
    String titleStr;
    String descriptionStr;
    Integer monthVal;
    Integer dayVal;
    Integer hourVal;
    Integer minVal;
    Button submitEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //instantiate widgets
        title = (EditText) findViewById(R.id.eventTitleField);
        description = (EditText) findViewById(R.id.eventDescriptionField);
        month = (EditText) findViewById(R.id.eventMonthField);
        day = (EditText) findViewById(R.id.eventDayField);
        time = (EditText) findViewById(R.id.eventTimeField);

    }

public void submitEventBtnPressed(){
    //Get strings
    titleStr = title.getText().toString();
    descriptionStr = description.getText().toString();
    monthVal = Integer.valueOf(month.getText().toString());
    dayVal = Integer.valueOf(day.getText().toString());
    String timeVal = time.getText().toString();
    hourVal = Integer.valueOf(timeVal.substring(0,2));
    minVal = Integer.valueOf(timeVal.substring(2, 4));
    this.finish();
}

}
