package ru.mirea.kolpakovap.dialogfinal;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), (view, hour, minute) -> {},
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
    }
}