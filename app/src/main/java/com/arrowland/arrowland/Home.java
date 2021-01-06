package com.arrowland.arrowland;

import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arrowland.arrowland.Classes.App;
import com.arrowland.arrowland.Classes.BadgeDrawable;
import com.arrowland.arrowland.Classes.ConnectivityReceiver;
import com.arrowland.arrowland.Classes.NetworkSchedulerService;
import com.arrowland.arrowland.Fragment.AboutFragment;
import com.arrowland.arrowland.Fragment.ContactFragment;
import com.arrowland.arrowland.Fragment.GalleryFragment;
import com.arrowland.arrowland.Fragment.HomeFragment;
import com.arrowland.arrowland.Fragment.LessonFragment;
import com.arrowland.arrowland.Fragment.MembershipFragment;
import com.arrowland.arrowland.Fragment.NotificationFragment;
import com.arrowland.arrowland.Fragment.PaymentInstructionFragment;
import com.arrowland.arrowland.Fragment.PreMembershipAndEnrollmentFragment;
import com.arrowland.arrowland.Fragment.ReservationFragment;
import com.arrowland.arrowland.Fragment.ReservationListFragment;
import com.arrowland.arrowland.REST.ApiClient;
import com.arrowland.arrowland.REST.ApiInterface;
import com.arrowland.arrowland.REST.Lesson;
import com.arrowland.arrowland.REST.NotificationList;
import com.arrowland.arrowland.REST.Notifications;
import com.arrowland.arrowland.REST.ReservationList;
import com.arrowland.arrowland.REST.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnSignIn;
    private TextView txtName;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private boolean isLoggedIn = false;
    private static final int MENU_ITEM_MYRESERVATION = 6;
    private static final int MENU_ITEM_LOGOUT = 0 ;
//    private static final int MENU_ITEM_MEMBERSHIP = 5;
//    private static final int MENU_ITEM_LESSON = MENU_ITEM_MEMBERSHIP + 1;
    private int count,id;
    private DrawerLayout drawer;
    private TextView reservationCount;
    private Menu mMenu;
    private LayerDrawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_home);


        // SHAREDPREFERNCE

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);
        String name = sharedPreferences.getString("Name","");
        id = sharedPreferences.getInt("ID",0);



        // SET DEFAULT FRAGMENT

        if(savedInstanceState == null) {
            setDefaultFragment();

        }

        // TOOLBAR

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // NAVIGATION BAR

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(this);



        // INITIALIZE

        btnSignIn = headerView.findViewById(R.id.btnSignIn);
        txtName = headerView.findViewById(R.id.txtViewName);



        if(isLoggedIn) {
            btnSignIn.setVisibility(View.GONE);
            addMenuItem();
//            initializeCountDrawer();
            txtName.setText("Hi " + name);


        }



    }


    @Override
    public void onResume() {
        super.onResume();

        changeMembershipMenu();
        lessonChangeMenu();
        FirebaseToken();


        Intent mInt = getIntent();
//        String frag = mInt.getExtras().getString("frag");
        if(mInt.hasExtra("frag")) {
            String frags = mInt.getExtras().getString("frag");
            Fragment fragment;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if(frags.equals("fragmentReservation")) {
                fragment = new ReservationListFragment();
                ft.replace(R.id.content_frame,fragment);
                navigationView.getMenu().getItem(7).setChecked(true);


            }else if(frags.equals("fragmentPendingMembership")) {

                fragment = new MembershipFragment();
                ft.replace(R.id.content_frame,fragment);
                navigationView.getMenu().getItem(3).setChecked(true);

            }else if(frags.equals("fragmentPaymentInstruction")) {
                fragment = new PaymentInstructionFragment();
                ft.replace(R.id.content_frame,fragment);
                navigationView.getMenu().getItem(6).setChecked(true);

            }else if(frags.equals("fragmentLesson")) {
                fragment = new LessonFragment();
                ft.replace(R.id.content_frame,fragment);
                navigationView.getMenu().getItem(5).setChecked(true);

            }else if(frags.equals("fragmentMembership")) {
                fragment = new MembershipFragment();
                ft.replace(R.id.content_frame,fragment);
                navigationView.getMenu().getItem(6).setChecked(true);

            }
            mInt.removeExtra("frag");


            ft.commit();




        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(isLoggedIn) {
            getMenuInflater().inflate(R.menu.home,menu);

            Drawable drawable = menu.getItem(0).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.fontWhite), PorterDuff.Mode.SRC_IN);
            icon = (LayerDrawable) menu.getItem(0).getIcon();
            loadCountNotification();



            return true;

        }else {
            return super.onCreateOptionsMenu(menu);

        }

//


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            Fragment fragment = new NotificationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//        int count = getFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.content_frame,fragment);
            ft.commit();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }


    // NAVIGATION BAR CLICK

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        item.setChecked(true);
        item.setCheckable(true);

        int id = item.getItemId();

        Toolbar appbarLayout = (Toolbar) findViewById(R.id.toolbar);
        if (id == R.id.home) {
            fragment = new HomeFragment();

        } else if (id == R.id.about) {
            fragment = new AboutFragment();


        } else if (id == R.id.gallery) {
            fragment = new GalleryFragment();

        } else if (id == R.id.contact) {
            fragment = new ContactFragment();

        } else if (id == R.id.reservation) {
            if (isLoggedIn) {


                fragment = new ReservationFragment();
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme).create();
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("You must log in first before you can set your reservation");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                    }
                });


                alertDialog.show();
            }

        }else if(id == R.id.payment_instruction) {
            if(isLoggedIn) {
                fragment = new PaymentInstructionFragment();

            }else {
                final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme).create();
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("You must log in first before you can set your reservation");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                    }
                });


                alertDialog.show();
            }

        }else if(id == R.id.pre_membership_enrollment){
            if(isLoggedIn) {
                fragment = new PreMembershipAndEnrollmentFragment();

            }else {
                final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme).create();
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("You must log in first before you can set your reservation");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                    }
                });


                alertDialog.show();

            }

        }else if(id == R.id.membership) {
            if(isLoggedIn) {
                fragment = new MembershipFragment();

            }else {
                final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme).create();
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("You must log in first before you can set your reservation");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                    }
                });


                alertDialog.show();

            }


//            loadJsonStatus();


        }else if(id == R.id.lessons) {
            if(isLoggedIn) {
                fragment = new LessonFragment();

            }else {
                final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme).create();
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("You must log in first before you can set your reservation");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                    }
                });


                alertDialog.show();

            }




        }else if(id == MENU_ITEM_MYRESERVATION) {
            fragment = new ReservationListFragment();

        } else if (id == MENU_ITEM_LOGOUT) {
            Toast.makeText(this, "Successfully Logout", Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = getSharedPreferences("Login",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);



        }

        if (fragment !=null) {



            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void addMenuItem() {



        NavigationView navVIew = findViewById(R.id.nav_view);
        Menu menu = navVIew.getMenu();
//        menu.add(R.id.group1,6,Menu.NONE,"Apply for Membership");
        menu.add(R.id.group1,MENU_ITEM_MYRESERVATION, Menu.NONE,"My Reservation").setIcon(R.drawable.ic_description_black_24dp).setActionView(R.layout.menu_counter).setCheckable(true);
        menu.add(Menu.NONE,MENU_ITEM_LOGOUT,Menu.NONE,"Logout").setIcon(R.drawable.lg);
        navVIew.invalidate();

        reservationCount = (TextView) navVIew.getMenu().findItem(6).getActionView();
        initializeCountDrawer();





    }

    private void initializeCountDrawer() {

        // RESERVATION COUNT
        reservationCount.setGravity(Gravity. CENTER_VERTICAL);
        reservationCount.setTypeface(null, Typeface.BOLD);
        reservationCount.setTextColor(getResources().getColor(R.color.colorAccent));

    }


    // SET HOME FRAGMENT

    public void setDefaultFragment(){
        Fragment fragment = new HomeFragment();

        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }
    }

    // LOGIN BUTTON

    public void btnSignIn(View view){

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }
    private int getTotalCount(int totalCount) {
        return totalCount;

    }

    public void loadCountNotification() {




        ApiInterface inf = ApiClient.getApiService();
        Call<Notifications> call = inf.countNotification(id);
        call.enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                if(response.isSuccessful()) {
//                    Toast.makeText(Home.this, "" + response.body().getCount(), Toast.LENGTH_SHORT).show();
                    setBadgeCount(Home.this,icon,response.body().getCount());


                }

            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {


            }
        });

//        return count;

    }







    private void changeMembershipMenu() {

        ApiInterface inf = ApiClient.getApiService();
        Call<com.arrowland.arrowland.REST.Membership> call = inf.getMembershipStatus(id);
        call.enqueue(new Callback<com.arrowland.arrowland.REST.Membership>() {
            @Override
            public void onResponse(Call<com.arrowland.arrowland.REST.Membership> call, Response<com.arrowland.arrowland.REST.Membership> response) {

                com.arrowland.arrowland.REST.Membership membershipResponse = response.body();


                if(response.isSuccessful()) {

                    if(membershipResponse.getStatus().equals("None") ) {
                        NavigationView navVIew = findViewById(R.id.nav_view);
                        Menu menu = navVIew.getMenu();
                        MenuItem membershipMenu = menu.findItem(R.id.membership);
//                        membershipMenu.setTitle("Apply for Membership");
//                        membershipMenu.setVisible(false);

                    }else {

                        NavigationView navVIew = findViewById(R.id.nav_view);
                        Menu menu = navVIew.getMenu();
                        MenuItem membershipMenu = menu.findItem(R.id.membership);
                        membershipMenu.setTitle("My Membership");
//                        membershipMenu.setVisible(true);
                    }






                }else{
                    Toast.makeText(Home.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<com.arrowland.arrowland.REST.Membership> call, Throwable t) {


            }
        });

    }

    private void lessonChangeMenu() {
        ApiInterface inf = ApiClient.getApiService();
        Call<Lesson> call = inf.getLessonStatus(id);
        call.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                Lesson mResposne = response.body();
                if(response.isSuccessful()) {
                    if(mResposne.getStatus().equals("None")) {
                        NavigationView navVIew = findViewById(R.id.nav_view);
                        Menu menu = navVIew.getMenu();
                        MenuItem membershipMenu = menu.findItem(R.id.lessons);
//                        membershipMenu.setTitle("Apply for Membership");
//                        membershipMenu.setVisible(false);

                    }else {
                        NavigationView navVIew = findViewById(R.id.nav_view);
                        Menu menu = navVIew.getMenu();
                        MenuItem membershipMenu = menu.findItem(R.id.lessons);
                        membershipMenu.setTitle("My Lesson Enrollment");
//                        membershipMenu.setVisible(true);

                    }
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {

            }
        });
    }

    private void FirebaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

                if(id != 0) {


                    insertNewToken(newToken);

                }
            }
        });
    }


    public void insertNewToken(String token) {



        ApiInterface apiInterface = ApiClient.getApiService();
        Call<User> call = apiInterface.userToken(id,token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {

                    Log.d("onResponse", "" + response.body());

                }else {
                    Toast.makeText(Home.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d("onFailure",""+t);

            }
        });
//


    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count){
        BadgeDrawable badge;

        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if(reuse != null && reuse instanceof BadgeDrawable){
            badge = (BadgeDrawable) reuse;
        }else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge,badge);

    }
}
