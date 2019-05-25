package ferinaramdaning.com.project_uas;


import android.net.Uri;
import android.os.Bundle;
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
public class Books_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private static final String TAG = "RecyclerViewFragment";
    private List<Book> bookList = new ArrayList<>();
    private Recycler_Adapter adapter;
    private View rootView;

    public Books_Fragment() {
        // Required empty public constructor
    }

    public static Books_Fragment newInstance(String param1, String param2){
        Books_Fragment fragment = new Books_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_books, container,false );
        rootView.setTag(TAG);
        setRecycler();
        return rootView;
    }

    private void setRecycler(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.booksapp_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        networkcall();
    }

    private void networkcall(){
        String API_BASE_URL = "https://www.googleapis.com/books/v1/";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build())
                .build();

        BookService bookService = retrofit.create(BookService.class);

        Call<Books> call = bookService.getBooks();
        call.enqueue(new Callback<Books>() {
            @Override
            public void onResponse(Call<Books> call, retrofit2.Response<Books> response) {
                if (response.isSuccessful()){
                    List<Item> itemList = response.body().getItems();
                    for (Item item : itemList){
                        VolumeInfo volumeInfo = item.getVolumeInfo();
                        SaleInfo saleInfo = item.getSaleInfo();
                        RetailPrice retailPrice = saleInfo.getRetailPrice();
                        String tittle = volumeInfo.getTitle();
                        List<String> authList = volumeInfo.getAuthors();
                        String authors = "";Integer pages = 0;
                        if (authList.size() == 0)
                            authors = "by Unknown";
                        else
                            authors = "by "+authList.get(0);
                        ImageLinks links = volumeInfo.getImageLinks();
                        String imageUrl = links.getSmallThumbnail();
                        double rating = 0, price = 0;
                        if (volumeInfo.getAverageRating() != null){
                            rating = volumeInfo.getAverageRating();
                        }else {
                            rating = 1;
                        }

                        if (retailPrice.getAmount() != null){
                            price = retailPrice.getAmount();
                        }else {
                            price = 0.00;
                        }

                        if (volumeInfo.getPageCount() != 0){
                            pages = volumeInfo.getPageCount();
                        }else {
                            pages = 0;
                        }
                        bookList.add(new Book(tittle, authors, imageUrl, rating, price, pages));
                    }
                    adapter = new Recycler_Adapter(getActivity(), bookList);
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), response.body()+"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Books> call, Throwable t) {

            }
        });
    }

    public void onButtonPressed(Uri uri){
        if (mListener != null){
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
