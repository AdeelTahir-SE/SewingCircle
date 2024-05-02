package com.adeeltahir.sewingcircle;
import com.adeeltahir.sewingcircle.AccountInfo;
import com.adeeltahir.sewingcircle.CurrentCustomer;
import android.os.Bundle;
import android.view.MenuItem;
import com.adeeltahir.sewingcircle.R;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
HomeFragment homeFragment=new HomeFragment();
AccountInfo accountInfo=new AccountInfo();
CurrentCustomer currentCustomer=new CurrentCustomer();
PreviousCustomers previousCustomers=new PreviousCustomers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
        BadgeDrawable badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.CurrentCustomer);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(5);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {

                    if (menuItem.getItemId()==R.id.home)  {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
                        return true;
                    }
                  else if (menuItem.getItemId()==R.id.CurrentCustomer)  {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,currentCustomer).commit();
                        return true;
                    }
                   else if (menuItem.getItemId()==R.id.PreviousOrders)  {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,previousCustomers).commit();
                        return true;
                    }
                    else if (menuItem.getItemId()==R.id.Accountinfo)  {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,accountInfo).commit();
                        return true;
                    }
                return false;


            }
        });
    }
}