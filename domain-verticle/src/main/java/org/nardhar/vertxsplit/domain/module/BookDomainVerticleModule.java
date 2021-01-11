package org.nardhar.vertxsplit.domain.module;

import dagger.Module;
import dagger.Provides;
import org.nardhar.vertxsplit.domain.BookDomain;
import org.nardhar.vertxsplit.domain.wrapper.BookDomainVerticleWrapper;

@Module
public class BookDomainVerticleModule {

    @Provides
    public BookDomain provideServiceDomain(BookDomainVerticleWrapper serviceDomainVerticleWrapper) {
        return serviceDomainVerticleWrapper;
    }

}
