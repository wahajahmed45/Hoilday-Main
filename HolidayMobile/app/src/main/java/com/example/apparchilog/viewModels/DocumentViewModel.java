package com.example.apparchilog.viewModels;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.apparchilog.models.holiday.Document;
import com.example.apparchilog.models.responses.MessageResponse;
import com.example.apparchilog.repositories.DocumentRepository;
import com.example.apparchilog.repositories.inter_responses.IMessageResponse;

public class DocumentViewModel extends ViewModel {

    private final DocumentRepository documentRepository;
    private final MutableLiveData<String> documentMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Document> fileMutableLiveData;

    private final MutableLiveData<Boolean> isLoading;

    public DocumentViewModel() {
        documentRepository = new DocumentRepository();
        isLoading = new MutableLiveData<>();
        fileMutableLiveData = new MutableLiveData<>();
    }

    public void saveDocument(Long vacationId, String fileName, String downloadUrl, String unit) {
        isLoading.setValue(true);
        documentRepository.saveDocument(vacationId, new Document(fileName, downloadUrl,unit), new IMessageResponse() {
            @Override
            public void onResponse(MessageResponse message) {
                if (message != null) {
                    isLoading.setValue(false);
//                    weatherMutableLiveData.setValue(weather);
                    documentMessageLiveData.setValue("Save success");
                    Log.d("TAG_DOCUMENT", message.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.setValue(false);
                documentMessageLiveData.setValue("Save failed");
            }
        });

    }
    public MutableLiveData<String> getDocumentMessageLiveData() {
        return documentMessageLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }


}

