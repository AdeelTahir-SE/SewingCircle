package com.adeeltahir.sewingcircle;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

public class HomeFragment extends Fragment {

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the SearchView in the fragment layout
        searchView = rootView.findViewById(R.id.searchView);

        // Set up the SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search operation here
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search operation as text changes
                performSearch(newText);
                return true;
            }
        });

        return rootView;
    }

    private void performSearch(String query) {
        // Implement your search logic here
        // You can filter data, update UI, etc. based on the search query
    }
}
