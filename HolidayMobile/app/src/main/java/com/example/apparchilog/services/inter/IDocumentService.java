package com.example.apparchilog.services.inter;

import com.example.apparchilog.models.holiday.Document;
import com.example.apparchilog.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface IDocumentService {

    @POST("vacations/document/{id_vacation}")
    Call<MessageResponse> createDocument(@Path("id_vacation") Long idVacation, @Body Document document, @Header("Authorization") String token);
}
