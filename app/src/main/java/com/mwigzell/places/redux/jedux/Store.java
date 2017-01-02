package com.mwigzell.places.redux.jedux;

import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.redux.original.Subscription;

import java.util.ArrayList;
import java.util.List;

public final class Store<A, S> {

    public interface Reducer<A, S> {
        S reduce(A action, S currentState);
    }

    public interface Middleware<A, S> {
        void dispatch(Store<A, S> store, A action, NextDispatcher<A> next);
    }

    public interface NextDispatcher<A> {
        void dispatch(A action);
    }

    private S currentState;

    private final Reducer<A, S> reducer;
    private final List<Subscriber> subscribers;

    private final Middleware<A, S> dispatcher = new Middleware<A, S>() {
        @Override
        public void dispatch(Store<A, S> store, A action, NextDispatcher<A> next) {
            synchronized (this) {
                currentState = store.reducer.reduce(action, currentState);
            }
            for (int i = 0; i < subscribers.size(); i++) {
                store.subscribers.get(i).onStateChanged();
            }
        }
    };

    private final List<NextDispatcher<A>> next = new ArrayList<>();

    public Store(Reducer<A, S> reducer, S state, Middleware<A, S> ...middlewares) {
        this.reducer = reducer;
        this.currentState = state;
        this.subscribers = new ArrayList<>();

        this.next.add(new NextDispatcher<A>() {
            public void dispatch(A action) {
                Store.this.dispatcher.dispatch(Store.this, action, null);
            }
        });
        for (int i = middlewares.length-1; i >= 0; i--) {
            final Middleware<A, S> mw = middlewares[i];
            final NextDispatcher<A> n = next.get(0);
            next.add(0, new NextDispatcher<A>() {
                public void dispatch(A action) {
                    mw.dispatch(Store.this, action, n);
                }
            });
        }
    }

    public S dispatch(A action) {
        this.next.get(0).dispatch(action);
        return this.getState();
    }

    public S getState() {
        return this.currentState;
    }

    public Subscription subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        return Subscription.create(subscribers, subscriber);
    }
}
