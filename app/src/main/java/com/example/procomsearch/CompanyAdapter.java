package com.example.procomsearch;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procomsearch.history.HistoryUtil;

import java.util.ArrayList;

/**
 * @author Chaofan Li
 */
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder>{

    private CompanyFactory companies;
    private ArrayList<String> companiesRank;
    private ArrayList<Double> proPowerList;
    private int userId;
    String rankAttr = "";


    public CompanyAdapter(CompanyFactory companies, ArrayList<String> companiesRank,ArrayList<Double> proPowerList, int userId) {
        this.companies = companies;
        this.userId = userId;
        this.companiesRank =  companiesRank;
        this.proPowerList = proPowerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.companyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Company company = companies.get(position);
                HistoryUtil.addHistory(company);
                Intent intent = new Intent(v.getContext(),CompanyPageActivity.class);
                intent.putExtra("user id",userId);
                intent.putExtra("companyName",company.getName());
                intent.putExtra("stockNo",company.getStockNo());
                intent.putExtra("imageId",company.getImage_id());
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Company company = companies.get(position);
        if (companiesRank!=null) {
            rankAttr = companiesRank.get(position);
        }
        holder.promoted.setImageResource(R.drawable.fire);
        holder.promoted.setVisibility(View.INVISIBLE);
        holder.companyIcon.setImageResource(company.getImage_id());
        holder.companyName.setText(company.getName());
        holder.company_stockNo.setText(company.getStockNo());
        holder.companyRankAttr.setText(rankAttr);
        if (company.getPromoted()>0.0){
            holder.promoted.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return companies.count();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //companyView stores the whole item layout (parent layout of image and text)
        View companyView;
        ImageView companyIcon;
        TextView companyName,companyRankAttr,company_stockNo;
        ImageView promoted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyView = itemView;
            companyIcon = (ImageView) itemView.findViewById(R.id.company_icon);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            companyRankAttr = (TextView) itemView.findViewById(R.id.company_rank_attr);
            promoted = (ImageView) itemView.findViewById(R.id.promoted_icon);
            company_stockNo = (TextView)itemView.findViewById(R.id.company_no);
        }
    }
}
