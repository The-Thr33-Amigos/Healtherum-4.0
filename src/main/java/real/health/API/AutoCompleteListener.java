package real.health.API;

public interface AutoCompleteListener {
    void onAddressSelected(String address);
    String getSelectedSuggestion();
}
