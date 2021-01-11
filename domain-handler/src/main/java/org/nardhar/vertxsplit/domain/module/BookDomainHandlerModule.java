package org.nardhar.vertxsplit.domain.module;

import dagger.Module;
import dagger.Provides;
import org.nardhar.vertxsplit.domain.BookDomain;
import org.nardhar.vertxsplit.domain.BookDomainHandler;

@Module
public class BookDomainHandlerModule {

    @Provides
    public BookDomain provideServiceDomain(BookDomainHandler bookDomainHandler) {
        return bookDomainHandler;
    }

}
