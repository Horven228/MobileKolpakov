package ru.mirea.kolpakovap.dialogfinal;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Здравствуй МИРЭА!")
                .setMessage("Успех близок?")
                .setPositiveButton("Иду дальше", (dialog, id) -> ((MainActivity)getActivity()).onOkClicked())
                .setNeutralButton("На паузе", (dialog, id) -> ((MainActivity)getActivity()).onNeutralClicked())
                .setNegativeButton("Нет", (dialog, id) -> ((MainActivity)getActivity()).onCancelClicked());
        return builder.create();
    }
}