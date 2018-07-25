package b.team.works.u22.hal.u22teamb;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 店舗詳細画面マップタブのフラグメント.
 *
 * @author Taiga Hirai
 */
public class StoreDetailsTab2Fragment extends Fragment {

    private static final String ARG_PARAM = "page";
    private String mParam;
    private OnFragmentInteractionListener mListener;

    /**
     * コンストラクタ.
     */
    public StoreDetailsTab2Fragment() {
    }

    public static StoreDetailsTab2Fragment newInstance(int page) {
        StoreDetailsTab2Fragment fragment = new StoreDetailsTab2Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public void onStart() {

        FemaleStoreDetailsActivity activity = (FemaleStoreDetailsActivity) this.getActivity();

        if(activity.mMap == null) {
            // SupportMapFragmentを取得
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.storeDetailMaps);
            mapFragment.getMapAsync(activity);
        }

        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int page = getArguments().getInt(ARG_PARAM, 0);
        View view = inflater.inflate(R.layout.fragment_store_details_tab2, container, false);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
