package com.example.lr6;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToysFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToysFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DBHelper dbHelper;
    ListView listView;

    public ToysFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToysFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToysFragment newInstance(String param1, String param2) {
        ToysFragment fragment = new ToysFragment();
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
        View view = inflater.inflate(R.layout.fragment_toys, container, false);

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
        String tableName = "toys";
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