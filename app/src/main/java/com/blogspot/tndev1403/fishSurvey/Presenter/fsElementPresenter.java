package com.blogspot.tndev1403.fishSurvey.Presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.blogspot.tndev1403.fishSurvey.Model.Config.ApplicationConfig;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsCategorize;
import com.blogspot.tndev1403.fishSurvey.Model.Entity.fsElement;
import com.blogspot.tndev1403.fishSurvey.Presenter.fsAdapter.fsElementAdapter;
import com.blogspot.tndev1403.fishSurvey.R;
import com.blogspot.tndev1403.fishSurvey.View.fsCatchedInputActivity;
import com.blogspot.tndev1403.fishSurvey.View.fsElementActivity;

import java.util.ArrayList;

public class fsElementPresenter {
    public static fsElement CURRENT_SELECTED_ELEMENT;
    fsElementActivity mContext;
    ArrayList<fsElement> elements;
    fsElementAdapter adapter;
    public int CATEGORIZE_ID = -1;
    public String CATEGORIZE_NAME = "";

    public fsElementPresenter(fsElementActivity mContext) {
        this.mContext = mContext;
        initArguments();
        initGridView();
    }

    private void initArguments() {
        Intent currentIntent = mContext.getIntent();
        CATEGORIZE_ID = currentIntent.getIntExtra(ApplicationConfig.CategorizeAPI.ID, -1);
        CATEGORIZE_NAME = currentIntent.getStringExtra(ApplicationConfig.CategorizeAPI.Name);
    }

    private void initGridView() {
        elements = fsElement.getFromAPI(""); // Empty for test
        adapter = new fsElementAdapter(mContext, elements);
        mContext.gridView.setAdapter(adapter);
        gridViewItemClickedEvent();
    }

    private void gridViewItemClickedEvent() {
        mContext.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fsElement element = elements.get(position);
                CURRENT_SELECTED_ELEMENT = element;
                Intent catchedIntent = new Intent(mContext, fsCatchedInputActivity.class);
                mContext.startActivity(catchedIntent);
            }
        });
        mContext.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View full_preview = layoutInflater.inflate(R.layout.dialog_preview, null);
                builder.setView(full_preview);
                ImageView imageView = (ImageView) full_preview.findViewById(R.id.dialog_full_preview);
                ImageView imageView1 = (ImageView) full_preview.findViewById(R.id.dialog_close_preview);


                final AlertDialog Ok = builder.create();
                Ok.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Ok.show();

                fsElement element = elements.get(position);
                if (element.getFeatureImage()!=null)
                    imageView.setImageBitmap(element.getFeatureImage());
                else
                    imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_404));

                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ok.cancel();
                    }
                });
                return true;
            }
        });
    }
}