package com.mwigzell.places.redux.original;

import com.mwigzell.places.redux.AppState;

// f(Action, State) -> State
public interface Reducer<A extends Action, S extends State> {
    S call(A action, S state);
}
