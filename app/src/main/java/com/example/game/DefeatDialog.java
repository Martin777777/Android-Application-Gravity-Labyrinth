package com.example.game;


import android.app.Dialog;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class DefeatDialog extends Dialog {

    TextView winnerText;
    ImageView menu;


    public TextView getWinnerText() {
        return winnerText;
    }

    public ImageView getMenu() {
        return menu;
    }

    public DefeatDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.defeat_dialog);
        setCanceledOnTouchOutside(false);

        Window window = this.getWindow();
        window.setWindowAnimations(R.style.backDialogAnimation);
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.setAttributes(lp);
        System.out.println(lp.width);
        System.out.println(lp.height);


        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        winnerText = this.findViewById(R.id.text_winner);
        menu = this.findViewById(R.id.defeat_menu);

        winnerText.setSelected(true);

    }



    @Override
    public void onBackPressed() {

    }

}
