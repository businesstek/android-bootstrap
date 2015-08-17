package com.bt.BootParse;

import android.accounts.AccountManager;
import android.content.Context;

import com.bt.BootParse.authenticator.ApiKeyProvider;
import com.bt.BootParse.authenticator.BootstrapAuthenticatorActivity;
import com.bt.BootParse.authenticator.LogoutService;
import com.bt.BootParse.core.BootstrapService;
import com.bt.BootParse.core.Constants;
import com.bt.BootParse.core.PostFromAnyThreadBus;
import com.bt.BootParse.core.RestAdapterRequestInterceptor;
import com.bt.BootParse.core.RestErrorHandler;
import com.bt.BootParse.core.TimerService;
import com.bt.BootParse.core.UserAgentProvider;
import com.bt.BootParse.ui.BootstrapTimerActivity;
import com.bt.BootParse.ui.CheckInsListFragment;
import com.bt.BootParse.ui.MainActivity;
import com.bt.BootParse.ui.NavigationDrawerFragment;
import com.bt.BootParse.ui.NewsActivity;
import com.bt.BootParse.ui.NewsListFragment;
import com.bt.BootParse.ui.UserActivity;
import com.bt.BootParse.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                TimerService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
