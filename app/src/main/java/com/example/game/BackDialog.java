package com.example.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrance.EntranceActivity;
import com.example.user.Rank;
import com.example.user.RankAdaptor;

import java.util.Locale;

public class BackDialog extends Dialog {

    ImageView menu;
    ImageView continuePlay;
    ImageView again;

    LinearLayout layout;
    LinearLayout worldRank;
    LinearLayout personalRank;

    TextView textView;
    int model;

    public ImageView getMenu() {
        return menu;
    }

    public ImageView getContinuePlay() {
        return continuePlay;
    }

    public ImageView getAgain() {
        return again;
    }

    public BackDialog(@NonNull Context context, int time, int level, int model) {
        super(context);

        setContentView(R.layout.back_dialog);
        setCanceledOnTouchOutside(false);
        this.model = model;

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

        layout = this.findViewById(R.id.dialog_layout);

        menu = this.findViewById(R.id.back_menu);
        continuePlay = this.findViewById(R.id.back_continue);
        again = this.findViewById(R.id.back_again);

        worldRank = this.findViewById(R.id.back_world_rank);
        personalRank = this.findViewById(R.id.back_personal_rank);

        textView = this.findViewById(R.id.current_time);
        textView.setText(String.format(Locale.ENGLISH ,"%02d : %02d", time / 60, time % 60 ));

        worldRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRank(lp, window, context, level, 0);
            }
        });

        personalRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EntranceActivity.isLogin) {
                    showRank(lp, window, context, level, 1);
                }
                else {
                    Toast.makeText(context, "You have to login first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showRank(WindowManager.LayoutParams lp, Window window, Context context, int level, int type) {
        try {
            layout.setVisibility(View.INVISIBLE);

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000);
            valueAnimator.setRepeatCount(0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    lp.width = (int) (layout.getWidth() * (1 - 0.2*value));
                    lp.height = (int) (layout.getHeight() * (1 + 0.5*value));
                    window.setAttributes(lp);
                }
            });
            valueAnimator.start();

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    Handler handler;


                    View rankView = LayoutInflater.from(context).inflate(R.layout.rank, (ViewGroup) layout.getParent(), false);
                    RecyclerView recyclerView = rankView.findViewById(R.id.rec_view);

                    ImageView back = rankView.findViewById(R.id.rank_back);

                    TextView title = rankView.findViewById(R.id.rank_title);
                    if (type == 0) {
                         title.setText("World Rank");
                    }
                    else if (type == 1) {
                        title.setText("Personal Rank");
                    }

                    handler = new Handler(Looper.myLooper()){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            ((ViewGroup) layout.getParent()).addView(rankView, 0);
                        }
                    };

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (type == 0) {
                                    recyclerView.setAdapter(new RankAdaptor(context, Rank.showAll(level, model)));
                                } else if (type == 1) {
                                    recyclerView.setAdapter(new RankAdaptor(context, Rank.showPersonal(level, EntranceActivity.username, model)));
                                }
                                handler.sendEmptyMessage(0);
                            }
                            catch (Exception e) {
                                Looper.prepare();
                                Toast.makeText(context, "Please check your network!", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((ViewGroup) layout.getParent()).removeView(rankView);

                            ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0, 1);
                            valueAnimator1.setDuration(1000);
                            valueAnimator1.setRepeatCount(0);
                            valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    float value = (float) valueAnimator.getAnimatedValue();
                                    lp.width = (int) (layout.getWidth() * (0.8 + 0.2*value));
                                    lp.height = (int) (layout.getHeight() * (1.5 - 0.5*value));
                                    window.setAttributes(lp);
                                }
                            });
                            valueAnimator1.start();

                            valueAnimator1.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    layout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                }
            });
        }
        catch (Exception e) {
            Toast.makeText(context, "Please check your network!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void setTime(int time) {
        textView.setText(String.format(Locale.ENGLISH ,"%02d : %02d", time / 60, time % 60 ));
    }
}
