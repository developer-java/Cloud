package view.kz.persistence.types;

public enum UserType {
    FULL(500L), DEMO(50L);

    private Long size;

    private UserType(Long size){
        this.size = size;
    }

    public Long getSize() {
        return size * 1024 * 1024;
    }
}
