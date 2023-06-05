public class CommitmentViewModel extends ViewModel {
    private MutableLiveData<String> nameLiveData;
    private MutableLiveData<String> amountLiveData;

    public LiveData<String> getNameLiveData() {
        if (nameLiveData == null) {
            nameLiveData = new MutableLiveData<>();
        }
        return nameLiveData;
    }

    public LiveData<String> getAmountLiveData() {
        if (amountLiveData == null) {
            amountLiveData = new MutableLiveData<>();
        }
        return amountLiveData;
    }

    public void setName(String name) {
        if (nameLiveData != null) {
            nameLiveData.setValue(name);
        }
    }

    public void setAmount(String amount) {
        if (amountLiveData != null) {
            amountLiveData.setValue(amount);
        }
    }
}
