package com.marionageh.treatmentreminder;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.marionageh.treatmentreminder.LiveDataCustomsClass.InsertAsyanTask;
import com.marionageh.treatmentreminder.adapters.TreatmentAdapter;
import com.marionageh.treatmentreminder.customClasses.CustomAsyanTask;
import com.marionageh.treatmentreminder.customClasses.SavingAsyncTask;
import com.marionageh.treatmentreminder.database.TreatmentViewModel;
import com.marionageh.treatmentreminder.models.Treatment;
import com.marionageh.treatmentreminder.ui.NoDataFragment;
import com.marionageh.treatmentreminder.ui.ReminderFragment;
import com.marionageh.treatmentreminder.widget.TreatmentWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreen extends AppCompatActivity implements TreatmentAdapter.ListItemClick, InsertAsyanTask.CustomReminder, CustomAsyanTask.CustomAsyncListener {

    //For intent
    public static final String Data_FROM_DATABASE = "DATA";
    //For Views
    @BindView(R.id.fab_add_treat_ms)
    FloatingActionButton fab_ms;
    @BindView(R.id.fragment_cotanier_ms)
    FrameLayout fragment_cotanier_ms;
    @BindView(R.id.tabs_ms)
    TabLayout tabs_ms;


    public static Activity fa;


    //Values
    List<Treatment> treatmentList;
    private TreatmentViewModel viewModel;
    //For Treatment add Reminder
    private String reminderTitle = "";

    //Saving instant state
    public static final String TREATMENT_LIST_SAVING = "SavingTreatment";
    //Fragment Tag
    public static final String REMINDER_FRAGMENT_TAG = "REMindetr_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        fa = this;
        //Check the intent if not null get data
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            treatmentList = intent.getParcelableArrayListExtra(Data_FROM_DATABASE);
        }
        //For Views Initailn
        iniviews();

        //For Saving Instat Sate
        //For Fragment Transactiion With Saving Instatne State
        if (savedInstanceState != null) {
            treatmentList = savedInstanceState.getParcelableArrayList(TREATMENT_LIST_SAVING);
        } else {
            initFragments(treatmentList);
        }
        viewModel = ViewModelProviders.of(this).get(TreatmentViewModel.class);

    }

    public void initFragments(List<Treatment> treatmentList) {
        //this method for inflate choose any layout will inflate
        FragmentManager fM = getSupportFragmentManager();
        Fragment fragment = null;
        if (treatmentList.size() == 0) {
            fragment = new NoDataFragment();
            fM.beginTransaction().replace(fragment_cotanier_ms.getId(), fragment).commit();

        } else {
            fragment = new ReminderFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ReminderFragment.GET_EXTRA_LIST, (ArrayList<? extends Parcelable>) this.treatmentList);
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fM
                    .beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            fragmentTransaction.replace(fragment_cotanier_ms.getId(), fragment, REMINDER_FRAGMENT_TAG).commit();

        }

    }

    private void iniviews() {
        //fab listner
        fab_ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTreatmentReminder();
            }
        });
        //for tablayout
        TabLayout.Tab tab = tabs_ms.newTab();
        tabs_ms.addTab(tab);
        tab.setText(getResources().getString(R.string.Main_Screen_title));
    }

    private void addTreatmentReminder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_title));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    return;
                }

                reminderTitle = input.getText().toString();
                Treatment treatment = new Treatment(reminderTitle, getResources().getString(R.string.Default_note), "", "", false, "", "", true);
                viewModel.insertTreatment(treatment, MainScreen.this);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.Cancel_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onListReceived(List<Treatment> treatmentList) {


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TREATMENT_LIST_SAVING, (ArrayList<? extends Parcelable>) treatmentList);

    }

    @Override
    public void OnItemClick(int Postion) {

    }

    @Override
    public void onInsertFinshed(List<Treatment> treatmentList) {
        //Check if data added or not
        if (treatmentList.size() == 1) {
            this.treatmentList = treatmentList;
            //for inflate the fragment that have data
            initFragments(treatmentList);

            Snackbar.make(findViewById(R.id.main_coordinator_ms), getResources().getString(R.string.the_Treatment_added), Snackbar.LENGTH_SHORT).show();
            //We will Refresh the list newst data
            //Todo We will Swape Adatper for data next
            ReminderFragment fragment = (ReminderFragment) getSupportFragmentManager().findFragmentByTag(REMINDER_FRAGMENT_TAG);
            // Check if the tab fragment is available
            if (fragment != null) {
                // Call your method in the TabFragment
                fragment.notifiayAdpter(treatmentList);
            }
            TreatmentWidgetService.startActionGetUpdate(this);


        } else if (treatmentList.size() == 0) {
            Snackbar.make(findViewById(R.id.main_coordinator_ms), getResources().getString(R.string.faild_to_add), Snackbar.LENGTH_SHORT).show();

        }
    }

    public void UpdateAdapter(List<Treatment> treatmentyList) {
        ReminderFragment fragment = (ReminderFragment) getSupportFragmentManager().findFragmentByTag(REMINDER_FRAGMENT_TAG);
        // Check if the tab fragment is available
        if (fragment != null) {
            // Call your method in the TabFragment
            fragment.notifiayAdpter(treatmentyList);
        }
    }


}
