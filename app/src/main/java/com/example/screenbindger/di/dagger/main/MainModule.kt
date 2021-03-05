package com.example.screenbindger.di.dagger.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewAction
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewEvent
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewState
import com.example.screenbindger.view.fragment.details.DetailsViewAction
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import com.example.screenbindger.view.fragment.review.ReviewViewAction
import com.example.screenbindger.view.fragment.review.ReviewViewEvent
import com.example.screenbindger.view.fragment.review.ReviewViewState
import com.example.screenbindger.view.fragment.trending.TrendingViewAction
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewAction
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideUser(): UserEntity =
        UserEntity()

    @MainScope
    @Provides
    fun provideChangeableFragmentStateObservable(): FragmentStateObservable {
        return FragmentStateObservable()
    }

    @MainScope
    @Provides
    fun provideUserState(userEntity: UserEntity): ObjectState<UserEntity> {
        return ObjectState.InitialState(userEntity)
    }

    @MainScope
    @Provides
    fun provideProfilePictureUri(): MutableLiveData<Uri?> {
        return MutableLiveData(null)
    }

    @MainScope
    @Provides
    fun provideUserStateObservable(
        userState: ObjectState<UserEntity>,
        profilePictureUri: MutableLiveData<Uri?>,
        userEntity: UserEntity
    ): UserStateObservable {
        return UserStateObservable(MutableLiveData(userState), profilePictureUri, userEntity)
    }

    @MainScope
    @Provides
    fun provideTrendingViewState(): MutableLiveData<TrendingViewState> =
        MutableLiveData(TrendingViewState.Fetching)

    @MainScope
    @Provides
    fun provideTrendingViewAction(): MutableLiveData<TrendingViewAction> =
        MutableLiveData(TrendingViewAction.FetchMovies)


    @MainScope
    @Provides
    fun provideUpcomingViewState(): MutableLiveData<UpcomingViewState> =
        MutableLiveData(UpcomingViewState.Fetching)

    @MainScope
    @Provides
    fun provideUpcomingViewAction(): MutableLiveData<UpcomingViewAction> =
        MutableLiveData(UpcomingViewAction.FetchMovies)

    @MainScope
    @Provides
    fun provideMovieDetailsViewState(): DetailsFragmentViewState {
        return DetailsFragmentViewState()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewAction(): MutableLiveData<Event<DetailsViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewEvent(): MutableLiveData<Event<DetailsViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewState(): MutableLiveData<FavoritesViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewAction(): MutableLiveData<Event<FavoritesViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewEvent(): MutableLiveData<Event<FavoritesViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewState(): MutableLiveData<ReviewViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewAction(): MutableLiveData<Event<ReviewViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewEvent(): MutableLiveData<Event<ReviewViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideCoroutineIo() = CoroutineScope(IO)


}