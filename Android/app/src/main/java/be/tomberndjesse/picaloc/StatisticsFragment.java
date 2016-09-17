package be.tomberndjesse.picaloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import be.tomberndjesse.picaloc.utils.SettingsUtil;
import be.tomberndjesse.picaloc.utils.SharedPreferencesKeys;

/**
 * Created by jesse on 17/09/2016.
 */
public class StatisticsFragment extends Fragment {
    private Button mSignOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        //TextView messageTextView = (TextView)v.findViewById(R.id.textView);
        //messageTextView.setText(message);

        mSignOut = (Button) v.findViewById(R.id.sign_out);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                new SettingsUtil(getActivity()).setString(SharedPreferencesKeys.TokenString, "");
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });
        return v;
    }
}
