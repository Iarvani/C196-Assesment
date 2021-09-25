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

import com.c196project.InstructorDetailsActivity;
import com.c196project.models.Instructor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.c196project.InstructorEditActivity;
import com.c196project.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.c196project.utilities.Constants.INSTRUCTOR_ID_KEY;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {

    private final List<Instructor> instructors;
    private final Context context;
    private final RecyclerContext recyclerContext;
    private InstructorSelectedListener instructorSelectedListener;

    public InstructorAdapter(List<Instructor> instructors, Context context, RecyclerContext recyclerContext, InstructorSelectedListener instructorSelectedListener) {
        this.instructors = instructors;
        this.context = context;
        this.recyclerContext = recyclerContext;
        this.instructorSelectedListener = instructorSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.instructor_list_cardview, parent, false);
        return new ViewHolder(view, instructorSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAdapter.ViewHolder holder, int position) {
        final Instructor instructor = instructors.get(position);
        holder.tvName.setText(instructor.getName());
        holder.tvPhone.setText(instructor.getPhone());
        holder.tvEmail.setText(instructor.getEmail());

        switch(recyclerContext) {
            case MAIN:
                holder.instructorFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit));
                holder.instructorImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(context, InstructorDetailsActivity.class);
                    intent.putExtra(INSTRUCTOR_ID_KEY, instructor.getId());
                    context.startActivity(intent);
                });

                holder.instructorFab.setOnClickListener(v -> {
                    Intent intent = new Intent(context, InstructorEditActivity.class);
                    intent.putExtra(INSTRUCTOR_ID_KEY, instructor.getId());
                    context.startActivity(intent);
                });
                break;
            case CHILD:
                holder.instructorFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete));
                holder.instructorFab.setOnClickListener(v -> {
                    if(instructorSelectedListener != null) {
                        instructorSelectedListener.onInstructorSelected(position, instructor);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return instructors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_instructor_name)
        TextView tvName;
        @BindView(R.id.card_instructor_fab)
        FloatingActionButton instructorFab;
        @BindView(R.id.card_instructor_email)
        TextView tvEmail;
        @BindView(R.id.card_instructor_phone)
        TextView tvPhone;
        @BindView(R.id.btn_instructor_details)
        ImageButton instructorImageBtn;
        InstructorSelectedListener instructorSelectedListener;

        public ViewHolder(View itemView, InstructorSelectedListener instructorSelectedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.instructorSelectedListener = instructorSelectedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            instructorSelectedListener.onInstructorSelected(getAdapterPosition(), instructors.get(getAdapterPosition()));
        }
    }

    public interface InstructorSelectedListener {
        void onInstructorSelected(int position, Instructor instructor);
    }
}
