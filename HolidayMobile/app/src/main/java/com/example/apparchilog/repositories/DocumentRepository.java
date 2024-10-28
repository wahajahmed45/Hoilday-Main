package com.example.apparchilog.repositories;


import com.example.apparchilog.models.holiday.Document;
import com.example.apparchilog.models.responses.MessageResponse;
import com.example.apparchilog.repositories.inter_responses.IMessageResponse;
import com.example.apparchilog.services.RetrofitClientInstance;

import com.example.apparchilog.services.instance.CurrentInstanceUser;
import com.example.apparchilog.services.inter.IDocumentService;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DocumentRepository {
    private final String token = CurrentInstanceUser.getInstance().getToken();

    public void saveDocument(Long vacationId, Document document, IMessageResponse iMessageResponse) {
        IDocumentService service = RetrofitClientInstance.getRetrofitInstance().create(IDocumentService.class);
        Call<MessageResponse> call = service.createDocument(vacationId, document, "Bearer " + token);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NotNull Call<MessageResponse> call, @NotNull Response<MessageResponse> response) {
                if (response.isSuccessful() & response.body() != null) {
                    iMessageResponse.onResponse(response.body());
                } else {
                    iMessageResponse.onFailure(new Throwable("Failed to save document"));
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageResponse> call, @NotNull Throwable t) {
                iMessageResponse.onFailure(t);
            }
        });
    }
}
