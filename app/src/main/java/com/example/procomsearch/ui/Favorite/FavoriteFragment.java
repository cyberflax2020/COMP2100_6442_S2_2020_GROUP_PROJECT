package com.example.procomsearch.ui.Favorite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procomsearch.Company;
import com.example.procomsearch.CompanyFactory;
import com.example.procomsearch.FavoriteAdapter;
import com.example.procomsearch.R;
import com.example.procomsearch.SelectActivity;

/**
 * @author Chaofan Li
 */
public class FavoriteFragment extends Fragment {
    private static int userId;
    private static CompanyFactory favoriteCompanies = new CompanyFactory();
    private static SQLiteDatabase db;
    private static TextView emptyText;
    RecyclerView recyclerView;

    private FavoriteViewModel favoriteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoriteViewModel =
                ViewModelProviders.of(this).get(FavoriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);


        emptyText = (TextView) root.findViewById(R.id.favorite_empty_text);

        SelectActivity activity = (SelectActivity) getActivity();

        //get database:
        db = SQLiteDatabase.openOrCreateDatabase("data/data/" + activity.getPackageName() + "/databases/" + getString(R.string.database_name), null);

        userId = activity.getuserid();
        if (userId <= 0) {
            Toast.makeText(activity, "You didn't sign in", Toast.LENGTH_SHORT).show();
            emptyText.setText("You need to login to see the favorite list.");
            emptyText.setVisibility(View.VISIBLE);
        } else {

            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);
            iniFavoriteList();
            if (favoriteCompanies.size() == 0) {
                emptyText.setVisibility(View.VISIBLE);
            }
        }
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        iniFavoriteList();
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(favoriteCompanies, userId);
        if (userId <= 0) {
            return;
        } else {
            recyclerView.setAdapter(favoriteAdapter);
        }
    }

    private void iniFavoriteList() {
        favoriteCompanies.clear();
        Cursor cursor = db.rawQuery("select * from Favorite where userId = ?", new String[]{userId + ""});
        if (cursor.moveToFirst()) {
            do {
                String companyName = cursor.getString(cursor.getColumnIndex("companyName"));
                String stockNo = cursor.getString(cursor.getColumnIndex("stockNo"));
                favoriteCompanies.add(new Company(companyName, stockNo, null, 0.0));
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    public static void removeFavorite(Company company) {
        if (favoriteCompanies.size() == 1) {
            emptyText.setVisibility(View.VISIBLE);
        }
        favoriteCompanies.remove(company);

        String stockNo = company.getStockNo();

        db.execSQL("delete from Favorite where userId = ? and stockNo = ?", new String[]{
                userId + "", stockNo
        });

    }

}