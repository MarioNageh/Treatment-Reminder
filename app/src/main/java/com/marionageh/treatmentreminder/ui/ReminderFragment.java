package com.marionageh.treatmentreminder.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.marionageh.treatmentreminder.AddTreatment;
import com.marionageh.treatmentreminder.Alarams.AlarmAdapter;
import com.marionageh.treatmentreminder.MainScreen;
import com.marionageh.treatmentreminder.Places.Geofencing;
import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.adapters.TreatmentAdapter;
import com.marionageh.treatmentreminder.customClasses.DeleteAsyanTask;
import com.marionageh.treatmentreminder.customClasses.SavingAsyncTask;
import com.marionageh.treatmentreminder.models.Treatment;
import com.marionageh.treatmentreminder.widget.TreatmentWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class ReminderFragment extends Fragment implements TreatmentAdapter.ListItemClick, DeleteAsyanTask.CustomReminder, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    //Views
    @BindView(R.id.recycler_view_re_fr)
    RecyclerView recyclerView;

    @BindView(R.id.add_loacation_frag)
    ImageView image_add_loacation;
    @BindView(R.id.location_text)
    TextView textView;
    //CardViewLoaction
    @BindView(R.id.Location_cardview)
    CardView cardView_Location;
    @BindView(R.id.enable_switch_loaction)
    Switch Control_Laction;
    @BindView(R.id.edit_loacation_frag)
    ImageView edit_loacation_frag;
    @BindView(R.id.adView)
    AdView mAdView;

    //To Store the Home Loaction
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;

    //Geting data from Shared Preferencess
    //For get From Prefeerences
    SharedPreferences.Editor editor;
    private static final String PLACE_NAME = "PLACE_NAME";
    private static final String PLACE_ID = "PLACE_ID";
    private static final String IS_ENABlED = "IS_ENABLED";
    ///////Button add Loaction
    boolean isArrowDown = true;
    private static final String IS_ARROW_Down_KEY = "ARROW";

    //IS Switch
    boolean IsChecked;

    // Constants
    public static final String TAG = ReminderFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST_edit = 2;


    //Adapteres
    public TreatmentAdapter treatmentAdapter = new TreatmentAdapter(null, this, null);


    //For exractdata
    public static final String GET_EXTRA_LIST = "LISt";

    //Values
    List<Treatment> treatmentList;

    //For Saving Instant State
    public static final String ON_SAVE_INSTANT_STATE_TRETMENT_LIST = "LISt_trat_Ment";

    public ReminderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the no data layout
        View view = inflater.inflate(R.layout.reminder_fragment, container, false);
        ButterKnife.bind(this, view);

        //get init shared pre
        editor = getActivity().getPreferences(MODE_PRIVATE).edit();


        //Get DATA Via Bundel && No need to check size of the list
        if (getArguments() == null && savedInstanceState == null) return null;
        treatmentList = getArguments().getParcelableArrayList(GET_EXTRA_LIST);
        ///For Saving INstntState
        if (savedInstanceState != null) {
            treatmentList = savedInstanceState.getParcelableArrayList(ON_SAVE_INSTANT_STATE_TRETMENT_LIST);
            isArrowDown = savedInstanceState.getBoolean(IS_ARROW_Down_KEY);
            if (isArrowDown) {
                ArrowUp();
            } else {
                ArrowDown();
            }
        }

        treatmentAdapter = new TreatmentAdapter(treatmentList, this, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(treatmentAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Stop This Reminder
                new AlarmAdapter().deleteAlarm(getContext(), (int) viewHolder.itemView.getTag());

                new DeleteAsyanTask(getContext(), (int) viewHolder.itemView.getTag(), ReminderFragment.this).execute();

            }
        }).attachToRecyclerView(recyclerView);


        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);


        SizeScreens();
        PutListnertoButtons();
        return view;
    }


    @Override
    public void OnItemClick(int Postion) {
        Treatment treatment = treatmentList.get(Postion);
        Intent intent = new Intent(getContext(), AddTreatment.class);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fade_in, R.anim.fade_out).toBundle();
        intent.putExtra(AddTreatment.TREAMENT_SENDER, treatment);
        getContext().startActivity(intent, bundle);


    }

    private void SizeScreens() {
        if (getContext().getResources().getBoolean(R.bool.IsTowPane)) {
            //Check Is LandScape or Not
            if (getContext().getResources().getBoolean(R.bool.ISLandScape)) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                        2, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
            } else {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
            }

        } else {
            //Is Tablet
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                    3, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

        }
    }

    public void notifiayAdpter(List<Treatment> treatmentList) {
        this.treatmentList = treatmentList;
        treatmentAdapter.Swapadapter(treatmentList);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ON_SAVE_INSTANT_STATE_TRETMENT_LIST, (ArrayList<? extends Parcelable>) treatmentList);
        outState.putBoolean(IS_ARROW_Down_KEY, isArrowDown);
    }

    @Override
    public void onDeleteFinshed(List<Treatment> treatmentList) {
        //Update Widget
        TreatmentWidgetService.startActionGetUpdate(getContext());

        this.treatmentList = treatmentList;
        treatmentAdapter.Swapadapter(treatmentList);

        //This for inflate layout no data
        if (treatmentList.size() == 0) {
            //UnRigister ALl Geo Fancess
             mGeofencing.unRegisterAllGeofences();
            ((MainScreen) getActivity()).initFragments(treatmentList);
        }
    }

    //////////////////////////////GEO Fancing////////////////////////////////////////////////////////////////////////////////////////

    private void PutListnertoButtons() {
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(getActivity(), this)
                    .build();

            mGeofencing = new Geofencing(getContext(), mClient);

        }

        String loaction_id = getActivity().getPreferences(MODE_PRIVATE).getString(PLACE_ID, "null");
        //// If There Is Id For Place IN Shared Pre we will Appear The Plus + Button
        if (loaction_id.equals("null")) {
            image_add_loacation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    putPermission();
                }
            });
        } else {
            PrepareSwitch();
            refreshuiforloaction();
        }
    }


    private void putPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);

        } else {

            addPlaceToPreferenc();
        }
    }

    private void addPlaceToPreferenc() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        refreshPlacesData();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            addLoactionfromResult(data);
        }
        /////// This To Display The Toast Message
        if (requestCode == PLACE_PICKER_REQUEST_edit && resultCode == Activity.RESULT_OK) {
            addLoactionfromResult(data);
            Toast.makeText(getContext(), getString(R.string.Loaction_Changed), Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("MMMaaaaaaaaaaaaa", "AAA");
        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addPlaceToPreferenc();
            }
        }
    }


    ///////// Now If We Set The Loaction We Will Save It In Shared Pre
    private void addLoactionfromResult(Intent data) {
        Place place = PlacePicker.getPlace(getContext(), data);
        if (place == null) {
            return;
        }

        // Extract the place information from the API
        String placeName = place.getAddress().toString();
        String placeID = place.getId();
        // Insert a new place into DB
        //   editor.putString(PLACE_NAME, placeName);
        editor.putString(PLACE_ID, placeID);
        editor.apply();
        // and we will update the Ui OF This List
        refreshuiforloaction(placeName);
    }


    /////Refresh List
    public void refreshPlacesData() {
        //No We Will Get The Ids Of PLace and Update The Geofances
        String placeID;
        placeID = getActivity().getPreferences(MODE_PRIVATE).getString(PLACE_ID, "null");
        IsChecked = getActivity().getPreferences(MODE_PRIVATE).getBoolean(IS_ENABlED, false);

        if (placeID.equals("null")) return;
        List<String> guids = new ArrayList<String>();

        guids.add(placeID);
        Log.e("MARIOLOCATION", placeID);

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                mGeofencing.updateGeofencesList(places);
                if (IsChecked) mGeofencing.registerAllGeofences();
            }
        });
    }

    ///////////////////////UI Refresh Location
    private void refreshuiforloaction() {
        editor = getActivity().getPreferences(MODE_PRIVATE).edit();


        //Now we get the DataFrom The Prefernces  place id
        //then update the TextView
        //now if have loaction we need no change the + iamge to < iamge and put listner to arrows
        String placeName, placeID;
        placeID = getActivity().getPreferences(MODE_PRIVATE).getString(PLACE_ID, "null");
        ///////////////////////////////////////Geting Data From Places
        Log.e("PlaceId", placeID);
        if (placeID.equals("null")) return;
        List<String> guids = new ArrayList<String>();
        guids.add(placeID);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer placees) {

                textView.setText(placees.get(0).getAddress());
            }
        });


        /////////Now wE Will Hige + button Wirh The Arrows
        image_add_loacation.setImageResource(R.drawable.ic_arrow_drop_down);
        image_add_loacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isArrowDown) {
                    ArrowDown();
                } else {
                    ArrowUp();
                }
            }
        });
    }

    private void refreshuiforloaction(String placeName) {
        editor = getActivity().getPreferences(MODE_PRIVATE).edit();


        //Now we get the DataFrom The Prefernces  place id
        //then update the TextView
        //now if have loaction we need no change the + iamge to < iamge and put listner to arrows
        String placeID;
        placeID = getActivity().getPreferences(MODE_PRIVATE).getString(PLACE_ID, "null");
        ///////////////////////////////////////Geting Data From Places

        textView.setText(placeName);


        /////////Now wE Will Hige + button Wirh The Arrows
        image_add_loacation.setImageResource(R.drawable.ic_arrow_drop_down);
        image_add_loacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isArrowDown) {
                    ArrowDown();
                } else {
                    ArrowUp();
                }
            }
        });
    }

    /// Remove CardView For Loaction
    private void ArrowUp() {
        isArrowDown = true;
        image_add_loacation.setImageResource(R.drawable.ic_arrow_drop_down);
        cardView_Location.setVisibility(View.GONE);

    }

    /// add CardView For Loaction
    private void ArrowDown() {
        image_add_loacation.setImageResource(R.drawable.ic_arrow_drop_up);
        isArrowDown = false;
        cardView_Location.setVisibility(View.VISIBLE);
        edit_loacation_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_Loaction();
            }
        });
        PrepareSwitch();

    }

    private void PrepareSwitch() {
        // IsChecked Of The Default if false
        // We will Change The Check Box To value of pre
        // No IF Change the value of switch
        //We will Save the value and Register all GeoFances if IsChecked True

        IsChecked = getActivity().getPreferences(MODE_PRIVATE).getBoolean(IS_ENABlED, false);
        Control_Laction.setChecked(IsChecked);
        Control_Laction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(IS_ENABlED, isChecked);
                editor.apply();
                IsChecked = isChecked;
                if (isChecked) {
                    // if the Switch if True we will Update The List Of PLaces
                    refreshPlacesData();
                    mGeofencing.registerAllGeofences();
                } else mGeofencing.unRegisterAllGeofences();
            }
        });
    }

    private void edit_Loaction() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST_edit);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mClient.stopAutoManage(getActivity());
        mClient.disconnect();
    }

    //////////////////////////////////////////////////////
}
