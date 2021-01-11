package org.nardhar.vertxsplit.controller.module;

import dagger.Module;
import dagger.Provides;
import org.nardhar.vertxsplit.controller.ApiController;

@Module
public class ApiControllerModule {

    @Provides
    public ApiController provideApiController() {
        return new ApiController();
    }
}
