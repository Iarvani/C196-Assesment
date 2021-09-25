package com.c196project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.models.Term;
import com.c196project.guis.RecyclerContext;
import com.c196project.guis.TermAdapter;
import com.c196project.viewmodels.TermViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermActivity extends AppCompatActivity {

    @BindView(R.id.term_recycler_view)
    RecyclerView mTermRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, TermEditActivity.class);
        startActivity(intent);
    }

    private List<Term> termData = new ArrayList<>();
    private TermAdapter termAdapter;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        mTermRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTermRecyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Term>> termObserver =
                termEntities -> {
                    termData.clear();
                    termData.addAll(termEntities);

                    if(termAdapter == null) {
                        termAdapter = new TermAdapter(termData, TermActivity.this, RecyclerContext.MAIN);
                        mTermRecyclerView.setAdapter(termAdapter);
                    } else {
                        termAdapter.notifyDataSetChanged();
                    }
                };
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.terms.observe(this, termObserver);
    }
}
