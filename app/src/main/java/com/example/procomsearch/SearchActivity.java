package com.example.procomsearch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procomsearch.dataFrame.ArrayListIntent;
import com.example.procomsearch.dataFrame.Company_Index;
import com.example.procomsearch.history.HistoryUtil;

import java.util.ArrayList;

/**
 * @author Chaofan Li
 */
public class SearchActivity extends AppCompatActivity {

    private int userId=0;
    private String inputSearch;
    private ArrayListIntent AI;
    String rankAttribute;

    static CompanyFactory companies = new CompanyFactory();
    static ArrayList<String> rankAttr = new ArrayList<>();
    static ArrayList<Double> proPowerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AI=(ArrayListIntent)getIntent().getSerializableExtra("search rst");
        rankAttribute = getIntent().getStringExtra("rank attributes");

        userId=AI.getuserid();
        inputSearch=AI.getInputSearch();
        int size = 0;
        size = AI.getList().size();

        //get the input result from search fragment
        Toast.makeText(SearchActivity.this, "Found "+ size + " Companies ", Toast.LENGTH_SHORT).show();

        initCompanies(AI);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CompanyAdapter adapter = new CompanyAdapter(companies, rankAttr, proPowerList, userId);
        System.out.println("onCreate");
        recyclerView.setAdapter(adapter);
    }


    private void initCompanies(ArrayListIntent AI) {
        companies.clear();
        proPowerList.clear();
        rankAttr.clear();
        ArrayList<Company_Index> rst = AI.getList();

        for (Company_Index c:rst
             ) {
            rankAttr.add(rankAttribute+" = "+c.Key);
            companies.add(new Company(c.Name,c.getCode(),null,c.getPromotedPower()));
            proPowerList.add(c.getPromotedPower());
        }
    }

}