package be.tomberndjesse.picaloc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.tomberndjesse.picaloc.Retrofit.Empty;
import be.tomberndjesse.picaloc.Retrofit.Post;
import be.tomberndjesse.picaloc.Retrofit.PostClient;
import be.tomberndjesse.picaloc.Retrofit.PostLocation;
import be.tomberndjesse.picaloc.Retrofit.ServiceGenerator;
import be.tomberndjesse.picaloc.utils.SettingsUtil;
import be.tomberndjesse.picaloc.utils.SharedPreferencesKeys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakePictureActivity extends BaseActivity {
    private ImageView mImageView;
    private Button mUploadButton;
    private Button mTakePicture;
    private Button mSignOut;
    private EditText mCaption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        enableLocationUpdates(2000, 5000); //Fastest updateInterval and updateInterval

        mCaption = (EditText) findViewById(R.id.caption);

        mCaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0)
                    mUploadButton.setVisibility(View.VISIBLE);
                else if(photoURI == null)
                    mUploadButton.setVisibility(View.GONE);
            }
        });

        mImageView = (ImageView) findViewById(R.id.taken_image);
        mUploadButton = (Button) findViewById(R.id.upload_image);
        mUploadButton.setVisibility(View.GONE);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                mUploadButton.setVisibility(View.GONE);
                if(photoURI != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://picaloc-2acb6.appspot.com");

                    // Points to "images"
                    StorageReference imagesRef = storageRef.child("images");

                    StorageReference riversRef = storageRef.child("images/" + photoURI.getLastPathSegment());
                    final UploadTask uploadTask = riversRef.putFile(photoURI);
                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads TODO
                            Toast.makeText(getApplicationContext(), "Failed uploading",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                            mUploadButton.setVisibility(View.VISIBLE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sendBackendRequest(taskSnapshot.getDownloadUrl());
                        }
                    });
                }
                else{
                    sendBackendRequest(Uri.parse(""));
                }

            }
        });

        mTakePicture = (Button) findViewById(R.id.take_image);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        mSignOut = (Button) findViewById(R.id.sign_out);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                new SettingsUtil(getApplicationContext()).setString(SharedPreferencesKeys.TokenString, "");
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoURI;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "be.tomberndjesse.picaloc.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                Picasso.with(this).load(photoURI).rotate(90).into(mImageView);
                //mImageView.setImageBitmap(imageOrientationValidator(imageBitmap, photoURI.getPath()));
                mUploadButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void sendBackendRequest(Uri downloadUrl){
        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
        Log.d("test", downloadUrl.toString());

        PostClient client = ServiceGenerator.createService(PostClient.class, getApplicationContext());

        Pattern stopWords = Pattern.compile("\\b(?:i|https://firebasestorage.googleapis.com/v0/b/picaloc-2acb6.appspot.com)\\b\\s*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = stopWords.matcher(downloadUrl.toString());
        String clean = matcher.replaceAll("");

        Post post = new Post(mCaption.getText().toString(), clean, new PostLocation(getLocation()));
        client.addPost(post).enqueue(new Callback<Empty>() {
            @Override
            public void onResponse(Call<Empty> call, Response<Empty> response) {
                String text = "Failed uploading.";
                if(response.isSuccessful()) {
                    text = "Uploaded";
                    photoURI = null;
                    mImageView.setImageResource(android.R.color.transparent);
                    mCaption.setText("");
                }
                else{
                    mUploadButton.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
                hideProgressBar();

            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed uploading",
                        Toast.LENGTH_SHORT).show();
                mUploadButton.setVisibility(View.VISIBLE);
                hideProgressBar();
            }
        });
    }
}
