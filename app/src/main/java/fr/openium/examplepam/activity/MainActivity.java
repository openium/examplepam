package fr.openium.examplepam.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import fr.openium.examplepam.R;
import fr.openium.examplepam.fragment.ContactListFragment;
import fr.openium.examplepam.fragment.HistoryListFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menu_contact_list);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.menu_history: {
                fragment = new HistoryListFragment();
                break;
            }
            case R.id.menu_contact_list: {
                fragment = new ContactListFragment();
                break;
            }
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commitAllowingStateLoss();
        }

        return true;
    }
}
