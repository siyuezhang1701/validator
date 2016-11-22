package com.thoughtworks.ketsu.domain;

public class EntityId extends AssertionConcern {
    private String id;

    public EntityId(String id) {
        setId(id);
    }

    private void setId(String id) {
        assertArgumentNotNull(id, "entity id can not by null");
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityId)) return false;

        EntityId entityId = (EntityId) o;

        return id.equals(entityId.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String id() {
        return id;
    }
}
