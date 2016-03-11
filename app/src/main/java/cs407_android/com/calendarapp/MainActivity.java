package cs407_android.com.calendarapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
      implements OnDayPageOpenedListener
{
    static final int ADD_EVENT_REQUEST = 1;  // The request code

    //ORM variables;
    DaoMaster.DevOpenHelper calendarDBHelper;
    SQLiteDatabase calendarDB;
    DaoMaster daoMaster;
    DaoSession daoSession;
    EventDao guestDao;
    List<Event> eventListFromDB;
    private ArrayAdapter<Event> eventListAdapter;
    public static ArrayList<Event> eventList;
    //List<Event> dayEventListFromDB;
    //ArrayAdapter dayEventListAdapter;

    public ArrayAdapter getEventListAdapter(){
        if (eventListAdapter == null){
            return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventList);
        }
        else return eventListAdapter;
    }

    //Date variables
    static Calendar calendar = Calendar.getInstance();
    static int currMonth = calendar.get(Calendar.MONTH);
    static int currDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    static int currDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    static HashMap<Integer, String> months = new HashMap<Integer, String>();
    static HashMap<Integer, String> weekdays = new HashMap<Integer, String>();

    //Tab variables
    private SectionsPagerAdapter mSectionsPagerAdapter; /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ViewPager mViewPager; /**
     * The {@link ViewPager} that will host the section contents.
     */
    Context context;

   //helper methods
    static public String getDayOfWeekStr(){
        return weekdays.get(currDayOfWeek);
    }
    static public String getMonthStr(){
        return months.get(currMonth);
    }
    public void setup() {
        months.put(Calendar.JANUARY, "January"); //KAD TODO make string values
        months.put(Calendar.FEBRUARY, "February");
        months.put(Calendar.MARCH, "March");
        months.put(Calendar.APRIL, "April");
        months.put(Calendar.MAY, "May");
        months.put(Calendar.JUNE, "June");
        months.put(Calendar.JULY, "July");
        months.put(Calendar.AUGUST, "August");
        months.put(Calendar.SEPTEMBER, "September");
        months.put(Calendar.OCTOBER, "October");
        months.put(Calendar.NOVEMBER, "November");
        months.put(Calendar.DECEMBER, "December");

        weekdays.put(Calendar.SUNDAY, "Sunday");
        weekdays.put(Calendar.MONDAY, "Monday");
        weekdays.put(Calendar.TUESDAY, "Tuesday");
        weekdays.put(Calendar.WEDNESDAY, "Wednesday");
        weekdays.put(Calendar.THURSDAY, "Thursday");
        weekdays.put(Calendar.FRIDAY, "Friday");
        weekdays.put(Calendar.SATURDAY, "Saturday");

        //  System.out.println("Today is " + weekdays.get(currDayOfWeek) + "Month: " + months.get(currMonth) + "Day: " + currDayOfMonth );

        //add activity
        Random rng = new Random();
        //int month = rng.nextInt(27) + 1;
        int month = Calendar.MARCH;
        int day = rng.nextInt(28) + 1;
        int hour = rng.nextInt(24) + 1;
        int minute = rng.nextInt(60) + 1;
        saveEvent("Lunch", month, day, hour, minute, "Eat something healthy today!");


    }
    static public void updateCalendar(int numDaysToAdd){
        calendar.add(Calendar.DAY_OF_MONTH, numDaysToAdd);
        currDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        currDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        currMonth = calendar.get(Calendar.MONTH);
        //reset calendar
        calendar.add(Calendar.DAY_OF_MONTH, -numDaysToAdd);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instantiate objects
        context = this;
        eventList= new ArrayList<>();
        eventListAdapter = getEventListAdapter();

        initDatabase(); //setup
        setup();
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each section of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up "add event" floating button
        FloatingActionButton addEventButton = (FloatingActionButton) findViewById(R.id.addItemBtn);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              addActivityBtnPressed();
            }
        });
        eventListAdapter.notifyDataSetChanged();
    }

    public void addActivityBtnPressed(){
        Intent addEventIntent = new Intent(this, AddEventActivity.class);
        startActivityForResult(addEventIntent, ADD_EVENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_EVENT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user added an event.
                String[] fields = data.getStringArrayExtra("eventFieldsList");

                saveEvent(fields[0],Integer.valueOf(fields[1]),
                        Integer.valueOf(fields[2]), Integer.valueOf(fields[3]),
                        Integer.valueOf(fields[4]), fields[5]); //KAD TODO idk

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //KAD top action bar: month day
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem month = menu.findItem(R.id.action_month);
        month.setTitle(getMonthStr());
        MenuItem weekday = menu.findItem(R.id.action_dayOfWeek);
        weekday.setTitle(getDayOfWeekStr());
        MenuItem dayOfMonth = menu.findItem(R.id.action_dayOfMonth);
        dayOfMonth.setTitle("" + currDayOfMonth);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_month) {
            //Display drop-down menu with months KAD TODO
            return true;
        }
        else if (id == R.id.action_dayOfMonth) {
            //Display drop-down menu with appropriate number of days KAD TODO
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    /*Database helper methods*/
    private void initDatabase() {
        calendarDBHelper = new DaoMaster.DevOpenHelper(this, "ORM.sqlite", null);
        calendarDB = calendarDBHelper.getWritableDatabase();

        //Get DaoMaster
        daoMaster = new DaoMaster(calendarDB);

        //Create database and tables if non existent
        daoMaster.createAllTables(calendarDB, true);

        //Create DaoSession instance
        daoSession = daoMaster.newSession();

        //From DaoSession instance, get instance of GuestDao
        guestDao = daoSession.getEventDao();

        if (guestDao.queryBuilder().where(
                EventDao.Properties.Display.eq(true)).list() == null)
        {
            //If list is null, then database tables were created for first time in
            // previous lines, so call "closeReopenDatabase()"
            closeReopenDatabase();
        }
        //Get list of Guest objects in database using QueryBuilder
        //HINT: All instances of Guest objects will have their Display property set equal to true
        eventListFromDB = guestDao.queryBuilder().where(
                EventDao.Properties.Display.eq(true)).list();

        //Add all Guest objects from List to guestList ArrayList and use
        //(cont.) "adapter.notifyDataSetChanged()" to update list
        if (eventListFromDB != null) {

            for (Event event : eventListFromDB)
            {
                if (event == null)
                {
                    return;
                }
                Toast.makeText(context, "Added Guests from Database", Toast.LENGTH_SHORT).show();
                eventList.add(event);
            }
            eventListAdapter.notifyDataSetChanged();
        }


    }

    public void saveEvent(String title, Integer month, Integer dayOfMonth, Integer startHour,
                          Integer startMinute, String description){
        //Generate random Id for Event object to place in database
        Random rand = new Random();

        //Create Event instance using provided data
        Event newEvent = new Event(rand.nextLong(), title, month, dayOfMonth, startHour, startMinute, description, true);

        //Insert Event instance into guestDao
        guestDao.insert(newEvent);

        //Close and reopen database to ensure Guest object is saved
        closeReopenDatabase();
    }

    private void closeDatabase() {
        daoSession.clear();
        calendarDB.close();
        calendarDBHelper.close();
    }

    private void closeReopenDatabase() {
        closeDatabase();

        calendarDBHelper = new DaoMaster.DevOpenHelper(this, "ORM.sqlite", null);
        calendarDB = calendarDBHelper.getWritableDatabase();

        //Get DaoMaster
        daoMaster = new DaoMaster(calendarDB);

        //Create database and tables
        daoMaster.createAllTables(calendarDB, true);

        //Create DaoSession
        daoSession = daoMaster.newSession();

        //Create customer addition/removal instances
        guestDao = daoSession.getEventDao();

    }

    public void getEventsByDay(){
//        listView.setAdapter(dudeAdapter);
//        //Get list of Guest objects in database using QueryBuilder
//        //HINT: All instances of Guest objects will have their Display property set equal to true
//        dudeListFromDB = EventDao.queryBuilder().where(
//                EventDao.Properties.DayOfMonth.eq(currDayOfMonth)).list();
//
//        //Add all Guest objects from List to guestList ArrayList and use
//        //(cont.) "adapter.notifyDataSetChanged()" to update list
//        if (dudeListFromDB != null) {
//
//            for (Guest guest : dudeListFromDB)
//            {
//                if (guest == null)
//                {
//                    return;
//                }
//                Toast.makeText(context, "Added Dudes from Database", Toast.LENGTH_SHORT).show();
//                dudeList.add(guest.getFirstName() + " " + guest.getLastName());
//            }
//            dudeAdapter.notifyDataSetChanged();
//        }
    }


    @Override
    public ArrayAdapter OnListViewed() {
        //adapter = eventListAdapter;
        return eventListAdapter;
        //lv.setAdapter(eventListAdapter);
        //System.out.println("hooray");
    }


////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int offset) {
            // getItem is called to instantiate the fragment for the given page - the next day
            // Return a DayFragment
            return DayViewFragment.newInstance(offset + 1);
        }

        @Override
        public int getCount() {
            // Show 365 total pages - one year. TODO idk
            return 365;
        }

        @Override
        public CharSequence getPageTitle(int position) { //TODO prolly can use this
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * A fragment that displays a list of one day's events.
     */
    public static class DayViewFragment extends Fragment {

        OnDayPageOpenedListener mCallback;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            /*This makes sure that the container activity has implemented the callback interface. If not, it throws an exception */
            try {
                mCallback = (OnDayPageOpenedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnDayPageOpenedListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mCallback = null;
        }

         /**
         * Each fragment has an offset from the current day's Month and Date
         */
        private static final String ARG_SECTION_NUMBER = "section number";
        //KAD try
        ArrayAdapter tryAdapter;
        private static final String ARG_LIST_ADAPTER = "list adapter";
        //
        private int section_number;

        public DayViewFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DayViewFragment newInstance(int sectionNumber) {
            DayViewFragment fragment = new DayViewFragment();
            Bundle args = new Bundle();
//            args.putInt(ARG_CURR_MONTH, month);
//            args.putInt(ARG_CURR_DAY_OF_MONTH, dayOfMonth);
//            args.putInt(ARG_CURR_DAY_OF_WEEK, dayOfWeek);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            args.putSerializable(ARG_LIST_ADAPTER, adapter);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

                  if (getArguments() != null) {
                section_number = getArguments().getInt(ARG_SECTION_NUMBER);
                //tryAdapter = getArguments().get
            }
        }


//    public ArrayAdapter getTryArrayAdapter() {
//        return (ArrayAdapter) getArguments().getSerializable(FRAGMENT_BUNDLE_KEY);
//    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            MainActivity act = (MainActivity)getActivity().getParent();
            //update date:
            Log.println(1, "*", "section number is: " + section_number); //TODO remove
            act.updateCalendar(section_number);


            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("Events for " + getDayOfWeekStr() + ", " + getMonthStr() + " " + currDayOfMonth);

            ListView eventListView = (ListView) rootView.findViewById(R.id.eventListView);
            //KAD ahhhhhhhhhhhhhhhhh
            tryAdapter = mCallback.OnListViewed();

//KAD ahhhhhhhhhhh
            eventListView.setAdapter(tryAdapter);
            tryAdapter.notifyDataSetChanged();




            return rootView;
        }
    }





}
