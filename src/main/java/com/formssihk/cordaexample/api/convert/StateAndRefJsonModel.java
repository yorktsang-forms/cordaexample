package com.formssihk.cordaexample.api.convert;

import lombok.Data;
import net.corda.core.contracts.StateRef;

@Data
public class StateAndRefJsonModel<T> {
    StateRef ref;
    T _data;
    String _type;
}
