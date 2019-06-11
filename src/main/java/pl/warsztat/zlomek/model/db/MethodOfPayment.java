package pl.warsztat.zlomek.model.db;

public enum MethodOfPayment {
    CASH("Got\u00F3wka"),
    CARD("Karta"),
    TRANSFER("Przelew");

    public final String label;

    private MethodOfPayment(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
