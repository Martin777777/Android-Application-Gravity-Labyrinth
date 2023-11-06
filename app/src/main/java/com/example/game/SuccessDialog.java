package com.example.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.animation.Animation;
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

public class SuccessDialog extends Dialog {

    ImageView menu;
    ImageView again;
    ImageView next;
    ImageView share;

    LinearLayout layout;
    LinearLayout worldRank;
    LinearLayout personalRank;


    TextView title;
    TextView textView;
    int model;

    boolean breakWorld;
    boolean breakPersonal;

    public ImageView getMenu() {
        return menu;
    }

    public ImageView getAgain() {
        return again;
    }

    public ImageView getNext() {
        return next;
    }

    public ImageView getShare() {
        return share;
    }

    public boolean isBreakPersonal() {
        return breakPersonal;
    }

    public boolean isBreakWorld() {
        return breakWorld;
    }

    public SuccessDialog(@NonNull Context context, int time, int level, int model) {
        super(context);

        setContentView(R.layout.success_dialog);
        setCanceledOnTouchOutside(false);
        this.model = model;
        this.breakWorld = false;
        this.breakPersonal = false;

        new Thread(() -> {
            if (EntranceActivity.isLogin) {
                try {
                    boolean[] newRecord = Rank.addToRank(level, time, EntranceActivity.username, model);
                    if (newRecord[0]) {
                        breakWorld = true;
                        Looper.prepare();
                        Toast.makeText(context, "New world record!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else if (newRecord[1]) {
                        breakPersonal = true;
                        Looper.prepare();
                        Toast.makeText(context, "New personal record!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
                catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(context, "Please check your network!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();

        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialogAnimation);
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

        menu = this.findViewById(R.id.img_menu);
        again = this.findViewById(R.id.img_again);
        next = this.findViewById(R.id.img_next);
        share = this.findViewById(R.id.img_share);
        worldRank = this.findViewById(R.id.world_rank);
        personalRank = this.findViewById(R.id.personal_rank);

        title = this.findViewById(R.id.success_title);
        textView = this.findViewById(R.id.text_time);
        textView.setText(String.format(Locale.ENGLISH ,"Time used: %02d : %02d", time / 60, time % 60 ));

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
                    System.out.println(lp.height);
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

    public TextView getTitle() {
        return this.title;
    }
}
