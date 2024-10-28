package com.example.apparchilog.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apparchilog.R;
import com.example.apparchilog.adapters.VacationAdapter;
import com.example.apparchilog.fragments.CreateDocumentFragment;
import com.example.apparchilog.fragments.CreateHolidayFragment;
import com.example.apparchilog.fragments.DocumentFragment;
import com.example.apparchilog.models.requests.PlaceRequest;
import com.example.apparchilog.viewModels.VacationViewModel;
import com.example.apparchilog.views.HomeActivity;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

    public static void showToast(Context context, String message, int durationInMillis) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);

        new CountDownTimer(durationInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }

    public static void changeColor(TextView status, Context context) {
        switch (status.getText().toString()) {
            case "Future":
                status.setBackgroundColor(ContextCompat.getColor(context, R.color.status_active));
                break;
            case "Past":
                status.setBackgroundColor(ContextCompat.getColor(context, R.color.status_inactive));
                break;
            default:
                status.setBackgroundColor(ContextCompat.getColor(context, R.color.status_pending));
                break;
        }
    }

    public static @NotNull PlaceRequest parseAddress(Address address, String city, String country) {
        PlaceRequest placeRequest = new PlaceRequest();
        if (address != null) {
            placeRequest.setLatitude(address.getLatitude());
            placeRequest.setLongitude(address.getLongitude());
            placeRequest.setCodePostal(address.getPostalCode());
            placeRequest.setRue(address.getFeatureName());
        }
        placeRequest.setPays(country);
        placeRequest.setVille(city);

        return placeRequest;
    }

    public static boolean isValid(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate,
                                  String city, String country, MutableLiveData<String> errorMessageLiveData) {
        if (TextUtils.isEmpty(name)) {
            errorMessageLiveData.setValue("1.Nom est requis");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            errorMessageLiveData.setValue("2.Description est requise");
            return false;
        }
        if (startDate == null) {
            errorMessageLiveData.setValue("3.Date de début est requis");
            return false;
        }
        if (endDate == null) {
            errorMessageLiveData.setValue("4.Date de fin est requis");
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetDateTime now = OffsetDateTime.now();

            if (endDate.isBefore(startDate)) {
                errorMessageLiveData.setValue("5.La date de fin doit être après la date de début");
                return false;
            }

            if (startDate.isAfter(endDate) || startDate.equals(endDate)) {
                errorMessageLiveData.setValue("6.La date de début doit être avant la date de fin");
                return false;
            }

            if (startDate.isBefore(now) || endDate.isBefore(now)) {
                errorMessageLiveData.setValue("7.La date de début ou de fin doit être avant la date d'aujourd'hui");
                return false;
            }
        }
        if (TextUtils.isEmpty(country)) {
            errorMessageLiveData.setValue("8.Pays est riquis");
            return false;
        }
        if (TextUtils.isEmpty(city)) {
            errorMessageLiveData.setValue("9.Ville est requise");
            return false;
        }
        return true;
    }


    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long timestamp) {
        Date date = new java.util.Date(timestamp * 1000);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(java.util.TimeZone.getDefault());
        return sdf.format(date);
    }

    public static String formatDate(OffsetDateTime dateTime) {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy - HH:mm");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.format(formatter);
        }
        return "";
    }

    public static void showVacationSelectionDialog(Context context, FragmentActivity activity, ViewModelStoreOwner viewModelStoreOwner, boolean isPdfFragment, LifecycleOwner viewLifecycleOwner, Long activityId, boolean isVacation) {
        AlertDialog vacationDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_vacation, null);
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        RecyclerView recyclerViewVacations = dialogView.findViewById(R.id.recyclerViewVacations);
        recyclerViewVacations.setLayoutManager(new LinearLayoutManager(context));
        builder.setView(dialogView);

        VacationViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(VacationViewModel.class);
        VacationAdapter adapter = new VacationAdapter(context, new ArrayList<>(), true);
        recyclerViewVacations.setAdapter(adapter);

        vacationDialog = builder.create();
        vacationDialog.show();

        viewModel.loadAllVacations();
        viewModel.getIsLoading().observe(viewLifecycleOwner, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerViewVacations.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                recyclerViewVacations.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getAllVacationsLiveData().observe(viewLifecycleOwner, newVacations -> {
            if (newVacations.isEmpty()) {
                tvDialogTitle.setText("Vous n'avez pas encore de vacance!");
            }
            adapter.updateData(newVacations);
        });
        adapter.setOnVacationClickListener(vacation -> {
            if (!vacation.getStatus().equals("Passé")) {
                goToFragmentCreation(vacation.getId(), activity, isPdfFragment, activityId, isVacation);
                vacationDialog.dismiss();
            }
        });
    }

    private static void goToFragmentCreation(Long id_vacation, FragmentActivity activity, boolean isPdfFragment, Long activityId, boolean isVacation) {
        if (isPdfFragment) {
            CreateDocumentFragment createDocumentFragment = CreateDocumentFragment.newInstance(id_vacation, activityId, isVacation);
            ((HomeActivity) activity).replaceFragment(createDocumentFragment, true, "Ajouter un document");

        } else {
            CreateHolidayFragment createHolidayFragment = CreateHolidayFragment.newInstance(id_vacation, 0L, true);
            ((HomeActivity) activity).replaceFragment(createHolidayFragment, true, "Créer Activité");
        }
    }


}
