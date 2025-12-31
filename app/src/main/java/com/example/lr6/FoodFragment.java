package com.example.lr6;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class FoodFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    DBHelper dbHelper;
    ListView listView;

    public FoodFragment() {

    }

    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        listView = view.findViewById(R.id.myListView);
        dbHelper = new DBHelper(requireContext());

        try {
            dbHelper.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadDataFromDB();

        return view;
    }

    private void loadDataFromDB() {
        String tableName = "Foods";
        String columnName = "name";
        ArrayList<String> dataList = dbHelper.getAllItems(tableName);

        if (dataList.isEmpty()) {
            dataList.add("Нет данных.");
            dataList.add("Проверьте имя таблицы: " + tableName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                dataList
        );
        listView.setAdapter(adapter);
    }
}
