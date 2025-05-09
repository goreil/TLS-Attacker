/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.message;

import de.rub.nds.modifiablevariable.ModifiableVariableFactory;
import de.rub.nds.modifiablevariable.singlebyte.ModifiableByte;
import de.rub.nds.tlsattacker.core.constants.HandshakeMessageType;
import de.rub.nds.tlsattacker.core.constants.KeyUpdateRequest;
import de.rub.nds.tlsattacker.core.protocol.handler.KeyUpdateHandler;
import de.rub.nds.tlsattacker.core.protocol.parser.KeyUpdateParser;
import de.rub.nds.tlsattacker.core.protocol.preparator.KeyUpdatePreparator;
import de.rub.nds.tlsattacker.core.protocol.serializer.KeyUpdateSerializer;
import de.rub.nds.tlsattacker.core.state.Context;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.util.Objects;

@XmlRootElement(name = "KeyUpdate")
public class KeyUpdateMessage extends HandshakeMessage {

    private ModifiableByte requestMode;

    public KeyUpdateMessage() {
        super(HandshakeMessageType.KEY_UPDATE);
        this.setIncludeInDigest(false);
    }

    @Override
    public KeyUpdateHandler getHandler(Context context) {
        return new KeyUpdateHandler(context.getTlsContext());
    }

    @Override
    public KeyUpdateParser getParser(Context context, InputStream stream) {
        return new KeyUpdateParser(stream, context.getTlsContext());
    }

    @Override
    public KeyUpdatePreparator getPreparator(Context context) {
        return new KeyUpdatePreparator(context.getChooser(), this);
    }

    @Override
    public KeyUpdateSerializer getSerializer(Context context) {
        return new KeyUpdateSerializer(this);
    }

    public final void setRequestMode(KeyUpdateRequest requestMode) {
        this.requestMode =
                ModifiableVariableFactory.safelySetValue(this.requestMode, requestMode.getValue());
    }

    public void setRequestMode(ModifiableByte requestMode) {
        this.requestMode = requestMode;
    }

    public ModifiableByte getRequestMode() {
        return this.requestMode;
    }

    @Override
    public String toShortString() {
        return "KU";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.requestMode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyUpdateMessage other = (KeyUpdateMessage) obj;
        return Objects.equals(this.requestMode, other.requestMode);
    }
}
