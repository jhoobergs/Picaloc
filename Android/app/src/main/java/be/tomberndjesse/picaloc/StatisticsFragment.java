package be.tomberndjesse.picaloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import be.tomberndjesse.picaloc.utils.SettingsUtil;
import be.tomberndjesse.picaloc.utils.SharedPreferencesKeys;

/**
 * Created by jesse on 17/09/2016.
 */
public class StatisticsFragment extends Fragment {
    private Button mSignOut;
    private TextView mPoints;
    private TextView mEmail;
    private TextView mName;
    private ImageView mImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        mPoints = (TextView) v.findViewById(R.id.points);
        mEmail = (TextView) v.findViewById(R.id.email);
        mName = (TextView) v.findViewById(R.id.name);
        mImage = (ImageView) v.findViewById(R.id.image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mEmail.setText(user.getEmail());
        mName.setText(user.getDisplayName());
        Picasso.with(getActivity()).load(user.getPhotoUrl()).into(mImage);

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
