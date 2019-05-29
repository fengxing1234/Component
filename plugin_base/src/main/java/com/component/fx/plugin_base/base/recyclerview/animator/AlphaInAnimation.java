package com.component.fx.plugin_base.base.recyclerview.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class AlphaInAnimation implements BaseAnimation {

    private Float mFrom;

    public AlphaInAnimation() {
        this(0f);
    }

    public AlphaInAnimation(float alpha) {
        this.mFrom = alpha;
    }

    @Override
    public Animator[] getAnimations(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1.0f)};
    }
}
