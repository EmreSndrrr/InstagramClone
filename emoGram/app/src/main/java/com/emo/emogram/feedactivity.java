package com.emo.emogram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.emo.emogram.adapter.PostAdapter;
import com.emo.emogram.databinding.ActivityFeedactivityBinding;
import com.emo.emogram.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;



public class feedactivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    private ActivityFeedactivityBinding binding;
    PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFeedactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        postArrayList = new ArrayList<>();



        auth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();

        binding.feedrecview.setLayoutManager(new LinearLayoutManager(this));
        postAdapter=  new  PostAdapter(postArrayList);
        binding.feedrecview.setAdapter(postAdapter);

    }
    private void getData(){


        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(feedactivity.this,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
                if (value !=null){
                    for (DocumentSnapshot snapshot: value.getDocuments()){
                        Map<String, Object> data= snapshot.getData();
                        //casting
                        String userEmail = (String)  data.get("useremail");
                        String comment= (String) data.get("comment");
                        String downloadUrl= (String) data.get("downloadurl");

                        Post  post = new Post(userEmail,comment,downloadUrl);
                        postArrayList.add(post);

                    }
                    postAdapter.notifyDataSetChanged();

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post){
            Intent intentToUpload=new Intent(feedactivity.this,uploadactivity.class);
            startActivity(intentToUpload);

        }else if (item.getItemId()==R.id.signout){
            auth.signOut();

            Intent intentToMain= new Intent(feedactivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}