package me.hgo.invitation;

import java.util.List;

interface CreditRepo {

    List<Transaction> gainsOf(CharSequence id);

    Transaction createNewTransaction(Transaction transaction);
}
