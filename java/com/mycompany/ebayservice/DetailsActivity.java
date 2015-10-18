package com.mycompany.ebayservice;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookSdk;
import com.facebook.internal.WebDialog;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;



import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;


public class DetailsActivity extends Activity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_details);

        String item="";
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            item = extras.getString("item");
            
        }
        String item_url="";
        try
        {
            JSONObject jsonObject = new JSONObject(item);

            if(jsonObject.getJSONObject("sellerInfo").getString("topRatedSeller").equals("true")) {
                ImageView imageViewtop = (ImageView) findViewById(R.id.topRatedPic);
                String urltop = "http://cs-server.usc.edu:45678/hw/hw8/itemTopRated.jpg";
                new ImageLoadTask(urltop, imageViewtop).execute();
            }

            //Image and stuff!
            item_url=jsonObject.getJSONObject("basicInfo").getString("viewItemURL");
            ImageView itemPic = (ImageView) findViewById(R.id.itemPic);
            ImageView itemPic2 = (ImageView) findViewById(R.id.itemPic2);
            String superImage=jsonObject.getJSONObject("basicInfo").getString("pictureURLSuperSize");
            if(superImage.length()==0) {
                String itemPicUrl = jsonObject.getJSONObject("basicInfo").getString("galleryURL");
                TextView tv = (TextView)findViewById(R.id.itemTitle);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tv.getLayoutParams();
                params.setMargins(0, 20, 0, 0);
                tv.setLayoutParams(params);
                if(itemPicUrl.length()!=0)
                    new ImageLoadTask(itemPicUrl,itemPic).execute();
                else
                    new ImageLoadTask("http://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/NA_gills_icon.svg/300px-NA_gills_icon.svg.png",itemPic).execute();
            }
            else{
                TextView tv = (TextView)findViewById(R.id.itemTitle);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tv.getLayoutParams();
                params.setMargins(0, 270, 0, 0);
                tv.setLayoutParams(params);
                new ImageLoadTask(superImage,itemPic2).execute();
            }



            TextView itemTitle = (TextView) findViewById(R.id.itemTitle);
            itemTitle.setText(jsonObject.getJSONObject("basicInfo").getString("title"));

            TextView itemPrice = (TextView) findViewById(R.id.itemPrice);
            TextView placeLocation = (TextView) findViewById(R.id.placeHold);
            String mainPrice = "Price : $"+jsonObject.getJSONObject("basicInfo").getString("convertedCurrentPrice");
            String shippingCost = jsonObject.getJSONObject("basicInfo").getString("shippingServiceCost");
            if(shippingCost.length()==0)
                mainPrice+=" (Free Shipping)";
            else
            {
                double itemshippingCost = Double.parseDouble(shippingCost);
                if (itemshippingCost == 0)
                    mainPrice+=" (Free Shipping)";
                else
                    mainPrice += " (+ $" + itemshippingCost + " Shipping) ";
            }
            itemPrice.setText(mainPrice);
            final String Loc = jsonObject.getJSONObject("basicInfo").getString("location");
            if(Loc.length()==0)
                placeLocation.setText("N/A");
            else
                placeLocation.setText(Loc);

            // FB stuff
            final String alpha=mainPrice;
            ImageView imageViewfb = (ImageView) findViewById(R.id.facebookPic);
            String urlfb="http://cs-server.usc.edu:45678/hw/hw8/fb.png";
            new ImageLoadTask(urlfb,imageViewfb).execute();
            final String as2 = jsonObject.getJSONObject("basicInfo").getString("galleryURL");
            final String url2=item_url;
            imageViewfb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    TextView itemTitle = (TextView) findViewById(R.id.itemTitle);
                   String temp="";
                    if(Loc.length()==0)
                        temp="N/A";
                    else
                    temp=Loc;

                    shareOnFacebook(itemTitle.getText().toString(),alpha,temp,as2,url2);
                    

                }
            });

            

            // Seller Info
            TextView ewf23r23r = (TextView) findViewById(R.id.usdfsdf);
            if(jsonObject.getJSONObject("sellerInfo").getString("sellerUserName").length()==0){
                awersdf34r24.setText("N/A");
            }
            else {
                awersdf34r24.setText(jsonObject.getJSONObject("sellerInfo").getString("sellerUserName"));
            }

            TextView asdflk12491o = (TextView) findViewById(R.id.sdf4235);
            if(jsonObject.getJSONObject("sellerInfo").getString("feedbackScore").length()==0){
                asdflk12491o.setText("N/A");
            }
            else {
                asdflk12491o.setText(jsonObject.getJSONObject("sellerInfo").getString("feedbackScore"));
            }

            TextView sdfsdf1414 = (TextView) findViewById(R.id.htfh345);
            if(jsonObject.getJSONObject("sellerInfo").getString("positiveFeedbackPercent").length()==0){
                sdfsdf1414.setText("N/A");
            }
            else {
                sdfsdf1414.setText(jsonObject.getJSONObject("sellerInfo").getString("positiveFeedbackPercent"));
            }

            TextView pkpj02u34 = (TextView) findViewById(R.id.dsfg345);
            if(jsonObject.getJSONObject("sellerInfo").getString("feedbackRatingStar").length()==0){
                pkpj02u34.setText("N/A");
            }
            else {
                pkpj02u34.setText(jsonObject.getJSONObject("sellerInfo").getString("feedbackRatingStar"));
            }

            if(jsonObject.getJSONObject("sellerInfo").getString("topRatedSeller").equals("false")){
                ImageView imageView = (ImageView) findViewById(R.id.topRatedValue);
                String url="https://cdn3.iconfinder.com/data/icons/musthave/256/Delete.png";
                new ImageLoadTask(url,imageView).execute();
            }
            else{
                ImageView imageView = (ImageView) findViewById(R.id.asdf34);
                String url="http://pixabay.com/static/uploads/photo/2012/04/26/19/45/check-42926_640.png";
                new ImageLoadTask(url,imageView).execute();
            }


            TextView sdlkfaw4jp24 = (TextView) findViewById(R.id.er32);
            if(jsonObject.getJSONObject("sellerInfo").getString("storeName").length()==0){
                sdlkfaw4jp24.setText("N/A");
            }
            else {
                sdlkfaw4jp24.setText(jsonObject.getJSONObject("sellerInfo").getString("storeName"));
            }

            // Shipping info
            TextView fsdfsdf2342 = (TextView) findViewById(R.id.afs34);

            if(jsonObject.getJSONObject("shippingInfo").getString("shippingType").length()==0){
                fsdfsdf2342.setText("N/A");
            }
            else {
                fsdfsdf2342.setText(jsonObject.getJSONObject("shippingInfo").getString("shippingType"));
            }

            TextView hrth35 = (TextView) findViewById(R.id.poii7);
            if(jsonObject.getJSONObject("shippingInfo").getString("handlingTime").length()==0){
                hrth35.setText("N/A");
            }
            else {
                hrth35.setText(jsonObject.getJSONObject("shippingInfo").getString("handlingTime"));
            }


            TextView fasdgh3h = (TextView) findViewById(R.id.q34t);
            if(jsonObject.getJSONObject("shippingInfo").getString("shipToLocations").length()==0){
                fasdgh3h.setText("N/A");
            }
            else {
                fasdgh3h.setText(jsonObject.getJSONObject("shippingInfo").getString("shipToLocations"));
            }

            if(jsonObject.getJSONObject("shippingInfo").getString("expeditedShipping").equals("false")){
                ImageView imageView = (ImageView) findViewById(R.id.43q);
                String url="https://cdn3.iconfinder.com/data/icons/musthave/256/Delete.png";
                new ImageLoadTask(url,imageView).execute();
            }
            else{
                ImageView imageView = (ImageView) findViewById(R.id.sdf);
                String url="http://pixabay.com/static/uploads/photo/2012/04/26/19/45/check-42926_640.png";
                new ImageLoadTask(url,imageView).execute();
            }

            if(jsonObject.getJSONObject("shippingInfo").getString("oneDayShippingAvailable").equals("false")){
                ImageView imageView = (ImageView) findViewById(R.id.as34);
                String url="https://cdn3.iconfinder.com/data/icons/musthave/256/Delete.png";
                new ImageLoadTask(url,imageView).execute();
            }
            else{
                ImageView imageView = (ImageView) findViewById(R.id.aesf23);
                String url="http://pixabay.com/static/uploads/photo/2012/04/26/19/45/check-42926_640.png";
                new ImageLoadTask(url,imageView).execute();
            }

            if(jsonObject.getJSONObject("shippingInfo").getString("returnsAccepted").equals("false")){
                ImageView imageView = (ImageView) findViewById(R.id.dfa3);
                String url="https://cdn3.iconfinder.com/data/icons/musthave/256/Delete.png";
                new ImageLoadTask(url,imageView).execute();
            }
            else{
                ImageView imageView = (ImageView) findViewById(R.id.fsd32);
                String url="http://pixabay.com/static/uploads/photo/2012/04/26/19/45/check-42926_640.png";
                new ImageLoadTask(url,imageView).execute();
            }
            // Basic Info
            TextView catNajujtyj645eValue = (TextView) findViewById(R.id.fdsa3);
            if(jsonObject.getJSONObject("basicInfo").getString("categoryName").length()==0){
                catNajujtyj645eValue.setText("N/A");
            }
            else {
                catNajujtyj645eValue.setText(jsonObject.getJSONObject("basicInfo").getString("categoryName"));
            }

            TextView hsdafhiisdf = (TextView) findViewById(R.id.vsad3);
            if(jsonObject.getJSONObject("basicInfo").getString("conditionDisplayName").length()==0){
                hsdafhiisdf.setText("N/A");
            }
            else {
                hsdafhiisdf.setText(jsonObject.getJSONObject("basicInfo").getString("conditionDisplayName"));
            }

            TextView thisfknsdfsdf = (TextView) findViewById(R.id.ihkj0);
            if(jsonObject.getJSONObject("basicInfo").getString("listingType").length()==0){
                thisfknsdfsdf.setText("N/A");
            }
            else {
                thisfknsdfsdf.setText(jsonObject.getJSONObject("basicInfo").getString("listingType"));
            }


        }catch (JSONException e1)
        {
        }
        final String  as=item_url;
        Button buyitnow = (Button) findViewById(R.id.buyNowButton);
        buyitnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openWebURL(as);
            }
        });

        final Button fasdfasdf = (Button) findViewById(R.id.basicInfoButton);
        final Button 1341 = (Button) findViewById(R.id.sellerInfoButton);
        final Button sadgsag = (Button) findViewById(R.id.shippingInfoButton);

        sdfae2e.setOnClickListener(new View.OnClickListener() {
            public fasdf onClick(View v) {
                sadf213r.setBackgroundColor(0xFFA0A3A3);
                sadf.setBackgroundColor(0xFFE8E8E8);
                koljoi32.setBackgroundColor(0xFFE8E8E8);

                RelativeLayout basicInfoLayout = (RelativeLayout) findViewById(R.id.basicInfoRelativeArray);
                bjasdfbogerg.setVisibility(View.VISIBLE);

                RelativeLayout sellerInfoLayout = (RelativeLayout) findViewById(R.id.sellerInfoRelativeArray);
                bjasdfbogergsfasdf.setVisibility(View.GONE);

                RelativeLayout shippingInfoLayout = (RelativeLayout) findViewById(R.id.shippingInfoRelativeArray);
                bjasdfbogergsdfsdfsdf.setVisibility(View.GONE);
            }

        });


        


        iiiop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sdfgtry.setBackgroundColor(0xFFA0A3A3);
                ytrewey.setBackgroundColor(0xFFE8E8E8);
                ertytr.setBackgroundColor(0xFFE8E8E8);

                RelativeLayout dbbd = (RelativeLayout) findViewById(R.id.basicInfoRelativeArray);
                dbbd.setVisibility(View.GONE);

                RelativeLayout mkonj = (RelativeLayout) findViewById(R.id.sellerInfoRelativeArray);
                mkonj.setVisibility(View.GONE);

                RelativeLayout iuhbiuhb = (RelativeLayout) findViewById(R.id.shippingInfoRelativeArray);
                iuhbiuhb.setVisibility(View.VISIBLE);
            }

        });

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
                String ff=result.getPostId();
                if(ff!=null){
                    Toast.makeText(getApplicationContext(), "Posted Story, ID: "+ff, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Post Cancelled", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sellerInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sdf432t24t.setBackgroundColor(0xFFA0A3A3);
                sdafas.setBackgroundColor(0xFFE8E8E8);
                sfsdf3.setBackgroundColor(0xFFE8E8E8);

                RelativeLayout sadfsaf = (RelativeLayout) findViewById(R.id.basicInfoRelativeArray);
                sadfsaf.setVisibility(View.GONE);

                RelativeLayout llkho = (RelativeLayout) findViewById(R.id.sellerInfoRelativeArray);
                llkho.setVisibility(View.VISIBLE);

                RelativeLayout llkho2 = (RelativeLayout) findViewById(R.id.shippingInfoRelativeArray);
                llkho2.setVisibility(View.GONE);
            }

        });

    }
    private void shareOnFacebook(String content,String de, String loc, String url, String url2) {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(content)
                    .setContentDescription(de+","+loc)
                    .setContentUrl(Uri.parse(url2))
                    .setImageUrl(Uri.parse(url))
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
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
