package fi.muni.pv168.ui.form;

public interface FormResultListener<T> {

    public void onSubmit(T data);
    public void onCancel();

}
