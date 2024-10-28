package com.example.apparchilog.fragments;

import android.graphics.Color;
import android.os.Bundle;


import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Event;
import com.example.apparchilog.utils.EventDecorator;
import com.example.apparchilog.viewModels.ScheduleViewModel;
import com.prolificinteractive.materialcalendarview.*;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.*;

import static com.example.apparchilog.utils.Utils.formatDate;


public class CalendarFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private MaterialCalendarView calendarView;
    private ScheduleViewModel mViewModel;
    private Map<CalendarDay, Event> eventMap;
    private LinearLayout eventDetailsContainer;
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventDate;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewID(view);
        configureCalendarView();
        loadSchedule();
        observerViewModel();
        setupDateClickListener();

    }

    private void loadEvents(List<Event> events) {
        HashSet<CalendarDay> eventDates = new HashSet<>();
        eventMap = new HashMap<>();

        for (Event event : events) {
            CalendarDay day = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                day = CalendarDay.from(event.getStartDate().getYear(), event.getStartDate().getMonthValue(), event.getStartDate().getDayOfMonth());
            }
            eventDates.add(day);
            eventMap.put(day, event);
        }

        calendarView.addDecorator(new EventDecorator(Color.RED, eventDates));
    }

    private void setupDateClickListener() {
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NotNull MaterialCalendarView widget, @NotNull CalendarDay date, boolean selected) {
                Event event = eventMap.get(date);
                if (event != null) {
                    showEventDetails(event);
                } else {
                    hideEventDetails();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSchedule();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showEventDetails(Event event) {
        eventTitle.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
        eventDate.setText(formatDate(event.getStartDate()));
        eventDetailsContainer.setVisibility(View.VISIBLE);
    }

    private void hideEventDetails() {
        eventDetailsContainer.setVisibility(View.GONE);
    }

    private void loadSchedule() {
        mViewModel.loadSchedules();
    }

    private void initViewID(@NotNull View view) {
        calendarView = view.findViewById(R.id.calendarView);
        eventDetailsContainer = view.findViewById(R.id.eventDetailsContainer);
        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.eventDescription);
        eventDate = view.findViewById(R.id.eventDate);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void configureCalendarView() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        LocalDate instance = LocalDate.now();
        calendarView.setSelectedDate(instance);

        calendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.of(Calendar.DAY_OF_MONTH))
                .setMinimumDate(CalendarDay.from(2010, 4, 3))
                .setMaximumDate(CalendarDay.from(2030, 5, 12))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

    }

    private void observerViewModel() {
        mViewModel.getListEventMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                if (!events.isEmpty()) {
                    loadEvents(events);
                }
            }
        });
        mViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        mViewModel.getEventMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(CalendarFragment.this.getContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }
}