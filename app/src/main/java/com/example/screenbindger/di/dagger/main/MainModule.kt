package com.example.screenbindger.di.dagger.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.domain.cast.CastEntity
import com.example.screenbindger.model.state.ObjectState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.details.*
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewAction
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewEvent
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewState
import com.example.screenbindger.view.fragment.profile.FragmentStateObservable
import com.example.screenbindger.view.fragment.review.ReviewViewAction
import com.example.screenbindger.view.fragment.review.ReviewViewEvent
import com.example.screenbindger.view.fragment.review.ReviewViewState
import com.example.screenbindger.view.fragment.ShowListViewState
import com.example.screenbindger.view.fragment.genres.GenresViewState
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
    fun provideUpcomingViewState(): MutableLiveData<ShowListViewState> =
        MutableLiveData(ShowListViewState.Fetching)

    @MainScope
    @Provides
    fun provideGenresViewState(): MutableLiveData<GenresViewState> =
        MutableLiveData(GenresViewState.Loading)


    @MainScope
    @Provides
    fun provideShowViewState(): ShowViewState {
        return ShowViewState.Fetching
    }

    @MainScope
    @Provides
    fun provideCastsViewState(): CastsViewState {
        return CastsViewState.Fetching
    }

    @MainScope
    @Provides
    fun provideCastsList(): List<CastEntity> {
        return listOf()
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