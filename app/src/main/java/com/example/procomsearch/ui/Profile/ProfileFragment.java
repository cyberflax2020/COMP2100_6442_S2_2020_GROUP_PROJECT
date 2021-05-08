package com.example.procomsearch.ui.Profile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procomsearch.CompanyAdapter;
import com.example.procomsearch.CompanyFactory;
import com.example.procomsearch.R;
import com.example.procomsearch.SelectActivity;
import com.example.procomsearch.history.HistoryUtil;

/**
 * @author Chaofan Li
 */
public class ProfileFragment extends Fragment {
    private static int userId;
    private static CompanyFactory historyCompany = new CompanyFactory();
    private static SQLiteDatabase db;
    private static TextView emptyText;
    TextView userNameView, userIdView,historyView;
    Button clearHistory;
    RecyclerView recyclerView;
    CompanyAdapter adapter;
    ImageView profilePhoto;

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        SelectActivity activity = (SelectActivity)getActivity();

        if (userId>0) {
            HistoryUtil.writeToDatabase(userId);
        }

        userNameView = (TextView)root.findViewById(R.id.user_name);
        userIdView = (TextView)root.findViewById(R.id.user_id);
        emptyText = (TextView)root.findViewById(R.id.empty_text2);
        historyView = (TextView)root.findViewById(R.id.history_text);
        clearHistory = (Button)root.findViewById(R.id.clear_history);
        profilePhoto = (ImageView) root.findViewById(R.id.user_image);
        //get database
        db = SQLiteDatabase.openOrCreateDatabase("data/data/"+activity.getPackageName()+"/databases/"+getString(R.string.database_name),null);
        //get userId
        userId = activity.getuserid();
        if(userId<=0){
            emptyText.setText("You need to login to see the profile.");
            emptyText.setVisibility(View.VISIBLE);
            userNameView.setVisibility(View.INVISIBLE);
            userIdView.setVisibility(View.INVISIBLE);
            clearHistory.setVisibility(View.INVISIBLE);
            historyView.setVisibility(View.INVISIBLE);
            profilePhoto.setVisibility(View.INVISIBLE);
        }
        else {
            //set userName and userId
            userNameView.setText(getUserName(userId));
            userIdView.setText("UID: "+userId+"");

            recyclerView = (RecyclerView) root.findViewById(R.id.history_list);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());

            //read history from database
            HistoryUtil.readFromDatabase(userId);
            adapter = new CompanyAdapter(HistoryUtil.getHistory(),null,null, userId);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
            if (HistoryUtil.getHistory().size()==0){
                recyclerView.setVisibility(View.INVISIBLE);
                emptyText.setText("Your history is empty.");
                emptyText.setVisibility(View.VISIBLE);
            }
        }
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                emptyText.setText("Your history is empty.");
                emptyText.setVisibility(View.VISIBLE);
                HistoryUtil.clearHistory(userId,"History");
                recyclerView.setAdapter(adapter);
            }
        });
        return root;
    }

    private String getUserName(int userId){
        String userName = null;
        Cursor cursor = db.rawQuery("select name from Users where id = ?", new String[]{userId+""});
        if (cursor.moveToFirst()){
            userName = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        return userName;
    }


}