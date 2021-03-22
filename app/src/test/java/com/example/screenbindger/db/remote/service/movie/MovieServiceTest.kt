package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails
import com.example.screenbindger.db.remote.response.movie.trailer.TrailersResponse
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.ShowListViewState
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

const val MOVIE_ID = 1

@ExperimentalCoroutinesApi
class MovieServiceTest {

    @Mock
    private lateinit var api: MovieApi
    private lateinit var movieService: MovieService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieService = MovieService(api)
    }



    @Test
    fun getUpcoming_listNotEmpty_returnsFetched() = runBlockingTest {
        whenever(api.getUpcomingMovies(MOVIE_ID)).thenReturn(responseSuccess_movieListPopulated())

        val viewState: ShowListViewState = movieService.getUpcoming(MOVIE_ID)

        assertThat(viewState, instanceOf(ShowListViewState.Fetched.Movies::class.java))
    }

    @Test
    fun getUpcoming_listEmpty_returnsFetched() = runBlockingTest {
        whenever(api.getUpcomingMovies(MOVIE_ID)).thenReturn(responseSuccess_movieListNotPopulated())

        val viewState = movieService.getUpcoming(MOVIE_ID)

        assertThat(viewState, instanceOf(ShowListViewState.Fetched.Movies::class.java))
    }

    @Test
    fun getUpcoming_responseError_returnsError() = runBlockingTest {
        whenever(api.getUpcomingMovies(MOVIE_ID)).thenReturn(responseError_movie())

        val viewState = movieService.getUpcoming(MOVIE_ID)

        assertThat(viewState, instanceOf(ShowListViewState.NotFetched::class.java))
    }

    @Test
    fun getMovieTrailersInfo_listNotEmpty_returnFetched() = runBlockingTest {
        whenever(api.getMovieTrailers(movieId = MOVIE_ID)).thenReturn(
            responseSuccess_movieTrailerListPopulated()
        )

        val viewEvent = movieService.getMovieTrailersInfo(MOVIE_ID)

        assertThat(viewEvent, instanceOf(DetailsViewEvent.TrailersFetched::class.java))
    }

    @Test
    fun getMovieTrailersInfo_listEmpty_returnFetched() = runBlockingTest {
        whenever(api.getMovieTrailers(movieId = MOVIE_ID)).thenReturn(
            responseSuccess_movieTrailerListEmpty()
        )

        val viewEvent = movieService.getMovieTrailersInfo(MOVIE_ID)

        assertThat(viewEvent, instanceOf(DetailsViewEvent.TrailersNotFetched::class.java))
    }

    @Test
    fun getMovieTrailersInfo_responseError_returnsError() = runBlockingTest {
        whenever(api.getMovieTrailers(movieId = MOVIE_ID)).thenReturn(
            responseError_movieTrailerList()
        )

        val viewEvent = movieService.getMovieTrailersInfo(MOVIE_ID)

        assertThat(viewEvent, instanceOf(DetailsViewEvent.TrailersNotFetched::class.java))
    }

    private fun responseSuccess_movieListPopulated(): Response<MoviesResponse> {
        val m1 = ShowEntity()
        val m2 = ShowEntity()
        return Response.success(MoviesResponse(1, 12, listOf(m1, m2)))
    }

    private fun responseSuccess_movieListNotPopulated(): Response<MoviesResponse> {
        return Response.success(MoviesResponse(1, 12, emptyList()))
    }

    private fun responseError_movie(): Response<MoviesResponse> {
        val errorResponse =
            ("{\n" + "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}")
                .toByteArray()
                .toResponseBody()
        return Response.error(404, errorResponse)
    }

    private fun responseSuccess_movieTrailerListPopulated(): Response<TrailersResponse> {
        val trailer1 = TrailerDetails()
        val trailer2 = TrailerDetails()
        return Response.success(TrailersResponse(MOVIE_ID, listOf(trailer1, trailer2)))
    }

    private fun responseSuccess_movieTrailerListEmpty(): Response<TrailersResponse> {
        return Response.success(TrailersResponse(MOVIE_ID, emptyList()))
    }

    private fun responseError_movieTrailerList(): Response<TrailersResponse> {
        val errorResponse =
            ("{\n" + "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}")
                .toByteArray()
                .toResponseBody()
        return Response.error(404, errorResponse)
    }

}


