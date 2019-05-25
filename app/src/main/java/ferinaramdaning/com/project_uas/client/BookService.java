package ferinaramdaning.com.project_uas.client;

import ferinaramdaning.com.project_uas.pojo.Books;
import retrofit2.Call;
import retrofit2.http.GET;


public interface BookService {

    @GET("volumes?q=maxResults=10&key=AIzaSyDjRIsF0OJskdadlFe7eSp84svB_xtb-kw")
    Call<Books> getBooks();
}
