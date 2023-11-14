public enum Operations {
    ADD("Add"),
    NEGATIVE("Negative"),
    MULTIPLY("Multiply"),
    POWER("Power"),
    LOG("Log"),
    LINEAR_CONTRAST("Linear contrast"),
    TOZERO("To zero"),
    OTSU("Otsu");
    private String name;
    Operations(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
