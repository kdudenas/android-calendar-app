package cs407_android.com.calendarapp;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Owner on 3/11/2016.
 */
// Container Activity must implement this interface
public interface OnDayPageOpenedListener {
    public ArrayAdapter OnListViewed();
    public void onActResult(int requestCode, int resultCode, Intent data);
    public void openEventPage(Event eventClicked);
}
