package com.mycompany.ebayservice;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ResultActivity extends Activity {

    //Identify each of the Card Values here -
    TextView[] sTxt= new TextView[5];
    TextView[] ssTxt= new TextView[5];
    ImageView[] sPTxt= new ImageView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Instantiate the Card Items :
        sTxt[0]=(TextView)findViewById(R.id.sampleText1);
        sTxt[1]=(TextView)findViewById(R.id.sampleText2);
        sTxt[2]=(TextView)findViewById(R.id.sampleText3);
        sTxt[3]=(TextView)findViewById(R.id.sampleText4);
        sTxt[4]=(TextView)findViewById(R.id.sampleText5);

        ssTxt[0]=(TextView)findViewById(R.id.sampleSubText1);
        ssTxt[2]=(TextView)findViewById(R.id.sampleSubText3);
        ssTxt[3]=(TextView)findViewById(R.id.sampleSubText4);
        ssTxt[4]=(TextView)findViewById(R.id.sampleSubText5);

        sPTxt[0]=(ImageView)findViewById(R.id.samplePhoto1);
        sPTxt[1]=(ImageView)findViewById(R.id.samplePhoto2);
        sPTxt[2]=(ImageView)findViewById(R.id.samplePhoto3);
        sPTxt[3]=(ImageView)findViewById(R.id.samplePhoto4);
        sPTxt[4]=(ImageView)findViewById(R.id.samplePhoto5);

        String top="";
        String result="";
        ArrayList<String> getStuff;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            getStuff = extras.getStringArrayList("var");
            result=getStuff.get(0);
            top=getStuff.get(1);
        }

        // Converting the result String to a JSON Object and then picking the individual items
        try
        {

            JSONObject jsonObject = new JSONObject(result);
            String  ackStr = jsonObject.getString("ack");
            int resultCount = Integer.parseInt(jsonObject.getString("resultCount"));

            if(resultCount>5)
                resultCount=5;

            //Populate the CardViews :

            for(int i=0;i<resultCount;i++)
            {
                String item = "item"+i;
                String photo="samplePhoto"+i;

                String itemTitle = jsonObject.getJSONObject(item).getJSONObject("basicInfo").getString("title");
                String itemPrice = "Price : $"+jsonObject.getJSONObject(item).getJSONObject("basicInfo").getString("convertedCurrentPrice");
                String shippingCost = jsonObject.getJSONObject(item).getJSONObject("basicInfo").getString("shippingServiceCost");
                String itemPicUrl = jsonObject.getJSONObject(item).getJSONObject("basicInfo").getString("galleryURL");
                if(itemPicUrl.length()!=0)
                    new ImageLoadTask(itemPicUrl,sPTxt[i]).execute();
                else
                    new ImageLoadTask("http://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/NA_gills_icon.svg/300px-NA_gills_icon.svg.png",sPTxt[i]).execute();


                if(shippingCost.length()==0)
                    itemPrice+=" (Free Shipping)";
                else
                {
                    double itemshippingCost = Double.parseDouble(shippingCost);
                    if (itemshippingCost == 0)
                        itemPrice+=" (Free Shipping)";
                    else
                        itemPrice += " (+ $" + itemshippingCost + " Shipping) ";
                }

                // set link to image
                final String itemURL=jsonObject.getJSONObject(item).getJSONObject("basicInfo").getString("viewItemURL");
                sPTxt[i].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        openWebURL(itemURL);

                    }
                });

                sTxt[i].setText(itemTitle);
                ssTxt[i].setText(itemPrice);
                //String s="cv1";
                android.support.v7.widget.CardView cardView;
                if(i==0) {
                    cardView = (android.support.v7.widget.CardView) findViewById(R.id.cv1);
                    cardView.setVisibility(View.VISIBLE);
                }
                else if (i==1) {
                    cardView = (android.support.v7.widget.CardView) findViewById(R.id.cv2);
                    cardView.setVisibility(View.VISIBLE);
                }
                else if (i==2) {
                    cardView = (android.support.v7.widget.CardView) findViewById(R.id.cv3);
                    cardView.setVisibility(View.VISIBLE);
                }
                else if (i==3) {
                    cardView = (android.support.v7.widget.CardView) findViewById(R.id.cv4);
                    cardView.setVisibility(View.VISIBLE);
                }
                else if(i==4) {
                    cardView = (android.support.v7.widget.CardView) findViewById(R.id.cv5);
                    cardView.setVisibility(View.VISIBLE);
                }

            }

            TextView resultsText = (TextView)findViewById(R.id.text_ResultsNumber);
            resultsText.setText("Results for "+top);

            RelativeLayout relativeLayoutObj = (RelativeLayout)findViewById(R.id.relativeLayoutResult);
            relativeLayoutObj.setVisibility(View.VISIBLE);

        }
        catch(JSONException e1)
        {
        }

        // THIRD STUFF
        final String finalResult = result;                      //Got to check if this alters implementation
        TextView text1 = (TextView) findViewById(R.id.sampleText1);
        TextView text2 = (TextView) findViewById(R.id.sampleText2);
        TextView text3 = (TextView) findViewById(R.id.sampleText3);
        TextView text4 = (TextView) findViewById(R.id.sampleText4);
        TextView text5 = (TextView) findViewById(R.id.sampleText5);
        text1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String item0="";

                try {
                    JSONObject jsonObject = new JSONObject(finalResult);
                    JSONObject item0Object = jsonObject.getJSONObject("item0");
                    item0=item0Object.toString();

                }catch (JSONException e1)
                {
                }

                Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                i.putExtra("item",item0);
                startActivity(i);
            }});

        text2.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String item0="";

                try {
                    JSONObject jsonObject = new JSONObject(finalResult);
                    JSONObject item0Object = jsonObject.getJSONObject("item1");
                    item0=item0Object.toString();

                }catch (JSONException e1)
                {
                }

                Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                i.putExtra("item",item0);
                startActivity(i);
            }});

        text3.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String item0="";

                try {
                    JSONObject jsonObject = new JSONObject(finalResult);
                    JSONObject item0Object = jsonObject.getJSONObject("item2");
                    item0=item0Object.toString();

                }catch (JSONException e1)
                {
                }

                Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                i.putExtra("item",item0);
                startActivity(i);
            }});

        text4.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String item0="";

                try {
                    JSONObject jsonObject = new JSONObject(finalResult);
                    JSONObject item0Object = jsonObject.getJSONObject("item3");
                    item0=item0Object.toString();

                }catch (JSONException e1)
                {
                }

                Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                i.putExtra("item",item0);
                startActivity(i);
            }});

        text5.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String item0="";

                try {
                    JSONObject jsonObject = new JSONObject(finalResult);
                    JSONObject item0Object = jsonObject.getJSONObject("item4");
                    item0=item0Object.toString();

                }catch (JSONException e1)
                {
                }

                Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                i.putExtra("item",item0);
                startActivity(i);
            }});
        // THIRD STUFF
    }

    public void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(inURL) );
        startActivity( browse );
    }
    //AsynTask to Load Images

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
