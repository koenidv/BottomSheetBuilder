package com.koenidv.bottomsheetbuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.graphics.drawable.DrawableCompat;

//  Created by koenidv on 21.01.2019.

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class BottomSheetBuilder {

    private BottomSheetDialog mBottomSheet;
    private Context mContext;
    private int mSheetStyle = 0;
    private String mTag;
    private LinearLayout mLayout;
    private TextView titleTextView;
    private onItemClickListener mItemClickListener;
    private onSheetDismissedListener mDismissedListener;

    private int mItemHeight;
    private int mItemPadding;
    private int mDrawablePadding;
    private int mDefaultColor = 0;
    private int mDefaultIcon = 0;

    private Boolean mDismissedByAction = false;


    /**
     * Basic constructor
     *
     * @param context Sets the context for the BottomSheet
     */
    public BottomSheetBuilder(@NonNull Context context) {
        setup(context);
    }

    /**
     * Same as {@link BottomSheetBuilder}, but with a style
     *
     * @param context Sets the context for the BottomSheet
     * @param style   Sets the style for the BottomSheet
     */
    public BottomSheetBuilder(@NonNull Context context, @StyleRes int style) {
        setup(context);
        mSheetStyle = style;
    }

    /**
     * Same as {@link BottomSheetBuilder}, but with a tag
     *
     * @param context Sets the context for the BottomSheet
     * @param tag     Sets the tag so that the BottomSheet can be recognized later
     */
    public BottomSheetBuilder(@NonNull Context context, String tag) {
        setup(context);
        mTag = tag;
    }

    /**
     * Same as {@link BottomSheetBuilder}, but with a style and a tag
     *
     * @param context Sets the context for the BottomSheet
     * @param style   Sets the style for the BottomSheet
     * @param tag     Sets the tag so that the BottomSheet can be recognized later
     */
    public BottomSheetBuilder(@NonNull Context context, @StyleRes int style, String tag) {
        setup(context);
        mSheetStyle = style;
        mTag = tag;
    }

    /**
     * Basic set-up. Creates a LinearLayout,
     * default height and padding and adds the title to the Layout
     *
     * @param context The context to use
     */
    private void setup(Context context) {
        mContext = context;
        mLayout = new LinearLayout(mContext);
        mLayout.setOrientation(LinearLayout.VERTICAL);

        mItemHeight = (int) (48 * mContext.getResources().getDisplayMetrics().density + 0.5f);
        mItemPadding = (int) (16 * mContext.getResources().getDisplayMetrics().density + 0.5f);
        mDrawablePadding = mItemPadding;
        mDefaultColor = mContext.getResources().getColor(android.R.color.primary_text_light);

        titleTextView = new TextView(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleTextView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
        } else {
            titleTextView.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
        }
        titleTextView.setGravity(Gravity.CENTER_VERTICAL);
        titleTextView.setVisibility(View.GONE);
        mLayout.addView(titleTextView);
    }

    /*
     *
     * Title
     *
     */

    /**
     * Sets the title of the BottomSheet
     *
     * @param text The text to display
     */
    public BottomSheetBuilder setTitle(String text) {
        if (text == null) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(text);
        }
        return this;
    }

    /**
     * Sets the title of the BottomSheet from a resource ID
     *
     * @param stringRes A string resource ID
     */
    public BottomSheetBuilder setTitle(@StringRes int stringRes) {
        titleTextView.setText(stringRes);
        return this;
    }

    /**
     * Sets the title and its style of the BottomSheet
     *
     * @param text  The text to display
     * @param style The title's style
     *              Default is android.R.TextAppearance_DeviceDefault_Medium
     */
    public BottomSheetBuilder setTitle(String text, int style) {
        setTitle(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleTextView.setTextAppearance(style);
        } else {
            titleTextView.setTextAppearance(mContext, style);
        }
        return this;
    }

    /**
     * Sets the title's text color
     *
     * @param color A color (not color resource)
     */
    public BottomSheetBuilder setTitleColor(@ColorInt int color) {
        titleTextView.setTextColor(color);
        return this;
    }


    /*
     *
     * Items
     *
     */


    /**
     * Create a pre-formatted TextView
     */
    private TextView createItem(String text) {
        final TextView itemTextView = new TextView(mContext);
        itemTextView.setText(text);
        itemTextView.setPadding(mItemPadding, 0, mItemPadding, 0);
        itemTextView.setCompoundDrawablePadding(mDrawablePadding);
        itemTextView.setGravity(Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleTextView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_PopupMenu);
        } else {
            titleTextView.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Widget_PopupMenu);
        }
        itemTextView.setTextColor(mDefaultColor);
        TypedValue outValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        itemTextView.setBackgroundResource(outValue.resourceId);
        itemTextView.setId(mLayout.getChildCount() - 1);
        itemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClicked(v, itemTextView.getId(), mTag);
                    assert mBottomSheet != null;
                    mDismissedByAction = true;
                    mBottomSheet.dismiss();
                }
            }
        });

        if (mDefaultIcon != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(mDefaultIcon, 0, 0, 0);
            } else {
                itemTextView.setCompoundDrawablesWithIntrinsicBounds(mDefaultIcon, 0, 0, 0);
            }
        }

        return itemTextView;
    }

    /**
     * Create a pre-formatted TextView with an icon
     */
    private TextView createItem(String text, @DrawableRes int drawableRes) {
        TextView itemTextView = createItem(text);
        if (mDefaultIcon == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableRes, 0, 0, 0);
                itemTextView.setCompoundDrawableTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{mDefaultColor}));
            } else {
                Drawable normalDrawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    normalDrawable = mContext.getDrawable(drawableRes);
                } else {
                    normalDrawable = mContext.getResources().getDrawable(drawableRes);
                }
                assert normalDrawable != null;
                Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                DrawableCompat.setTint(wrapDrawable, mDefaultColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    itemTextView.setCompoundDrawablesRelative(wrapDrawable, null, null, null);
                } else {
                    itemTextView.setCompoundDrawables(wrapDrawable, null, null, null);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(mDefaultIcon, 0, 0, 0);
                itemTextView.setCompoundDrawableTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{mDefaultColor}));
            } else {
                Drawable normalDrawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    normalDrawable = mContext.getDrawable(mDefaultIcon);
                } else {
                    normalDrawable = mContext.getResources().getDrawable(mDefaultIcon);
                }
                assert normalDrawable != null;
                Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                DrawableCompat.setTint(wrapDrawable, mDefaultColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    itemTextView.setCompoundDrawablesRelative(wrapDrawable, null, null, null);
                } else {
                    itemTextView.setCompoundDrawables(wrapDrawable, null, null, null);
                }
            }
            if (mTag != null) {
                Log.d("BottomSheetBuilder: " + mTag + ": ", "Applying default icon, as set by setIcon(int). Can be overridden by setIcon(int, int position).");
            } else {
                Log.d("BottomSheetBuilder: ", "Applying default icon, as set by setIcon(int). Can be overridden by setIcon(int, int position).");
            }
        }
        return itemTextView;
    }

    /**
     * Add an item to the BottomSheet
     *
     * @param text The title of the item
     */
    public BottomSheetBuilder addItems(String... text) {
        for (String thisText : text)
            mLayout.addView(createItem(thisText));
        return this;
    }

    /**
     * Add an item as resource Id
     *
     * @param stringRes The resource Id of the item text
     */
    public BottomSheetBuilder addItems(@StringRes int... stringRes) {
        for (int thisStringRes : stringRes)
            mLayout.addView(createItem(mContext.getString(thisStringRes)));
        return this;
    }

    /**
     * Adds items with icons to the BottomSheet
     *
     * @param strings The strings to show
     * @param icons   The resource ids of the icons
     */
    public BottomSheetBuilder addItems(String[] strings, @DrawableRes int[] icons) {
        if (strings.length > icons.length) {
            if (mTag != null) {
                Log.e("BottomSheetBuilder: " + mTag + ": ", "Showing items without icons: Not enough icons specified.\nYou have to specify an icon for every String.\nTo add items without icons after items with icons, you can simply call addItems(String) again.");
            } else {
                Log.e("BottomSheetBuilder: ", "Showing items without icons: Not enough icons specified.\nYou have to specify an icon for every String.\nTo add items without icons after items with icons, you can simply call addItems(String) again.");
            }
            for (String string : strings) {
                mLayout.addView(createItem(string));
            }
        } else {
            for (int i = 0; i < strings.length; i++) {
                mLayout.addView(createItem(strings[i], icons[i]));
            }
        }
        return this;
    }

    /**
     * Adds items from resource ids with icons to the BottomSheet
     *
     * @param stringRes The resource ids of the Strings to show
     * @param icons     The resource ids of the icons
     */
    public BottomSheetBuilder addItems(@StringRes int[] stringRes, @DrawableRes int[] icons) {
        if (stringRes.length > icons.length) {
            if (mTag != null) {
                Log.e("BottomSheetBuilder: " + mTag + ": ", "Showing items without icons: Not enough icons specified.\nYou have to specify an icon for every String.\nTo add items without icons after items with icons, you can simply call addItems(String) again.");
            } else {
                Log.e("BottomSheetBuilder: ", "Showing items without icons: Not enough icons specified.\nYou have to specify an icon for every String.\nTo add items without icons after items with icons, you can simply call addItems(String) again.");
            }
            for (int stringRe : stringRes) {
                mLayout.addView(createItem(mContext.getString(stringRe)));
            }
        } else {
            for (int i = 0; i < stringRes.length; i++) {
                mLayout.addView(createItem(mContext.getString(stringRes[i]), icons[i]));
            }
        }
        return this;
    }

    /**
     * Add an item with a specific text color
     *
     * @param text  The text to display
     * @param color The text's color
     */
    public BottomSheetBuilder addItem(String text, @ColorInt int color) {
        TextView itemTextView = createItem(text);
        itemTextView.setTextColor(color);
        mLayout.addView(itemTextView);
        return this;
    }

    /**
     * Add an item with a specific text color
     *
     * @param stringRes The resource Id of the item text
     * @param color     The text's color
     */
    public BottomSheetBuilder addItem(@StringRes int stringRes, @ColorInt int color) {
        TextView itemTextView = createItem(mContext.getString(stringRes));
        itemTextView.setTextColor(color);
        mLayout.addView(itemTextView);
        return this;
    }

    /**
     * Sets the icon of a specific entry or entries
     *
     * @param icon      The icon's resource id
     * @param positions Optional: The positions in the list of items
     */
    public BottomSheetBuilder setIcon(@DrawableRes int icon, @NonNull int... positions) {
        if (positions.length == 0) {
            for (int i = 1; i < mLayout.getChildCount(); i++) {
                TextView itemTextView = (TextView) mLayout.getChildAt(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
                    itemTextView.setCompoundDrawableTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{mDefaultColor}));
                } else {
                    Drawable normalDrawable = mContext.getResources().getDrawable(icon);
                    Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                    DrawableCompat.setTint(wrapDrawable, mDefaultColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        itemTextView.setCompoundDrawablesRelative(wrapDrawable, null, null, null);
                    } else {
                        itemTextView.setCompoundDrawables(wrapDrawable, null, null, null);
                    }
                }
                mDefaultIcon = icon;
            }
        } else {
            for (int pos : positions) {
                TextView itemTextView = (TextView) mLayout.getChildAt(pos);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
                    itemTextView.setCompoundDrawableTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{mDefaultColor}));
                } else {
                    Drawable normalDrawable = mContext.getResources().getDrawable(icon);
                    Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                    DrawableCompat.setTint(wrapDrawable, mDefaultColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        itemTextView.setCompoundDrawablesRelative(wrapDrawable, null, null, null);
                    } else {
                        itemTextView.setCompoundDrawables(wrapDrawable, null, null, null);
                    }
                }
            }
        }
        return this;
    }

    /**
     * Sets the color of the text and icon of an item
     *
     * @param color     The color to set the item(s) to
     * @param positions Optional: The positions in the list of items
     */
    public BottomSheetBuilder setItemColor(@ColorInt int color, int... positions) {
        if (positions.length == 0) {
            for (int i = 1; i < mLayout.getChildCount(); i++) {
                TextView itemTextView = (TextView) mLayout.getChildAt(i);
                itemTextView.setTextColor(color);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemTextView.setCompoundDrawableTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{color}));
                } else {
                    Drawable normalDrawable;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        normalDrawable = itemTextView.getCompoundDrawablesRelative()[0];
                    } else {
                        normalDrawable = itemTextView.getCompoundDrawables()[0];
                    }
                    Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                    DrawableCompat.setTint(wrapDrawable, color);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        itemTextView.setCompoundDrawablesRelative(wrapDrawable, null, null, null);
                    } else {
                        itemTextView.setCompoundDrawables(wrapDrawable, null, null, null);
                    }
                }

                mDefaultColor = color;
            }
        } else {
            for (int pos : positions) {
                try {
                    TextView itemTextView = (TextView) mLayout.getChildAt(pos + 1);
                    itemTextView.setTextColor(color);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        itemTextView.setCompoundDrawableTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{color}));
                    } else {
                        Drawable normalDrawable;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            normalDrawable = itemTextView.getCompoundDrawablesRelative()[0];
                        } else {
                            normalDrawable = itemTextView.getCompoundDrawables()[0];
                        }
                        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                        DrawableCompat.setTint(wrapDrawable, color);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            itemTextView.setCompoundDrawablesRelative(wrapDrawable, null, null, null);
                        } else {
                            itemTextView.setCompoundDrawables(wrapDrawable, null, null, null);
                        }
                    }
                } catch (NullPointerException npe) {
                    if (mTag != null) {
                        Log.e("BottomSheetBuilder: " + mTag + ": ", "Can't set the color of a non-existent item.");
                    } else {
                        Log.e("BottomSheetBuilder: ", "Can't set the color of a non-existent item.");
                    }
                }
            }
        }
        return this;
    }


    /*
     *
     * Listeners
     *
     */

    public BottomSheetBuilder setOnItemClickListener(onItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
        return this;
    }

    public BottomSheetBuilder setOnSheetDismissedListener(onSheetDismissedListener onSheetDismissedListener) {
        mDismissedListener = onSheetDismissedListener;
        return this;
    }


    /*
     *
     * Styling
     *
     */

    /**
     * Sets the BottomSheet'S style
     *
     * @param style The style resource id
     */
    public BottomSheetBuilder setStyle(@StyleRes int style) {
        mSheetStyle = style;
        return this;
    }

    /**
     * Sets a tag so that the BottomSheet can be recognized later
     *
     * @param tag The tag
     */
    public BottomSheetBuilder setTag(String tag) {
        mTag = tag;
        return this;
    }

    /**
     * Sets the height of the individual items
     *
     * @param height The height in dp, default is 48dp
     */
    public BottomSheetBuilder setItemHeight(int height) {
        mItemHeight = (int) (height * mContext.getResources().getDisplayMetrics().density + 0.5f);
        return this;
    }

    /**
     * Sets the height of the individual items
     *
     * @param height The height in pixel
     */
    public BottomSheetBuilder setItemHeightPx(int height) {
        mItemHeight = height;
        return this;
    }

    /**
     * Sets the padding of the individual items
     *
     * @param padding The padding in dp, default is 16dp
     */
    public BottomSheetBuilder setItemPadding(int padding) {
        mItemPadding = (int) (padding * mContext.getResources().getDisplayMetrics().density + 0.5f);
        return this;
    }

    /**
     * Sets the padding of the individual items
     *
     * @param padding The padding in pixel
     */
    public BottomSheetBuilder setItemPaddingPx(int padding) {
        mItemPadding = padding;
        return this;
    }


    /*
     *
     *
     *
     */

    /**
     * Show the BottomSheet
     */
    public void show() {
        if (mLayout.getParent() == null) {
            //Set the padding and height of the items
            for (int child = 0; child < mLayout.getChildCount(); child++) {
                ((TextView) mLayout.getChildAt(child)).setHeight(mItemHeight);
                mLayout.getChildAt(child).setPadding(mItemPadding, 0, mItemPadding, 0);
            }
            mLayout.getChildAt(mLayout.getChildCount() - 1).setPadding(mItemPadding, 0, mItemPadding, mItemPadding / 2);
            ((TextView) mLayout.getChildAt(mLayout.getChildCount() - 1)).setHeight(mItemHeight + mItemPadding / 2);

            if (mSheetStyle != 0)
                mBottomSheet = new BottomSheetDialog(mContext, mSheetStyle);
            else
                mBottomSheet = new BottomSheetDialog(mContext);

            mBottomSheet.setContentView(mLayout);
            mBottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mDismissedListener != null && !mDismissedByAction)
                        mDismissedListener.onDismissed(mTag);
                }
            });
            mBottomSheet.show();
        } else {
            if (mTag != null) {
                Log.e("BottomSheetBuilder: " + mTag + ": ", "The BottomSheetDialog is already shown");
            } else {
                Log.e("BottomSheetBuilder: ", "The BottomSheetDialog is already shown");
            }
        }
    }

    public void dismiss() {
        if (mBottomSheet != null) {
            mDismissedByAction = true;
            mBottomSheet.dismiss();
        }
    }


    public interface onItemClickListener {
        void onItemClicked(View view, int which, String tag);
    }

    public interface onSheetDismissedListener {
        void onDismissed(String tag);
    }

}
