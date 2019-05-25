package ferinaramdaning.com.project_uas;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ferinaramdaning.com.project_uas.adapter.Recycler_Adapter;
import ferinaramdaning.com.project_uas.client.BookService;
import ferinaramdaning.com.project_uas.pojo.Book;
import ferinaramdaning.com.project_uas.pojo.Books;
import ferinaramdaning.com.project_uas.pojo.ImageLinks;
import ferinaramdaning.com.project_uas.pojo.Item;
import ferinaramdaning.com.project_uas.pojo.RetailPrice;
import ferinaramdaning.com.project_uas.pojo.SaleInfo;
import ferinaramdaning.com.project_uas.pojo.VolumeInfo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class EBookFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "RecyclerViewFragment";
    private List<Book> bookList = new ArrayList<>();
    private Recycler_Adapter adapter;
    private View rootView;
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EBookFragment() {
        // Required empty public constructor
    }

    public static EBookFragment newInstance(String param1, String param2) {
        EBookFragment fragment = new EBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ebook,container,false);
        rootView.setTag(TAG);
        setRecycler();
        return rootView;
    }

    private void setRecycler() {
        recyclerView = (RecyclerView)rootView.findViewById(R.id.booksapp_recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        networkcall();
    }

    private void networkcall() {

        String API_BASE_URL = "https://www.googleapis.com/books/v1/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build())
                .build();

        BookService ebooksInterface = retrofit.create(BookService.class);
        Call<Books> call = ebooksInterface.geteBooks();
        call.enqueue(new Callback<Books>() {
            @Override
            public void onResponse(@NonNull Call<Books> call, @NonNull Response<Books> response) {

                if(response.isSuccessful()){
                    List<Item> itemList = response.body().getItems();
                    for (Item item : itemList) {
                        VolumeInfo volumeInfo = item.getVolumeInfo();
                        SaleInfo salesinfo = item.getSaleInfo();
                        RetailPrice retail_price = salesinfo.getRetailPrice();
                        String title = volumeInfo.getTitle();
                        List<String> authList = volumeInfo.getAuthors();
                        String authors= "";Integer pages = 0;
                        if(authList.size() == 0)
                            authors = "by Unknown";
                        else
                            authors = "by "+authList.get(0);
                        ImageLinks links = volumeInfo.getImageLinks();
                        String imageUrl = links.getSmallThumbnail();
                        double rating = 0,price= 0;
                        if(volumeInfo.getAverageRating() != null){
                            rating = volumeInfo.getAverageRating();
                        }else {
                            rating = 1;
                        }

                        if(retail_price.getAmount() != null){
                            price = retail_price.getAmount();
                        }else {
                            price = 0.00;
                        }
                        int count = volumeInfo.getPageCount();
                        if(count != 0){
                            pages = volumeInfo.getPageCount();
                        }else {
                            pages = 0;
                        }
                        bookList.add(new Book(title, authors, imageUrl, rating, price, pages));
                    }
                    adapter = new Recycler_Adapter(getActivity(),bookList);
                    recyclerView.setAdapter(adapter);

                }else {
                    Toast.makeText(getContext(), response.body()+"", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Books> call, @NonNull Throwable t) {

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
