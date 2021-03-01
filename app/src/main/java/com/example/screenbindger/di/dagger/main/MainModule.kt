package com.example.screenbindger.di.dagger.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewAction
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewEvent
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewState
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewAction
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewAction
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewEvent
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewState
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewAction
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentViewAction
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentViewState
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
    fun provideTrendingViewState(): MutableLiveData<TrendingFragmentViewState> =
        MutableLiveData(TrendingFragmentViewState())

    @MainScope
    @Provides
    fun provideTrendingViewAction(): MutableLiveData<TrendingFragmentViewAction> =
        MutableLiveData(TrendingFragmentViewAction.FetchMovies)


    @MainScope
    @Provides
    fun provideUpcomingViewState(): MutableLiveData<UpcomingFragmentViewState> =
        MutableLiveData(UpcomingFragmentViewState())

    @MainScope
    @Provides
    fun provideUpcomingViewAction(): MutableLiveData<UpcomingFragmentViewAction> =
        MutableLiveData(UpcomingFragmentViewAction.FetchMovies)

    @MainScope
    @Provides
    fun provideMovieDetailsViewState(): DetailsFragmentViewState {
        return DetailsFragmentViewState()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewAction(): MutableLiveData<Event<DetailsFragmentViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideMovieDetailsViewEvent(): MutableLiveData<Event<DetailsFragmentViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewState(): MutableLiveData<FavoriteMoviesFragmentViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewAction(): MutableLiveData<Event<FavoriteMoviesFragmentViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideFavoriteMoviesViewEvent(): MutableLiveData<Event<FavoriteMoviesFragmentViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewState(): MutableLiveData<ReviewFragmentViewState> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewAction(): MutableLiveData<Event<ReviewFragmentViewAction>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideReviewFragmentViewEvent(): MutableLiveData<Event<ReviewFragmentViewEvent>> {
        return MutableLiveData()
    }

    @MainScope
    @Provides
    fun provideCoroutineIo() = CoroutineScope(IO)


}