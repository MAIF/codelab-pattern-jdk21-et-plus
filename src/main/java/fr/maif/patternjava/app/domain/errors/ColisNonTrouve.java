package fr.maif.patternjava.app.domain.errors;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=false)
public class ColisNonTrouve extends ColisException {
    public final String reference;
}
