package com.example.apparchilog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Document;
import com.example.apparchilog.views.NavigationActivity;
import com.example.apparchilog.views.PdfViewerActivity;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.List;


public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private final List<Document> mDocumentList;
    private final Context mContext;

    public DocumentAdapter(Context context, List<Document> items) {
        mDocumentList = items;
        mContext = context;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final DocumentViewHolder holder, int position) {
        Document document = mDocumentList.get(position);
        holder.mTextViewFileName.setText(document.getFileName());
        holder.mTvSize.setText(document.getUnits());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent = new Intent(mContext, PdfViewerActivity.class);
                intent.putExtra("document_uri", document.getFileUri());
                mContext.startActivity(intent);*/
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(document.getFileUri()), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDocumentList.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextViewFileName;
        public final TextView mTvSize;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewFileName = itemView.findViewById(R.id.textViewFileName);
            mTvSize = itemView.findViewById(R.id.tvSize);
        }
    }
}