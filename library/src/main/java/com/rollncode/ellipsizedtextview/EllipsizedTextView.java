package com.rollncode.ellipsizedtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * @author Sviatoslav Koliesnik kolesniksy@gmail.com
 * @since 16.11.16
 * Can throw IndexOutOfBoundsException if TextView size is too small or postfix is larger than raw text.
 */
public class EllipsizedTextView extends TextView
        implements ViewTreeObserver.OnGlobalLayoutListener {

    private CharSequence mPostfix;
    private CharSequence mRawText;
    private final SpannableStringBuilder mSpannableStringBuilder;
    private final StringBuilder mStringBuilder;
    private OnEllipsizedTextViewListener mListener;
    private final ClickableSpan mClickableSpan;

    public EllipsizedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EllipsizedTextView,
                0, 0);

        try {
            mPostfix = typedArray.getString(R.styleable.EllipsizedTextView_postfix);
        } finally {
            typedArray.recycle();
        }

        if (getMaxLines() == -1) {
            setMaxLines(2);
        }

        mSpannableStringBuilder = new SpannableStringBuilder();
        mStringBuilder = new StringBuilder();

        mClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        setMovementMethod(LinkMovementMethod.getInstance());

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void setOnEllipsizedTextViewListener(OnEllipsizedTextViewListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        if (mListener != null && action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            x += getScrollX();
            y += getScrollY();

            final Layout layout = getLayout();
            final int line = layout.getLineForVertical(y);
            final int off = layout.getOffsetForHorizontal(line, x);
            final ClickableSpan[] spans = getText() instanceof Spanned ? ((Spanned) getText()).getSpans(off, off, ClickableSpan.class) : null;

            if (spans != null && spans.length > 0) {
                mListener.onPostfixClicked();
            } else {
                mListener.onTextClicked();
            }
        }
        return super.onTouchEvent(event);

    }

    public void setPostfix(CharSequence postfix) {
        mPostfix = postfix;
    }

    private void setSpannable() throws IndexOutOfBoundsException {

        if (TextUtils.isEmpty(mRawText)) {
            setText(null);
            return;
        }

        setText(mRawText);
        final Layout layout = getLayout();
        mSpannableStringBuilder.clear();
        mSpannableStringBuilder.clearSpans();

        if (layout != null && getPaint().measureText(mRawText.toString()) >= getMeasuredWidth()) {

            boolean needToCutEnd = layout.getLineCount() > getMaxLines();
            mStringBuilder.delete(0, mStringBuilder.length()); // clear StringBuilder
            if (needToCutEnd) {
                mStringBuilder.append("…");
            }
            if (!TextUtils.isEmpty(mPostfix)) {
                mStringBuilder.append(' ').append(mPostfix);
            }

            int sum = getOffsetForPosition(getWidth(), getHeight());

            mSpannableStringBuilder.append(needToCutEnd
                    ? mRawText.toString().substring(0, (sum > mRawText.length() ? mRawText.length() : sum) - (mStringBuilder.length() + 2))
                    : mRawText).append(mStringBuilder);
        } else {
            mSpannableStringBuilder.append(mRawText);
            if (!TextUtils.isEmpty(mPostfix)) {
                mSpannableStringBuilder.append(' ').append(mPostfix);
            }
        }

        if (!TextUtils.isEmpty(mPostfix)) {
            mSpannableStringBuilder.setSpan(mClickableSpan, mSpannableStringBuilder.length() - mPostfix.length(), mSpannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(mSpannableStringBuilder);
    }

    @Override
    public void onGlobalLayout() {
        final Layout layout = getLayout();
        if (layout != null) {
            if (mRawText == null) {
                mRawText = getText();
            }
            try {
                setSpannable();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}
