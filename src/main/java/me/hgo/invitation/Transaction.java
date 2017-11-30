package me.hgo.invitation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Transaction {

    @Setter
    Long id;
    int amount;
    CharSequence fromId;
    CharSequence toId;
}
