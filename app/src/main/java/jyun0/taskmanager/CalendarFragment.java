package jyun0.taskmanager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jyun0.taskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    private ScrollView scroll;
    private EditText edit;
    private SwipeMenuListView listView;
    private Button addButton;

    private int Year,Month,Day;
    private TextView text;

    private Item item;
    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserId = mFirebaseUser.getUid();

        Date today = new Date();
        final Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR,1);

        final CalendarPickerView datePicker = (CalendarPickerView)view.findViewById(R.id.calendar_grid);
        datePicker.init(today,calendar.getTime()).withSelectedDate(today);
        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
//
//                final String TodoDate = calendar.get(Calendar.DAY_OF_MONTH) + "/"+  calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
                final View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_calendar,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                scroll = mView.findViewById(R.id.scroll);
                edit = (EditText) mView.findViewById(R.id.edittext);
                addButton = (Button) mView.findViewById(R.id.add_button);
                //set up listView
                final SwipeMenuListView listView = (SwipeMenuListView) mView.findViewById(R.id.list);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

//                Item item = new Item();
                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        // set item width
                        deleteItem.setWidth(170);
                        // set a icon
                        deleteItem.setIcon(R.drawable.ic_delete);
                        // add to menu
                        menu.addMenuItem(deleteItem);
                    }
                };

// set creator
                listView.setMenuCreator(creator);
                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        switch (index){
                            case 0:
                                mDatabase.child("users").child(mUserId).child("items")
                                .orderByChild("title")
                                .equalTo((String)listView.getItemAtPosition(position))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChildren()){
                                            DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                            firstChild.getRef().removeValue();
                                            adapter.remove((String)listView.getItemAtPosition(position));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                break;
                        }
                        return false;
                    }
                });

//                Calendar caledar = Calendar.getInstance();
//                Year = calendar.get(Calendar.YEAR);
//                Month =  calendar.get(Calendar.MONTH);
//                Day =  calendar.get(Calendar.DAY_OF_MONTH);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String listItem = edit.getText().toString();
                        mDatabase.child("users").child(mUserId).child("items").push().child("title").setValue(listItem);
//                        String dateString = Year + "/" + String.format("%02d",(Month + 1)) + String.format("%02d", Day);
//                        text.setText(dateString);
                        adapter.add(listItem);
                        listView.getAdapter();

                     }
                });

                // Use Firebase to populate the list
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String string = (String) dataSnapshot.child("title").getValue();
                        arrayList.add(string);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        String string = (String) dataSnapshot.child("title").getValue();
                        arrayList.remove(string);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                builder = builder.setView(mView);
                builder.setTitle("To Do List");
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        return view;
    }

}
