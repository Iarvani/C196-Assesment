package com.c196project.guis;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.c196project.R;
import com.c196project.TermDetailsActivity;
import com.c196project.TermEditActivity;
import com.c196project.models.Term;
import com.c196project.utilities.TextFormats;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.c196project.utilities.Constants.TERM_ID_KEY;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {

    private final List<Term> terms;
    private final Context context;
    private final RecyclerContext recyclerContext;

    public TermAdapter(List<Term> terms, Context context, RecyclerContext recyclerContext) {
        this.terms = terms;
        this.context = context;
        this.recyclerContext = recyclerContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term_list_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder holder, int position) {
        final Term term = terms.get(position);
        holder.tvTitle.setText(term.getTitle());
        String startAndEnd = TextFormats.cardDateFormat.format(term.getStartDate()) + " to " + TextFormats.cardDateFormat.format(term.getEndDate());
        holder.tvDates.setText(startAndEnd);

        switch(recyclerContext) {
            case MAIN:
                holder.termFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit));
                holder.termImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(context, TermDetailsActivity.class);
                    intent.putExtra(TERM_ID_KEY, term.getId());
                    context.startActivity(intent);
                });

                holder.termFab.setOnClickListener(v -> {
                    Intent intent = new Intent(context, TermEditActivity.class);
                    intent.putExtra(TERM_ID_KEY, term.getId());
                    context.startActivity(intent);
                });
                break;
            case CHILD:
                holder.termFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_term_title)
        TextView tvTitle;
        @BindView(R.id.card_term_fab)
        FloatingActionButton termFab;
        @BindView(R.id.card_term_dates)
        TextView tvDates;
        @BindView(R.id.btn_term_details)
        ImageButton termImageBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
