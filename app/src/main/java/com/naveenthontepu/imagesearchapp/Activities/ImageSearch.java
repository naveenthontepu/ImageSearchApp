package com.naveenthontepu.imagesearchapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.naveenthontepu.imagesearchapp.AnimatorEndListener;
import com.naveenthontepu.imagesearchapp.R;
import com.naveenthontepu.imagesearchapp.RecyclerViewAdapterViewHolders.SearchImagesResultAdaptor;
import com.naveenthontepu.imagesearchapp.Retrofit.ImageSearchApi;
import com.naveenthontepu.imagesearchapp.Retrofit.ImageSearchApiCallback;
import com.naveenthontepu.imagesearchapp.Utilities;
import com.naveenthontepu.imagesearchapp.image.ImageCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageSearch extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.searchEntryText)
    EditText searchEntryText;
    @Bind(R.id.searchResultImages)
    RecyclerView searchResultImages;
    @Bind(R.id.errorText)
    TextView errorText;
    @Bind(R.id.loading)
    ProgressBar loading;
    private Utilities utilities;
    List<String> imageUrls;
    SearchImagesResultAdaptor searchImagesResultAdaptor;
    ImageCache imageCache;
    final long ANIMATION_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int width = searchResultImages.getWidth();
        utilities.printLog("onPostResume width = " + width);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        utilities.printLog("onWindowFocusChanged");
        int width = searchResultImages.getWidth();
        utilities.printLog("width = " + width);
        if (searchImagesResultAdaptor == null) {
            searchImagesResultAdaptor = new SearchImagesResultAdaptor(imageUrls, this);
            searchResultImages.setLayoutManager(new StaggeredGridLayoutManager(width / 200, StaggeredGridLayoutManager.VERTICAL));
            searchResultImages.setAdapter(searchImagesResultAdaptor);
        }
    }

    private void initialize() {
        utilities = new Utilities();
        imageUrls = new ArrayList<>();
        imageCache = ImageCache.getInstance(this);
        utilities.printLog("image cache = " + imageCache);
        searchEntryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    utilities.printLog("image action search = " + EditorInfo.IME_ACTION_SEARCH);
                    preformSearch();
                }
                return false;
            }
        });
        errorText.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }


    private void preformSearch() {
        utilities.printLog("have to write the api code");
        String searchText = searchEntryText.getText().toString();
        if (validateText(searchText)) {
            if (utilities.isNetworkAvailable(this)) {
                setLoadingView();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEntryText.getWindowToken(), 0);
                utilities.printLog("api has to be written");
                String urlPart = "/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=200&pilimit=50&generator=prefixsearch&gpssearch=";
                String url = urlPart + searchText;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://en.wikipedia.org")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ImageSearchApi imageSearchApi = retrofit.create(ImageSearchApi.class);
                Call<JsonElement> imageSearchCall = imageSearchApi.getImageSearchApi(url);
                ImageSearchApiCallback imageSearchApiCallback = new ImageSearchApiCallback() {
                    @Override
                    public void processResponse(Response<JsonElement> response) {
                        processImageSearchResponse(response);
                    }

                    @Override
                    protected void processError(String message) {
                        setDoneView(message);
                    }
                };
                imageSearchCall.enqueue(imageSearchApiCallback);
            } else {
                errorText.setText("Unable to connect to Internet");
                setViewVisible(errorText);
            }
        }else {
            errorText.setText("Please enter text to search.");
            setViewVisible(errorText);
        }
    }

    private void setLoadingView() {
        setViewVisible(loading);
        if (errorText.getVisibility() == View.VISIBLE) {
            setViewGone(errorText);
        }
        if (searchResultImages.getVisibility() == View.VISIBLE) {
            setViewGone(searchResultImages);
        }
    }

    private void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.animate().alpha(1.0f).setDuration(300);
    }

    private void setViewGone(View view) {
        view.animate()
                .alpha(0.0f)
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorEndListener(View.GONE, view));
    }

    private void processImageSearchResponse(Response<JsonElement> response) {
        if (imageUrls != null && imageUrls.size() > 0) {
            utilities.printLog("image urls size = " + imageUrls.size());
            imageUrls.clear();
            searchImagesResultAdaptor.notifyDataSetChanged();
        }
        utilities.printLog("the response = " + response);
        utilities.printLog("the body = " + response.body().toString());
        try {
            JSONObject jsonObject = new JSONObject(response.body().toString());
            JSONObject query = jsonObject.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");
            Iterator<String> keys = pages.keys();
            int i = 0;
            while (keys.hasNext()) {
                try {
                    String url = pages.getJSONObject(keys.next()).getJSONObject("thumbnail").getString("source");
                    utilities.printLog("object " + i + " = " + url);
                    imageUrls.add(url);
                } catch (Exception e) {
                    utilities.printLog("object " + i + "= no source");
                }
                i += 1;
            }
            searchImagesResultAdaptor.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String defaultErrorText = "Your search - <b>"+searchEntryText.getText().toString()+"</b> did not match with any page title images";
        setDoneView(defaultErrorText);
    }

    private void setDoneView(String text) {
        setViewGone(loading);
        if (imageUrls != null && imageUrls.size() > 0) {
            setViewVisible(searchResultImages);
        } else {
            errorText.setText(Html.fromHtml(text));
            setViewVisible(errorText);
        }
    }

    private boolean validateText(String s) {
        s = s.trim();
        if (!s.equals("")) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.searchButton)
    public void onClick() {
        preformSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                if (imageCache != null) {
                    imageCache.clearDiskCache();
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageCache.clearMemoryCache();
    }
}
