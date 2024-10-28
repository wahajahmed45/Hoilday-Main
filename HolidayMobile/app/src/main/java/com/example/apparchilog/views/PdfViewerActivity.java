package com.example.apparchilog.views;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.apparchilog.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PdfViewerActivity extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView = findViewById(R.id.pdfView);
        if (getIntent() != null) {
            String fileUri = getIntent().getStringExtra("document_uri");
            if(fileUri != null) {
                Uri uri = Uri.parse(fileUri);
                pdfView.fromUri(uri);
            }
        }
    }
}