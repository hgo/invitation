package me.hgo.invitation;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class MemCodeRepo implements CodeRepo {
    HashMap<UUID, Code> map = new HashMap<>();

    @Override
    public boolean isCodeExists(String digitCode) {
        return findByCode(digitCode) != null;
    }

    @Override
    public String getUserCode(CharSequence id) {
        Optional<Code> code = map.values().stream().filter(c -> c.getSubscriber().getId().equals(id)).findFirst();
        return code.isPresent() ? code.get().getCode() : null;
    }

    @Override
    public void createNewCode(Code entity) {
        map.put(UUID.randomUUID(), entity);
    }

    @Override
    public Code findByCode(String code) {
        Optional<Code> codeEntity = map.values().stream().filter(c -> c.getCode().equals(code)).findFirst();
        return codeEntity.isPresent() ? codeEntity.get() : null;
    }
}
