package com.rapid.furnitureaugmentreal;


import androidx.fragment.app.Fragment;

import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;
import com.rapid.furnitureaugmentreal.activities.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomArFragment extends ArFragment {


    public CustomArFragment() {
        // Required empty public constructor
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        getPlaneDiscoveryController().setInstructionView(null);
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        session.configure(config);
        getArSceneView().setupSession(session);


//          if ((((MainActivity) getActivity()).setupAugmentedImagesDb(config, session))) {
//            Log.d("SetupAugImgDb", "Success");
//        } else {
//            Log.e("SetupAugImgDb","Faliure setting up db");
//        }
        return config;
    }


}
