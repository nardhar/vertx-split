package org.nardhar.vertxsplit.controller.module;

import dagger.Module;
import dagger.Provides;
import org.nardhar.vertxsplit.controller.BookController;
import org.nardhar.vertxsplit.domain.BookDomain;

@Module
public class BookControllerModule {

    @Provides
    public BookController provideServiceController(BookDomain bookDomain) {
        return new BookController(bookDomain);
    }
}
