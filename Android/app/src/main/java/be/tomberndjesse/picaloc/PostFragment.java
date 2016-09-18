package be.tomberndjesse.picaloc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.phenotype.Configuration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jesse on 17/09/2016.
 */
public class PostFragment extends Fragment {
    private ImageView mImageView;
    private Button mUploadButton;
    private FloatingActionButton mTakePicture;
    private EditText mCaption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        mCaption = (EditText) v.findViewById(R.id.caption);

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

        mImageView = (ImageView) v.findViewById(R.id.taken_image);
        mUploadButton = (Button) v.findViewById(R.id.upload_image);
        mUploadButton.setVisibility(View.GONE);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).showProgressBar();
                mUploadButton.setVisibility(View.GONE);
                if(photoURI != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://picaloc-2acb6.appspot.com");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    StorageReference riversRef = storageRef.child("images/" + photoURI.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putBytes(data);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads TODO
                            Toast.makeText(getActivity(), "Failed uploading",
                                    Toast.LENGTH_SHORT).show();
                            ((BaseActivity) getActivity()).hideProgressBar();
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

        mTakePicture = (FloatingActionButton) v.findViewById(R.id.take_image);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        return v;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoURI;
    Bitmap photoBitmap;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        "be.tomberndjesse.picaloc.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

                new ResizeOperation().execute(getActivity().getResources().getConfiguration().orientation);

            //mImageView.setImageBitmap(imageOrientationValidator(imageBitmap, photoURI.getPath()));


        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

        PostClient client = ServiceGenerator.createService(PostClient.class, getActivity());

        Pattern stopWords = Pattern.compile("\\b(?:i|https://firebasestorage.googleapis.com/v0/b/picaloc-2acb6.appspot.com)\\b\\s*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = stopWords.matcher(downloadUrl.toString());
        String clean = matcher.replaceAll("");

        Post post = new Post(mCaption.getText().toString(), clean, new PostLocation(((BaseActivity) getActivity()).getLocation()));
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
                Toast.makeText(getActivity(), text,
                        Toast.LENGTH_SHORT).show();
                ((BaseActivity) getActivity()).hideProgressBar();

            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed uploading",
                        Toast.LENGTH_SHORT).show();
                mUploadButton.setVisibility(View.VISIBLE);
                ((BaseActivity) getActivity()).hideProgressBar();
            }
        });
    }

    private class ResizeOperation extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = Picasso.with(getActivity()).load(photoURI).get();
                photoBitmap = (params[0] == android.content.res.Configuration.ORIENTATION_LANDSCAPE && imageBitmap.getWidth() > imageBitmap.getHeight()) ? Picasso.with(getActivity()).load(photoURI).resize(imageBitmap.getWidth()/5, imageBitmap.getHeight()/5).get():
                        Picasso.with(getActivity()).load(photoURI).resize(imageBitmap.getWidth()/20, imageBitmap.getHeight()/20).rotate(90).get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            mImageView.setImageBitmap(photoBitmap);
            mUploadButton.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
