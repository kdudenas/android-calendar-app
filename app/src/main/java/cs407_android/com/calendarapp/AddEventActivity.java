package cs407_android.com.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEventActivity extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText date;
    EditText time;
    String titleStr;
    String descriptionStr;
    String monthStr;
    String dayStr;
    String hourStr;
    String minStr;
    Button submitEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //instantiate widgets
        title = (EditText) findViewById(R.id.eventTitleField);
        description = (EditText) findViewById(R.id.eventDescriptionField);
        date = (EditText) findViewById(R.id.eventDateField);
        time = (EditText) findViewById(R.id.eventTimeField);
        submitEventBtn = (Button) findViewById(R.id.submitEventBtn);
        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEventBtnPressed();
            }
        });

    }

    public void submitEventBtnPressed(){
        //Get strings
        titleStr = title.getText().toString();
        descriptionStr = description.getText().toString();
        String dateStr = date.getText().toString();
        monthStr = dateStr.substring(0, 2);
        dayStr = dateStr.substring(3, 5);
        String timeVal = time.getText().toString();
        hourStr = timeVal.substring(0, 2);
        minStr = timeVal.substring(3, 5);

        String[] eventFields = {titleStr, monthStr, dayStr, hourStr, minStr,
                descriptionStr};

        Intent intent = new Intent();
        intent.putExtra("eventFieldsList", eventFields);
        setResult(RESULT_OK, intent);

        this.finish();
    }

}
