package com.mycompany.ebayservice;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;

import org.json.JSONObject;
import org.json.JSONException;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookSdk;
import com.facebook.internal.WebDialog;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
// new stuff
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
// neew stuff

public class MainActivity extends Activity {


    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // DropDown Layout
        Spinner dropdown = (Spinner)findViewById(R.id.dropdown_SortBy);
        String[] items = new String[]{"Best Match", "Price: highest first", "Price + Shipping: highest first", "Price + Shipping: lowest first"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        // Need to validate and stuff. If it passes call the following function.

        // Clear BUTTON
        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((TextView)findViewById(R.id.errorMssg2)).setText("");
                ((TextView)findViewById(R.id.errorMssg)).setText("");
                Spinner dropdown = (Spinner)findViewById(R.id.dropdown_SortBy);
                dropdown.setAdapter(adapter);
                EditText key = ((EditText) findViewById(R.id.text_Keywords));
                EditText from = (EditText) findViewById(R.id.text_PriceFrom);
                EditText to = (EditText) findViewById(R.id.text_PriceTo);
                key.setText("");
                from.setText("");
                to.setText("");

            }

        });


        // Send button
        Button searchButton = (Button) findViewById(R.id.button_send);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ((TextView)findViewById(R.id.errorMssg)).setText("");
                ((TextView)findViewById(R.id.errorMssg2)).setText("");
                EditText key = ((EditText) findViewById(R.id.text_Keywords));
                EditText from = (EditText) findViewById(R.id.text_PriceFrom);
                EditText to = (EditText) findViewById(R.id.text_PriceTo);
                Spinner spinner=(Spinner) findViewById(R.id.dropdown_SortBy);
                String from_input=(from.getText().toString());
                String to_input=(to.getText().toString());
                String key_input=Uri.encode(key.getText().toString());
                String spinner_select=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Log.d("log_tag", spinner_select);
                String str="";
                if(!validate_key(key_input)){
                    str+="The keyword must not be empty\n";

                }

                ((TextView)findViewById(R.id.errorMssg)).setText(str);


                if(str=="") {
                    String result = "";
                    //http://ebayservice-env.elasticbeanstalk.com/index.php
                    //http://cs-server.usc.edu:28013/HW8/index.php
                    //from=from_input
                    //to=to_input
                    //sort=spinner_select
                    String str_para="";
                    str_para="http://ebayservice-env.elasticbeanstalk.com/index.php";
                    str_para+="?"+"key="+key_input;
                    str_para+="&"+"from="+from_input;
                    str_para+="&"+"to="+to_input;
                    str_para+="&paginationInput.pageNumber=1&paginationInput.entriesPerPage=5";
                    //str_para+="&"+"sort="+spinner_select;
                    /*
                    <option value="BestMatch" selected>Best Match</option>
                                <option value="CurrentPriceHighest">Price: highest first</option>
                                <option value="PricePlusShippingHighest">Price + Shipping: highest first</option>
                                <option value="PricePlusShippingLowest">Price + Shipping: lowest first</option>
                     */
                    //{"Best Match", "Price: highest first", "Price + Shipping: highest first", "Price + Shipping: lowest first"}
                    if(spinner_select.equals("Best Match")){
                        spinner_select="BestMatch";
                    }
                    else if(spinner_select.equals("Price: highest first")){
                        spinner_select="CurrentPriceHighest";
                    }
                    else if(spinner_select.equals("Price + Shipping: highest first")){
                        spinner_select="PricePlusShippingHighest";
                    }
                    else if (spinner_select.equals("Price + Shipping: lowest first")){
                        spinner_select="PricePlusShippingLowest";
                    }

                    str_para+="&"+"sort="+spinner_select;

                    String[] url_pass = {str_para};
                    new getXML().execute(url_pass);
                    Log.d("log_tag", "Working! URL GENERATED => " + result);
                }

            }

        });



        //((TextView)findViewById(R.id.errorMssg)).setText("Your Text");
        // Need to validate and stuff. If it passes call the following function.

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Post Cancelled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void  onSuccess(Sharer.Result result){
                Toast.makeText(getApplicationContext(), "Posted Story with a success", Toast.LENGTH_SHORT).show();
            }
        });
        //shareOnFacebook("This is test...");

        /*Button butt1= (Button) findViewById(R.id.pub);
        butt1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                i.putExtra("hey","there");
                startActivity(i);
            }

        });*/


        /*// Send button
        Button searchButton = (Button) findViewById(R.id.button_send);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String result="";
                new getXML().execute("http://cs-server.usc.edu:28013/HW8/index.php","abc",result);
                Log.d("log_tag","Working! URL GENERATED => "+result);

            }

        });*/
        // New stuff

        /*
        Log.e("Test", "Before ImageView!!!");
        Button butt1= (Button) findViewById(R.id.pub);

        butt1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ImageView imageView = (ImageView) findViewById(R.id.samplePhoto);
                String url="http://i.kinja-img.com/gawker-media/image/upload/s--hUWAg1eC--/wsgtilb9ibbxysybe3mu.png";
                new ImageLoadTask(url,imageView).execute();
            }

        });

        Log.e("Test", "After ImageView!!!");
        */

    }

    // New STFF

    class getXML extends AsyncTask<String,Void,String>{

        private Exception exception;
        @Override
        protected String doInBackground(String... url){

            InputStream is = null;          //Returned from Server
            String result = null;
            StringBuilder  sb = null;

            try{

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url[0]);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                is = entity.getContent();               // Retrieves the content from eBay Server



            }catch (Exception e){
                Log.e("log_tag", "Error in http connection" + e.toString());
                return null;
            }

            //Generating a String from the inputStream returned from the eBay Server
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                sb = new StringBuilder();
                sb.append(reader.readLine()+"\n");
                String line="0";

                while((line=reader.readLine())!=null){
                    sb.append(line+"\n");
                }

                is.close();
                result=sb.toString();


                return result;
            }
            catch (Exception e){
                return null;
            }

        }

        protected void onPostExecute(String result){
            Intent i = new Intent(getApplicationContext(),ResultActivity.class);
            String[] array=new String[2];
            array[0]=result;
            String ackStr="";
            try {
                JSONObject jsonObject = new JSONObject(result);
                ackStr = jsonObject.getString("ack");
            }
            catch(Exception e){
            }
            if(ackStr.equals("Success")) {
                EditText key = (EditText) findViewById(R.id.text_Keywords);
                array[1] = "'" + key.getText().toString() + "'";
                //i.putExtra("var",array);
                ArrayList<String> al = new ArrayList<String>();
                //Convert string array to a collection
                Collection l = Arrays.asList(array);
                al.addAll(l);
                i.putStringArrayListExtra("var", al);
                startActivity(i);
            }
            else{
                ((TextView)findViewById(R.id.errorMssg2)).setText("No Results found");

            }

        }
    }
    private boolean validate_key(String content) {
        if(content.length()==0){
            return false;
        }
        return true;
    }
    // New STFF
    private void shareOnFacebook(String content) {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(content)
                    .setContentDescription(
                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .setImageUrl(Uri.parse("http://i.kinja-img.com/gawker-media/image/upload/s--hUWAg1eC--/wsgtilb9ibbxysybe3mu.png"))
                    .build();

           shareDialog.show(linkContent);
        }
        else{
            Toast.makeText(this, "NOT CALLED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        // Facebook web
        Button post = (Button)findViewById(R.id.pub);
        post.setOnClickListener(new View.OnClickListener() {

            private void publishFeedDialog(){

                Bundle params = new Bundle();
                params.putString("name", "Facebook SDK for Android");
                params.putString("caption", "Build great social apps and get more installs.");
                params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
                params.putString("link", "https://developers.facebook.com/android");
                params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");


                WebDialog feedDialog = (
                        new WebDialog.FeedDialogBuilder(this,Session.getActiveSession(),params)).setOnCompleteListener(new WebDialog.OnCompleteListener() {

                            @Override
                            public void onComplete(Bundle values,FacebookException error) {
                                if (error == null) {
                                    // When the story is posted, echo the success
                                    // and the post Id.
                                    final String postId = values.getString("post_id");
                                    if (postId != null) {
                                        Toast.makeText(getApplicationContext(),
                                                "Posted story, id: " + postId,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // User clicked the Cancel button
                                        Toast.makeText(getApplicationContext(),
                                                "Publish cancelled",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else if (error instanceof FacebookOperationCanceledException) {
                                    // User clicked the "x" button
                                    Toast.makeText(getApplicationContext(),
                                            "Publish cancelled",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Generic, ex: network error
                                    Toast.makeText(getApplicationContext(),
                                            "Error posting story",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        })
                        .build();
                feedDialog.show();
            }



            @Override
            public void onClick(View v) {
                publishFeedDialog();

            }
        });

        // Facebook Web


            post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ();

            }
        });

        setContentView(R.layout.activity_main);
    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
