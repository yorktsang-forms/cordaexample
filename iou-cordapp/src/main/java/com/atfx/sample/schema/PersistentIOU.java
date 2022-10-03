package com.atfx.sample.schema;

import net.corda.core.schemas.PersistentState;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "iou_states")
public class PersistentIOU extends PersistentState {
    @Column(name = "user_public_key")
    String userPublicKey;

    @Column(name = "linear_id")
    @Type(type = "uuid-char")
    UUID linearId;

    public PersistentIOU(String userPublicKey, UUID linearId) {
        super();
        this.userPublicKey = userPublicKey;
        this.linearId = linearId;
    }

    public PersistentIOU() {
        this("", UUID.randomUUID());
    }
}