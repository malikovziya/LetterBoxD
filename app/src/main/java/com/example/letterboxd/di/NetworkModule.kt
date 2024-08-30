package com.example.letterboxd.di


import com.example.letterboxd.data.remote.api.MyApi
import com.example.letterboxd.data.remote.repository.CategoryMoviesRepositoryImpl
import com.example.letterboxd.data.remote.repository.CreditDetailsRepositoryImpl
import com.example.letterboxd.data.remote.repository.DetailsTabRepositoryImpl
import com.example.letterboxd.data.remote.repository.ExplorePageRepositoryImpl
import com.example.letterboxd.data.remote.repository.FilmCastsRepositoryImpl
import com.example.letterboxd.data.remote.repository.FilmCrewRepositoryImpl
import com.example.letterboxd.data.remote.repository.FilmDetailsRepositoryImpl
import com.example.letterboxd.data.remote.repository.FilmReviewsRepositoryImpl
import com.example.letterboxd.data.remote.repository.HomepageReviewRepositoryImpl
import com.example.letterboxd.data.remote.repository.LoginRepositoryImpl
import com.example.letterboxd.data.remote.repository.PersonDetailsRepositoryImpl
import com.example.letterboxd.data.remote.repository.PopularFilmRepositoryImpl
import com.example.letterboxd.data.remote.repository.ReviewDetailsRepositoryImpl
import com.example.letterboxd.data.remote.repository.SignUpRepositoryImpl
import com.example.letterboxd.domain.repository.CategoryMoviesRepository
import com.example.letterboxd.domain.repository.CreditDetailsRepository
import com.example.letterboxd.domain.repository.DetailsTabRepository
import com.example.letterboxd.domain.repository.ExplorePageRepository
import com.example.letterboxd.domain.repository.FilmCastsRepository
import com.example.letterboxd.domain.repository.FilmCrewRepository
import com.example.letterboxd.domain.repository.FilmDetailsRepository
import com.example.letterboxd.domain.repository.FilmReviewsRepository
import com.example.letterboxd.domain.repository.HomepageReviewsRepository
import com.example.letterboxd.domain.repository.LoginRepository
import com.example.letterboxd.domain.repository.PersonDetailsRepository
import com.example.letterboxd.domain.repository.PopularFilmRepository
import com.example.letterboxd.domain.repository.ReviewDetailsRepository
import com.example.letterboxd.domain.repository.SignUpRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun getClient() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getMyApi(client : Retrofit) : MyApi {
        return client.create(MyApi::class.java)
    }

    @Singleton
    @Provides
    fun getMovieRepository(api : MyApi) : PopularFilmRepository{
        return PopularFilmRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun getFriendsReviewRepository(api : MyApi) : HomepageReviewsRepository{
        return HomepageReviewRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun getHomePageCategoriesRepository(api : MyApi) : CategoryMoviesRepository{
        return CategoryMoviesRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun getFilmDetailsRepository(api : MyApi) : FilmDetailsRepository {
        return FilmDetailsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun getFilmCastsRepository(api : MyApi) : FilmCastsRepository {
        return FilmCastsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun getFilmCrewRepository(api : MyApi) : FilmCrewRepository {
        return FilmCrewRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun getFilmReviewsRepository(api : MyApi) : FilmReviewsRepository {
        return FilmReviewsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun getExplorePageRepository(api : MyApi) : ExplorePageRepository {
        return ExplorePageRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun getReviewDetailsRepository(api : MyApi) : ReviewDetailsRepository {
        return ReviewDetailsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun getCreditDetailsRepository(api : MyApi) : CreditDetailsRepository {
        return CreditDetailsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun getDetailsTabRepository(api : MyApi) : DetailsTabRepository {
        return DetailsTabRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun getPersonDetailsRepository(api : MyApi) : PersonDetailsRepository {
        return PersonDetailsRepositoryImpl(api)
    }
}