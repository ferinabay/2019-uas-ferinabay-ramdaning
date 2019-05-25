package ferinaramdaning.com.project_uas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ferinaramdaning.com.project_uas.R;
import ferinaramdaning.com.project_uas.pojo.Book;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {
    private List<Book> books = new ArrayList<>();
    private Context context;
    private String Free = "FREE";
    public Recycler_Adapter(Context context, List<Book> bookList) {
        this.context = context;
        books = bookList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_item,parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        Picasso.get().load(book.getImage()).into(holder.book_image);
        holder.book_title.setText(book.getTittle());
        if (book.getTittle().length() > 40)
            holder.book_title.append("...");
        String auth = book.getAuthors();
        holder.book_author.setText(auth);
        holder.book_ratingBar.setRating((float) book.getRating());
        if (book.getPrice() != 0.00)
            holder.books_page.setText(String.valueOf(book.getPrice()));
        else
            holder.book_price.setText(Free);
        String page = book.getPages()+" Pages";
        holder.books_page.setText(page);
    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView book_image;
        TextView book_title,book_author,book_price,books_page;
        RatingBar book_ratingBar;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            book_image = itemView.findViewById(R.id.booksapp_image);
            book_title = itemView.findViewById(R.id.booksapp_name);
            book_author = itemView.findViewById(R.id.booksapp_author);
            book_ratingBar = itemView.findViewById(R.id.booksapp_rating);
            book_price = itemView.findViewById(R.id.booksapp_price);
            books_page = itemView.findViewById(R.id.booksapp_pages);
        }
    }
}
