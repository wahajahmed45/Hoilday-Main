package com.example.apparchilog.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Document;
import com.example.apparchilog.viewModels.DocumentViewModel;
import com.example.apparchilog.views.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class CreateDocumentFragment extends Fragment {
    private static final int PICK_PDF_REQUEST = 1;

    private ImageButton buttonSelectPDF;
    private Button buttonUploadPDF;
    private TextView textViewFileName;
    private ProgressBar progressBar;
    private Uri pdfUri;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    DocumentViewModel mDocumentViewViewModel;
    private static final String ARG_VACATION_ID = "vacation_id";
    private static final String ARG_ACTIVITY_ID = "activity_id";
    private static final String ARG_IS_VACATION = "is_vacation";
    private Long activityId;
    private Long vacationId;
    private boolean isVacation;
    private Long fileSize;
    private String readableFileSize;

    public CreateDocumentFragment() {

    }

    public static CreateDocumentFragment newInstance(Long vacationId, Long activityI, boolean isVacation) {
        CreateDocumentFragment fragment = new CreateDocumentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VACATION_ID, vacationId);
        args.putLong(ARG_ACTIVITY_ID, activityI);
        args.putBoolean(ARG_IS_VACATION, isVacation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDocumentViewViewModel = new ViewModelProvider(this).get(DocumentViewModel.class);
        if (getArguments() != null) {
            vacationId = getArguments().getLong(ARG_VACATION_ID);
            activityId = getArguments().getLong(ARG_ACTIVITY_ID);
            isVacation = getArguments().getBoolean(ARG_IS_VACATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initIdView(view);
        initFirebase();
        onClickListener();
        observerViewModel();
    }

    private void observerViewModel() {
        mDocumentViewViewModel.getDocumentMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message.contains("success")) {
                    DocumentFragment documentFragment = DocumentFragment.newInstance(vacationId, activityId, isVacation);
                    ((HomeActivity) requireActivity()).replaceFragment(documentFragment, true, "Documents");
                    Toast.makeText(getActivity(), "Téléchargement réussir", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDocumentViewViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void initFirebase() {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference("messages");
    }

    private void onClickListener() {
        buttonSelectPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        buttonUploadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfUri != null) {
                    uploadPDF();
                } else {
                    Toast.makeText(getActivity(), "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });
    }

    private void initIdView(View view) {
        buttonSelectPDF = view.findViewById(R.id.ibSelectPDF);
        buttonUploadPDF = view.findViewById(R.id.buttonUploadPDF);
        textViewFileName = view.findViewById(R.id.textViewFileName);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void selectPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), PICK_PDF_REQUEST);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            String fileName = getFileName(pdfUri);
            ///  fileSize = ;
            readableFileSize = getReadableFileSize(getFileSize(pdfUri));
            textViewFileName.setText(fileName + " (" + readableFileSize + ")");
            buttonUploadPDF.setEnabled(true);
        }
    }

    private long getFileSize(Uri uri) {
        Cursor returnCursor = requireContext().getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        long size = returnCursor.getLong(sizeIndex);
        returnCursor.close();
        return size;
    }

    private String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String getFileName(String dataString) {
        return dataString.substring(dataString.lastIndexOf("/") + 1);
    }

    private String getFileName(Uri uri) {
        String fileName = uri.getLastPathSegment();
        if (fileName != null) {
            int cut = fileName.lastIndexOf('/');
            if (cut != -1) {
                fileName = fileName.substring(cut + 1);
            }
        }
        return fileName;
    }

    private void uploadPDF() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Téléchargement...");
        progressDialog.show();
        final String fileName = getFileName(pdfUri);
        final StorageReference fileReference = storageReference.child("documents/" + fileName + ".pdf");

        fileReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                databaseReference.child(databaseReference.push().getKey()).setValue(new Document(fileName, downloadUrl, readableFileSize));
                                mDocumentViewViewModel.saveDocument(vacationId, fileName, downloadUrl, readableFileSize);
                                Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Chargement..." + (int) progress + "%");

                    }
                });
    }

    /* private void sendDocumentInfoToBackend(final String fileName, final String downloadUrl) {
         String backendUrl = "https://your-backend-api.com/documents"; // Replace with your backend URL

         RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
         StringRequest stringRequest = new StringRequest(Request.Method.POST, backendUrl,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         Toast.makeText(getActivity(), "Document info saved", Toast.LENGTH_SHORT).show();
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(getActivity(), "Failed to save document info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
             }
         }) {
             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<>();
                 params.put("fileName", fileName);
                 params.put("url", downloadUrl);
                 return params;
             }
         };

         requestQueue.add(stringRequest);
     }*/
    @Override
    public void onResume() {
        super.onResume();
        assert getActivity() != null;
        ((HomeActivity) getActivity()).showBackButton(true);
        ((HomeActivity) requireActivity()).setToolbarTitle("Ajouter un document");

    }
}