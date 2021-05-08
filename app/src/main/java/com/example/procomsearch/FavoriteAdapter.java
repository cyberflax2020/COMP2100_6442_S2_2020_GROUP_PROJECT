package com.example.procomsearch;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import static com.example.procomsearch.FavoriteActivity.addFavorite;
//import static com.example.procomsearch.FavoriteActivity.removeFavorite;
import static com.example.procomsearch.ui.Favorite.FavoriteFragment.removeFavorite;

/**
 * @author Chaofan Li
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private CompanyFactory favoriteCompanies;
    private int userId;


    public FavoriteAdapter(CompanyFactory companies, int userId) {
        this.favoriteCompanies = companies;
        this.userId = userId;

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
                Company company = favoriteCompanies.get(position);
                //FavoriteFragment.printCompanies();
                Intent intent = new Intent(v.getContext(),CompanyPageActivity.class);
                intent.putExtra("user id",userId);
                intent.putExtra("companyName",company.getName());
                intent.putExtra("stockNo",company.getStockNo());
                intent.putExtra("imageId",company.getImage_id());
                v.getContext().startActivity(intent);
            }
        });

        holder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Company company = favoriteCompanies.get(position);
                removeFavorite(company);
                removeFavoriteFromDatabase(company,userId);
                notifyDataSetChanged();
                notifyItemRemoved(position);
            }
        });

        return holder;
    }

    private void removeFavoriteFromDatabase(Company company, int userId) {

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Company company = favoriteCompanies.get(position);
        holder.companyIcon.setImageResource(company.getImage_id());
        holder.companyName.setText(company.getName());
        holder.likeIcon.setImageResource(R.drawable.close);
    }

    @Override
    public int getItemCount() {
        return favoriteCompanies.count();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //companyView stores the whole item layout (parent layout of image and text)
        View companyView;
        ImageView companyIcon;
        TextView companyName;
        ImageView likeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyView = itemView;
            companyIcon = (ImageView) itemView.findViewById(R.id.company_icon);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            likeIcon = (ImageView) itemView.findViewById(R.id.promoted_icon);
        }
    }
}
