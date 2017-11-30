package me.hgo.invitation;

interface CodeRepo {

    boolean isCodeExists(String digitCode);

    String getUserCode(CharSequence id);

    void createNewCode(Code entity);

    Code findByCode(String code);
}
